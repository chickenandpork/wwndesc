/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;


    public class PureStorageDescription extends WWNDesc
    {
        public PureStorageDescription(String wwn)
        {
            super(wwn);
        }
        public static String pattern = "^524a937.*";
        public static boolean strong = false;

        public PureStorageDescription(boolean brief, String wwn)
        {
            super(brief, wwn);
        }
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
    }
