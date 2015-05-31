# --*- Makefile -*--
#------------------------------------------------------------------------------
#$Author$
#$Date$ 
#$Revision$
#$URL$
#------------------------------------------------------------------------------

MAKECONF_FILES = ${filter-out %~, ${wildcard Makeconf*}}

ifneq ("${MAKECONF_FILES}","")
include ${MAKECONF_FILES}
endif

#------------------------------------------------------------------------------

SRC_DIR = src
SCRIPT_DIR = scripts

JFILES = ${wildcard ${SRC_DIR}/*.java}
CFILES = ${JFILES:%.java=%.class}

.PHONY: all clean distclean cleanAll test tests

all: ${CFILES}

#------------------------------------------------------------------------------

MAKELOCAL_FILES = ${filter-out %~, ${wildcard Makelocal*}}

ifneq ("${MAKELOCAL_FILES}","")
include ${MAKELOCAL_FILES}
endif

test tests: ${JFILES}
	set -x; for i in $^; do ./$$i; done

%.class: %.java
	${JAVAC} ${JFLAGS} $<

clean:
	rm -f ${CFILES}

distclean cleanAll: clean
