#! /bin/sh

if java EBNFParse < tests/inputs/text3.txt
then
    echo OK
else
    echo Parse error
fi
