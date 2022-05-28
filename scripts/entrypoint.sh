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

./skinny db:migrate ${DB_ENV:-development}

java -jar standalone-build/target/scala-2.13/openreports-standalone-assembly-0.1.0-SNAPSHOT.jar
