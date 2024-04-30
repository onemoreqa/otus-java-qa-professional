#!/usr/bin/env bash
GGR_IP=`docker inspect ggr -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
SELENOID_UI_IP=`docker inspect selenoid-ui -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
JENKINS_IP=`docker inspect jenkins -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`


echo $GGR_IP ' = ggr ip address'
echo $SELENOID_UI_IP ' = selenoid_ui ip address'
echo $JENKINS_IP ' = jenkins ip address'

NGINX_CONF_PATH="$PWD/etc/nginx/ggr.conf"

cat > "${NGINX_CONF_PATH}" <<- EOM
server {
    listen 80 default_server;
    server_name localhost;
    server_name 127.0.0.1;
    server_name onqa.su;

    location / {
      proxy_pass http://${SELENOID_UI_IP}:8080;
      proxy_http_version 1.1;
      proxy_set_header        Upgrade \$http_upgrade;
      proxy_set_header        Connection "Upgrade";
      proxy_set_header        Host \$host;
      proxy_buffering         off;

      location ~ /wd/hub/ {
        proxy_pass http://${GGR_IP}:4444;
      }

      location /video/ {
        proxy_pass http://localhost:4445;
      }

      location /jenkins {
        proxy_pass http://0.0.0.0:8080;
      }

      location /portainer {
        proxy_pass http://0.0.0.0:9443;
      }

   }
}


EOM
