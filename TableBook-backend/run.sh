#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mvn clean package
echo "Built target/TableBook.war. Deploy it to Apache Tomcat 10.1+ webapps/."
