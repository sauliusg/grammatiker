#! /bin/sh

if ./scripts/bnf-tree BNF.grammar tests/inputs/text1.txt
then
    echo OK
else
    echo Parse error
fi
