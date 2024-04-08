#### ДЗ #3: Rest-assured

###### Цель:
- Написать автотесты с использованием Rest-assured.
- Описание API - https://petstore.swagger.io

###### Описание/Пошаговая инструкция выполнения домашнего задания:
- Написать тест кейсы в комментах к классу
- Тест на создание пользователя
- Тест на получение пользователя по имени


###### Варианты запуска тестов:
```bash
mvn clean test -Dparallel=4
```
- Ожидается, что в target/allure-results появятся артефакты *.json для отчета
- Локальный запуск аллюра: IDE -> Plugins -> allure -> allure:serve

###### Сбилдить тесты и запустить в докере:
```shell
docker build -t apitests:0.0.1 .
#docker run --rm --network=host -it apitests:0.0.1 <THREADS>
docker run --rm --network=host -it apitests:0.0.1 4
# с переопределением entrypoint'a для отладки:
docker run --rm --entrypoint=/bin/bash --network=host -it apitests:0.0.1
```

###### Запуск в докере с папкой m2:
```shell
docker build -t apitests:0.0.1 .
docker run --rm --network=host -v /home/egor/.m2:/root/.m2 -it apitests:0.0.1 4
```

###### Пушим образ в registry
```shell
docker build -t localhost:5005/apitests:0.0.1 .
docker push localhost:5005/apitests:0.0.1
```

---
###### Критерии оценки (из 10 баллов):
- +2 есть тест кейсы
- +2 используется RequestSpecification или ResponseSpecification
- +3 не менее 2-х тестов на создание пользователя
- +3 не менее 2-х тестов на получение пользователя по имени