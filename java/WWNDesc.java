/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;

public class WWNDesc
    {
        BigInteger wwn;
        boolean brief;

        public WWNDesc(String wwn)
        {
            this(false, wwn);
        }
        public WWNDesc(boolean brief, String wwn)
        {
            this.wwn = new BigInteger(wwn,16);
            this.brief = brief;
        }
        public String toString() { return null; }
    }
