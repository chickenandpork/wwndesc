nodist_pkgdata_JAVA  = WWNDescription.java
nodist_pkgdata_JAVA += version.java

nodist_pkgdata_DATA = @PACKAGE@.jar

EXTRA_DIST  = $(nodist_pkgdata_JAVA) WWNDescription.java.in
EXTRA_DIST += version.java.in
CONFIG_CLEAN_FILES = @PACKAGE@.jar $(notdir $(subdir)).jar


AM_JAVACFLAGS= -cp $(JAVAROOT):$(top_builddir)/depjars/poi-3.8-20120326.jar:$(top_builddir)/depjars/poi-ooxml-3.8-20120326.jar:$(top_builddir)/depjars/xmlbeans-2.3.0.jar:$(top_builddir)/depjars/poi-ooxml-schemas-3.8-20120326.jar:$(top_builddir)/depjars/dom4j-1.6.1.jar:$(top_builddir)/depjars/commons-io-2.3.jar:@GETOPTJAR@


BUILDTMP=$(shell date +%s)

BUILT_SOURCES = 

JAVAROOT = bld
BUILT_SOURCES += javaroot.stamp

javaroot.stamp: $(nodist_pkgdata_JAVA)
	mkdir -p $(JAVAROOT) && touch $@

$(notdir $(subdir)).jar: classnodist_pkgdata.stamp
	awk -v BUILDTOP="bld" '/^package / {gsub (";$$",""); gsub ("\\.","/",$$2); SUBS[$$2]=$$2 } END { FIRST=0; for (n in SUBS) { if (0 == FIRST++) printf "jar cvf $@"; printf " -C %s %s",BUILDTOP,n }; printf "\n"; }' $(nodist_pkgdata_JAVA) | $(SHELL)
	touch $(notdir $(subdir)).class

clean-local:
	CLASSES=`for i in $(nodist_pkgdata_JAVA); do awk -v FILE="$$i" '/^package / {gsub (";$$",""); gsub (".java",".class",FILE); gsub ("\\\\.","/",$$2); printf "%s/%s\n",$$2,FILE}' $(srcdir)/$$i; done`; (cd $(top_builddir)/ && rm -f $$CLASSES)


@PACKAGE@.jar: $(notdir $(subdir)).jar @GETOPTJAR@ $(BUILT_SOURCES) 
	@-rm -fr $(@D)/.workdir
	@mkdir -p $(@D)/.workdir
	@echo "Manifest-Version: 1.0" > $(@D)/.workdir/MANIFEST.MF
	@echo "Created-By: @PACKAGE@-@VERSION@" >> $(@D)/.workdir/MANIFEST.MF
	echo "Main-Class: org.smallfoot.wwn.WWNDescription" >> $(@D)/.workdir/MANIFEST.MF
	ln -f $^ $(@D)/.workdir/
	for i in $(^F) ; do case $$i in javaroot.stamp) ;; *) (cd $(@D)/.workdir && jar xf $$i) ;; esac; done
	(cd $(@D)/.workdir && jar cm MANIFEST.MF org) > $@
	rm -fr $(@D)/.workdir



