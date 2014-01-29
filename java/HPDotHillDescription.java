/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

    /**
     * Descriptor for (Dot Hill Systems Modular Smart Array) HP P2000
     *
     * These descriptors are fairly weak: I'm somewhat sure of the serial, but only two matches for the port-number logic.
     */
    public class HPDotHillDescription extends WWNDesc
    {
        public HPDotHillDescription(String wwn)
        {
            super(wwn);
        }

        public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
        {
            if (wwn.matches("2[0-9a-f][0-9a-f][0-9a-f]00c0ff.*"))
            return new HPDotHillDescription(wwn);
            else
                return null;
        }

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
