#! /bin/sh

if java BNFParse < tests/inputs/bnf.txt
then
    echo OK
else
    echo Parse error
fi
