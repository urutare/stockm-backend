#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE DATABASE user_db;
  CREATE DATABASE category_db;
  CREATE DATABASE sync_db;
  CREATE DATABASE stock_db;
EOSQL