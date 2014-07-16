nodist_java_JAVA  = $(srcdir)/DevRole.java
nodist_java_JAVA += $(srcdir)/AIXLPARServerDescription.java $(srcdir)/CiscoSwitchDescription.java $(srcdir)/CiscoUCSServerDescription.java
nodist_java_JAVA += $(srcdir)/EMCClariionDescription.java $(srcdir)/EMCSymmetrixDescription.java $(srcdir)/EMCVMAXDescription.java $(srcdir)/EMCVPLEXDescription.java
nodist_java_JAVA += $(srcdir)/HDSVSPDescription.java $(srcdir)/IBMDescription.java $(srcdir)/IBM3700Description.java $(srcdir)/IBM500507630Description.java $(srcdir)/IBMSVCDescription.java
nodist_java_JAVA += $(srcdir)/HP3ParDescription.java $(srcdir)/HPDotHillDescription.java $(srcdir)/HPVirtualServerDescription.java
nodist_java_JAVA += $(srcdir)/NetAppDescription.java $(srcdir)/OraclePillarDescription.java
nodist_java_JAVA += $(srcdir)/PureStorageDescription.java
nodist_java_JAVA += $(srcdir)/SymbiosDescription.java
nodist_java_JAVA += $(srcdir)/WWNDesc.java $(srcdir)/WWNDescription.java
nodist_java_JAVA += $(srcdir)/version.java
javadir = $(pkgdir)

nodist_java_DATA = @PACKAGE@.jar

EXTRA_DIST  = $(nodist_java_JAVA) WWNDescription.java.in
EXTRA_DIST += version.java.in
CONFIG_CLEAN_FILES = @PACKAGE@.jar


AM_JAVACFLAGS= @JAVAC_FLAGS@ -cp $(JAVAROOT):$(top_builddir)/depjars/poi-3.8-20120326.jar:$(top_builddir)/depjars/poi-ooxml-3.8-20120326.jar:$(top_builddir)/depjars/xmlbeans-2.3.0.jar:$(top_builddir)/depjars/poi-ooxml-schemas-3.8-20120326.jar:$(top_builddir)/depjars/dom4j-1.6.1.jar:$(top_builddir)/depjars/commons-io-2.3.jar:@GETOPTJAR@


BUILDTMP=$(shell date +%s)

BUILT_SOURCES = 

JAVAROOT = bld
BUILT_SOURCES += javaroot.stamp

javaroot.stamp: $(nodist_java_JAVA)
	mkdir -p $(JAVAROOT) && touch $@

@PACKAGE@.jar: classjava.stamp
	awk -v BUILDTOP="bld" '/^package / {gsub (";$$",""); gsub ("\\.","/",$$2); SUBS[$$2]=$$2 } END { FIRST=0; for (n in SUBS) { if (0 == FIRST++) printf "jar cvf $@"; printf " -C %s %s",BUILDTOP,n }; printf "\n"; }' $(nodist_java_JAVA) | $(SHELL)
	touch $(notdir $(subdir)).class

clean-local:
	CLASSES=`for i in $(nodist_java_JAVA); do awk -v FILE="$$i" '/^package / {gsub (";$$",""); gsub (".java",".class",FILE); gsub ("\\\\.","/",$$2); printf "%s/%s\n",$$2,FILE}' $(srcdir)/$$i; done`; (cd $(top_builddir)/ && rm -f $$CLASSES)
	rm -fr bld javaroot.stamp WWNDescription.java version.java

