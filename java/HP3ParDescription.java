/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * HP3ParDescription (ie 3Par-1234:0:0:1) breaks out the serial, chassis, node, and port information from the WWPN
 */
public class HP3ParDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public HP3ParDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public HP3ParDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
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
        else if (wwn.matches("2[0-9a-f][0-9a-f][0-9a-f]0002ac.*"))        /* 50002AC000020C3A */
            return new HP3ParDescription(brief, wwn);
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

        /* convert 2N:SP:00:20:AC:MM:MM:MM to 3Par-Serial:N:S:P */
        /* ie 20:12:00:02:AC:00:0C:3A is serial number 1303130 */
        BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
        /* serDirPort[0]=20120002ac ; serDirPort[1]=000c3a */

        BigInteger portChassisNode[] = serDirPort[0].divideAndRemainder(new BigInteger("1000000",16));
        /* portChassisNode[0]=2012 ; portChassisNode[1]=0002ac */
        BigInteger chassisNode[] = portChassisNode[0].divideAndRemainder(new BigInteger("100",16));
        /* chassisNode[0]=20 ; chassisNode[1]=12 */

        return res + String.format("3Par-%04d:%x:%x:%x",serDirPort[1].intValue(), chassisNode[0].intValue() % 16, chassisNode[1].intValue() / 16, chassisNode[1].intValue() % 16);
    }
}

