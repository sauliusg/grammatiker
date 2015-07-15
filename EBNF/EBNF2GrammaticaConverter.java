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

    protected Node exitMetaIdentifier( Production node ) throws ParseException
    {
        String rule_name = ((Token)node.getChildAt(0)).getImage();

        for( int i = 1; i < node.getChildCount(); i++ ) {
            Token letter = (Token)node.getChildAt(i).getChildAt(0);
            rule_name += letter.getImage();
        }

        rule_name = rule_name.replace( "-", "_" );

        node.addValue( rule_name );

        return node;
    }

    private void registerUsedMetaIdentifiers( Node node )
    {
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "meta_identifier" )) {
                used_productions.put( child.getValue(0).toString(),
                                      child );
            } else {
                registerUsedMetaIdentifiers( child );
            }
        }
    }

    protected Node exitSyntaxRule( Production node ) throws ParseException
    {
        Node rule_name_node = node.getChildAt(0);
        String rule_name = rule_name_node.getValue(0).toString();

        defined_productions.put( rule_name, node );
        registerUsedMetaIdentifiers( node );

        return node;
    }

    private void printTerminalString( Node node )
    {
        int nchildren = node.getChildCount();
        for( int i = 0; i < nchildren; i++ ) {
            Node child = node.getChildAt(i);
            String cn = child.getName();
            if( cn.equals( "first_quote_symbol" ) ||
                cn.equals( "second_quote_symbol" )) {
                System.out.print( ((Token)child).getImage() );
            } else if( cn.equals( "first_terminal_character" ) ||
                       cn.equals( "second_terminal_character" )) {
                Token token = (Token)child.getChildAt(0);
                System.out.print( token.getImage() );
            }
        }
    }

    private void printSequence( Node node )
    {
        for( int i = 1; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt( i );
            if( child.getName().equals( "definitions_list" )) {
                printDefinitionsList( child );
            }
        }
    }

    private void printSpecial( Node node )
    {
        int nchildren = node.getChildCount();
        for( int i = 0; i < nchildren; i++ ) {
            Node child = node.getChildAt(i);
            String cn = child.getName();
            if( cn.equals( "special_sequence_symbol" )) {
                System.out.print( "\"" );
            } else if( cn.equals( "special_sequence_character" )) {
                Token character = (Token)child.getChildAt(0);
                String charimage = character.getImage();
                System.out.print( charimage );
            }
        }
    }

    private void printSyntacticPrimary( Node node )
    {
        String name = node.getName();

        if( name.equals( "meta_identifier" )) {
            System.out.print( node.getValue(0).toString() );
        } else if( name.equals( "terminal_string" )) {
            printTerminalString( node );
        } else if( name.equals( "optional_sequence" )) {
            System.out.print( "[ " );
            printDefinitionsList( node );
            System.out.print( " ]" );
        } else if( name.equals( "repeated_sequence" )) {
            System.out.print( "{ " );
            printSequence( node );
            System.out.print( " }" );
        } else if( name.equals( "grouped_sequence" )) {
            System.out.print( "( " );
            printSequence( node );
            System.out.print( " )" );
        } else if( name.equals( "special_sequence" )) {
            printSpecial( node );
        } else {
            System.out.print( "UNKNOWN-TERM-" + node.getName() );
        }
    }

    private void printSyntacticFactor( Node node )
    {
        Boolean reported = true;
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "syntactic_primary" )) {
                printSyntacticPrimary( child.getChildAt(0) );
            } else {
                if( !reported ) {
                    System.err.print( "NOTE, repetition counts " +
                                      "are not (yet) supported in " +
                                      "Grammatica" );
                    reported = true;
                }
            }
        }
    }

    private void printSyntacticTerm( Node node )
    {
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "syntactic_factor" )) {
                printSyntacticFactor( child );
            } else if( child.getName().equals( "except_symbol" )) {
                // System.err.print( "NOTE, syntactic exceptions " +
                //                   "are not (yet) supported in " +
                //                   "Grammatica" );
            }
        }
    }

    private void printSingleDefinition( Node node )
    {
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "syntactic_term" )) {
                printSyntacticTerm( child );
            } else if( child.getName().equals( "concatenate_symbol" )) {
                System.out.print( " " );
            }
        }        
    }

    private void printDefinitionsList( Node node )
    {
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "single_definition" )) {
                printSingleDefinition( child );
            } else if( child.getName().equals( "definitions_list" )) {
                printDefinitionsList( child );
            } else if( child.getName().equals
                       ( "definition_separator_symbol" )) {
                System.out.print( " | " );
            }
        }
    }

    private void printSyntaxRule( Node node )
    {
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "meta_identifier" )) {
                String rule_name = child.getValue(0).toString();
                System.out.print( rule_name + " " );
            } else if( child.getName().equals( "defining_symbol" )) {
                System.out.print( "= " );
            } else if( child.getName().equals( "definitions_list" )) {
                printDefinitionsList( child );
            }
        }

        System.out.println( ";" );
    }

    private void printRules( Node node, String prefix )
    {
        Node syntax = node.getChildAt(0);

        int children_count = syntax.getChildCount();
        for( int i = 0; i < children_count; i++ ) {
            Node child = syntax.getChildAt( i );
            if( child.getName().equals( "syntax_rule" )) {
                printSyntaxRule( child );
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
                                    "define rule '" +
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
        this.printRules( node, "" );
        this.checkDefinedProductions();
        return null;
    }

}
