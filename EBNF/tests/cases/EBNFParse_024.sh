#! /bin/sh

if java EBNFParse <<EOF
rule1 = "qoo-qoo" | "kva" rule2 ;
EOF
then
    echo OK
else
    echo Parse error
fi
