#! /bin/sh

SCRIPT=$(basename $0 .sh | sed 's/_[0-9][0-9]*$//')

if ./scripts/${SCRIPT} <<EOF
(* Test specials: *)
number = 10 * digit;
digit = "0" | "1";
EOF
then
    echo OK
else
    echo Parse error
fi
