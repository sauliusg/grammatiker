#! /bin/sh

if java EBNFParse <<EOF
(* Test spaces around the exclusion character: *)
letter = "A" | "B" | "C";
wowel  = "A" | "E" | "I" | "O" | "U";
consonant = letter - ( wowel );
EOF
then
    echo OK
else
    echo Parse error
fi
