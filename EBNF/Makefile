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
TOOL_DIR = $(shell if [ -e lib ]; then echo lib; else echo tools; fi)
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

# Grammatica installation:
GRAMMATICA_JARS := $(shell find ${TOOL_DIR}/ -name \*.jar | sort | tr "\n" ":")

JARDIR = $(firstword $(dir $(subst :, ,${GRAMMATICA_JARS})))
GRAMMATICA_JAR = $(filter ${JARDIR}grammatica%.jar,$(subst :, ,${GRAMMATICA_JARS}))

CLASSPATH := ${GRAMMATICA_JARS}generated:${SRC_DIR}:${CLASSPATH}

export CLASSPATH

JAVAC = javac

.PHONY: all clean distclean cleanAll

.PRECIOUS: ${GEN_DIR}/%.java

all: ${CFILES} ${JFILES}

#------------------------------------------------------------------------------

#------------------------------------------------------------------------------

MAKELOCAL_FILES = ${filter-out %~, ${wildcard Makelocal*}}

ifneq ("${MAKELOCAL_FILES}","")
include ${MAKELOCAL_FILES}
endif

${GEN_DIR}/%Analyzer.java: %.grammar
	java -jar ${GRAMMATICA_JAR} $< --javaoutput $(dir $@)

${GEN_DIR}/%Constants.java: ${GEN_DIR}/%Analyzer.java
	test -f $@ && touch $@

${GEN_DIR}/%Parser.java: ${GEN_DIR}/%Analyzer.java
	test -f $@ && touch $@

${GEN_DIR}/%Tokenizer.java: ${GEN_DIR}/%Analyzer.java
	test -f $@ && touch $@

%.class: %.java ${GJAVA}
	${JAVAC} ${JFLAGS} $<

#------------------------------------------------------------------------------
# Tests

.PHONY: test tests out output outputs

TEST_DIR     = tests/cases
TST_OUT_DIR  = tests/outputs

TEST_FILES   = $(wildcard ${TEST_DIR}/*.sh)
TEST_OUTPUTS = ${TEST_FILES:${TEST_DIR}/%.sh=${TST_OUT_DIR}/%.out}
TEST_DIFFS   = ${TEST_FILES:${TEST_DIR}/%.sh=${TST_OUT_DIR}/%.diff}

test tests: ${TEST_DIFFS}

out output outputs: ${TEST_OUTPUTS}

${TST_OUT_DIR}/%.out: ${TEST_DIR}/%.sh
	@test -f $@ || ./$< 2>&1 | tee $@
	@test -f $@ && touch $@

${TST_OUT_DIR}/%.diff: ${TEST_DIR}/%.sh ${TST_OUT_DIR}/%.out ${CFILES}
	@printf "%-30s: " $(notdir $<)
	@./$< 2>&1 | diff $(word 2,$^) - > $@; \
	if [ $$? = 0 ]; then echo OK; \
	else echo FAILED:; cat $@; fi

#------------------------------------------------------------------------------

.PHONY: listdiff

listdiff:
	@find ${TST_OUT_DIR} -name '*.diff' -size +0 \
	| xargs --no-run-if-empty ls -l

#------------------------------------------------------------------------------

clean:
	rm -f ${TEST_DIFFS}

distclean cleanAll: clean
	rm -f ${CFILES}
	rm -f ${GJAVA}
