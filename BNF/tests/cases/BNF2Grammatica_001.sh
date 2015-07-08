#! /bin/sh

if java BNF2Grammatica < tests/inputs/bnf.txt
then
    echo "//" OK
else
    echo Parse error
fi
