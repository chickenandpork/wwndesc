wwndesc
=======

WWNDesc is a tool I use to give functional usable nicknames/aliases to devices present on a SAN when a customer may not have them but needs identifiers quickly.  In some cases, these names are used as the canonical names: cases involving very large organizations where the name isn't a huge deal, but having a name is.

For example:
  you give me: 50000972081349ad
  I give you:  VMax-HK192601234-12gB (or VMax-1234-12gB)

BUILDING
========

This is a standard AutoTools build, so:

1) autoreconf -vfi

2) make install (or "make check" to include that plus run the tests)

3) make rpm (if so inclined)


java/wwndesc.jar is the proper built jar file; convjars is where "convenience jars" are built if 
you're less concerned with license purity and more concerned with:

1) your tests are bogus.  Let me test exactly what you tested; or

2) I need it working like yesterday.  Holy crap please help me.
      Gimme something to download immediately to make the pain stop.


We've all been there.  In both cases.  grab convjars/wwndesc.jar, it's not sanitary, but it works.


USAGE
=====

Example usage at:

    java -jar wwndesc.jar -H

Example Search for an Alias/Description:

    String wwn = "50000972081349ad";
    WWNDescription desc = new WWNDescription();
    
    WWNDesc alias;
    
    if (null != (alias = desc.getWWNDescriptor(wwn)))
        System.out.println(alias.toString());
    else
        System.out.println("no alias generated from "+wwn);


DOCUMENTATION
=============

The canonical code repository is at https://github.com/chickenandpork/wwndesc/

API documentation is based on JavaDoc-generated cross-referenced pages pushed to http://chickenandpork.github.io/wwndesc/ and pushed using:

    autoreconf -vfi && ./configure --with-doxygen && make doc && cd htdocs && git push
    autoreconf -vfi && ./configure --with-doxygen && make doc DOXYMESSAGE="a commit message"


DEVELOPMENT
===========

There are a few "housekeeping" items in the codebase, such as pkg/Doxyfile.in, the GITDESCRIPTION stuff in the Makefile.am, etc.

A bit more unusual: java/version.java (http://chickenandpork.github.io/wwndesc/classorg_1_1smallfoot_1_1wwn_1_1version.html) is created from java/version.java.in to include the version in a simple parseable output to ensure that the jar file is read, and the version itself can be read, interpreted, and passed forth in autoconf-generated configure scripts in dependent projects.

The basic structure of this project is where WWNDescription's (http://chickenandpork.github.io/wwndesc/classorg_1_1smallfoot_1_1wwn_1_1WWNDescription.html) getWWNDescriptor(String wwn) searches through a maintained list of WWNDesc descendents for a OUI or pattern that matches; the WWNDesc descendent is instantiated, and returned, or getWWNDescription() returns a null.

@see org.smallfoot.wwn.WWNDescription.getWWNDescriptor

Adding new WWN patterns herein is a case of either extending the existing patterns as manufacturers evolve their usage, or adding new descendent classes of WWNDesc, and adding their classnames to the getWWNDescriptor() class.  You can see that the getWWNDescriptor() function call is relatively simple at this point: the logic for whether a WWNDesc descendent matches a given pattern is moved to static function calls.  For example, if NetAddDescription::getDesc(boolean, boolean, String) decides that the given WWN matches, it'll return an instance of itself; otherwise, it'll return a null.  This was done to allow more than one pattern to be represented in the WWNDesc for later expansion: for example, a HDS super-class that returns configured subclasses, or configures instances of itself for different personalities.


