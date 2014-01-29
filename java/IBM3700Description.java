/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

public class IBM3700Description extends WWNDesc
{
    public IBM3700Description(String wwn)
    {
        super(wwn);
    }
    public IBM3700Description(boolean brief, String wwn)
    {
        super(brief, wwn);
    }
    public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
    {
        if (wwn.matches("2[0-9a-f]0[0-9a-f]0020c2.*"))
            return new IBM3700Description(brief, wwn);
        else
            return null;
    }

    public String toString()
    {
        String res = super.toString();
        if (null == res) res = "";

        /* convert 2P:0N:0020C2:MMMMMM into RamSan-MMMMMM-FC-NP */
        /* for example start with 21:08:0020c2:078332 and produce RamSan-G8332-FC-2B */
        BigInteger nodePortOUISer[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
        /* nodePortOUISer[0]=21080020c2 ; nodePortOUISer[1]=078332 */

        BigInteger nodePortOUI[] = nodePortOUISer[0].divideAndRemainder(new BigInteger("1000000",16));
        /* nodePortOUI[0]=2108 ; nodePortOUI[1]=0020c2 */
        BigInteger nodePort[] = nodePortOUI[0].divideAndRemainder(new BigInteger("100",16));
        /* nodePort[0]=21 ; nodePort[1]=08 */
        BigInteger serial[] = nodePortOUISer[1].divideAndRemainder(new BigInteger("10000",16));
        /* serial[0]=07 ; serial[1]=8332 */

        return res + String.format("RamSan-%c%04X-FC-%x%c",serial[0].intValue()-1+'A', serial[1].intValue(), nodePort[1].intValue()/4, nodePort[0].intValue() % 16 +'A');
    }
}
