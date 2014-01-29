/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

public class HP3ParDescription extends WWNDesc
{
    public HP3ParDescription(String wwn)
    {
        super(wwn);
    }
    public HP3ParDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
    }

    public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
    {
        if (wwn.matches("2[0-9a-f][0-9a-f][0-9a-f]0002ac.*"))        /* 50002AC000020C3A */
            return new HP3ParDescription(brief, wwn);
        else
            return null;
    }

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

