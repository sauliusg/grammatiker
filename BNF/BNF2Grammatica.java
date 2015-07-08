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

        BNF2GrammaticaConverter converter;

	BNFParser parser = 
	    new BNFParser( new InputStreamReader( System.in ),
                           converter = new BNF2GrammaticaConverter() );
	parser.parse();

        if( converter.getErrorCount() > 0 ) {
            System.exit( 1 );
        }
    }
}
