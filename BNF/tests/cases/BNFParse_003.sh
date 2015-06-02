#! /bin/sh

if java BNFParse < tests/inputs/text3.txt
then
    echo OK
else
    echo Parse error
fi
