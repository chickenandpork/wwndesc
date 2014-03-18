/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * EMCVMAXDescription (ie VMax-HK192601234-12gB or VMax-1234-12gB) breaks out the country of manufacturer, serial and FA port from the WWPN. @sa EMCClariionDescription @sa EMCSymmetrixDescription @sa EMCVPLEXDescription
 */
public class EMCVMAXDescription extends WWNDesc
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public EMCVMAXDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public EMCVMAXDescription(boolean brief, String wwn)
    {
        super(brief,wwn);
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
        if (wwn.matches("5000097.*"))
            return new EMCVMAXDescription(brief, wwn);
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

        /* for example start with 50000972081349AD on https://community.emc.com/thread/118881 */
        BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));
        res += "VMax-";

        /* our example now has 2081349AD */
        /* get the first four of the serial: 4 bits: {reserved/0} {A}{B} {reserved/0} */
        BigInteger countDirPort[] = serDirPort.divideAndRemainder(new BigInteger("200000000",16));     /* countDirPort[0] = 2, countDirPort[1] = 081349AD */
        BigInteger modelDirPort[] = countDirPort[1].divideAndRemainder(new BigInteger("8000000",16));     /* modelDirPort[0] = 1, modelDirPort[1] = 01349AD */
        if (!brief)
        {
            switch (countDirPort[0].intValue())
            {
            case 1: /* 0-01-0 == USA/HK19 seen on 20k VMax? */ /* 0-01-1 == USA/40k VMax? */
                res += "HK19";
                break;
            case 2: /* 0-10-0 == Ireland/CK29 */
                res += "CK29";
                break;
            default:/* unknown */
                res += "Unkn";
            }

            /* our example now has 081349AD - get the 4-bit model code */
            //BigInteger modelDirPort[] = countDirPort[1].divideAndRemainder(new BigInteger("8000000",16));     /* modelDirPort[0] = 1, modelDirPort[1] = 01349AD */
//System.out.println("modelDirPort[0] is "+modelDirPort[0] + " --> "+modelDirPort[0].intValue());
            switch (modelDirPort[0].intValue())
            {
            case 0: /* 000000: 40k V-Max */
                res += "46";
                break;
            case 1: /* 000001: 20k V-Max? */
            case (1+32): /* 100001: Unknown */
                res += "26";
                break;
            case 24: /* 011000: V-Max SE */
                res += "49";
                break;
	    /* Previously, bit #32 was reserved set to 0.  This is no longer the case */
            case 32: /* 100000: V-Max 40k 4-engine (TMO) */
                res += "57";
                break;
            default:/* unknown */
                res += "UN";
            }
        }

        /* our example now has 081349AD - get the 5-digit serial number ending */
        BigInteger serialDirPort[] = modelDirPort[1].divideAndRemainder(new BigInteger("400",16));     /* serialDirPort[0] = 004d2, serialDirPort[1] = 1AD */

        /* our example now has 1AD - get the 4-bit Processor code */
        BigInteger procDirPort[] = serialDirPort[1].divideAndRemainder(new BigInteger("40",16));     /* procDirPort[0] = 6, procDirPort[1] = 2D */

        /* our example now has 2D - get the 4-bit Director code */
        BigInteger dirDirPort[] = procDirPort[1].divideAndRemainder(new BigInteger("4",16));     /* dirDirPort[0] = C, dirDirPort[1] = %01 */
        return res + String.format("%0"+(brief?4:5)+"d-%02d%c%c",serialDirPort[0].intValue(),dirDirPort[0].intValue()+1,procDirPort[0].intValue()+'a',dirDirPort[1].intValue()+'A');
    }
}
