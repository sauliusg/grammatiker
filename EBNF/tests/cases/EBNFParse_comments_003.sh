#! /bin/sh

if java EBNFParse < tests/inputs/comments3.txt
then
    echo OK
else
    echo Parse error
fi
