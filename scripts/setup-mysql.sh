#!/bin/bash

set -e

# === CHECK PARAMETERS ===
if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <path_to_pem_file> <server_ip>"
  exit 1
fi

PEM_FILE="$1"
SERVER_IP="$2"
REMOTE_USER="ec2-user"

echo "=== Connecting to server and installing MariaDB (MySQL compatible) ==="

ssh -i "$PEM_FILE" "${REMOTE_USER}@${SERVER_IP}" bash -s <<'ENDSSH'

set -e

MYSQL_ROOT_PASSWORD="admin12345"
DATABASE_NAME="vlxtr-core"
MYSQL_PORT=3306

echo "=== Updating system ==="
sudo dnf update -y

echo "=== Installing MariaDB (MySQL-compatible) ==="
sudo dnf install -y mariadb105-server

echo "=== Enabling and starting MariaDB ==="
sudo systemctl enable mariadb
sudo systemctl start mariadb

echo "=== Waiting for MariaDB socket to be ready ==="
sleep 5

echo "=== Securing MariaDB ==="
sudo mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '${MYSQL_ROOT_PASSWORD}'; FLUSH PRIVILEGES;"

echo "=== Creating database '${DATABASE_NAME}' ==="
mariadb -u root -p"${MYSQL_ROOT_PASSWORD}" -e "CREATE DATABASE IF NOT EXISTS \`${DATABASE_NAME}\`;"

echo "=== Confirming database exists ==="
mariadb -u root -p"${MYSQL_ROOT_PASSWORD}" -e "SHOW DATABASES LIKE '${DATABASE_NAME}';"

echo ""
echo "✅ MariaDB setup complete!"
echo " - Host: localhost"
echo " - Port: ${MYSQL_PORT}"
echo " - User: root"
echo " - Password: ${MYSQL_ROOT_PASSWORD}"
echo " - JDBC URL: jdbc:mysql://localhost:${MYSQL_PORT}/${DATABASE_NAME}"

ENDSSH

echo "✅ MariaDB deployment finished successfully."
