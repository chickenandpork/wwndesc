/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;
//import java.util.*;

/**
 * @file
 */

/**
 * SymbiosDescription (ie DS3400-123456:A:FC1) breaks out the UUID, Controller, and FC port for a DS3400 storage array
 * ref: IBM Storage Systems WWPN Determination
 */
public class SymbiosDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public SymbiosDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public SymbiosDescription(boolean brief, String wwn)
    {
        super(brief,wwn);
    }

    /**
     * If this class matches or describes the given WWN, returns a new instance of this class loaded with the given WWN.
     *
     * @return new instance of this class, or null if the given wwn does not match this class
     * @param strong is ignored: this class is a strong representation, not a weak one based on empirical matching, hence can always be used with confidence
     * @param brief is used to ask for a shorter description: a more concise nickname or alias
     * @param wwn the WWN (WWPN or WWNN, but typically WWPN) to match
     */
    public static WWNDesc getDesc(/* ignored */ boolean strong, boolean brief, String wwn)
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
        else if (wwn.matches("20[23][4-7]00a0b8[0-9a-f]{6}"))
            return new SymbiosDescription(brief, wwn);
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

        /* for example start with 2024:00a0b8:123456 */

        BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));		/* serDirPort[0] is 2024:00a0b8, serDirPort[1] is 123456 */

        BigInteger dirPort[] = serDirPort[0].divideAndRemainder(new BigInteger("1000000",16));		/* dirPort[0] is 2024, dirPort[1] is 00a0b8 */

        /*
         * 08 -> WWNN
         * 18 -> WWPN C0-0a ?
         * 28 -> WWPN C0-0b ?
         * 19 -> WWPN C1-0a
         * 29 -> WWPN C1-0b
         */

        int controller = (dirPort[0].intValue() / 0x010) % 2;
        int port = (dirPort[0].intValue() % 2);

        char SP = 'A';
        SP += controller;

        if (brief)
            return res + String.format("DS3400-%06x:%c:%d",serDirPort[1].intValue(),SP,port+1);
        else
            return res + String.format("DS3400-%06x-ctrl%c-FC%d",serDirPort[1].intValue(),SP,port+1);
    }
}
