/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

/**
 * @file
 *
 * WWNDesc is the basic abstract ancestor: every descriptor is a descendent class extending
 * this one, adding logic to the toString() function's output.  toString() may choose to
 * use the brief variable return a more brief output.  Additionally, when called from
 * WWNDescription.getWWNDescriptor(), a static function is often used to choose whether
 * an instance of the WWNDesc descendent should be created.
 */

/**
 * WWNDesc is the basic generic class from which each vendor-specific pattern is built upon; similar to an abstract parent but populated methods
 */
public class WWNDesc
{
    protected BigInteger wwn;	/**< the WWN that the instance tried to describe */
    protected boolean brief;	/**< whether a more brief output should be offered in @ref toString() (ie the difference between "VMax-HK192601234-12gB" and "VMax-1234-12gB") .. the class may choose to ignore this value */

    protected DevRole role = DevRole.TARGET;	/**< role of the device: Target, Switch, Initiator */

    /**
     * create an instance with @ref brief set to false.  This is a convenience function to support the older constructor model
     * 
     * @throw java.lang.NullPointerException if the given wwn is null
     * 
     * @param wwn the WWN to evaluate and describe
     */
    public WWNDesc(String wwn)
    {
        this(false, wwn);
    }
    /**
     * create an instance with the given WWN.  Values given to the constructor are simply copied to internal variables for later use
     * 
     * @throw java.lang.NullPointerException if the given wwn is null
     * 
     * @param brief whether an abbreviated description is requested
     * @param wwn the WWN to evaluate and describe
     */
    public WWNDesc(boolean brief, String wwn) { this (brief, wwn, DevRole.TARGET); }
    /**
     * create an instance with the given WWN.  Values given to the constructor are simply copied to internal variables for later use
     * 
     * @throw java.lang.NullPointerException if the given wwn is null
     * 
     * @param brief whether an abbreviated description is requested
     * @param wwn the WWN to evaluate and describe
     * @param role the device role to assign
     */
    public WWNDesc(boolean brief, String wwn, DevRole role)
    {
	if (null == wwn) throw new java.lang.NullPointerException("wwn value must not be null");

        this.wwn = new BigInteger(wwn.replaceAll(":",""),16);
        this.brief = brief;
        this.role = role;
    }
    /**
     * describe the WWN: produce a short (shorter if this.brief = true) description suitable for use as an alias for this WWN
     * 
     * @return the alias for the WWN constructed from the WWN bit fields
     */
    public String toString()
    {
        return null;
    }

    /**
     * describe the WWPN's unique port label/index
     * 
     * @return the unique name for the port WWPN
     */
    public String descPort()
    {
        return null;
    }

    /** simple pass-thru class to define internal value for a Initiator in an idempotent way */
    public static class WWNDescInitiator extends WWNDesc {
        public WWNDescInitiator(String wwn) { super(false, wwn, DevRole.INITIATOR); }		/**< create a new Initiator device without brief naming capability */
        public WWNDescInitiator(boolean brief, String wwn) { super(brief, wwn, DevRole.INITIATOR); }		/**< create a new Initiator device with given brief capability */
	public static boolean isA(int role) { return ( 0 < (role & DevRole.INITIATORbit) ); }		/**< lightweight method to check for membership in this role */
    }

    /** simple pass-thru class to define internal value for a Switch in an idempotent way */
    public static class WWNDescSwitch extends WWNDesc {
        public WWNDescSwitch(String wwn) { super(false, wwn, DevRole.SWITCH); }		/**< create a new Switch device without brief naming capability */
        public WWNDescSwitch(boolean brief, String wwn) { super(brief, wwn, DevRole.SWITCH); }		/**< create a new Switch device with given brief capability */
	public static boolean isA(int role) { return ( 0 < (role & DevRole.SWITCHbit) ); }		/**< lightweight method to check for membership in this role */
    }

    /** simple pass-thru class to define internal value for a Target in an idempotent way */
    public static class WWNDescTarget extends WWNDesc {
        public WWNDescTarget(String wwn) { super(false, wwn, DevRole.TARGET); }		/**< create a new Target device without brief naming capability */
        public WWNDescTarget(boolean brief, String wwn) { super(brief, wwn, DevRole.TARGET); }		/**< create a new Target device with given brief capability */
	public static boolean isA(int role) { return ( 0 < (role & DevRole.TARGETbit) ); }		/**< lightweight method to check for membership in this role */
    }
}
