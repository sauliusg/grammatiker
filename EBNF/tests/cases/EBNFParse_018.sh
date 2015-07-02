#! /bin/sh

if java EBNFParse <<EOF
(* Test quotes in comments: *)
(* rule's thing -- unbalanced single quote if you remove this one: ' *)
(* a "string in a comment" *)
repeated2 = 100 * "A";
EOF
then
    echo OK
else
    echo Parse error
fi
