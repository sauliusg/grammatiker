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
    private int last_special = 0;

    private HashMap<String,Node> tokens = new HashMap<String,Node>();
    private HashMap<String,String> toknames =
        new HashMap<String,String>();

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

    protected Node exitInteger( Production node )
        throws ParseException
    {
        String int_image = "";

        for( int i = 0; i < node.getChildCount(); i++ ) {
            Token child = (Token)node.getChildAt(i);
            String digit = child.getImage();
            int_image += digit;
        }

        int int_value = Integer.parseInt( int_image );
        node.addValue( int_value );
        node.addValue( int_image );

        return node;
    }

    protected Node exitTerminalString( Production node )
        throws ParseException
    {
        String terminal = "";
        int nchildren = node.getChildCount();
        for( int i = 0; i < nchildren; i++ ) {
            Node child = node.getChildAt(i);
            String cn = child.getName();
            if( cn.equals( "first_quote_symbol" ) ||
                cn.equals( "second_quote_symbol" )) {
                String ch = ((Token)child).getImage();
                terminal += ch;
            } else if( cn.equals( "first_terminal_character" ) ||
                       cn.equals( "second_terminal_character" )) {
                Token token = (Token)child.getChildAt(0);
                String ch = token.getImage();
                terminal += ch;
            }
        }
        node.addValue( terminal );
        tokens.put( terminal, node );
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

    protected Node exitSpecialSequence( Production node )
    {
        String special = "";
        String brace = "<<";

        int nchildren = node.getChildCount();
        for( int i = 0; i < nchildren; i++ ) {
            Node child = node.getChildAt(i);
            String cn = child.getName();
            if( cn.equals( "special_sequence_symbol" )) {
                special += brace;
                brace = ">>";
            } else if( cn.equals( "special_sequence_character" )) {
                Token character = (Token)child.getChildAt(0);
                String charimage = character.getImage();
                if( charimage.equals( "U" )) {
                    special += "\\";
                } else if( charimage.equals( "+" )) {
                    special += "x";
                } else if( charimage.equals( " " )) {
                    // skip spaces;
                } else {
                    special += charimage;
                }
            }
        }
        String special_name = "SPECIAL_" + last_special++;
        node.addValue( special );
        node.addValue( special_name );
        tokens.put( special, node );
        toknames.put( special, special_name );
        return node;
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
        System.out.print( node.getValue(0).toString() );
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

    private void printSpecialSequence( Node node )
    {
        System.out.print( node.getValue(1).toString() );
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
            printSpecialSequence( node );
        } else {
            System.out.print( "UNKNOWN-TERM-" + node.getName() );
        }
    }

    private void printSyntacticFactor( Node node )
    {
        Boolean reported = true;
        Boolean comment_started = false;
        for( int i = 0; i < node.getChildCount(); i ++ ) {
            Node child = node.getChildAt(i);
            if( child.getName().equals( "syntactic_primary" )) {
                if( comment_started ) {
                    // System.out.print( " */ " );
                    // System.out.print( "{ " );
                }
                printSyntacticPrimary( child.getChildAt(0) );
                if( comment_started ) {
                    // System.out.print( " } " );
                }
                comment_started = false;
            } else {
                if( !reported ) {
                    System.err.print( "NOTE, repetition counts " +
                                      "are not (yet) supported in " +
                                      "Grammatica" );
                    reported = true;
                }
                if( child.getName().equals( "integer" )) {
                    // System.out.print( " /* " );
                    comment_started = true;
                    System.out.print( (int)child.getValue(0) );
                    System.out.print( " * " );
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
                System.out.print( " /* - " );
                for( i++; i < node.getChildCount(); i++ ) {
                    Node exception_child = node.getChildAt(i);
                    printSyntacticFactor( exception_child.getChildAt(0) );
                }
                System.out.print( " */" );
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
            if( toknames.containsKey( token )) {
                System.out.println( toknames.get( token ) + " = " + token );
            } else {
                System.out.println( "TOKEN_" + token_nr++ + " = " + token );
            }
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
