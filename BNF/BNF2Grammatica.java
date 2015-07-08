//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;

class BNF2Grammatica {
    public static void main( String [] args )
        throws ParserCreationException, ParserLogException {

	BNFParser parser = 
	    new BNFParser( new InputStreamReader( System.in ),
                           new BNF2GrammaticaConverter() );
	parser.parse();
    }
}
