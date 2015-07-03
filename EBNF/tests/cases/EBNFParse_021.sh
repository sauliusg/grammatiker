#! /bin/sh

if java EBNFParse <<EOF
empty1 = | rule1 ;
empty2 = | rule2;
EOF
then
    echo OK
else
    echo Parse error
fi
