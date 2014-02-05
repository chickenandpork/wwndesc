/* smallfoot.org is owned by allan.clark */
package org.smallfoot.wwn;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * @file
 * In situations where nicknames simply are not present, but we need descriptors in short-order,
 * the "--wwn=" or "-w" ooption can be used to get a description of what the most likely Nickname
 * would be.  For example, "java -jar wwndesc.jar -w 5006048ACCC86A32" results in "Symm-182500953-05bA"
 * matching http://www.emcstorageinfo.com/2007/08/how-to-decode-symmetrix-world-wide.html
 * Note that --briefestimate and --nobriefestimate configure for more brief estimate/suggested nicknames
 *
 * @section Examples
 *
 *  java -jar vict.jar --wwn 500610601234567
 *
 *  java -jar vict.jar --briefestimate --wwn 500610601234567
 *
 *  java -jar vict.jar --nobriefestimate --wwn 5000097301234564
 *
 * @section Known Issues
 */

public class WWNDescription
{
    public boolean briefWWNEstimate = false;

    /** test case: this matches a single item known to exist in the Demo Database */
    public class DemoTestDesc extends WWNDesc
    {
        public DemoTestDesc(String wwn)
        {
            super(wwn);
        }
        public String toString()
        {
            String res = super.toString();
            if (null == res) res = "";

            return res + "TestDevice";
        }
    }

    public WWNDesc getWWNDescriptor(String val)
    {
        val = val.replaceAll(":","").toLowerCase();
        WWNDesc d = null;

        if (null != (d = EMCSymmetrixDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = EMCClariionDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = EMCVMAXDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = EMCVPLEXDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = PureStorageDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = NetAppDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = HDSVSPDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = IBM3700Description.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = IBMSVCDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = HP3ParDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = HPDotHillDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;
        else if (null != (d = OraclePillarDescription.getDesc(/* strong*/ false, briefWWNEstimate, val)))
            return d;

        else if (val.toLowerCase().equalsIgnoreCase("200100110d123400"))
            return new DemoTestDesc(val);

        else
            return new WWNDesc(val);
    }



    /** usage messages are useful to those of us with short memories as well (hey, I just need to add swap!) */
    public void usage(String proc)
    {
        System.out.println("Usage: "+proc+" -V|--version|-H|--help");

        System.out.println("     : "+proc+" [--briefestimate] [--wwn|-w <WWN> [--wwn|-w <WWN> [--wwn|-w <WWN>]]]");
        System.out.println("   ie: "+proc+" -w 2100000b08123450");
        System.out.println("   ie: "+proc+" --wwn 2100000b08123450");
        System.out.println("   ie: "+proc+" --briefestimate -w 2100000b08123450");
        System.out.println("   ie: "+proc+" -w 21:00:00:0b:08:12:34:50");
        System.out.println("   ie: "+proc+" -w 21:00:000b08:123450");
    }



    /**
     * Main function, as you can tell.
     *
     * this function merely parses the command-line to dispatch actions to the meat of the meal above.
     * I'm using an actual GetOpt because, yes, I'm a UNIX/C hack, re-using 3-decades-old knowledge,
     * but this also preserves both extensibility and the ability to use longopts in scripts as a
     * way to self-document what the tool's doing.
     *
     * No real intelligence herein except the parse-and-redirect action.
     */
    public static void main(String args[])
    {
        WWNDescription m = new WWNDescription();

        java.util.Vector<LongOpt> options = new java.util.Vector();

        /* Always always ALWAYS provide a quick reference and a version output */
        options.add(new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'H'));
        options.add(new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'V'));

        options.add(new LongOpt("wwn", LongOpt.REQUIRED_ARGUMENT, null, 'w'));
        options.add(new LongOpt("briefestimate", LongOpt.NO_ARGUMENT, null, 0x1000));
        options.add(new LongOpt("nobriefestimate", LongOpt.NO_ARGUMENT, null, 0x1001));

        org.smallfoot.getopt.GetOpt g = new org.smallfoot.getopt.GetOpt("wwndesc", args, options);

        int c;
        while ((c = g.getopt()) != -1)
        {
            switch(c)
            {
            case 0x1000: /* switch to brief WWNDescs */
                m.briefWWNEstimate = true;
                break;
            case 0x1001: /* switch to verbose WWNDescs */
                m.briefWWNEstimate = false;
                break;
            case 'w': /* inspect a WWN */
            {
                WWNDesc wwn;
                if ((null == g.getOptarg()) || (16 != g.getOptarg().replaceAll(":","").length()))
                    System.out.println("no result: parameter "+g.getOptarg()+" needs to be a 16-digit hexadecimal");
                else if (null == (wwn = m.getWWNDescriptor(g.getOptarg())))
                    System.out.println("no result: WWN "+g.getOptarg()+" has no descriptor");
                else System.out.println(wwn.toString());
            }
            break;

            /*
             * Follows is the "house-keeping": versions, usage, and the catch-all for bad options.
             */
            case 'V':	// print the version and quit
            {
                System.out.println(g.consistentVersion("1.0-$Revision: 631 $")+"\n");
                return;
            }

            default:
            case '?':
                // during build, this is just a dump of options; in shipping, this falls-thru to usage.
                //System.out.println("option \""+c+"\" selected");
                //System.out.println("long index = "+g.getLongind());

            case 'H':
                m.usage(g.progname());
                break;
            }
        }

        /*
         * I had to add this since there seems to be a lack of dropping out when the options are done,
         * or a delay huge enough that my impatience is triggered (ie 2 seconds or more!).  Maybe
         * invesitgate this further when there's more time, as this is redundant.
         */
        java.lang.System.exit(0);
    }
}
