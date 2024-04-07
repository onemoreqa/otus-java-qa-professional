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
mvn clean test -Dparallel=1
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