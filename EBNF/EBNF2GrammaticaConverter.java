//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;

import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;
import net.percederberg.grammatica.parser.ParseException;

class EBNF2GrammaticaConverter extends EBNFAnalyzer {

    private int error_count = 0;

    private HashMap<String,Node> tokens = new HashMap<String,Node>();

    private HashMap<String,Node> defined_productions =
        new HashMap<String,Node>();

    private HashMap<String,Node> used_productions =
        new HashMap<String,Node>();

    public int getErrorCount() { return this.error_count; }

    protected void enterEbnf( Production node ) throws ParseException
    {
        // System.out.println( "Entering the 'BNF' node." );
    }

    protected void childEbnf( Production node, Node child )
        throws ParseException
    {
        // System.out.println( "Procesisng children of the 'BNF' node." );
        super.childEbnf( node, child );
    }

    protected Node exitLiteral( Production node ) throws ParseException
    {
        Token token = (Token)node.getChildAt(0);
        String token_string = token.getImage();

        tokens.put( token_string, token );

        return node;
    }

    protected Node exitRuleName( Production node ) throws ParseException
    {
        Token rule_name_token = (Token)node.getChildAt(1);
        String rule_name;

        rule_name = rule_name_token.getImage().replace( "-", "_" );
        rule_name_token.addValue( rule_name );
        used_productions.put( rule_name, node );

        return node;
    }

    protected Node exitProduction( Production node ) throws ParseException
    {
        Node rule_name_node = node.getChildAt(0);
        Token rule_name_token = (Token)rule_name_node.getChildAt(1);
        String rule_name = rule_name_token.getValue(0).toString();

        defined_productions.put( rule_name, node );

        return node;
    }

    private void printTerm( Node node )
    {
        Node term_def = node.getChildAt( 0 );

        if( term_def.getName().equals( "rule_name" )) {
            Token rule_name_token = (Token)term_def.getChildAt( 1 );
            System.out.print( rule_name_token.getValue(0) );
        } else {
            String term_name = term_def.getName();

            if( term_name.equals( "literal" )) {
                Token rule_name_token = (Token)term_def.getChildAt( 0 );
                String value = rule_name_token.getImage();
                System.out.print( value );
            } else if( term_name.equals( "optional" )) {
                System.out.print( "[ " );
                Node option_list = term_def.getChildAt( 1 );
                printTermList( option_list );
                System.out.print( " ]" );
            } else if( term_name.equals( "repeated" )) {
                System.out.print( "{ " );
                Node option_list = term_def.getChildAt( 1 );
                printTermList( option_list );
                System.out.print( " }" );
            } else if( term_name.equals( "group" )) {
                System.out.print( "( " );
                Node option_list = term_def.getChildAt( 1 );
                printTermList( option_list );
                System.out.print( " )" );
            } else {
                System.out.print( "UNKNOWN-TERM-" + term_name );
            }
        }
        if( node.getChildCount() > 1 ) {
            Token repetition = (Token)node.getChildAt( 1 );
            String text = repetition.getImage();
            if( text.equals( "+" ) || text.equals( "*" )) {
                System.out.print( text );
            } else {
                System.out.print( "UNKNOWN-REPETITION_CHAR-" + text );
            }
        }
    }

    private void printTermList( Node node )
    {
        Node term = node.getChildAt( 0 );
        printTerm( term );
        if( node.getChildCount() > 1 ) {
            System.out.print( " " );
            Node tail_list = node.getChildAt( 1 );
            printTermList( tail_list );
        }
    }

    private void printExpression( Node node )
    {
        Node term_list = node.getChildAt( 0 );
        printTermList( term_list );
        if( node.getChildCount() > 1 ) {
            System.out.print( " | " );
            Node tail_expression = node.getChildAt( 2 );
            printExpression( tail_expression );
        }
    }

    private void printProduction( Node node )
    {
        Node rule_expr = node.getChildAt(2);
        Node rule_name_node = node.getChildAt(0);
        Token rule_name_token = (Token)rule_name_node.getChildAt(1);

        System.out.print( rule_name_token.getValue(0) + " = " );

        printExpression( rule_expr );

        System.out.println( ";" );
    }

    private void printProductions( Node node, String prefix )
    {
        int children_count = node.getChildCount();
        for( int i = 0; i < children_count; i++ ) {
            Node child = node.getChildAt( i );
            if( child.getName().equals( "production" )) {
                printProduction( child );
            } else
                if( child.getName().equals( "production_list" )) {
                    printProductions( child, prefix );
                }
        }
    }

    private void printTokens()
    {
        String[] tokens;
        Set<String> token_set = this.tokens.keySet();
        tokens = token_set.toArray( new String[0] );
        Arrays.sort( tokens );


        int token_nr = 0;
        for( String token : tokens ) {
            System.out.println( "TOKEN_" + token_nr++ + " = " + token );
        }
    }

    private void checkDefinedProductions()
    {
        String[] productions;
        Set<String> used_production_set = this.used_productions.keySet();
        productions = used_production_set.toArray( new String[0] );
        Arrays.sort( productions );

        for( String production_name : productions ) {
            if( !defined_productions.containsKey( production_name )) {
                System.err.println( "ERROR, the grammar uses but does not " +
                                    "define production '" +
                                    production_name + "'" );
                this.error_count ++;
            }
        }
    }

    protected Node exitEbnf( Production node ) throws ParseException
    {
        System.out.println( "%header%" );
        System.out.println( "GRAMMARTYPE = \"LL\"" );
        System.out.println( "\n%tokens%" );
        this.printTokens();
        System.out.println( "\n%productions%" );
        this.printProductions( node, "" );
        this.checkDefinedProductions();
        return null;
    }

}
