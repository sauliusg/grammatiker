#! /bin/sh

if java BNFParse < tests/inputs/text4.txt
then
    echo OK
else
    echo Parse error
fi
