#### ДЗ #6: Разворачивание Jenkins'a и подключение джоб:

#### Цель: Необходимо настроить Jenkins, который будет запускать тесты по тригеру и выдавать читаемый отчет в Allure.

#### Описание/Пошаговая инструкция выполнения домашнего задания:
- Сдача проекта будет происходить путем выдачи xml с настроенными job'ами
- Джоба с запуском Selenium/Selenide тестов по тестированию Otus'a (можно из ранее сделанных) с возможностью выбора браузера
- Джоба с запуском Apppium тестов по тестированию мобильного приложения (можно из ранее сделанных)
- Обратите внимание, в п.2 должна быть реализована автоматическое скачивание приложения.

###### Критерии оценки (из 10 баллов):
- 4 балла за джобу с запуском Selenium
- 4 балла за джобу с appium
- 2 балла за читаемые отчеты

```shell
pip install --user jenkins-job-builder
jenkins-jobs --conf ./jobs/jobs.ini update ./jobs

#build slave:
docker build -f ./Dockerfile.maven -t localhost:5005/maven_slave:1.0.0 .
#push to registry
docker push localhost:5005/maven_slave:1.0.0
# check slave state: {"repositories":['maven_slave']}
curl -v -X GET http://127.0.0.1:5005/v2/_catalog

```

- Плагины: docker, git, allure, Build user vars, http requests, pipeline utility steps