/*-------------------------------------------------------------------------*\
* $Author$
* $Date$ 
* $Revision$
* $URL$
\*-------------------------------------------------------------------------*/

%{
/* exports: */
#include <bnf_y.h>

/* uses: */

%}

%union {
    long i;
    char *s
}

%token <s> __IDENTIFIER
%token <s> __INTEGER_CONST
%token <s> __REAL_CONST
%token <s> __STRING_CONST

%%


%%

void compiler_compile_file( char *filename, cexception_t *ex )
{
    cexception_t inner;

    cexception_guard( inner ) {
        yyin = fopenx( filename, "r", ex );
	if( yyparse() != 0 ) {
	    int errcount = compiler_yy_error_number();
	    cexception_raise( &inner, COMPILER_UNRECOVERABLE_ERROR,
			      cxprintf( "compiler could not recover "
					"from errors, quitting now\n"
					"%d error(s) detected\n",
					errcount ));
	} else {
	    int errcount = compiler_yy_error_number();
	    if( errcount != 0 ) {
	        cexception_raise( &inner, COMPILER_COMPILATION_ERROR,
				  cxprintf( "%d error(s) detected\n",
					    errcount ));
	    }
	}
    }
    cexception_catch {
        if( yyin ) fclosex( yyin, ex );
	cexception_reraise( inner, ex );
    }
    fclosex( yyin, ex );
}

static int errcount = 0;

int compiler_yy_error_number( void )
{
    return errcount;
}

void compiler_yy_reset_error_count( void )
{
    errcount = 0;
}

int yyerror( char *message )
{
    extern char *progname;
    /* if( YYRECOVERING ) return; */
    errcount++;
    fflush(NULL);
    if( strcmp( message, "syntax error" ) == 0 ) {
	message = "incorrect syntax";
    }
    fprintf(stderr, "%s: %s(%d,%d): ERROR, %s\n", 
	    progname, compiler->filename,
	    compiler_flex_current_line_number(),
	    compiler_flex_current_position(),
	    message );
    fprintf(stderr, "%s\n", compiler_flex_current_line() );
    fprintf(stderr, "%*s\n", compiler_flex_current_position(), "^" );
    fflush(NULL);
    return 0;
}

void compiler_yy_debug_on( void )
{
#ifdef YYDEBUG
    yydebug = 1;
#endif
}

void compiler_yy_debug_off( void )
{
#ifdef YYDEBUG
    yydebug = 0;
#endif
}
