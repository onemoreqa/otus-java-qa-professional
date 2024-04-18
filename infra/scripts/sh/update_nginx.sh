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
    keepalive 32; # keepalive connections
    server 127.0.0.1:8080; # jenkins ip and port
}

# Required for Jenkins websocket agents
map \$http_upgrade \$connection_upgrade {
    default upgrade;
    '' close;
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

   # below from here https://github.com/mjstealey/jenkins-nginx-docker/blob/master/nginx/prefix_jenkins.conf
   # logging
   access_log      /var/log/nginx/jenkins.access.log;
   error_log       /var/log/nginx/jenkins.error.log;

   # this is the jenkins web root directory
   # (mentioned in the /etc/default/jenkins file)
   root            /var/jenkins_home/war/;

   # pass through headers from Jenkins that Nginx considers invalid
   ignore_invalid_headers off;

   location ~ "^/static/[0-9a-fA-F]{8}\/(.*)$" {
       # rewrite all static files into requests to the root
       # E.g /static/12345678/css/something.css will become /css/something.css
       rewrite "^/static/[0-9a-fA-F]{8}\/(.*)" /\$1 last;
   }

   location /jenkins/userContent {
       # have nginx handle all the static requests to userContent folder
       # note : This is the $JENKINS_HOME dir
       root /var/jenkins_home/;
       if (!-f \$request_filename) {
           # this file does not exist, might be a directory or a /**view** url
           rewrite (.*) /\$1 last;
           break;
       }
       sendfile on;
   }


   location /jenkins {
      sendfile off;
      proxy_pass         http://jenkins;
      proxy_redirect     default;
      proxy_http_version 1.1;

      # Required for Jenkins websocket agents
      proxy_set_header   Connection        \$connection_upgrade;
      proxy_set_header   Upgrade           \$http_upgrade;

      proxy_set_header   Host              \$host;
      proxy_set_header   X-Real-IP         \$remote_addr;
      proxy_set_header   X-Forwarded-For   \$proxy_add_x_forwarded_for;
      proxy_set_header   X-Forwarded-Proto \$scheme;
      proxy_set_header   X-Forwarded-Host  \$host;
      # proxy_set_header   X-Forwarded-Port  8080;
      proxy_max_temp_file_size 0;

      #this is the maximum upload size
      client_max_body_size       10m;
      client_body_buffer_size    128k;

      proxy_connect_timeout      90;
      proxy_send_timeout         90;
      proxy_read_timeout         90;
      proxy_buffering            off;
      proxy_request_buffering    off; # Required for HTTP CLI commands
      proxy_set_header Connection ""; # Clear for keepalive
      add_header 'X-SSH-Endpoint' 'jenkins.domain.tld:50022' always;
  }
}
EOM
