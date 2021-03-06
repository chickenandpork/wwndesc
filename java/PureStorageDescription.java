/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * PureStorageDescription (ie Pure-0123456-CT0.FC0) breaks out the serial number and port information from the WWPN.  Although Pure re-purposes Qlogic HBAs, they have the generosity and forethought to rebadge the WWPN with their own OUI.  Thanks, Pure!
 */
public class PureStorageDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public PureStorageDescription(String wwn)
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
        else if (wwn.matches("^524a937.*"))
            return new PureStorageDescription(brief, wwn);
        else
            return null;
    }

    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public PureStorageDescription(boolean brief, String wwn)
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

        /* for example start with 52:4a:93:7d:74:f1:14:10 or 5:24a937:d74f114:10 broken apart */

        BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

        /* our example now has d74f114:10 */
        BigInteger serPort[] = serDirPort.divideAndRemainder(new BigInteger("100",16));

        if (brief)
            return res + String.format("Pure-%07x:%d:%d",serPort[0].intValue(),serPort[1].intValue()/16,serPort[1].intValue() % 16);
        else
            return res + String.format("Pure-%07x-CT%d.FC%d",serPort[0].intValue(),serPort[1].intValue()/16,serPort[1].intValue() % 16);
    }

    /**
     * describe the WWPN's unique port label/index
     *
     * @return the unique name for the port WWPN
     */
    public String descPort()
    {
        BigInteger serPort[] = wwn.divideAndRemainder(new BigInteger("10",16));
        return String.format("FC%d",serPort[1].intValue() % 16);
    }
}
