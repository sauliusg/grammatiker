#! /bin/sh

if java EBNFParse <<EOF
(* Test rules with dashes, underscores and spaces in their names: *)
rule-name = r1, r2;
rule2_name = r1, r2;
rule name with spaces = another rule name with spaces, and yet another;
EOF
then
    echo OK
else
    echo Parse error
fi
