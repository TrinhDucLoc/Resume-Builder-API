#!/bin/bash

path=$(echo $1| sed s'/\/$//')

if [ -d "$path" ]; then
  if [ "$path" != "" ]; then
  for file in $path/* ; do
      fileName=$(echo "$file" | sed "s/.*\///" | sed -e s/.cer// )
      keytool -import -trustcacerts -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeit -noprompt -alias $fileName -file $file
  done
fi
fi
