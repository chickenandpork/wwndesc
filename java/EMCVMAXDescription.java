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

public class EMCVMAXDescription extends WWNDesc
    {
        public EMCVMAXDescription(String wwn)
        {
            super(wwn);
        }
        public EMCVMAXDescription(boolean brief, String wwn)
        {
            super(brief,wwn);
        }
        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            /* for example start with 50000972081349AD on https://community.emc.com/thread/118881 */
            BigInteger serDirPort = wwn.subtract(wwn.shiftRight(36).shiftLeft(36));
            res += "VMax-";

            /* our example now has 2081349AD */
            /* get the first four of the serial: 4 bits: {reserved/0} {A}{B} {reserved/0} */
            BigInteger countDirPort[] = serDirPort.divideAndRemainder(new BigInteger("100000000",16));     /* countDirPort[0] = 2, countDirPort[1] = 081349AD */
            BigInteger modelDirPort[] = countDirPort[1].divideAndRemainder(new BigInteger("8000000",16));     /* modelDirPort[0] = 1, modelDirPort[1] = 01349AD */
            if (!brief)
            {
                switch (countDirPort[0].intValue()/2)
                {
                case 1: /* 0-01-0 == USA/HK19 seen on 20k VMax? */ /* 0-01-1 == USA/40k VMax? */
                    res += "HK19";
                    break;
                case 2: /* 0-10-0 == Ireland/CK29 */
                    res += "CK29";
                    break;
                default:/* unknown */
                    res += "Unkn";
                }

                /* our example now has 081349AD - get the 4-bit model code */
                //BigInteger modelDirPort[] = countDirPort[1].divideAndRemainder(new BigInteger("8000000",16));     /* modelDirPort[0] = 1, modelDirPort[1] = 01349AD */
                switch (modelDirPort[0].intValue())
                {
                case 0: /* 00000: 40k V-Max */
                    res += "46";
                    break;
                case 1: /* 00001: 20k V-Max? */
                    res += "26";
                    break;
                case 24: /* 11000: V-Max SE */
                    res += "49";
                    break;
                default:/* unknown */
                    res += "UN";
                }
            }

            /* our example now has 081349AD - get the 5-digit serial number ending */
            BigInteger serialDirPort[] = modelDirPort[1].divideAndRemainder(new BigInteger("400",16));     /* serialDirPort[0] = 004d2, serialDirPort[1] = 1AD */

            /* our example now has 1AD - get the 4-bit Processor code */
            BigInteger procDirPort[] = serialDirPort[1].divideAndRemainder(new BigInteger("40",16));     /* procDirPort[0] = 6, procDirPort[1] = 2D */

            /* our example now has 2D - get the 4-bit Director code */
            BigInteger dirDirPort[] = procDirPort[1].divideAndRemainder(new BigInteger("4",16));     /* dirDirPort[0] = C, dirDirPort[1] = %01 */
            return res + String.format("%0"+(brief?4:5)+"d-%02d%c%c",serialDirPort[0].intValue(),dirDirPort[0].intValue()+1,procDirPort[0].intValue()+'a',dirDirPort[1].intValue()+'A');
        }
    }
