#### Запуск андроид тестов в контейнере selenoid

<details>
  <summary>Проблематика</summary>

- Предполагается, что в ./infra/etc/browsers.json лежит описание браузера:
```json
    "android": {
        "default": "8.1",
        "versions": {
            "8.1": {
                "image": "selenoid/android:8.1",
                "port": "4444",
                "path": "/wd/hub",
                "privileged": true,
                "volumes": ["/home/egor/appium/Andy.apk:/Andy.apk"],
                "env": [
                    "VERBOSE=true",
                    "APPIUM_ARGS=--log-level debug"
                ],
                "selenoid:options": {
                    "enableVNC": "true",
                    "enableLog": "true",
                    "enableVideo": "true"
                }
            }
        }
    }
- Наша *.apk лежит на локали по пути /home/egor/appium/Andy.apk

```
- Спулен образ браузера с андроидом 
```shell
docker pull selenoid/android:8.1
```
- Браузер разрешен на goGridRouter в файле ./infra/etc/grid-router/quota/test.xml (пример фрагмента)
```xml
    <browser name="android" defaultVersion="8.1">
        <version number="8.1">
            <region name="1">
                <host name="172.18.0.1" port="4445" count="1"/>
            </region>
        </version>
    </browser>
```
- По идее, для запуска тестов, достаточно выполнить команду:
```sh
mvn clean test -Dremote.url=http://0.0.0.0/wd/hub -Ddevice.name=android -Dplatform.name=8.1 -Dapk.path=/Andy.apk
```
- НО! Есть проблема вложенной виртуализации, когда контейнер для образа готовился не под той архитектурой, на которой будет запускаться. [баг](https://github.com/aerokube/selenoid/issues/1018)
- Решение есть, надо подготовить свой образ и тогда на локали всё заработает.

</details>

<details>
  <summary>Готовим образ</summary>

- Стягиваем себе репу aerokube https://github.com/aerokube/images 
- И выполняем скрипт: ./selenium/automate_android.sh
- Я выбрал дефолтные ответы, по скрипту должна подтягиваться нужная архитектура
- Последним шагом вводим имя образа, например android:8.1
- Проверяем, что образ в системе:

```shell
docker images
#android                         8.1                                600f1f863bcb   59 seconds ago   7.9GB
```

- Весит многовато, потому я не стал его засылать в докерхаб, чтоб стягивать оттуда.
- На нашей локали отработает если в browsers.json поменять значение:
- - "image": "selenoid/android:8.1"
- на
- - "image": "android:8.1"

</details>

<details>
  <summary>Запуск</summary>

```shell
cd infra
./restart.sh
```
- Произойдет перезапуск selenoid, selenoid-ui, ggr, ggr-ui и запуск если они были не запущены
- Ожидается, что перейдя на http://0.0.0.0 отобразится наша инфра, и отрабатывает ручной запуск chrome:121 (без доп. капабилитис)
- Андроид тоже запустится без доп. капабилитис в "голом" состоянии "БЕЗ Andy.apk"
- Добавим же Andy.apk. Коннектимся через Appium Inspector: 
- - Удаленный сервер: 0.0.0.0
- - Удаленный порт: 80
- - Удаленный путь: /wd/hub
- - Capabilities:
```json
{
  "appium:platformName": "Android",
  "appium:deviceName": "android",
  "appium:automationName": "UIAutomator2",
  "appium:pVersion": "8.1",
  "appium:app": "/Andy.apk",
  "appium:appPackage": "com.pyankoff.andy",
  "appium:appActivity": ".MainActivity",
  "appium:avd": "android8.1-1"
}
```
- В инспекторе видно, что часть шрифтов отобразится криво, и я связываю это с нестабильностью тестов.
- Через selenoid по vnc не удалось увидеть что происходит.
- Если передать в selenoid через UI ручной сессии эти же капабилитис, то чуда не происходит. Приложуха не накатится.

</details>