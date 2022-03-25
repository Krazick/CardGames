#!/bin/sh

if [ $# -lt 1 ]; then
    cat >&2 <<EOF
usage:
    $0 filename
    $0 source-file destination-file

    Converts an image to RGB color space. The first form manipulates
    the file in-place.
EOF
    exit 1
fi           

SOURCE_FILE=$1

if [ $# -lt 2 ]; then
    DESTINATION_FILE=$SOURCE_FILE
else
    DESTINATION_FILE=$2
fi

sips \
    --matchTo '/System/Library/ColorSync/Profiles/Generic RGB Profile.icc' \
    "$SOURCE_FILE" \
    --out "$DESTINATION_FILE"
