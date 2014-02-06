/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

public class EMCSymmetrixDescription extends WWNDesc
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public EMCSymmetrixDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public EMCSymmetrixDescription(boolean brief, String wwn)
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
        if (wwn.matches("5006048.*"))
            return new EMCSymmetrixDescription(brief, wwn);
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

        /* for example start with 5006048ACCC86A32 on http://www.emcstorageinfo.com/2007/08/how-to-decode-symmetrix-world-wide.html */
        BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

        /* our example now has ACCC86A32 */
        BigInteger sDirPort[] = serDirPort.divideAndRemainder(new BigInteger("40",16));
        BigInteger xDirPort[] = sDirPort[0].divideAndRemainder(new BigInteger("20000000",16));

        if (brief)
            res += String.format("Symm-%04d",xDirPort[1].mod(new BigInteger("10000",10)));
        else
            res += "Symm-"+xDirPort[1];

        BigInteger DirPort[] = sDirPort[1].divideAndRemainder(new BigInteger("10",16));
        res += String.format("-%02d",DirPort[1].intValue()+1);

        switch (4*xDirPort[0].intValue()+DirPort[0].intValue())
        {
        case 0:
            res += "aA";
            break;
        case 1:
            res += "bA";
            break;
        case 2:
            res += "aB";
            break;
        case 3:
            res += "bB";
            break;
        case 4:
            res += "cA";
            break;
        case 5:
            res += "dA";
            break;
        case 6:
            res += "cB";
            break;
        case 7:
            res += "dB";
            break;
        }

        return res;
    }
}
