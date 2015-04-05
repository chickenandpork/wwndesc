depjardir = @datadir@/@PACKAGE@/jars
dist_depjar_DATA = $(JARS)


EXTRA_DIST = $(nodist_pkgdata_JAVA)
CONFIG_CLEAN_FILES = $(JARS)

AM_JAVACFLAGS= -cp $(top_builddir)/$(subdir)/$(JAVAROOT)

BUILDTMP=$(shell date +%s)

JARS := 
BUILT_SOURCES := $(JARS)


# for reference
poi-3.8-20120326.jar poi-examples-3.8-20120326.jar poi-excelant-3.8-20120326.jar poi-ooxml-3.8-20120326.jar poi-ooxml-schemas-3.8-20120326.jar poi-scratchpad-3.8-20120326.jar: poi-bin-3.8-20120326.tar.gz
	tar xOf $< '*/$@' > $@

commons-logging-1.1.jar junit-3.8.1.jar log4j-1.2.13.jar: poi-bin-3.8-20120326.tar.gz
	tar xOf $< '*/lib/$@' > $@

dom4j-1.6.1.jar stax-api-1.0.1.jar xmlbeans-2.3.0.jar: poi-bin-3.8-20120326.tar.gz
	tar xOf $< '*/ooxml-lib/$@' > $@

poi-bin-3.8-20120326.tar.gz:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || curl -Lo $@ http://archive.apache.org/dist/poi/release/bin/poi-bin-3.8-20120326.tar.gz || rm $@
	test \! -f $@ || tar tzf $@ >/dev/null || rm $@
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
	touch $@


if FETCH_GETOPTJAR

JARS += getopt.jar

# getopt.jar includes both org.smallfoot.getopt and gnu.getopt

getopt.jar:
	test -f $${TMPDIR-/tmp}/$(@F) && cp $${TMPDIR-/tmp}/$(@F) $@ || true
	test -f $@ || wget -O $@ http://chickenandpork.github.io/wwndesc/$(@F) || curl -Lo $@ http://chickenandpork.github.io/wwndesc/$(@F)
	test -f $${TMPDIR-/tmp}/$(@F) || cp $@ $${TMPDIR-/tmp}/$(@F)
endif


check-local: $(JARS)
	for i in $<; do jar tf $$i >/dev/null || exit 1; done

