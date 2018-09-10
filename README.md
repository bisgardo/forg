forg
====

Very basic scripts for computing checksums of files and locate duplicates.

Compile jars
------------

    mvn clean package # Dumps two jars: target/treesum.jar and target/duplings.jar

Scripts
-------

- `treesum`: Visits directory tree and outputs Murmur3F checksum for each file to stdout (separated by TAB).
- `duplings`:  the output of `treesum` on stdin and outputs a TAB-separated list of duplicate files.

Usage
-----

Find duplicate files in file tree rooted at dir `<dir>`:

    java -jar target/treesum.jar <dir> | java -jar target/duplings.jar > dups.txt
    less dups.txt

Do someting about the duplicates:

    cat dups.txt | while IFS=$'\t' read -ra dups; do
        # dups: Bash array of duplicates...
    done
