#! /bin/sh

if ./scripts/bnf2grammatica tests/inputs/text-brackets.txt
then
    echo OK
else
    echo Parse error
fi
