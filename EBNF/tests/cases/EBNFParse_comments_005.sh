#! /bin/sh

if java EBNFParse <<EOF
(*
  Test the multi-line comments
*)
EOF
then
    echo OK
else
    echo Parse error
fi
