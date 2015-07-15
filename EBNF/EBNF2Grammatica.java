//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;

class EBNF2Grammatica {
    public static void main( String [] args )
        throws ParserCreationException, ParserLogException {

        EBNF2GrammaticaConverter converter;

	EBNFParser parser = 
	    new EBNFParser( new InputStreamReader( System.in ),
                            converter = new EBNF2GrammaticaConverter() );
	parser.parse();

        if( converter.getErrorCount() > 0 ) {
            System.exit( 1 );
        }
    }
}
