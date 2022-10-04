#!/bin/bash

until psql -h ${DB_HOST} -U ${DB_USER} -p ${DB_PORT} -c "\l"; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

echo "Postgres is up - executing command"

# Create database if not exists
psql -h ${DB_HOST} -U ${DB_USER} -p ${DB_PORT} -l | grep ${DB_NAME}; \
if [ $? -ne 0 ]; then \
  psql -h ${DB_HOST} -U ${DB_USER} -p ${DB_PORT} -c "CREATE DATABASE ${DB_NAME} ENCODING 'UTF8';"
fi

sbt flywayMigrate

export JAVA_TOOL_OPTIONS="--add-opens java.base/java.time=ALL-UNNAMED"

sbt run

