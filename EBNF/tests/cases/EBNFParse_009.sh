#! /bin/sh

if java EBNFParse <<EOF
  (* This is an example of a grammar *)
  (* BNF syntax itself *)
syntax = rule | rule,syntax;
rule   = optwhitespace, "<", rulename, ">", optwhitespace,
         "::=",
         optwhitespace, expression, lineend;
optwhitespace=" ",optwhitespace|" ";
expression=list|list,"|",expression;
lineend=optwhitespace,EOL|lineend,lineend;
list=term|term,optwhitespace,list;
term=literal|"<",rulename,">";
literal='"',text,'"'|"'",text,"'";

EOF
then
    echo OK
else
    echo Parse error
fi
