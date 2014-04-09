/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * IBMSVCDescription (ie SVCe0_Node0E_Port1) breaks out the SVC number, node, and port of a WWPN; it does not see where two nodes of a SVC are related
 */
public class IBMSVCDescription extends WWNDesc
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public IBMSVCDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public IBMSVCDescription(boolean brief, String wwn)
    {
        super(brief, wwn);
    }

    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * @return new instance of this class, or null if the given wwn does not match this class
     * @param strong is used to show only those patterns we're 100% certain about: this class is a strong representation of 505076801*, but a weak representation of all 50507680* (notice that the last nibble is fuzzy, meaning we could get duplicate names)
     * @param brief is used to ask for a shorter description: a more concise nickname or alias
     * @param wwn the WWN (WWPN or WWNN, but typically WWPN) to match
     */
    public static WWNDesc getDesc(boolean strong, boolean brief, String wwn)
    {
        if ((strong) && (wwn.matches("5005076801.*")) )
            return new IBMSVCDescription(brief, wwn);
        else if (wwn.matches("500507680.*"))
            return new IBMSVCDescription(brief, wwn);
        else
            return null;
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

        /* convert 5005076801P0xxNN into SVCxx_NodeNN_PortP */
        /* for example start with 5005076801:30ee81 and produce SVCee_Node81_Port3 */
        BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
        /* serDirPort[0]=5005076801 ; serDirPort[1]=30ee81 */

        BigInteger portChassisNode[] = serDirPort[1].divideAndRemainder(new BigInteger("10000",16));
        /* portChassisNode[0]=30 ; portChassisNode[1]=ee81 */
        BigInteger chassisNode[] = portChassisNode[1].divideAndRemainder(new BigInteger("100",16));

        return res + String.format("SVC%02x_%s%02X_%s%s",chassisNode[0].intValue(), (brief ? "N" : "Node"), chassisNode[1].intValue(), (brief ? "P" : "Port"), portChassisNode[0].intValue()/16);
    }
}
