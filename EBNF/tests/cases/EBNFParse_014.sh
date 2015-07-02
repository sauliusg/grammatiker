#! /bin/sh

if java EBNFParse <<EOF
(* Test names with hypens: *)
name = def;
name-def = def;
name2-def = def;
EOF
then
    echo OK
else
    echo Parse error
fi
