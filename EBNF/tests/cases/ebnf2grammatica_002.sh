#! /bin/sh

SCRIPT=$(basename $0 .sh | sed 's/_[0-9][0-9]*$//')

if ./scripts/${SCRIPT} <<EOF
(* Test specials: *)
number = [sign], digit, {digit}, [newline];
sign = "+" | "-";
digit = "0" | "1";
newline = ? \n ?;
EOF
then
    echo OK
else
    echo Parse error
fi
