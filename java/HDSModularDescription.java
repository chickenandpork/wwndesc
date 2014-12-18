/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * HDSModularDescription (ie HDS-D850-31787-CTL0-PortD) breaks out the model type, serial, and FA port from the WWPN
 */

public class HDSModularDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public HDSModularDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public HDSModularDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
    }

    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * This function should correspond to HDSVSPDescription#getDesc(boolean, boolean, String)
     * as both are redundant copies simply to avoid an error ustream being leaked out to the user
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
         * 010 TOM: HDS KA15062: breaks it up as N OOOOOO FFF S ssss P (N=5, OOOOOO = OUI, FFF = Family (010) ssss=Serial % 0x10000, P=Port (0-F)
         *        need to check against http://p2v.it/tools/wwndec/index.php?input=50060e8010139322
         *        need to check http://p2v.it/tools/wwndec/index.php?input=50060e8010139322
         */

        /* for example start with 50060e8010139322 from HDS KB15062 */
        BigInteger serDirPort = wwn.remainder(new BigInteger("1000000",16));

        /* our example now has 139322 - get the 3-nibble model code */
        BigInteger modelDirPort[];
        modelDirPort = serDirPort.divideAndRemainder(new BigInteger("100000",16));     /* modelDirPort[0] = 1, modelDirPort[1] = 39322 */
//System.out.println(String.format("split result: modelDirPort: %02x / %06x (%06d)", modelDirPort[0].intValue(), modelDirPort[1].intValue(), modelDirPort[1].intValue()));
        int model = modelDirPort[0].intValue();

        BigInteger serialPort[] = modelDirPort[1].divideAndRemainder(new BigInteger("10",16));     /* serialPort[0] = 3932, serialPort[1] = 2 */
        int serial = serialPort[0].intValue();
        int iPort = serialPort[1].intValue();
//System.out.println(String.format("split result: model %02d serial %05x / %06d port %d", model, serial, serial, iPort));

        switch (model)
        {
        case 0: /* DF800H/EH */
            res += "DF800H-";
            serial -= 10000;
            break;
        case 1: /* DF850MH */
            res += "DF850MH-";
            serial -= 70000;
            break;
        case 2: /* DF800M/EM */
            res += "DF800M-";
            serial -= 10000;
            break;
        case 3: /* DF850S */
            res += "DF850S-";
            serial -= 70000;
            break;
        case 4: /* DF800S/ES/EXS */
            res += "DF800ES-";
            serial -= 10000;
            break;
        case 5: /* DF850XS */
            res += "DF850XS-";
            serial -= 70000;
            break;
        case 9: /* SA800 (The value is set to 9 if Serial# is 65536(decimal) or more) */
            serialPort[0] = serialPort[0].add(new BigInteger("100000", 16));
        /* fall-thru */
        case 8: /* SA800 */
            res += "SA800";
            break;
        case 0x0B: /* SA810 :A or B (The value is set to B if Serial# is 65536(decimal) or more) */
            serialPort[0] = serialPort[0].add(new BigInteger("100000", 16));
        /* fall-thru */
        case 0x0A: /* SA810 */
            res += "SA810";
            break;
        default: /* unknown */
            res += String.format("HDSUnkn%03x-",model);
        }
//System.out.println(String.format("munge result: model %02d serial %05x / %06d port %d", model, serial, serial, iPort));
        if (serial < 0) serial += 0x10000;
//System.out.println(String.format("munge result: model %02d serial %05x / %06d port %d", model, serial, serial, iPort));


        char port = 'A';
        port += (iPort % 8);
        int ctl = iPort / 8;

        /* our example now has 39322 - get the serial number */
        //serialPort[0] = serialPort[0].subtract(new BigInteger("70000",10));
        if (brief)
            return res + String.format("%05d-%d%c",serial, ctl, port);
        else
            return res + String.format("%05d-CTL%d-Port%c",serial, ctl, port);
    }
}
