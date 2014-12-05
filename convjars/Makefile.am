nodist_pkgdata_DATA = @PACKAGE@.jar

CONFIG_CLEAN_FILES = @PACKAGE@.jar

BUILDTMP=$(shell date +%s)

JAVAROOT = bld

# need to watch out for cross-device links where a user's separate /home/ space is not the /usr/share filesystem
# cannot use --reflink=auto because builder supports openSuSE-11.2, which is older than dirt (would make for a lousy start point to a brand-new product)
@PACKAGE@.jar: ../java/@PACKAGE@.jar @GETOPTJAR@
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.wwn.WWNDescription"  >> $(@D)/.workdir/MANIFEST.MF
	for i in $^ ; do ABS=`cd $$(dirname $$i) && pwd -P`/`basename $$i`; echo "unpacking $${ABS}"; (cd $(@D)/.workdir  && jar xf $${ABS}) ; done
	find $(@D)/.workdir '(' -name \*.java -o -name COPYING.LIB -o -name \*.html -o -name README -o -name Makefile -o -name ChangeLog -o -name GetoptDemo.class -o -name LANGUAGES -o -name \*.xml ')' -exec rm {} \;
	(cd $(@D)/.workdir && jar cm MANIFEST.MF gnu org) > $@
	rm -fr $(@D)/.workdir
	touch $@
