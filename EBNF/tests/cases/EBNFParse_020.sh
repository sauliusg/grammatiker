#! /bin/sh

if java EBNFParse <<EOF
(* Test quotes in comments: *)
(* Lone question-marks in comments are now permitted? *)
(* a "string in a comment" *)
repeated2 = 100 * "A";
EOF
then
    echo OK
else
    echo Parse error
fi
