#!/usr/bin/env bash
set -euo pipefail
JAR="$(cd "$(dirname "$0")" && pwd)/target/md2wp.jar"
exec java ${JAVA_OPTS:-} -jar "$JAR" "$@"
