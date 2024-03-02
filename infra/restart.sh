#!/usr/bin/env bash
docker-compose down
docker-compose up -d
sleep 5
sh './update_nginx.sh'