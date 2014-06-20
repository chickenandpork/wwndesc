/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * HPVirtualServerDescription (ie HP-123456:010) breaks out the uniqueness number and port index from the WWPN.  This one is fairly weak in that HP clearly indicates the 00110a OUI is the unwashed-masses of roll-your-own-WWPN, and users do indeed roll random values.  This class is intended more to provide guidance to the uniqueness of a 2nnn00110axxxxxx device wherein for consistent xxxxxx values, nnn does change.
 */
public class HPVirtualServerDescription extends WWNDesc.WWNDescInitiator
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public HPVirtualServerDescription(String wwn)
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
//System.out.println("checking wwn "+wwn+" in role "+role+" strong: "+strong+" brief: "+brief+" isA: "+isA(role));
        if (!isA(role))
            return null;
        else if (strong)
            return null;
        else if (wwn.matches("2[0-9a-f]{3}00110a[0-9a-f]{6}"))
            return new HPVirtualServerDescription(brief, wwn);
        else
            return null;
    }

    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public HPVirtualServerDescription(boolean brief, String wwn)
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

        /* for example start with 20:13:00:11:0a:12:34:56 or 2:013:00110a:123456 broken apart */

        BigInteger portOuiSer[] = wwn.divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has portOuiSer = { [2:013:00110a],[123456] } */
        BigInteger portOui[] = portOuiSer[0].divideAndRemainder(new BigInteger("1000000",16));

        /* our example now has portOui = { [2:013], [00110a] } */

            return res + String.format("HPVC-%06x:%03x",portOuiSer[1].intValue(),portOui[0].intValue() % (1 << 12) );
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
