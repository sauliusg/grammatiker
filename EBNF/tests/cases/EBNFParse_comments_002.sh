#! /bin/sh

if java EBNFParse < tests/inputs/comments2.txt
then
    echo OK
else
    echo Parse error
fi
