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
    }

    protected Node exitBnf( Production node ) throws ParseException
    {
        System.out.println( "Reduced the 'BNF' node." );
        return null;
    }

}
