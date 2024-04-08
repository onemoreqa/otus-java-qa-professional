#### Поднимаем окружение через docker run:

###### Создаем сети для двух контейнеров selenoid:
```bash
docker network create selenoid
docker network create selenoid2
```

###### Поднимаем selenoid:
```bash
# Проверяем, что находимся в папке infra и выполняем команды
docker run -d --network selenoid --name selenoid -p 4445:4444 -v /var/run/docker.sock:/var/run/docker.sock -v ./etc/browsers.json:/etc/selenoid/browsers.json:ro aerokube/selenoid:1.11.2 -container-network=selenoid -limit 12
docker run -d --network selenoid2 --name selenoid2 -p 4446:4444 -v /var/run/docker.sock:/var/run/docker.sock -v ./etc/browsers.json:/etc/selenoid/browsers.json:ro aerokube/selenoid:1.11.2 -container-network=selenoid2 -limit 12
# Проверяем, что подняты два контейнера командой
docker ps
```

<details>
  <summary>Узнаем gateway сетей для контейнеров с selenoid:</summary>

```bash
docker inspect selenoid
docker inspect selenoid2
# Допустим эти значения будут = 172.18.0.1 и 172.19.0.1
# Проверяем, что порты открыты (на линуксе):
nc -vz 172.18.0.1 4445
nc -vz 172.19.0.1 4446
# На windows проверяется командой telnet 172.18.0.1 4445
```
</details>

<details>
  <summary>Редактируем файлик квоты goGridRouter ./etc/grid-router/quota/test.xml</summary>

```xml
<qa:browsers xmlns:qa="urn:config.gridrouter.qatools.ru">
    <browser name="chrome" defaultVersion="121.0">
        <version number="121.0">
            <region name="1">
                <host name="172.18.0.1" port="4445" count="1"/>
                <host name="172.19.0.1" port="4446" count="2"/>
            </region>
        </version>
        <version number="120.0">
            <region>
                <host name="172.18.0.1" port="4445" count="1"/>
                <host name="172.19.0.1" port="4446" count="2"/>
            </region>
        </version>
    </browser>
</qa:browsers>
```
</details>

<details>
  <summary>Пояснение по quota/test.xml:</summary>

- Для пользователя с логин/пароль test/test уже сгенерены креды в виде файла ./etc/grid-router/users.htpasswd
- Чтобы сгенерить квоту для нового пользователя требуется выполнить:
    - sudo apt install apache2-utils -y
    - sudo htpasswd -bc ./etc/grid-router/users.htpasswd someLogin somePass
- В результате появится файл someLogin.xml, который надо будет редактировать по аналогии с test.xml
- В файле users.htpasswd будет две строки, начинающиеся с имени пользователей test и someLogin
</details>


###### Поднимаем балансер goGridRouter и его UI:
```bash
docker run -d --name ggr -v ./etc/grid-router/:/etc/grid-router:ro -p 4444:4444 aerokube/ggr:1.7.2 -guests-allowed -guests-quota "test" -verbose -quotaDir /etc/grid-router/quota
docker run -d --name ggr-ui -p 8888:8888 -v ./etc/grid-router/quota/:/etc/grid-router/quota:ro aerokube/ggr-ui:1.2.0
# Проверяем результат:
docker ps
curl -s http://127.0.0.1:8888/status  # {"browsers":{"chrome":{"120.0":{},"121.0":{}}},"pending":0,"queued":0,"total":24,"used":0}

# Поднимаем selenoid-ui, линкуя его к ggr-ui 
docker run -d --rm --name selenoid-ui -p 8080:8080 aerokube/selenoid-ui:1.10.11 --selenoid-uri http://172.17.0.1:8888 
# Проверяем что на http://127.0.0.1:8080 у нас откроется веб-морда selenoid
```

###### Поднимаем nginx в режиме балансировщика:
```bash
# Важно, чтобы на 80 порту до этого ничего не было
docker run --name nginx -v ./etc/nginx:/etc/nginx/conf.d:ro -d --network=host nginx
# Теперь перейдя на http://127.0.0.1 нас кинет на 80 порт, где мы увидим selenoid
```

###### Пулим себе образы браузеров из ./etc/browsers.json:
```bash
docker pull selenoid/chrome:121.0
docker pull selenoid/chrome:120.0
# Без них selenoid не поймет, как запустить браузер
```

######  Проверяем создание ручной сессии браузера хром:

<details>
  <summary>Кидаем Curl</summary>

```bash
curl -H'Content-Type: application/json' http://127.0.0.1/wd/hub/session -d'{
    "capabilities": {
        "alwaysMatch": {
            "browserName": "chrome",
            "browserVersion": "121.0",
            "selenoid:options": {
                "name": "Session started using curl command...",
                "sessionTimeout": "1m"
            }
        }
    }
}'

```
</details>


<details>
  <summary>Ответ</summary>

```json
{
  "value": {
    "capabilities": {
      "acceptInsecureCerts": false,
      "browserName": "chrome",
      "browserVersion": "121.0.6167.85",
      "chrome": {
        "chromedriverVersion": "121.0.6167.85 (3f98d690ad7e59242ef110144c757b2ac4eef1a2-refs/branch-heads/6167@{#1539})",
        "userDataDir": "/tmp/.org.chromium.Chromium.QHZGab"
      },
      "fedcm:accounts": true,
      "goog:chromeOptions": {
        "debuggerAddress": "localhost:42035"
      },
      "networkConnectionEnabled": false,
      "pageLoadStrategy": "normal",
      "platformName": "linux",
      "proxy": {},
      "se:cdp": "ws://172.18.0.1:4446/devtools/dbd5167f4de1fda2416c115610d789ee/",
      "setWindowRect": true,
      "strictFileInteractability": false,
      "timeouts": {
        "implicit": 0,
        "pageLoad": 300000,
        "script": 30000
      },
      "unhandledPromptBehavior": "dismiss and notify",
      "webauthn:extension:credBlob": true,
      "webauthn:extension:largeBlob": true,
      "webauthn:extension:minPinLength": true,
      "webauthn:extension:prf": true,
      "webauthn:virtualAuthenticators": true
    },
    "sessionId": "d2860a4840359722f4ba02552388b744dbd5167f4de1fda2416c115610d789ee"
  }
}
```
</details>
