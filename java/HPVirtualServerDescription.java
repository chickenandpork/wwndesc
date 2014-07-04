/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * HPVirtualServerDescription (ie HP-123456:010) breaks out the uniqueness number
 * and port index from the WWPN.  This one is fairly weak in that HP clearly
 * indicates the 00110a OUI is the unwashed-masses of roll-your-own-WWPN, and users
 * do indeed roll random values.  This class is intended more to provide guidance
 * to the uniqueness of a 2nnn00110axxxxxx device wherein for consistent xxxxxx
 * values, nnn does change.
 *
 * http://h20628.www2.hp.com/km-ext/kmcsdirect/emr_na-c03169041-2.pdf
 * http://h20000.www2.hp.com/bc/docs/support/SupportManual/c03314921/c03314921.pdf.

 * http://h20000.www2.hp.com/bc/docs/support/SupportManual/c02494590/c02494590.pdf.
 *     50:01:43:80:02:A3:00:00 - 50:01:43:80:02:A4:FF:FF VCEM 
 *     50:06:0B:00:00:C2:62:00 - 50:06:0B:00:00:C3:61:FF HP Pre-defined (64 blocks of 1024 IDs: ie C2:62:00 - C2:65:FF
 *
 * http://www.filibeto.org/unix/hp-ux/lib/os/vpar/6.0/vpars-6.0-npiv-4AA3-8665ENW.pdf
 * The range that HP has reserved for NPIV is 0x50014c2000000000 to 0x50014c27ffffffff for Port WWNs and 0x50014c2800000000 to 0x50014c2fffffffff for Node WWNs.
 * ...
 * When manually configuring vHBAs, vWWNs may safely be picked from the range 0x100000110a0300 - 0x100000110a030fff
 *
 * http://h20000.www2.hp.com/bc/docs/support/SupportManual/c03967142/c03967142.pdf
 *     10:00:38:9d:30:60:00:00 - 10:00:38:9d:30:6f:ff:ff used in examples of API
 *
 * 50060b00 range used in http://www.filibeto.org/unix/hp-ux/lib/os/vpar/6.0/vpars-6.0-npiv-4AA3-8665ENW.pdf as a config example.  Customers tend to follow examples.
 *
 * 00:60:B0 Integrity and HP9000
 * 00:11:0a Compaq Proliant (500110a in servers)
 * 00:01:FE EVA from DEC
 * 00:17:A4 MSL Tape
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
