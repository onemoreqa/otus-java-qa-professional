#### Поднимаем окружение через docker compose:

###### Прежде чем начать поднимать:
<details>
  <summary>Открываем порты</summary>

- Очень важно, чтобы все порты указанные в yaml были доступны
- Если об этом не позаботиться, то докер контейнер не поднимется и будет не очевидный дебаг
- Открыть достаточно один раз и больше об этом не думать:
```bash
sudo apt-get install ufw # установка firewall
sudo ufw status verbose # проверка статуса
sudo ufw enable # включение службы
sudo ufw allow 80 # открытие конкретного порта
sudo ufw allow 3000:3100/tcp # открытие диапазона портов
sudo ufw allow from 123.45.67.89 to any port 22 # внешний хост сможет достучаться по порту 22
tail /var/log/ufw.log # узнать о последних проблемах с закрытыми портами
```

</details>

###### Вариант запуска selenoid + selenoid-ui:
- Действие лучше выполнить в пустой (новой) папке
<details>
  <summary>Создаем файл docker-compose.yaml</summary>

```yaml
version: '3'
networks:
  selenoid:
    external:
      name: selenoid

services:
  selenoid:
    image: "aerokube/selenoid:1.11.2"
    container_name: selenoid
    networks:
      selenoid: null
    ports:
      - "4444:4444"
    volumes:
      - "$PWD/selenoid/config:/etc/selenoid/" # assumed current dir contains browsers.json
      - "/var/run/docker.sock:/var/run/docker.sock"
      - "$PWD/selenoid/video:/opt/selenoid/video"
      - "$PWD/selenoid/logs:/opt/selenoid/logs"
    environment:
      - OVERRIDE_VIDEO_OUTPUT_DIR=$PWD/selenoid/video
    command: ["-conf", "/etc/selenoid/browsers.json", "-video-output-dir", "/opt/selenoid/video", "-log-output-dir", "/opt/selenoid/logs", "-container-network", "selenoid"]

  selenoid-ui:
    image: "aerokube/selenoid-ui:1.10.11"
    container_name: selenoid-ui
    networks:
      - selenoid
    links:
      - selenoid
    ports:
      - "8080:8080"
    command: ["--selenoid-uri", "http://selenoid:4444"]
```
</details>

```bash
# Находясь в одной директории с yaml файлом выполняем
docker-compose up -d 
```
- Ожидается, что перейдя на http://127.0.0.1:8080 отобразится selenoid-ui
- docker ps отобразит два поднятных контейнера

---

###### Основной запуск: selenoid, selenoid-ui, ggr, ggr-ui, nginx:
```bash
cd infra
docker-compose up -d 
```
- Ожидается, что перейдя на http://127.0.0.1 отобразится selenoid-ui
- docker ps отобразит поднятные контейнеры (больше чем два)
- Возможна ругань на сети, либо уже существующие контейнеры. В этом случае удаляем их.

---
###### Быстрый вариант:
- Пользуемся поднятой инфраструктурой по адресу http://95.181.151.41
- Ожидается, что отобразится selenoid-ui
- На удаленный хост можно направить свои автотесты