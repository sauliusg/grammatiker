#! /bin/sh

if java BNFParse <<EOF
## Test BNF comments.
// Also the C++ - style comments are permissible;
#  As well as Unix-style comments.
<grammar> ::= <rule>
EOF
then
    echo OK
else
    echo Parse error
fi
