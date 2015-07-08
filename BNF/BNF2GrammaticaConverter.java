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
        System.out.println( "Entering the 'BNF' node." );
    }

    protected void childBnf( Production node, Node child ) throws ParseException
    {
        System.out.println( "Procesisng children of the 'BNF' node." );
        super.childBnf( node, child );
    }

    private void printTree( Node node, String prefix )
    {
        int children_count = node.getChildCount();
        // System.out.println( "children_count = " + children_count );
        for( int i = 0; i < children_count; i++ ) {
            Node child = node.getChildAt( i );
            System.out.println( prefix + "child = " + child.toString() );
            printTree( child, prefix + "   " );
        }
    }

    protected Node exitBnf( Production node ) throws ParseException
    {
        System.out.println( "Reduced the 'BNF' node." );
        this.printTree( node, "" );
        return null;
    }

}
