/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * AIXLPARServerDescription (ie LPAR-012345:0) breaks out the uniqueness number and port index from the WWPN.  There may be difficulty here in determining whether a device is a switch or a NPIV bladecenter for servers; it's not obvious from the WWPNs
 */
public class AIXLPARServerDescription extends WWNDesc.WWNDescInitiator
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public AIXLPARServerDescription(String wwn)
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
        else if (wwn.matches("c05076[0-9a-f]{10}"))
            return new AIXLPARServerDescription(brief, wwn);
        else
            return null;
    }

    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public AIXLPARServerDescription(boolean brief, String wwn)
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

        /* for example start with c050760123450010 or c05076:012345:0010 broken apart -- I've noticed that the LPAs in a given frame, perhaps by convention only, have consistent 12 digits (including OUI) and differing last 16 bits that almost appear to be odd/even split amongst fabrics */

        BigInteger ouiSerPort[] = wwn.divideAndRemainder(new BigInteger("10000",16));

        /* our example now has ouiSerPort = { [c05076:012345],[0010] } */
        BigInteger ouiSer[] = ouiSerPort[0].divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has ouiSer = { [c05076], [012345] } */

        return res + String.format("LPAR-%06x:%04x",ouiSer[1].intValue(),ouiSerPort[1].intValue() );
    }

    /**
     * describe the WWPN's unique port label/index
     *
     * @return the unique name for the port WWPN
     */
    public String descPort()
    {
        BigInteger serPort[] = wwn.divideAndRemainder(new BigInteger("10000",16));
	return String.format("%04x",serPort[1].intValue());
    }
}
