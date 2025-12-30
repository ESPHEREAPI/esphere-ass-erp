#!/bin/bash
set -e

# Fichier marqueur pour vérifier si l'init a déjà été faite
INIT_FLAG="/var/lib/mysql/.init_completed"

if [ ! -f "$INIT_FLAG" ]; then
    echo "Première initialisation - Import du backup..."
    
    # Attendre que MySQL soit prêt
    until mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" -e "SELECT 1" > /dev/null 2>&1; do
        echo "En attente de MySQL..."
        sleep 2
    done
    
    # Importer le backup
    if [ -f /docker-entrypoint-initdb.d/biometry.sql ]; then
        echo "Import de biometry.sql..."
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" biometry < /docker-entrypoint-initdb.d/biometry.sql
        echo "Import terminé avec succès"
    fi
    
    # Créer le fichier marqueur
    touch "$INIT_FLAG"
    echo "Initialisation complète"
else
    echo "Base de données déjà initialisée - Skip import"
fi