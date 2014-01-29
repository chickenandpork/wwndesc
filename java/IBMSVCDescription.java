/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

//import com.adventnet.snmp.beans.SnmpTarget;
//import gnu.getopt.Getopt;
//import gnu.getopt.LongOpt;
//import java.io.*;
import java.math.BigInteger;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.*;
//import javax.activation.DataSource;
//import javax.activation.URLDataSource;
//import org.smallfoot.parser.ParserTee;
//import org.smallfoot.parser.zone.AliShowZoneParser;
//import org.smallfoot.parser.zone.Alias4Parser;
//import org.smallfoot.parser.zone.BNAZoneParser;
//import org.smallfoot.parser.zone.DeviceAliasParser;
//import org.smallfoot.parser.zone.NicknameParser;
//import org.smallfoot.parser.zone.ZPAliasEntry;
//import org.smallfoot.parser.zone.ZoneParser;
//import org.smallfoot.filexfer.FileTransferWinch;
//import org.smallfoot.filexfer.FileTransferWinch.FileTransferAbortedException;
//import org.smallfoot.filexfer.FileTransferWinch.FileTransferChecksumMismatchException;
//import org.smallfoot.filexfer.FileTransferWinch.FileTransferDataTransferException;
//import org.smallfoot.filexfer.FileTransferWinch.FileTransferIllegalFSMReplyException;
//import org.smallfoot.filexfer.FileTransferWinch.FileTransferWinchException;
//import org.smallfoot.filexfer.WinchFactory;

/**
 * @file
 */

public class IBMSVCDescription extends WWNDesc
    {
        public IBMSVCDescription(String wwn)
        {
            super(wwn);
        }
        public IBMSVCDescription(boolean brief, String wwn)
        {
            super(brief, wwn);
        }

        public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
        {
            if (wwn.matches("5005076801.*"))
            return new IBMSVCDescription(brief, wwn);
            else
                return null;
        }

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
