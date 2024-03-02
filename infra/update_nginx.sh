#!/usr/bin/env bash
GGR_IP=`docker inspect ggr -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
SELENOID_UI_IP=`docker inspect selenoid-ui -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`

echo $GGR_IP ' = new ggr ip address'
echo $SELENOID_UI_IP ' = new selenoid_ui ip address'

NGINX_CONF_PATH="./etc/nginx/ggr.conf"

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
    listen 80 default_server;

    location / {
      proxy_pass http://selenoid_ui/;
      proxy_http_version 1.1;
      proxy_set_header        Upgrade \$http_upgrade;
      proxy_set_header        Connection "Upgrade";
      proxy_set_header        Host \$host;
      proxy_buffering         off;
   }

   location ~ /static/js/ {
      proxy_pass http://selenoid_ui;
   }

   location ~ /static/css/ {
      proxy_pass http://selenoid_ui;
   }

   location ~ /static/media/ {
      proxy_pass http://selenoid_ui;
   }

   location /status {
      proxy_pass http://selenoid_ui;
   }

   location /events {
      proxy_pass http://selenoid_ui;
   }

   location ~ /vnc/ {
      proxy_pass http://selenoid_ui;
      proxy_http_version 1.1;
      proxy_set_header Upgrade \$http_upgrade;
      proxy_set_header Connection "upgrade";
      proxy_set_header Host \$host;
   }

   location ~ /logs/ {
      proxy_pass http://selenoid_ui;
      proxy_http_version 1.1;
      proxy_set_header Upgrade \$http_upgrade;
      proxy_set_header Connection "Upgrade";
      proxy_set_header Host \$host;
   }

   location ~ /wd/hub/ {
      proxy_pass http://ggr;
   }
}
EOM

docker-compose restart nginx