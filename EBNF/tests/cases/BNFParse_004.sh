#! /bin/sh

if java EBNFParse < tests/inputs/text4.txt
then
    echo OK
else
    echo Parse error
fi
