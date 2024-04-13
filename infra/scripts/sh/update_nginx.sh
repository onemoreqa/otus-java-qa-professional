#!/usr/bin/env bash
GGR_IP=`docker inspect ggr -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
SELENOID_UI_IP=`docker inspect selenoid-ui -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`


echo $GGR_IP ' = ggr ip address'
echo $SELENOID_UI_IP ' = selenoid_ui ip address'

NGINX_CONF_PATH="$PWD/etc/nginx/ggr.conf"

cat > "${NGINX_CONF_PATH}" <<- EOM
upstream selenoid_ui {
    server ${SELENOID_UI_IP}:8080;
}

upstream ggr {
    random;
    server ${GGR_IP}:4444;
}

EOM

cat >> "${NGINX_CONF_PATH}" <<- EOM
server {
    listen 8001 default_server;
    server_name localhost;
    server_name 127.0.0.1;

    location / {
      proxy_pass http://selenoid_ui/;
      proxy_http_version 1.1;
      proxy_set_header        Upgrade \$http_upgrade;
      proxy_set_header        Connection "Upgrade";
      proxy_set_header        Host \$host;
      proxy_buffering         off;

      location ~ (login|simple|jenkins) {
        proxy_pass http://localhost:8083;
      }

      location ~ /wd/hub/ {
        proxy_pass http://ggr;
      }

      location /video/ {
        proxy_pass http://localhost:4445;
      }

   }
}
EOM
