#! /bin/sh

if java EBNFParse <<EOF
(* *)rule=definition;
EOF
then
    echo OK
else
    echo Parse error
fi
