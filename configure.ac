dnl ***********************************************************************
dnl Process this file with autoconf to produce a configure script.
dnl ***********************************************************************
#AC_INIT(install-sh)
AC_INIT(wwndesc, 1.0)
AC_CONFIG_MACRO_DIR([m4])

AC_CANONICAL_HOST

AM_INIT_AUTOMAKE([foreign dist-bzip2])
AC_CONFIG_TESTDIR([tests-autotest])

dnl ***********************************************************************
dnl Checks for programs used during the build
dnl ***********************************************************************
dnl AC_PROG_CC
dnl AC_PROG_CXX
AC_PROG_INSTALL
AC_PROG_MAKE_SET

AC_SUBST(AUTOM4TE, autom4te)

SVN_CHECK_JDK([1.6],[1.6])
AM_CONDITIONAL(DO_JAVA, test "$JAVAC" != none)

dnl ***********************************************************************
dnl Checks for programs used during test, markup, diagrams. etc.  I'm a huge fan
dnl of automation, and if I can type a single command to make, check, package,
dnl and push a new release, HECK YEAH!
dnl 
dnl This was based on my work for the fctools project (nee vitools) so some may
dnl prune out in the near future
dnl ***********************************************************************
AC_PATH_PROGS(PATHAWK, [awk gawk nawk], [/bin/awk])
AC_PATH_PROGS(XMLLINT, [xmllint], [missing xmllint])
AC_ARG_WITH(xmllintcommentindent,
[AS_HELP_STRING([--with-xmllintcommentindent],[Set XML-comment indentation fudge such as xmllint-2.6.x/2.7.x differences; default=detect])],
[
    case "$withval" in
	"yes") WITH_XMLLINTINDENT="  " ;;
	"no")  WITH_XMLLINTINDENT=""  ;;
	*) case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	    206*) WITH_XMLLINTINDENT="" ;;
	    *) WITH_XMLLINTINDENT="  "  ;;
	    esac ;;
    esac
],
[
    case $(xmllint --version 2>&1 | awk '/version/ {print $NF}') in
	206*) WITH_XMLLINTINDENT="" ;;
	*) WITH_XMLLINTINDENT="  "  ;;
    esac
])
AC_SUBST([XMLLINTINDENT], ${WITH_XMLLINTINDENT})

AC_ARG_WITH(doxygen,
[AS_HELP_STRING([--with-doxygen],[Indicate Doxygen allowing a without-; (default=search)])],
[
    case "$withval" in
        ""|"yes")
                AC_PATH_PROGS(DOXYGEN, [doxygen], missing, $PATH:/Applications/Doxygen.app/Contents/Resources)
                AC_PATH_PROGS(FOP, [fop], missing)
                ;;
        "no")  DOXYGEN=missing ;;
        *) DOXYGEN=$withval ;;
    esac

    case "$DOXYGEN" in
        "missing") HAVE_DOXYGEN_MARKDOWN=no ;;
        *)
            case `$DOXYGEN --version` in
                1.[0-7]* ) HAVE_DOXYGEN_MARKDOWN=no;;
                *) HAVE_DOXYGEN_MARKDOWN=yes;;
            esac ;;
    esac
],
[
        DOXYGEN=missing
        HAVE_DOXYGEN_MARKDOWN=no
])
AC_SUBST(HAVE_DOXYGEN_MARKDOWN)
AM_CONDITIONAL(DO_DOXYGEN_MD, test "x$HAVE_DOXYGEN_MARKDOWN" = "xyes")
AC_PATH_PROGS(DOT, [dot], missing, $PATH:/usr/local/bin:/usr/local/graph*/bin)
AC_SUBST(PATHDOT, [`dirname "${DOT}"`])
AM_CONDITIONAL(DO_DOT, test "x$DOT" != "xmissing")
if test "x$DOT" != "xmissing"; then HAVE_DOT=yes; else HAVE_DOT=no; fi
AC_SUBST(HAVE_DOT)
AM_CONDITIONAL(DO_DOXYGEN, test "x$DOXYGEN" != "xmissing" -a "x$DOT" != "xmissing")



