#!/bin/bash
set -euo pipefail

export PGPASSWORD="${DB_PASSWORD}"

until psql -h "${DB_HOST}" -U "${DB_USER}" -p "${DB_PORT}" -c "\l" >/dev/null 2>&1; do
  echo "Postgres is unavailable - sleeping"
  sleep 1
done
echo "Postgres is up"

if ! psql -h "${DB_HOST}" -U "${DB_USER}" -p "${DB_PORT}" -lqt \
  | cut -d \| -f 1 | grep -qw "${DB_NAME}"; then
  psql -h "${DB_HOST}" -U "${DB_USER}" -p "${DB_PORT}" \
    -c "CREATE DATABASE ${DB_NAME} ENCODING 'UTF8';"
fi

# Flyway migration is expected to run as a separate deploy step
# (e.g. `sbt flywayMigrate` from CI, or `flyway/flyway` as an init container).

exec java ${JAVA_OPTS:-} -jar /app/open-reports-api.jar
