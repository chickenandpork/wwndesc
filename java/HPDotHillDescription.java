/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * HPDotHillDescription (ie P2000-123456-B1) breaks out a unique code and the port information from the WWPN of a Dot Hill Systems Modular Smart Array P2000 which HP resells.
 *
 * These descriptors are fairly weak: I'm somewhat sure of the serial, but only two matches for the port-number logic.
 */
public class HPDotHillDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public HPDotHillDescription(String wwn)
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
        else if (wwn.matches("2[0-9a-f][0-9a-f][0-9a-f]00c0ff.*"))
            return new HPDotHillDescription(wwn);
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

        /* convert 2170:00c0ff:012345 into P2000-012345-A2 */
        BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
        /* serDirPort[0]=217000c0ff ; serDirPort[1]=012345 */

        BigInteger portChassisNode[] = serDirPort[0].divideAndRemainder(new BigInteger("100000000",16));
        /* portChassisNode[0]=21 ; portChassisNode[1]=7000c0ff */
        BigInteger chassisNode[] = portChassisNode[0].divideAndRemainder(new BigInteger("10",16));
        /* chassisNode[0]=2 ; chassisNode[1]=1 */

        return res + String.format("P2000-%06x-%c%c",serDirPort[1].intValue(), 'A'+(chassisNode[1].intValue()/4), '1'+(chassisNode[1].intValue()%4));
    }
}
