#! /bin/sh

if java EBNFParse <<EOF
(* Test rules with dashes, underscores and spaces in their names: *)
rule-name = r1, r2;
rule2_name = r1, r2;
rule_name_with_spaces = another_rule_name_with_spaces, and_yet_another;
EOF
then
    echo OK
else
    echo Parse error
fi
