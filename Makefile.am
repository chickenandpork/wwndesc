DIST_SUBDIRS =	depjars java convjars tests-autotest
SUBDIRS =	depjars
if DO_JAVA
SUBDIRS +=	java convjars
endif
SUBDIRS +=	tests-autotest

ACLOCAL_AMFLAGS = -I m4

# if missing mkinstalldirs install-sh trigger errors, ignore the result code on the "system (cp ...)" command in automake
dnl CONFIG_CLEAN_FILES = aclocal.m4 config.guess config.sub config.h config.h.in ltconfig ltmain.sh missing mkinstalldirs install-sh Doxyfile @PACKAGE@.spec @PACKAGE@.jar changes.atom
CONFIG_STATUS_DEPENDENCIES = gitdescribe
EXTRA_DIST = @PACKAGE@.spec gitdescribe



if DO_DOXYGEN
doc: htdocs/README.dox Doxyfile
	$(DOXYGEN)
	if test -d $(<D)/.git; then (cd $(<D) && git add -A); if test -n "$(DOXYMESSAGE)" ; then (cd $(<D) && git commit -a -m "$(DOXYMESSAGE)" && git push); fi; else echo "$(<D) subdir not set as a subrepo; see http:// ..."; exit 1; fi

htdocs/README.dox: README.md Makefile
if DO_DOXYGEN_MD
	awk 'BEGIN { print "@file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } { printf "%s\n",$$0; } END {print "" }' $< > $@
else
	awk 'BEGIN { print "/** @file "; print "@mainpage JavaDoc API Markup for @PACKAGE@"; } /@PACKAGE@$$/ {next} /^=+$$/ {next} { printf " * %s\n",$$0; } END {print " */" }' $< > $@
endif DO_DOXYGEN_MD_FALSE
endif


if DO_ASTYLE
formatting: java/EMCClariionDescription.java java/EMCSymmetrixDescription.java java/EMCVMAXDescription.java java/EMCVPLEXDescription.java java/HDSVSPDescription.java java/HP3ParDescription.java java/HPDotHillDescription.java java/IBM3700Description.java java/IBMSVCDescription.java java/NetAppDescription.java java/OraclePillarDescription.java java/PureStorageDescription.java java/WWNDesc.java java/WWNDescription.java.in java/version.java
if DO_GITFORMATTING
	@git ls-files --other --exclude-standard --directory || { echo "###################### unignored local files: git add, git commit ?"; }
	@git diff --exit-code || { echo "###################### unstaged changes: git commit -a"; exit 1; }
	@git diff --cached --exit-code || { echo "###################### staged uncommitted changes: git commit"; exit 1; }
endif
	$(MAKE) check
	for i in $^; do $(ASTYLE) -A1 $$i; done
	$(MAKE) check
if DO_GITFORMATTING
	git commit -a -m 'formatting and whitespace'
endif DO_GITFORMATTING

endif DO_ASTYLE

if DO_GITFORMATTING
gitdescribe: .git/index
	git describe > $@
endif DO_GITFORMATTING


if HAVE_RPMBUILD
if DO_GITFORMATTING
re-dist-bzip2: gitdescribe
	rm -f @PACKAGE@.spec $(PACKAGE)-$(VERSION).tar.bz2
	touch configure
	$(MAKE) dist-bzip2
rpm: re-dist-bzip2
else
rpm: dist-bzip2
endif DO_GITFORMATTING_FALSE
	@RPMBUILD@ --define 'skipcheck 1' -ta $(PACKAGE)-$(VERSION).tar.bz2
endif
