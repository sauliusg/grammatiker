#! /bin/sh

if java EBNFParse < tests/inputs/comments4.txt
then
    echo OK
else
    echo Parse error
fi
