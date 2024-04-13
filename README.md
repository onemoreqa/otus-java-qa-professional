#### ДЗ #1: Автотест со своими ожиданиями:

###### Цель:
- Реализовать на практике полученные знания, с использованием Actions и своих ожиданий.

###### Описание/Пошаговая инструкция выполнения домашнего задания:
- Необходимо создать проект в Maven'e 
- Фабрику (WebDriverFactory), которая будет получать значение из окружения и запускать соответствующий браузер (Chrome, Firefox, Opera)
- Реализовать подсветку элементов перед нажатием, после нажатия вернуть данные в исходное состояние
- На главно странице Otus'a снизу найти список курсов(популярные курсы, специализации, рекомендации) и реализовать:
- Метод фильтр по названию курса
- Метод выбора курса, стартующего раньше всех/позже всех (при совпадении дат - выбрать любой) при помощи reduce
- Реализовать движение мыши и выбор курса при помощи библиотеки Actions


###### Варианты запуска тестов:
```bash
# локальный браузер ОС
mvn clean test -Dparallel=1

# в браузере селеноида
cd infra && ./restart.sh
cd ..
mvn clean test -Dparallel=1 -Dwebdriver.remote.url=http://0.0.0.0/wd/hub
mvn clean test -Dparallel=1 -Dbrowser=opera -Dwebdriver.remote.url=http://95.181.151.41/wd/hub -Dbrowser.version=105.0
mvn clean test -Dparallel=1 -Dbrowser=chrome -Dwebdriver.remote.url=http://95.181.151.41/wd/hub -Dbrowser.version=120.0

```

###### Сбилдить тесты и запустить в докере:
```shell
docker build -t uitests:0.0.1 .
#docker run --rm --network=host -it uitests:0.0.1 <THREADS> <BROWSER> <SELENOID_URL> <BROWSER_VERSION>
docker run --rm --network=host -it uitests:0.0.1 1 chrome http://0.0.0.0/wd/hub 121.0


# с переопределением entrypoint'a для отладки:
docker run --rm --entrypoint=/bin/bash --network=host -it uitests:0.0.1
# ВАЖНЫЙ момент! без --network=host докер направляя тесты в 0.0.0.0 передаст их не на селеноид, а себе же в контейнер
```

###### Запуск в докере с папкой m2:
```shell
docker build -t uitests:0.0.1 .
docker run --rm --network=host -v /home/egor/.m2:/root/.m2 -it uitests:0.0.1 1 chrome http://0.0.0.0/wd/hub 121.0
docker run --rm --network=host -v /home/egor/.m2:/root/.m2 -it uitests:0.0.1 1 chrome http://95.181.151.41/wd/hub 120.0
```

###### Пушим образ в registry
```shell
docker build -t localhost:5005/uitests:0.0.1 .
docker push localhost:5005/uitests:0.0.1
```

---
###### Критерии оценки (из 10 баллов):
- 2 Балла за реализацию фабрики
- 2 балла за подсветку элементов, +2 балла за возврат страницы в исходное состояние
- 1 бал за реализацию фильтра, +1 бал за реализацию reduce'ра
- 2 балла за реализацию Actions и выбора цвета
- 1 бал за каждое вмешательство преподавателя для запуска/работы тестов
- 2 балла за не реализованное задание
- Сдача происходит через git.
- Необходимо приложить инструкцию (вариант, написать в readme.md) по запуску.