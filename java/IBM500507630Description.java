/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * IBM500507630 covers the following types:
 * <ul>
 * <li>1750 DS6000      	500507630zFExxxx 500507630zyyxxxx</li>
 * <li>2105 ESS or Shark	5005076300c0xxxx 5005076300yyxxxx</li>
 * <li>2107 DS8000       	500507630zFFxxxx 500507630zyyyxxx</li>
 * </ul>
 *
 * @see http://aussiestorageblog.files.wordpress.com/2013/01/ibm-storage-systems-wwpn-determination-version-6-6.pdf
 * @see http://www-03.ibm.com/support/techdocs/atsmastr.nsf/WebIndex/TD105450
 * @see http://linuxvm.org/present/SHARE100/S9333NFa.pdf seems to imply that 50050763004029a0 is actually a tape ID within a IBM 3590(E) tape library
 */
public class IBM500507630Description extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public IBM500507630Description(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public IBM500507630Description(boolean brief, String wwn)
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
        else if (wwn.matches("500507630.*"))
            return new IBM500507630Description(brief, wwn);
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
