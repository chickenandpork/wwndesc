/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * CiscoUCSServerDescription (ie CiscoUCS-012-123:0) breaks out the uniqueness number and port index from the WWPN of a UCS-served physical host.  I do tend to see 20000025b5hhhfnnn such that:
 * <ul>
 * <li>hhh is a three-digit host index</li>
 * <li>f is either 0 or 1 based on fabric A or B</li>
 * <li>nnn is a three-digit server index</li>
 * </ul>
 *
 * The thing to remember about UCS is that the WWPN are entirely user-defined, are are UUIDs, so this is merely a convention.  It might fall apart on the next user!
 */
public class CiscoUCSServerDescription extends WWNDesc.WWNDescSwitch
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public CiscoUCSServerDescription(String wwn)
    {
        super(wwn);
    }

    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * @return new instance of this class, or null if the given wwn does not match this class
     * @param strong used to restrict matching to strong-matches only.  This is a weak-confidence description, so will return null for strong-only matching
     * @param brief is used to ask for a shorter description: a more concise nickname or alias
     * @param wwn the WWN (WWPN or WWNN, but typically WWPN) to match
     */
    public static WWNDesc getDesc(boolean strong, boolean brief, String wwn)
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
        else if (strong)
            return null;
        else if (wwn.matches("20000025b5[0-9a-f]{6}"))
            return new CiscoUCSServerDescription(brief, wwn);
        else if (wwn.matches("2[0-9a-f]{3}0026a6[0-9a-f]{6}"))
            return new CiscoSwitchDescription(brief, wwn);
        else
            return null;
    }

    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public CiscoUCSServerDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
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

        /* for example start with 20:00:00:25:b5:12:34:56 or 2:000:0025b5:123456 broken apart */

        BigInteger portOuiSerInd[] = wwn.divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has portOuiSerInd = { [2000:0025b5],[123456] } */
        BigInteger serInd[] = portOuiSerInd[0].divideAndRemainder(new BigInteger("1000",16));

        /* our example now has serInd = { [123], [456] } */

            return res + String.format("CiscoUCS-%03x:%03x",serInd[0].intValue(),serInd[1].intValue());
    }

    /**
     * describe the WWPN's unique port label/index
     *
     * @return the unique name for the port WWPN
     */
    public String descPort()
    {
        BigInteger serPort[] = wwn.divideAndRemainder(new BigInteger("1000",16));
	return String.format("%03x",serPort[1].intValue());
    }
}
