//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;

import net.percederberg.grammatica.parser.Node;
import net.percederberg.grammatica.parser.Production;
import net.percederberg.grammatica.parser.Token;
import net.percederberg.grammatica.parser.ParseException;

class BNF2GrammaticaConverter extends BNFAnalyzer {

    protected void enterBnf( Production node ) throws ParseException
    {
        // System.out.println( "Entering the 'BNF' node." );
    }

    protected void childBnf( Production node, Node child ) throws ParseException
    {
        // System.out.println( "Procesisng children of the 'BNF' node." );
        super.childBnf( node, child );
    }

    private void printTerm( Node node )
    {
        Node term_def = node.getChildAt( 0 );

        if( term_def.getName().equals( "rule_name" )) {
            Token rule_name_token = (Token)term_def.getChildAt( 1 );
            System.out.print( rule_name_token.getImage() );
        } else
        if( term_def.getName().equals( "literal" )) {
            Token rule_name_token = (Token)term_def.getChildAt( 0 );
            String value = rule_name_token.getImage();
            System.out.print( value );
        } else {
            System.out.print( "UNKNOWN-TERM" );
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

        System.out.print( rule_name_token.getImage() + " = " );

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

    protected Node exitBnf( Production node ) throws ParseException
    {
        System.out.println( "%header%" );
        System.out.println( "GRAMMARTYPE = \"LL\"" );
        System.out.println( "\n%tokens%" );
        // this.printTokens( node );
        System.out.println( "\n%productions%" );
        this.printProductions( node, "" );
        return null;
    }

}
