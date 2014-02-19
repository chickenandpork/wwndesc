/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * EMCVPLEXDescription (ie VPlex-07a3b-A0-FC00) breaks out the serial and FE/BE port from the WWPN. @sa EMCClariionDescription @sa EMCSymmetrixDescription @sa EMCVMAXDescription
 */
public class EMCVPLEXDescription extends WWNDesc
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public EMCVPLEXDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public EMCVPLEXDescription(boolean brief, String wwn)
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
        if (wwn.matches("50001442.*"))
            return new EMCVPLEXDescription(brief, wwn);
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

        /* for example start with 50001442607a3b00 on https://community.emc.com/thread/146058 */
        BigInteger serDirPort = wwn.subtract(wwn.shiftRight(32).shiftLeft(32));
        res += "VPlex-";

        /* our example now has 607a3b00 */
        /* get the next digit ("num"): even is A, odd is B. four of the serial: 4 bits: {reserved/0} {A}{B} {reserved/0} */
        BigInteger numSeedIOPort[] = serDirPort.divideAndRemainder(new BigInteger("10000000",16));		/* numSeedIOPort[0] = 6, numSeedIOPort[1] = 07a3b00 */

        //numSeedIOPort[0] = numSeedIOPort[0].shiftRight(7).shiftLeft(7);					/* numSeedIOPort[0] = 0 --> A Director, numSeedIOPort[1] = 07a3b00 */
        char director='A';
        director += (numSeedIOPort[0].intValue() % 2);

        BigInteger seedIOPort[] = numSeedIOPort[1].divideAndRemainder(new BigInteger("100",16));     /* seedIOPort[0] = 07a3b, seedIOPort[1] = 00 */

        /* Assume we're all VS2 architecture @todo fix the assumpiton that VS1 VPLEX no longer exist */
        String front = "un";
        switch (seedIOPort[1].intValue() / 16)
        {
        case 0:
            front = "FE";
            break;
        case 1:
            front = "BE";
            break;
        case 2:
            front = "WA";
            break;
        case 3:
            front = "LO";
            break;
            /* default: stick with an X, dunno what to do with that value */
        }

        /* want a result such as VPLEX02_E1B_BE13 */
        /* can get a brief result such as VPlex-07a3b-EB-BE13 */
        if (brief)
            return res + String.format("%05x-E%c-%s%d%d",seedIOPort[0].intValue(),director,front,seedIOPort[1].intValue() / 16, seedIOPort[1].intValue() % 16);
        /* can get a result such as VPlex-07a3b-A0-FC00 */
        else
            return res + String.format("%05x-%c%d-FC0%d",seedIOPort[0].intValue(),director,seedIOPort[1].intValue() / 16, seedIOPort[1].intValue() % 16);
    }
}
