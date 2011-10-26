#!/bin/sh
#
# print out the classpath including everything in the given directory
#
# Useful for running things:
#
# java -cp `resource/classpath.sh` org.mitre.isab.MainClassThingy

DIR=$1

if [ -z $1 ]; then
  DIR=lib/
fi

# build directory
echo -n "build"

# every jar in lib/
ls -1 $DIR/*jar | perl -pe 'chomp ; print ":";'
