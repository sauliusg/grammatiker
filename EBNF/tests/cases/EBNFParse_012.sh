#! /bin/sh

if java EBNFParse <<EOF
(* Test spaces around the alternative, repetition and grouping braces: *)
rule-name = r1, {r2}, { r 3 };
rule2_name = r1, [r2],[ r3 ] , [ r 5 ];
rule 3 name =( grouped rule1, grouped rule 2 );
rule 4 name = ( grouped rule1, grouped rule 2) ;
EOF
then
    echo OK
else
    echo Parse error
fi
