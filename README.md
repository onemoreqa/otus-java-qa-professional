#### ДЗ #6: Мобильное тестирование:

#### Цель: Написать тесты для мобилки
- Написать тесты при помощи selenide (appium) для проверки моб.приложения
- Разобраться с инжектированием через google Guice
- Тесты должны отрабатывать на локали (Appium server) и на selenoid

#### Описание/Пошаговая инструкция выполнения домашнего задания:
- Свериться с [инструкцией 1](infra/docs/appium_local_install.md)

###### Запуск тестов на локали (Appium Server + эмулятор)
- Включаем Appium Server (GUI) стандартно на 0.0.0.0:4723
- На локали должны присутствовать Android Studio и настроенный эмулятор
```bash
mvn clean test
```
- Окно с эмулятором откроется даже если ранее было закрыто
- Тестировал на android 8.1 и 14.0

###### Запуск тестов на локали (только selenoid, без эмулятора и appium server)
- Свериться с [инструкцией 2](infra/docs/appium_selenoid_run.md)
```shell
cd infra
./restart.sh
cd ..
mvn clean test -Dremote.url=http://0.0.0.0/wd/hub -Ddevice.name=android -Dplatform.name=8.1 -Dapk.path=/Andy.apk
```

###### Пушим образ в registry
```shell
docker build -t localhost:5005/mobiletests:0.0.1 .
docker push localhost:5005/mobiletests:0.0.1
```

###### Запуск тестов в докере:
```shell
# docker run --rm --network=host -v /home/egor/.m2/repository:/root/.m2/repository -v ./surefire-reports:/home/ubuntu/mobile_tests/target/surefire-reports -v ./results:/home/ubuntu/mobile_tests/target/allure-results --entrypoint=/bin/bash -it localhost:5005/mobiletests:0.0.1
# bash entrypoint.sh
docker run --rm --network=host -v /home/egor/.m2/repository:/root/.m2/repository -v ./surefire-reports:/home/ubuntu/mobile_tests/target/surefire-reports -v ./results:/home/ubuntu/mobile_tests/target/allure-results localhost:5005/mobiletests:0.0.1 http://0.0.0.0:4723/wd/hub Android 8.1 nexus emulator-5554 com.pyankoff.andy .MainActivity /home/egor/Desktop/otus-java-qa-professional/src/main/java/resources/Andy.apk
```