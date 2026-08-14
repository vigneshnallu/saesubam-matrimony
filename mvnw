#!/bin/sh
export MAVEN_HOME=.maven/apache-maven-3.9.9
exec $MAVEN_HOME/bin/mvn "$@"
