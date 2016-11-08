#! /bin/sh

set -ue

BASE_DIR=$(dirname $(dirname $(dirname $0)))
TOOL_DIR=${BASE_DIR}/tools

## echo ${TOOL_DIR}

GRAMMATICA_SRV=http://grammatica.percederberg.net
GRAMMATICA_URL=${GRAMMATICA_SRV}/download/stable/1.6/grammatica-1.6.zip
GRAMMATICA_ZIP=$(basename ${GRAMMATICA_URL})

if [ ! -f ${TOOL_DIR}/${GRAMMATICA_ZIP} ]
then
    wget -O ${TOOL_DIR}/${GRAMMATICA_ZIP} ${GRAMMATICA_URL}

    (
        cd ${TOOL_DIR}
        unzip ${GRAMMATICA_ZIP}
        cd $(basename ${GRAMMATICA_ZIP} .zip)
        ant -k
    )
else
    echo "$0: ${TOOL_DIR}/${GRAMMATICA_ZIP} is already downloaded -" \
        "will not replace it"
fi
