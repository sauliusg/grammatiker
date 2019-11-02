#! /bin/sh

SCRIPT=$(basename $0 .sh | sed 's/_[0-9][0-9]*$//')

if ./scripts/${SCRIPT} \
             tests/inputs/ternary.lark \
             tests/inputs/ternary_1.txt
then
    echo OK
else
    echo Parse error
fi
