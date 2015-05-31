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

SRC_DIR = .
GEN_DIR = generated
TOOL_DIR = tools
SCRIPT_DIR = scripts

GRAMMARS = $(wildcard *.grammar)

# Java files generated from the grammar:
GROOTS = $(patsubst %.grammar, %, ${GRAMMARS})
GJAVA = $(addprefix ${GEN_DIR}/, \
	$(addsuffix Analyzer.java,  ${GROOTS}) \
	$(addsuffix Constants.java, ${GROOTS}) \
	$(addsuffix Parser.java,    ${GROOTS}) \
	$(addsuffix Tokenizer.java, ${GROOTS}))

JSOURCES = $(wildcard ${SRC_DIR}/*.java)
JFILES = ${JSOURCES} ${GJAVA}
CFILES = ${JFILES:%.java=%.class}

# Grammatrica installation:
GRAMMATICA_JAR := $(shell find ${TOOL_DIR}/ -name grammatica\*.jar | sort | head -n 1)

CLASSPATH := $(shell find ${TOOL_DIR}/ -name \*.jar | tr "\n" ":")generated:${CLASSPATH}

export CLASSPATH

JAVAC = javac

.PHONY: all clean distclean cleanAll test tests

.PRECIOUS: ${GEN_DIR}/%.java

all: ${CFILES} ${JFILES}

#------------------------------------------------------------------------------

ifneq ("${MAKELOCAL_FILES}","")
include ${MAKELOCAL_FILES}
endif

${GEN_DIR}/%Analyzer.java: %.grammar
	java -jar ${GRAMMATICA_JAR} $< --javaoutput $(dir $@)

${GEN_DIR}/%Constants.java: ${GEN_DIR}/%Analyzer.java;
${GEN_DIR}/%Parser.java: ${GEN_DIR}/%Analyzer.java;
${GEN_DIR}/%Tokenizer.java: ${GEN_DIR}/%Analyzer.java;

${SRC_DIR}/%.class: ${SRC_DIR}/%.java ${GJAVA:%.java=%.class}
	${JAVAC} ${JFLAGS} $<

${GEN_DIR}/%.class: ${GEN_DIR}/%.java
	${JAVAC} ${JFLAGS} $<

test tests: ${CFILES}
	set -x; for i in $^; do ./$$i; done

clean:
	rm -f ${CFILES}

distclean cleanAll: clean
	rm -f ${GJAVA}
