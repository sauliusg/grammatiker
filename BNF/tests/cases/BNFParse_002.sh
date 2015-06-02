#! /bin/sh

if java BNFParse < tests/inputs/text2.txt
then
    echo OK
else
    echo Parse error
fi
