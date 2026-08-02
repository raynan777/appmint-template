#!/usr/bin/env sh
set -e
GRADLE_VERSION=8.5
DIR=$(dirname "$0")
exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
