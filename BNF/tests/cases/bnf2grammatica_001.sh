#! /bin/sh

if ./scripts/bnf2grammatica tests/inputs/bnf.txt
then
    echo OK
else
    echo Parse error
fi
