#!/bin/bash

BASEDIR=$(dirname $0)
echo BASEDIR
cd BASEDIR

java -cp "/lib/*:../dist/facebook-1.0-SNAPSHOT.jar" test/Main
