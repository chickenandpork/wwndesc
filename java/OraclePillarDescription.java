/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * Descriptor for (Oracle) Pillar Data Systems' Service Lifecycle Management devices
 */
public class OraclePillarDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public OraclePillarDescription(String wwn)
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
        else if (wwn.matches("2[12][0-9a-fA-F][0-9a-fA-F]000b08.*"))
            return new OraclePillarDescription(wwn);
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

        /* convert 2C00:000b08:SSSSSP into Axiom-SSSSS-cCpP */
        /* for example start with 2100:000b08:123450 and produce Axiom-12345-c0p0 */
        BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
        /* serDirPort[0]=2100:000b08 ; serDirPort[1]=123450 */

        BigInteger twoContZeroZero[] = serDirPort[0].divideAndRemainder(new BigInteger("100000000",16));
        /* twoContZeroZero[0]=21; twoContZeroZero[1]=00:000b08 */
        BigInteger twoCont[] = twoContZeroZero[0].divideAndRemainder(new BigInteger("10",16));
        /* twoCont[0]=2 ; twoCont[1]=1*/

        BigInteger serialPort[] = serDirPort[1].divideAndRemainder(new BigInteger("10",16));
        /* serialPort[0]=12345 ; serialPort[1]=0 */

        return res + String.format("Axiom-%05X-c%sp%X",serialPort[0].intValue(), twoCont[1].intValue()-1, serialPort[1].intValue());
    }
}
