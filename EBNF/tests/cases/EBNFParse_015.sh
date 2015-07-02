#! /bin/sh

if java EBNFParse <<EOF
(* Test terminals with "funny" characters: *)
magic-code = '#\#CIF_2.0' ;
EOF
then
    echo OK
else
    echo Parse error
fi
