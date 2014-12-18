/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

/** @file */

/**
 * This class seeks to provide simple bitfield type-checking of a device in a fabric
 * so that I can use type-specific actions in a dependent project.  By using this
 * class to lift the assumption that all known devices are Targets, I can create
 * additional possible matches that can be additionally checked when asked for more
 * than default target types.  This lets me later package the logic of subparts of
 * a device (WWNN and WWPN) into the device logic itself, packaging up in a single
 * place and avoiding needless diuplication without significantly stressing this
 * model
 *
 * @see http://www.javaworld.com/article/2076970/core-java/create-enumerated-constants-in-java.html
 */
public final class DevRole
{
    public final int value;		/**< what bit-field is this constant? */
    private static int max = 1;	/**< global singleton to keep track of the max DevRole.value so far */

    /** create a new constant, but private to stop extensions or descendents */
    private DevRole()
    {
        this.value = max;
        max <<= 1;
    }

    /**
     * return true if the device is an instance of the other.  For example: if (theTapeDevice.isA(TAPE)) {...}
     *
     * @param proto device to compare to
     *
     * @return whether device is an instance
     */
    public boolean isA(DevRole proto)
    {
        return isA (proto.value);
    }

    /**
     * return true if the device is an instance of the other.  For example: if (theTapeDevice.isA(TAPE)) {...}
     *
     * @param proto device to compare to
     *
     * @return whether device is an instance
     */
    public boolean isA(int proto)
    {
        return (0 < (value & proto));
    }

    /**
     * convenience accessor for this.max
     *
     * @return DevRole#max
     */
    public static int max()
    {
        return max;    /**< report highest left-shift bit value ever created; someDevice.value == (someDevice.value & (max()-1)) */
    }
    public static final DevRole TARGET = new DevRole();		/**< a device that offers storage via >1 LUNs ; contrast a TAPE offers only one LUN */
    static final int TARGETbit = TARGET.value;
    public static final DevRole INITIATOR = new DevRole();	/**< a device that initiates a storage request: a server, or a Storage Virtualizer port that talks to backing storage */
    static final int INITIATORbit = INITIATOR.value;
    public static final DevRole SWITCH = new DevRole();		/**< a device that routes/passes traffic: Switch, NPV device/back-of-rack, FCR */
    static final int SWITCHbit = SWITCH.value;
}
