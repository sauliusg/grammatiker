#! /bin/sh

if java EBNFParse <<EOF
(* Test repetition symbol: *)
repeated = 100*"A";
repeated2 = 100 * "A";
EOF
then
    echo OK
else
    echo Parse error
fi
