<h2 align="center">Сервис _auth_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Сервис управления безопасностью приложения "Система управления новостями"</h3>


<h4>Назначение:</h4>
  * управления безопасностью
  * сервис аутентификации и авторизации

<h4>Стек:</h4>
* java 17
* gradle
* springframework (web / data / security / oauth2)
* mapstruct
* postgresql
* liquibase
* redis
* springdoc-openapi
* junit / assertj
* testcontainers

<h4>Инициализация:</h4>
* Для работы необходимы установленные библиотеки и стартеры:
  * _by.clevertec:dto-box_ (Менеджер DTO)
  * _by.clevertec:spring-boot-logger-aspect-starter_ (Стартер логирования)
* Является микросервисом приложения "Система управления новостями". __Не предназначен для работы в автономном режиме__ 
* Для сборки микросервиса в автономном режиме введите команду ```./gradlew build``` в директории auth
* Порт локального развертывания __8003__ 

<h4>Endpoint:</h4>
* Конечные точки подробно описаны с использованием Open Api**
* __POST__ __/admin/set/{id}__ назначает пользователя по его идентификатору администратором. Требует авторизации под ролью администратора. Формат возвращаемого сообщения  ```{
  "status": 200,
  "timestamp": "2024-02-29T20:42:35.5421876",
  "message": "The role from user have been successful changed"
  }```
* __GET__ __/admin/users__ возвращает список всех зарегистрированных пользователей. Требует авторизации под ролью администратора. Формат возвращаемого сообщения  ```[
  {
  "id": 1,
  "username": "subscriber",
  "role": "ROLE_SUBSCRIBER"
  },
  {
  "id": 2,
  "username": "journalist",
  "role": "ROLE_JOURNALIST"
  }
 ]```
* __POST__ __/auth__ создает jwt-токен на основе полученной информации о пользователе. Формат возвращаемого сообщения  ```{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIl0sInN1YiI6ImFkbWluIiwiZXhwIjoxNzA5MjI1MTc0fQ.DRYv0YNex4x_A_1KmN2dNX7BLZDNxtZCLgImuLn9xxI"
  }```
* __POST__ __/registration__ регистрирует нового пользователя. Формат возвращаемого сообщения  ```{
  "status": 200,
  "timestamp": "2024-02-29T20:35:40.5155621",
  "message": "The user have been successful created"
  }```


<h4>**Open Api Swagger:</h4>
* документация доступна по адресу __/documentation__
* api-doc доступны по адресу __/api-doc__

<h4>Параметры запуска:</h4>
* Используется база данных PostgreSQL, поднятие таблиц происходит при старте сервиса
