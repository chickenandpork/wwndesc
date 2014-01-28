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

public class EMCClariionDescription extends WWNDesc
    {
        public EMCClariionDescription(String wwn)
        {
            super(wwn);
        }
        public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
        {   
            if (wwn.matches("5006016.*"))
                return new EMCClariionDescription(wwn);
            else
                return null;
        }   

        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            /* for example start with 5006016A12345678 on http://blog.fosketts.net/toolbox/emc-symmetrix-wwn-calculator/ */
            /* http://goingvirtual.wordpress.com/2011/09/09/how-i-zone-vnx-storage-arrays/ */
            /* http://www.thesangeek.com/2011/02/09/sangeek-clariion-cx4-wwn-interpretations/ */
            /* http://clariionblogs.blogspot.com/2007/11/storage-processor-ports-wwns.html */
            /* http://architecting.it/2013/09/06/what-emc-should-have-done-with-vnx/ */

            BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

            /* our example now has A12345678 */
            BigInteger serPort[] = serDirPort.divideAndRemainder(new BigInteger("100000000",16));

            Character SP;
            switch (serPort[0].intValue() / 8)
            {
            case 0:
                SP='A';
                break;
            case 1:
                SP='B';
                break;
            default:
                SP='X';
            }
            return res + String.format("CL-%08x-SP%c%d",serPort[1].intValue(),SP,serPort[0].intValue() % 8);
        }
    }
