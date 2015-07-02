#! /bin/sh

if java EBNFParse <<EOF
empty = ;
EOF
then
    echo OK
else
    echo Parse error
fi
