#!/usr/bin/env bash
# У меня на локали и на удаленной тачке композ отзывается по разному
# я решил это через алиас, но для подстраховки вызываю оба варианта через ||
# alias docker-compose='docker compose'

docker-compose down || docker compose down
docker-compose up -d || docker compose up -d
sleep 5

# Селеноиды могли подняться на других ip, скрипт ниже
# профилактически актуализирует конфиг ./etc/grid-router/quota/test.xml"
sh './scripts/sh/update_ggr.sh'
docker-compose restart ggr || docker compose restart ggr

# Чтобы ggr-ui нам отвечал и позволял создавать ручные сессии через браузер
# берутся свежие ip адреса контейнеров selenoid_ui и ggr и
# подставляются в конфиг ./etc/nginx/ggr.conf"
sh './scripts/sh/update_nginx.sh'
docker-compose restart nginx || docker compose restart nginx
# ожидается, что на порту 80 должно всё открываться

# удаляем старые вольюмы, чтоб не было переполнения!
docker volume prune -f
docker image prune -f