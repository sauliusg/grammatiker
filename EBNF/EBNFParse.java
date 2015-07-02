//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;

class EBNFParse {
    public static void main( String [] args )
        throws ParserCreationException, ParserLogException {
	EBNFParser parser = 
	    new EBNFParser( new InputStreamReader( System.in ));
	parser.parse();
    }
}
