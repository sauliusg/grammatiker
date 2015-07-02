#! /bin/sh

if java EBNFParse <<EOF
(* Test spaces around the alternative, repetition and grouping braces: *)
rule-name = r1, {r2}, { r3 };
rule2_name = r1, [r2],[ r3 ] , [ r5 ];
rule_3_name =( grouped_rule1, grouped_rule_2 );
rule_4_name = ( grouped_rule1, grouped_rule_2) ;
EOF
then
    echo OK
else
    echo Parse error
fi
