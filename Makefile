# --*- Makefile -*-------------------------------------------------------------
#$Author$
#$Date$ 
#$Revision$
#$URL$
#------------------------------------------------------------------------------

MAKECONF_FILES = ${filter-out %~, ${wildcard Makeconf*}}

ifneq ("${MAKECONF_FILES}","")
include ${MAKECONF_FILES}
endif

# A Makeconfig file might contain a line like the following one:

FIRST = BNF/

# The recursive Makefile will first descend to directories mentioned
# in ${FIRST}, and then into all the rest alphabetically:

DIRS  = ${FIRST} ${filter-out ${FIRST}, ${dir ${wildcard */Makefile}}}

.PHONY: all clean cleanAll distclean test tests

#------------------------------------------------------------------------------

all clean cleanAll distclean test tests:
	@for d in ${DIRS}; do test -d $$d && ${MAKE} -C $$d $@; done

#------------------------------------------------------------------------------

MAKELOCAL_FILES = ${filter-out %~, ${wildcard Makelocal*}}

ifneq ("${MAKELOCAL_FILES}","")
include ${MAKELOCAL_FILES}
endif
