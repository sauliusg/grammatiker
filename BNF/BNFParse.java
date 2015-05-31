//------------------------------------------------------------------------------
//$Author$
//$Date$
//$Revision$
//$URL$
//------------------------------------------------------------------------------

import java.io.*;

import net.percederberg.grammatica.parser.ParserCreationException;
import net.percederberg.grammatica.parser.ParserLogException;

class BNFParse {
    public static void main( String [] args )
        throws ParserCreationException, ParserLogException {
	BNFParser parser = 
	    new BNFParser( new InputStreamReader( System.in ));
	parser.parse();
    }
}
