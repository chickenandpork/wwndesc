/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * CiscoSwitchDescription (ie Cisco-001560-0123456:0) breaks out the uniqueness number and port index from the WWPN.  There may be difficulty here in determining whether a device is a switch or a NPIV bladecenter for servers; it's not obvious from the WWPNs
 */
public class CiscoSwitchDescription extends WWNDesc.WWNDescSwitch
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public CiscoSwitchDescription(String wwn)
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
        else if (wwn.matches("2[0-9a-f]{3}000dec[0-9a-f]{6}"))
            return new CiscoSwitchDescription(brief, wwn);
        else if (wwn.matches("2[0-9a-f]{3}001560[0-9a-f]{6}"))
            return new CiscoSwitchDescription(brief, wwn);
        else if (wwn.matches("2[0-9a-f]{3}00215a[0-9a-f]{6}"))
            return new CiscoSwitchDescription(brief, wwn);
        else if (wwn.matches("2[0-9a-f]{3}547fee[0-9a-f]{6}"))
            return new CiscoSwitchDescription(brief, wwn);
        else
            return null;
    }

    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public CiscoSwitchDescription(boolean brief, String wwn)
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

        /* for example start with 20:13:00:15:60:12:34:56 or 2:013:001560:123456 broken apart */

        BigInteger portOuiSer[] = wwn.divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has portOuiSer = { [2:013:001560],[123456] } */
        BigInteger portOui[] = portOuiSer[0].divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has portOui = { [2:013], [001560] } */

        if (brief)
            return res + String.format("Cisco-%06x:%d",portOuiSer[1].intValue(),portOui[0].intValue() % (1 << 12) );
        else
            return res + String.format("Cisco-%06x-%06x:%d",portOui[1].intValue(),portOuiSer[1].intValue(),portOui[0].intValue() % (1 << 12) );
    }

    /**
     * describe the WWPN's unique port label/index
     *
     * @return the unique name for the port WWPN
     */
    public String descPort()
    {
        BigInteger serPort[] = wwn.divideAndRemainder(new BigInteger("1000000000000",16));
        return String.format("%03x",serPort[0].intValue() % (1 << 12));
    }
}
