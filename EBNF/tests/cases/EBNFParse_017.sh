#! /bin/sh

if java EBNFParse <<EOF
(* Test nested comments: *)
(* a comment (* this is supposedly a bracketed_textual_comment *) *)
rule = definition;
EOF
then
    echo OK
else
    echo Parse error
fi
