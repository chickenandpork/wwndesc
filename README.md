wwndesc
=======

WWNDesc is a tool I use to give functional usable nicknames/aliases to devices present on a SAN when a customer may not have them but needs identifiers quickly.  In some cases, these names are used as the canonical names: cases involving very large organizations where the name isn't a huge deal, but having a name is.


BUILDING

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

Example usage at:

    java -jar wwndesc.jar -H

