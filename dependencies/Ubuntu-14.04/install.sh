#! /bin/sh

set -ue

sudo apt-get install -y ant
sudo apt-get install -y ant-contrib
sudo apt-get install -y junit
sudo apt-get install -y default-jdk

# For testing Python Lark parsers:

sudo apt-get install -y virtualenv

BASE_DIR=$(dirname $(dirname $(dirname $0)))
TOOL_DIR=${BASE_DIR}/tools

## echo ${TOOL_DIR}

GRAMMATICA_SRV=https://github.com/cederberg/grammatica/releases
GRAMMATICA_URL=${GRAMMATICA_SRV}/download/v1.6/grammatica-1.6.zip
GRAMMATICA_ZIP=$(basename ${GRAMMATICA_URL})

if [ ! -f ${TOOL_DIR}/${GRAMMATICA_ZIP} ]
then
    wget -O ${TOOL_DIR}/${GRAMMATICA_ZIP} ${GRAMMATICA_URL}

    (
        cd ${TOOL_DIR}
        unzip ${GRAMMATICA_ZIP}
        cd $(basename ${GRAMMATICA_ZIP} .zip)
        JVERSION=$(java -version 2>&1 | head -1 | awk -F'"' '{print $2}')
        JLANG=$(echo ${JVERSION} | awk -F. '{print $1}')
        if [ "${JLANG}" -gt 1 ]
        then
            JVERSION=${JLANG}
        else
            JVERSION=${JLANG}.$(echo ${JVERSION} | awk -F. '{print $2}')
        fi
        perl -i~ -pe "s/source=\"1\\.4\"/source=\"${JVERSION}\"/" build.xml
        perl -i  -pe "s/target=\"1\\.5\"/target=\"${JVERSION}\"/" build.xml
        ant -k
    )
else
    echo "$0: ${TOOL_DIR}/${GRAMMATICA_ZIP} is already downloaded -" \
        "will not replace it"
fi

# Install Python into virtualenv, and install Lark parser there:

virtualenv EBNF/virt

EBNF/virt/bin/pip install lark
EBNF/virt/bin/pip install lark-parser
