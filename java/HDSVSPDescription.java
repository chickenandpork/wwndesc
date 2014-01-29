/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

public class HDSVSPDescription extends WWNDesc
{
    public HDSVSPDescription(String wwn)
    {
        super(wwn);
    }
    public HDSVSPDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
    }
    public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
    {
        if (wwn.matches("50060e8.*"))
            return new HDSVSPDescription(brief, wwn);
        else
            return null;
    }

    public String toString()
    {
        String res = super.toString();
        if (null == res) res = "";
        if (!brief) res += "HDS-";

        /**
         * @todo 50060e8:
         *        005: USP-V: http://www.hitachi-storage.com/content/how-decode-usp-v-wwn
         *        00/5: USP-V: brionetka.com/linux/?p=38 breaks it up as NOOOOOOFFSSSSCP (N=5, OOOOOO = OUI, FF = Family {01=7700/Lightning, 02=9900/Thunder}, SSSS=Serial, C = Cluster, P=Port (0-F=A-Q skipping "I") note article confuses NAA marker as "50"
         *        need to check against http://p2v.it/tools/wwndec/index.php?input=50060E8010277210
         *        need to check http://p2v.it/tools/wwndec/index.php?input=50060e8016624800
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
        case 0x03: /* 003: 9900V */
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
