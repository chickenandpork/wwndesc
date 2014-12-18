/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * HDSVSPDescription (ie USPV-10098-CL2A) breaks out the model type, serial, and FA port from the WWPN
 */

public class HDSVSPDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public HDSVSPDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public HDSVSPDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
    }
    
    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * @return new instance of this class, or null if the given wwn does not match this class
     * @param strong is ignored: this class is a strong representation, not a weak one based on empirical matching, hence can always be used with confidence
     * @param brief is used to ask for a shorter description: a more concise nickname or alias
     * @param wwn the WWN (WWPN or WWNN, but typically WWPN) to match
     */
    public static WWNDesc getDesc(/* ignored */ boolean strong, boolean brief, String wwn)
    {
        return getDesc(strong, brief, wwn, DevRole.max()-1);
    }
    /**
     * @copydoc #getDesc(boolean, boolean, String)
     * @param role Role (Initiator/Switch/Target) to check for
     */
    public static WWNDesc getDesc (boolean strong, boolean brief, String wwn, int role)
    {
        if (!isA(role))
            return null;
        else if (wwn.startsWith("50060e8010"))
            return new HDSModularDescription(brief, wwn);
        else if (wwn.startsWith("50060e8"))
            return new HDSVSPDescription(brief, wwn);
        else
            return null;
    }

    /**
     * return a description or alias for this WWN; if brief is set to true during the call to getDesc(), then a shorter description or alias will be returned
     *
     * @see getDesc(boolean,boolean,String)
     *
     * @return generated alias or nickname for the WWN
     */
    public String toString()
    {
        String res = super.toString();
        if (null == res) res = "";
        if (!brief) res += "HDS-";

        /**
         * @todo 50060e8:
         *        005: USP-V: http://www.hitachi-storage.com/content/how-decode-usp-v-wwn
         *        00/5: USP-V: brionetka.com/linux/?p=38 breaks it up as NOOOOOOF FFSS SSCP (N=5, OOOOOO = OUI, FF = Family {01=7700/Lightning, 02=9900/Thunder}, SSSS=Serial, C = Cluster, P=Port (0-F=A-Q skipping "I") note article confuses NAA marker as "50"
         *        need to check against http://p2v.it/tools/wwndec/index.php?input=50060E8010277210
         *        need to check http://p2v.it/tools/wwndec/index.php?input=50060e8016624800
	 *
	 * NOTE that https://tuf.hds.com/wiki/pub/Main/Lunstat/lsu0011.pdf agrees with this class logic
	 *   - 50060E80034E3901 gives 9970 with serial 20025 
         */
        /* for example start with 50060e8005123442 on http://www.hitachi-storage.com/content/how-decode-usp-v-wwn */
        BigInteger serDirPort = wwn.remainder(new BigInteger("1000000000",16));

        /* our example now has 005123442 - get the 3-nibble model code */
        BigInteger modelDirPort[];
        modelDirPort = serDirPort.divideAndRemainder(new BigInteger("1000000",16));     /* modelDirPort[0] = 005, modelDirPort[1] = 123442 */

        switch (modelDirPort[0].intValue())
        {
        case 0x01: /* 001: 7700E */
            res += "7700E-";
            break;
        case 0x02: /* 002: 9900 */
            res += "9900-";
            break;
        case 0x03: /* 003: 9900V : HDS-9970 / HDS-9980 AKA HP-XP128 or HP-XP1024 */
            res += "9900V-";
            break;
        case 0x04: /* 004, 014: USP */
        case 0x14:
            res += "USP-";
            break;
        case 5: /* 005: regular USP-V */
        case 0x15:
            res += "USPV-";
            break;
        case 6: /* 006: seen and confirmed VSP at Associated Proj-00666 */
        case 0x16:
            /* unknown but seen at AdCenter: 50060e8015345800 is serial 78936 port CL1A */
            /* 016: unknown but seen at ChampionSG Proj-712 */
            res += "VSP-";
            break;
        case 0x10: /* 010: DF700/DF800/SA/DF850 */
            res += "HUSV-";
            break;
        case 0x13: /* Jan reinmuth: HUS VM */
            res += "HUSV-";
            break;
        default: /* unknown */
            res += String.format("HDSUnkn%03x-",modelDirPort[0].intValue());
        }

        /* our example now has 123442 - get the 4-digit serial number */
        BigInteger serialPort[] = modelDirPort[1].divideAndRemainder(new BigInteger("100",16));     /* serialPort[0] = 1234, serialPort[1] = 42 */
        serialPort[0] = serialPort[0].add(modelDirPort[0].divide(new BigInteger("10",16)).multiply(new BigInteger("10000",16)));
        BigInteger clusterPort[] = serialPort[1].divideAndRemainder(new BigInteger("10",16));     /* clusterPort[0] = 4, clusterPort[1] = 2 */
        char port = 'A';
        port += clusterPort[1].intValue();
        if (port > 'H') port ++;
        return res + String.format("%05d-CL%x%c",serialPort[0].intValue(),clusterPort[0].intValue()+1, port);
    }
}
