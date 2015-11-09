#!/bin/bash

INPUT=./inception_url.csv
OLDIFS=$IFS
IFS=","
while read Name URL
do
  res=`curl -s -I $URL | grep HTTP/1.1 | awk {'print $2'}`
  if [[ $res -ne 200 ]]
  then 
    echo "Error $res on $Name => $URL" >> logs.txt
  fi
done < $INPUT
IFS=$OLDIFS
