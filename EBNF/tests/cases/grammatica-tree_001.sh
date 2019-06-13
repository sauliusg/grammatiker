#! /bin/sh

SCRIPT=$(basename $0 .sh | sed 's/_[0-9][0-9]*$//')

if ./scripts/${SCRIPT} \
             tests/inputs/words-tabs.g \
             tests/inputs/tab-separated-words.txt
then
    echo OK
else
    echo Parse error
fi
