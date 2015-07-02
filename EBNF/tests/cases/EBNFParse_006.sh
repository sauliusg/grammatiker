#! /bin/sh

if java EBNFParse < tests/inputs/ebnf.txt
then
    echo OK
else
    echo Parse error
fi
