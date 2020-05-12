#!/bin/bash
set -e
export JAVA=java
JAVA --version
JAVA --module-path bin --module medusa.node