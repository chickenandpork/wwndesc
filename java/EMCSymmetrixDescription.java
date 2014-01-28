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

public class EMCSymmetrixDescription extends WWNDesc
    {
        public EMCSymmetrixDescription(String wwn)
        {
            super(wwn);
        }
        public EMCSymmetrixDescription(boolean brief, String wwn)
        {
            super(brief, wwn);
        }

	public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
	{
	    if (wwn.matches("5006048.*"))
		return new EMCSymmetrixDescription(brief, wwn);
	    else
		return null;
	}
	
        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            /* for example start with 5006048ACCC86A32 on http://www.emcstorageinfo.com/2007/08/how-to-decode-symmetrix-world-wide.html */
            BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));

            /* our example now has ACCC86A32 */
            BigInteger sDirPort[] = serDirPort.divideAndRemainder(new BigInteger("40",16));
            BigInteger xDirPort[] = sDirPort[0].divideAndRemainder(new BigInteger("20000000",16));

            if (brief)
                res += String.format("Symm-%04d",xDirPort[1].mod(new BigInteger("10000",10)));
            else
                res += "Symm-"+xDirPort[1];

            BigInteger DirPort[] = sDirPort[1].divideAndRemainder(new BigInteger("10",16));
            res += String.format("-%02d",DirPort[1].intValue()+1);

            switch (4*xDirPort[0].intValue()+DirPort[0].intValue())
            {
            case 0:
                res += "aA";
                break;
            case 1:
                res += "bA";
                break;
            case 2:
                res += "aB";
                break;
            case 3:
                res += "bB";
                break;
            case 4:
                res += "cA";
                break;
            case 5:
                res += "dA";
                break;
            case 6:
                res += "cB";
                break;
            case 7:
                res += "dB";
                break;
            }

            return res;
        }
    }
