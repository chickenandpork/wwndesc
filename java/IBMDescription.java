/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 */

/**
 * Centralize the sorting logic for various IBM products based on IBM Storage Systems WWPN Determination
 *
* @see http://aussiestorageblog.files.wordpress.com/2013/01/ibm-storage-systems-wwpn-determination-version-6-6.pdf
 */
public class IBMDescription extends WWNDesc.WWNDescTarget
{
    /** @copydoc WWNDesc#WWNDesc(String) */
    public IBMDescription(String wwn)
    {
        super(wwn);
    }
    /** @copydoc WWNDesc#WWNDesc(boolean,String) */
    public IBMDescription(boolean brief, String wwn)
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

        else if (wwn.matches("2[0-9a-f]0[0-9a-f]0020c2.*"))
            return new IBM3700Description(brief, wwn);

        if (wwn.matches("5005076801.*"))
            return new IBMSVCDescription(brief, wwn);

        /* the not-yet-done ones */
        /* DS3000/DS4000/DS5000 */
        //if (wwn.matches("20[0-9a-f]{2}00a0b8[0-9a-f]{6}"))
        //    return new IBMDescription(brief, wwn);

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
        return null;
    }
}
