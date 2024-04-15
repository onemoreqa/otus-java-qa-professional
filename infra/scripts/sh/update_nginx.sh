#!/usr/bin/env bash
GGR_IP=`docker inspect ggr -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
SELENOID_UI_IP=`docker inspect selenoid-ui -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`
JENKINS_IP=`docker inspect jenkins -f {{.NetworkSettings.Networks.infra_default.IPAddress}}`


echo $GGR_IP ' = ggr ip address'
echo $SELENOID_UI_IP ' = selenoid_ui ip address'
echo $JENKINS_IP ' = jenkins ip address'

NGINX_CONF_PATH="$PWD/etc/nginx/ggr.conf"

cat > "${NGINX_CONF_PATH}" <<- EOM
upstream ggr {
    random;
    server ${GGR_IP}:4444;
}

upstream selenoid_ui {
    server ${SELENOID_UI_IP}:8080;
}

upstream jenkins {
    server ${JENKINS_IP}:8080;
}


EOM

cat >> "${NGINX_CONF_PATH}" <<- EOM
server {
    listen 80 default_server;
    server_name localhost;
    server_name 127.0.0.1;

    location / {
      proxy_pass http://selenoid_ui/;
      proxy_http_version 1.1;
      proxy_set_header        Upgrade \$http_upgrade;
      proxy_set_header        Connection "Upgrade";
      proxy_set_header        Host \$host;
      proxy_buffering         off;

      location ~ /wd/hub/ {
        proxy_pass http://ggr;
      }

      location /video/ {
        proxy_pass http://localhost:4445;
      }
   }

   location ~ /static/[0-9a-z]\{8\} {
       proxy_pass http://jenkins;
   }

   location /jenkins {
     proxy_set_header        Host \$host:\$server_port;
     proxy_set_header        X-Real-IP \$remote_addr;
     proxy_set_header        X-forwarded-For \$proxy_add_x_forwarded_for;
     proxy_set_header        X-forwarded-Proto \$scheme;

     proxy_pass http://jenkins;
     proxy_read_timeout 90;

     proxy_http_version 1.1;
     proxy_request_buffering off;
     add_header 'X-SSH-Endpoint' 'jenkins.domain.tld:50022' always;
  }
}
EOM
