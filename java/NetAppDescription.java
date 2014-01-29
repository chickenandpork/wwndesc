/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import java.math.BigInteger;
//import java.util.*;

/**
 * @file
 */

public class NetAppDescription extends WWNDesc
    {
        public NetAppDescription(String wwn)
        {
            super(wwn);
        }
        public NetAppDescription(boolean brief, String wwn)
        {
            super(brief,wwn);
        }

        public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
        {
            if (wwn.matches("500a098.*"))
            return new NetAppDescription(brief, wwn);
            else
                return null;
        }

        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            /* for example start with 500a:0983:0000:4060 on http://pic.dhe.ibm.com/infocenter/storwize/ic/index.jsp?topic=%2Fcom.ibm.storwize.v7000.641.doc%2Fsvc_netapplocwwpn_327ams.html */
            /* http://exchangeengine.wordpress.com/2011/07/25/how-to-find-out-wwpn-for-netapp-storage/ */   /* admin is ssh://naroot@host */
            /* http://www.datadisk.co.uk/html_docs/netapp/netapp_cs.htm */
            /* https://communities.netapp.com/thread/31347
             *
             * "lun show -v" shows a NetApp "Serial#: 20pa3?DGVO5f"
             * "igroup show" shows 3 interfaces:
             *            50:0a:09:81:98:3d:73:3b (virtual?)
             * 		  50:0a:09:82:98:3d:73:3b (virtual?)
             * 		  50:0a:09:80:88:3d:73:3b (physical?)
             * "fcp config" shows 1 WWNN, 2 WWPN:
             * 0a:   ONLINE <ADAPTER UP>  Loop  No Fabric
                 *       host address 0000ef
                 *       portname 50:0a:09:81:98:3d:73:3b  nodename 50:0a:09:80:88:3d:73:3b
                 *       mediatype auto speed auto
             *
             * 0b:   LINK NOT CONNECTED <ADAPTER UP>
                 *       host address 000000
                 *       portname 50:0a:09:82:98:3d:73:3b  nodename 50:0a:09:80:88:3d:73:3b
                 *       mediatype auto speed auto
             *
             * It would seem that:
             *	     5 is of course the type, 00a098 is of course the OUI
             *	     08 is the primary WWNN; 19, 29 are virtual interfaces that are expressed as WWPN (on Controller 2?) (18,28 on controller 1?)
             *	     8:3d:73:3b or some subset is a serial
             *
             * It may be possible that other igroups have additional controllers -- for example, 98.18.8, 98.28.8, 98.38.8, 98.39.8, 98.48.8, 98.49.8
             *
             * This is still only one controller's igroups; there is a separate redundant controller that may own disks.  Maybe more controllers.  Adapter 0c/0d on controller #2?
             *
             * http://exchangeengine.wordpress.com/2011/07/25/how-to-find-out-wwpn-for-netapp-storage/ argues: 0b has a ..98.29.8 of WWNN's 98.08.8
             *
             * http://www.ebay.com/itm/like/290900844309?lpid=82 implies (FAS2020 controller, WWN=5-00a098-0-00027540) that the -0- is significant, but seems to support that the 00a098-0 is the WWNN, >0 is a WWPN
             */

            BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

            /* our example now has 300004060 */
            BigInteger serPort[] = serDirPort.divideAndRemainder(new BigInteger("1000000",16));		/* serPort[0] is 300, serPort[1] is 004060 */

            /*
             * 08 -> WWNN
             * 18 -> WWPN C0-0a ?
             * 28 -> WWPN C0-0b ?
             * 19 -> WWPN C1-0a
             * 29 -> WWPN C1-0b
             */

            int controller = (serPort[0].intValue() / 0x010) % 8;
            int offset = (serPort[0].intValue() / 0x100) % 8;
            char SP = 'a';
            if (0 == offset) SP = 'N';
            else SP += (offset-1);

            if (brief)
                return res + String.format("NetApp-%04x-SP%d%c",serPort[1].intValue() % 0x10000,controller,SP);
            else
                return res + String.format("NetApp-%06x-iGrp%d-0%c",serPort[1].intValue(),controller,SP);
        }
    }
