#!/bin/bash

DIR=$1
if [ "$DIR" == "" ]; then
    echo "At least DIR_NAME as first argument required! You may also add more FILE_NAME arguments.";
    exit 0;
fi

while true; do
    change=$(inotifywait -e close_write,moved_to,create $DIR .)
    change=${change#./ * }
    echo "$change"
    FILE_NAME=`echo $change | sed 's/^.*\s//g'`
    echo $FILE_NAME
#    if [ "$change" == *.zip -o "$change" == *.xml ]; then
    FILE_NAME_IS_VALID=`echo $FILE_NAME | grep -Ei '.zip|.xml'`
    echo $F1
    if [ $FILE_NAME_IS_VALID ]; then
	python ./kptxml2shp.py $DIR $change
    fi
done
