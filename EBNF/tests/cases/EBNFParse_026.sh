#! /bin/sh

if java EBNFParse < tests/inputs/err1.ebnf
then
    echo OK
else
    echo Parse error
fi
