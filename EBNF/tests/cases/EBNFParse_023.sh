#! /bin/sh

if java EBNFParse <<EOF
empty = | | | ;
empty1 = | | |;
empty2 = | | | rule2;
EOF
then
    echo OK
else
    echo Parse error
fi
