#! /bin/sh

if java EBNFParse <<EOF
empty = rule | ;
EOF
then
    echo OK
else
    echo Parse error
fi
