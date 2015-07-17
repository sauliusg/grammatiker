#! /bin/sh

if java EBNFParse <<EOF
rule1 = "qoo-qoo" | rule1 rule2 ;
EOF
then
    echo OK
else
    echo Parse error
fi
