#!/bin/bash

POSTGRES_USER=${DB_USER}
POSTGRES_PASSWORD=${DB_PASS}
POSTGRES_DB=${DB_NAME}
PGPASSWORD=${DB_PASS}

until psql -h ${DB_HOST} -U ${DB_USER} -c '\q'; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done

echo "Postgres is up - executing command"

# TODO: Add db migration

java -jar target/scala-2.13/open-report-api.jar