AC_PATH_PROGS(ASTYLE, [astyle], missing, $PATH:$HOME/bin)
AM_CONDITIONAL(DO_ASTYLE, test "x$ASTYLE" != "xmissing")
AC_PATH_PROGS(RPMBUILD, [rpmbuild], [missing rpmbuild])
AM_CONDITIONAL(HAVE_RPMBUILD, [test "x$RPMBUILD" != "xmissing rpmbuild"])


AC_PATH_PROGS(GIT, [git], [missing])
AC_MSG_CHECKING([version agreement with git tag])
BUILDID=$( (git describe; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 2)
CHKVER=$( (git describe; cat gitdescribe) 2>/dev/null |head -1| cut -d "-" -f 1)
if test "${CHKVER}" = "${VERSION}"
then 
  AC_MSG_RESULT([ok])
else
  AC_MSG_WARN([version mismatch])
fi
AC_SUBST(BUILDID)


AC_CHECK_PROG(GIT, [git], [git],[missing git])
AM_CONDITIONAL(DO_GITFORMATTING, test "$GIT" != "missing git" -a -f .git/config)
AC_PATH_PROGS(ECHO, [echo], [missing])

AC_ARG_ENABLE(autotest,
[AS_HELP_STRING([--enable-autotest],[Enable autotest testcases; breaks: make distcheck; default=disabled])],
[
    case "$enableval" in
	"yes") ENAB=yes ;;
	"no")  ENAB=no  ;;
	*) ENAB=no ;;
    esac
],
[
    ENAB=no
])
AM_CONDITIONAL(DO_AUTOTEST, test "$ENAB" = yes)


AC_ARG_WITH(getoptplus,
[AS_HELP_STRING([--with-getoptplus],[Enable inclusion of an improved getopt .jar; default=search])],
[
    case "$withval" in
        "yes") WITH_GETOPTJAR=$((locate getopt.jar) | grep -v .workdir | head -1) ;;
        "no")  WITH_GETOPTJAR=no  ;;
        *) WITH_GETOPTJAR="$withval" ;;
    esac
],
[
AC_MSG_CHECKING([for getopt.jar])
    WITH_GETOPTJAR=$((locate getopt.jar; echo $(pwd)/depjars/getopt.jar) | grep -v .workdir | head -1) ;
AC_MSG_RESULT([${WITH_GETOPTJAR}])
])
AM_CONDITIONAL(DO_GETOPTJAR, test "$WITH_GETOPTJAR" != no)
AM_CONDITIONAL(FETCH_GETOPTJAR, test "x$WITH_GETOPTJAR" = "x${PWD}/depjars/getopt.jar")
AC_SUBST([GETOPTJAR], ${WITH_GETOPTJAR})



AC_ARG_WITH(sudopostmove,
[AS_HELP_STRING([--with-sudopostmove],[Enable sudopostmove of a built RPM; default=disabled])],
[
    case "$withval" in
        "no")  WITH_SUDOPOSTMOVE=no  ;;
        *) WITH_SUDOPOSTMOVE="$withval" ;;
    esac
],
[
    WITH_SUDOPOSTMOVE=no
])
AM_CONDITIONAL(DO_SUDOPOSTMOVE, test "$WITH_SUDOPOSTMOVE" != no)
AC_SUBST([SUDOPOSTMOVE], ${WITH_SUDOPOSTMOVE})



AC_CONFIG_FILES(Makefile
  convjars/Makefile 
  depjars/Makefile 
  java/Makefile java/WWNDescription.java
  java/version.java
  tests-autotest/Makefile tests-autotest/testsuite.at
)

if test "x$RPMBUILD" != "xmissing rpmbuild"
then
  AC_CONFIG_FILES(wwndesc.spec:pkg/rpm/wwndesc.spec.in)
fi

if test "x$DOXYGEN" != "xmissing"
then
  AC_CONFIG_FILES(Doxyfile:pkg/Doxyfile.in)
fi


AC_OUTPUT
