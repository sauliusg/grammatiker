/*---------------------------------------------------------------------------*\
**$Author$
**$Date$ 
**$Revision$
**$URL$
\*---------------------------------------------------------------------------*/

/* The main program of the compiler and interpreter. Compiles and runs
   programs written in SL/SC languages.
 */

/* uses: */

#include <stdio.h>
#include <string.h>
#include <getoptions.h>
#include <bnf_y.h>
#include <cexceptions.h>

static char *source_URL = "$URL$";

static char *usage_text[2] = {

"Compile a BNF grammar\n",

"Options:\n"

/* "  --version  print program version (SVN Id) and exit\n" */

"  --help     print short usage message (this message) and exit\n"
};

static void usage( int argc, char *argv[], int *i, option_t *option,
		   cexception_t * ex )
{
    puts( usage_text[0] );
    puts( "Usage:" );
    printf( "   %s --options programs*.snl\n", argv[0] );
    printf( "   %s --options programs*.snl -- "
            "--program-options program-args\n", argv[0] );
    printf( "   %s --options -- program.snl "
            "--program-options program-args\n\n", argv[0] );
    puts( usage_text[1] );
    exit( 0 );
};

static void version( int argc, char *argv[], int *i, option_t *option,
                     cexception_t * ex )
{
    printf( "%s svnversion %s\n", argv[0], SVN_VERSION );
    printf( "%s\n", source_URL );
    exit( 0 );
}

/* static option_value_t verbose; */

static option_t options[] = {
#if 0
  { "-q", "--quiet",        OT_BOOLEAN_FALSE, &verbose },
  { "-q-","--no-quiet",     OT_BOOLEAN_TRUE,  &verbose },
  { NULL, "--vebose",       OT_BOOLEAN_TRUE,  &verbose },
#endif
  { NULL, "--help",         OT_FUNCTION,      NULL, &usage },
  { NULL, "--options",      OT_FUNCTION,      NULL, &usage },
  { NULL, "--version",      OT_FUNCTION,      NULL, &version },
  { NULL }
};

char *progname;

int main( int argc, char *argv[], char *env[] )
{
  cexception_t inner;
  char ** volatile files = NULL;
  int i;

  progname = argv[0];

  cexception_guard( inner ) {
      files = get_optionsx( argc, argv, options, &inner );
  }
  cexception_catch {
      fprintf( stderr, "%s: %s\n", progname, cexception_message( &inner ));
      exit(1);
  }

  if( files[0] == NULL ) {
      fprintf( stderr, "%s: Usage: %s grammar.bnf\n", progname, progname );
      fprintf( stderr, "%s: please try '%s --help' for more options\n",
               progname, progname );
      exit(2);
  }

  cexception_guard( inner ) {
      for( i = 0; files[i] != NULL; i++ ) {
          compiler_compile_file( files[i], &inner );
      }
  }
  cexception_catch {
      fprintf( stderr, "%s: %s\n", argv[0], cexception_message( &inner ));
      exit(3);
  }

  return 0;
}
