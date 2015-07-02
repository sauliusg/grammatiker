#! /bin/sh

if java EBNFParse < tests/inputs/text2.txt
then
    echo OK
else
    echo Parse error
fi
