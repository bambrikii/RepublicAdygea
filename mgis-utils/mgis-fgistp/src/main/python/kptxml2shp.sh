#!/bin/bash

DIR=$1
if [ "$DIR" == "" ]; then
    echo "At least DIR_NAME as first argument required! You may also add more FILE_NAME arguments.";
    exit 0;
fi

while true; do
    change=$(inotifywait -e close_write,moved_to,create $DIR .)
    change=${change#$DIR * }
    echo "$change"
    FILE_NAME=`echo $change | sed 's/^.*\s//g'`
    echo $FILE_NAME
#    if [ "$change" == *.zip -o "$change" == *.xml ]; then
    FILE_NAME_IS_VALID=`echo $FILE_NAME | grep -Ei '.zip|.xml'`
    echo $FILE_NAME_IS_VALID
    if [ $FILE_NAME_IS_VALID ]; then
    	#FILE_NAME_IS_XML=`echo $FILE_NAME | grep -Ei '.xml'`
    	#iconv -f WINDOWS-1251 -t UTF-8 $DIR$FILE_NAME > $DIR$FILE_NAME.tmp
    	#mv $DIR$FILE_NAME.tmp $DIR$FILE_NAME
	python ./kptxml2shp.py $DIR $FILE_NAME
    fi
done
