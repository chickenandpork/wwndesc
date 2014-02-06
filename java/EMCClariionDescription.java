/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

public class EMCClariionDescription extends WWNDesc
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public EMCClariionDescription(String wwn)
    {
        super(wwn);
    }
    
    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * @return new instance of this class, or null if the given wwn does not match this class
     * @param strong is ignored: this class is a strong representation, not a weak one based on empirical matching, hence can always be used with confidence
     * @param brief is ignored: this class has only one representation of the WWN description or alias
     * @param wwn the WWN (WWPN or WWNN, but typically WWPN) to match
     */
    public static WWNDesc getDesc(/* ignored */ boolean strong, /* ignored */ boolean brief, String wwn)
    {
        if (wwn.matches("5006016.*"))
            return new EMCClariionDescription(wwn);
        else
            return null;
    }

    /**
     * return a description or alias for this WWN
     *
     * @return generated alias or nickname for the WWN
     */
    public String toString()
    {
        String res = super.toString();
        if (null == res) res = "";

        /* for example start with 5006016A12345678 on http://blog.fosketts.net/toolbox/emc-symmetrix-wwn-calculator/ */
        /* http://goingvirtual.wordpress.com/2011/09/09/how-i-zone-vnx-storage-arrays/ */
        /* http://www.thesangeek.com/2011/02/09/sangeek-clariion-cx4-wwn-interpretations/ */
        /* http://clariionblogs.blogspot.com/2007/11/storage-processor-ports-wwns.html */
        /* http://architecting.it/2013/09/06/what-emc-should-have-done-with-vnx/ */

        BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

        /* our example now has A12345678 */
        BigInteger serPort[] = serDirPort.divideAndRemainder(new BigInteger("100000000",16));

        Character SP;
        switch (serPort[0].intValue() / 8)
        {
        case 0:
            SP='A';
            break;
        case 1:
            SP='B';
            break;
        default:
            SP='X';
        }
        return res + String.format("CL-%08x-SP%c%d",serPort[1].intValue(),SP,serPort[0].intValue() % 8);
    }
}
