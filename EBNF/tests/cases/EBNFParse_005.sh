#! /bin/sh

if java EBNFParse < tests/inputs/text5.txt
then
    echo OK
else
    echo Parse error
fi
