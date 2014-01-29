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

    /**
     * Descriptor for (Oracle) Pillar Data Systems' Service Lifecycle Management devices
     */
    public class OraclePillarDescription extends WWNDesc
    {
        public OraclePillarDescription(String wwn)
        {
            super(wwn);
        }

        public static WWNDesc getDesc(boolean /* ignored */ strong, boolean brief, String wwn)
        {
            if (wwn.matches("2[12][0-9a-fA-F][0-9a-fA-F]000b08.*"))
                return new OraclePillarDescription(wwn);
            else
                return null;
        }

        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            /* convert 2C00:000b08:SSSSSP into Axiom-SSSSS-cCpP */
            /* for example start with 2100:000b08:123450 and produce Axiom-12345-c0p0 */
            BigInteger serDirPort[] = wwn.divideAndRemainder(new BigInteger("1000000",16));
            /* serDirPort[0]=2100:000b08 ; serDirPort[1]=123450 */

            BigInteger twoContZeroZero[] = serDirPort[0].divideAndRemainder(new BigInteger("100000000",16));
            /* twoContZeroZero[0]=21; twoContZeroZero[1]=00:000b08 */
            BigInteger twoCont[] = twoContZeroZero[0].divideAndRemainder(new BigInteger("10",16));
            /* twoCont[0]=2 ; twoCont[1]=1*/

            BigInteger serialPort[] = serDirPort[1].divideAndRemainder(new BigInteger("10",16));
            /* serialPort[0]=12345 ; serialPort[1]=0 */

            return res + String.format("Axiom-%05X-c%sp%X",serialPort[0].intValue(), twoCont[1].intValue()-1, serialPort[1].intValue());
        }
    }
