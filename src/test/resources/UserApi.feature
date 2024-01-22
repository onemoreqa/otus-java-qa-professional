# language: en

@api
Feature: CRUD УЗ пользователя

  Scenario: Создание пользователя user1 с полным набором атрибутов
    Given Swagger: 'https://petstore.swagger.io/v2'
    And данные пользователя 'user1.json'
    When отправляется запрос на создание пользователя
    Then возвращается ответ со статусом '200'

  Scenario: Создание пользователя user2 с не полным набором атрибутов
    Given Swagger: 'https://petstore.swagger.io/v2'
    And данные пользователя 'user2.json'
    When отправляется запрос на создание пользователя
    Then возвращается ответ со статусом '200'

  Scenario: Проверка данных пользователя user1
    Given Swagger: 'https://petstore.swagger.io/v2'
    And данные пользователя 'user1.json'
    And имя пользователя через атрибут 'username'
    And проверяем полное совпадение
    When отправляется запрос на получение сведений о пользователе
    Then данные в файле и ответе с сайта совпадают

  Scenario: Проверка данных пользователя user2
    Given Swagger: 'https://petstore.swagger.io/v2'
    And данные пользователя 'user2.json'
    And имя пользователя через атрибут 'username'
    And проверяем частичное совпадение
    When отправляется запрос на получение сведений о пользователе
    Then данные в файле и ответе с сайта совпадают