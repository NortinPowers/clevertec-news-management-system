<h2 align="center">Сервис _gateway_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Сервис реализующий связь и точку входа в приложение "Система управления новостями"</h3>


<h4>Назначение:</h4>
  * управления новостями
  * управление комментариями
  * аутентификация и авторизация пользователей

<h4>Стек:</h4>
* java 17
* gradle
* spring-boot (web / data / validation / security)
* spring-cloud / openfeign / circuitbreaker / validation)
* springdoc-openapi
* junit / assertj
* wiremock

<h4>Инициализация:</h4>
* Для работы необходимы установленные библиотеки и стартеры:
  * _by.clevertec:dto-box_ (Менеджер DTO)
  * _by.clevertec:spring-boot-exception-handler-starter_ (Стартер обработки ошибок)
  * _by.clevertec:spring-boot-logger-aspect-starter_ (Стартер логирования)
* Является главным сервисом приложения "Система управления новостями". Для работы в штатном режиме необходима работа микросервисов __news__, __comment__, __auth__ 
* __Не предназначен для работы в автономном режиме__ 
* Для сборки микросервиса в автономном режиме введите команду ```./gradlew build``` в директории gateway
* Порт локального развертывания __8000__ 

<h4>Endpoint:</h4>
* Конечные точки подробно описаны с использованием Open Api** 
* __GET__ __/api/news__ возвращает выбранную страницу с определенным в запросе количеством новостей. Формат каждой возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```
* __GET__ __/api/news/{id}__ возвращает новость по ее уникальному идентификатору. Формат возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```
* __POST__ __/api/news__ сохраняет новую новость на основе предоставленных данных. Формат тела запроса ```{
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }```
* __PUT__ __/api/news/{id}__ обновляет данные новости по её уникальному идентификатору. Требует авторизации, роль администратор или журналист. Формат тела запроса ```{
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }``` 
* __PATH__ __/api/news/{id}__ частично обновляет некоторые данные новости по её уникальному идентификатору. Требует авторизации, роль администратор или журналист, проверка на авторство. Формат тела запроса ```{
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }```
* __DELETE__ __/api/news/{id}__ удаляет новость по его уникальному идентификатору. Требует авторизации, роль администратор или журналист, проверка на авторство. Формат возвращаемого сообщения ```{
  "status": 200,
  "timestamp": "2024-02-29T18:02:36.6523104",
  "message": "The news have been successful deleted"
  }```
* __GET__ __/api/news/search/{condition}__ возвращает выбранную страницу с определенным в запросе количеством новостей в соответствии с поисковым запросом. Требует авторизации. Формат каждой возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```
* __GET__ __/api/comments__ возвращает выбранную страницу с определенным в запросе количеством комментариев. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __GET__ __/api/comments/{id}__ возвращает комментарий по его уникальному идентификатору. Формат возвращаемого комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __POST__ __/api/comments__ сохраняет новый комментарий на основе предоставленных данных. Требует авторизации. Формат тела запроса ```{
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }```
* __PUT__ __/api/comments/{id}__ обновляет данные комментария по его уникальному идентификатору. Требует авторизации, проверка на авторство. Формат тела запроса ```{
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }```
* __PATH__ __/api/comments/{id}__ частично обновляет некоторые данные комментария по его уникальному идентификатору. Требует авторизации, проверка на авторство. Формат тела запроса ```{
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }```
* __DELETE__ __/api/comments/{id}__ удаляет комментарий по его уникальному идентификатору. Требует авторизации, проверка на авторство. Формат возвращаемого сообщения ```{
  "status": 200,
  "timestamp": "2024-02-29T18:02:36.6523104",
  "message": "The comments have been successful deleted"
  }```
* __GET__ __/api/comments/news/{newsId}__ возвращает выбранную страницу с определенным в запросе количеством комментариев в соответствии с идентификатором новости. Требует авторизации. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __GET__ __/api/comments/search/{condition}__ возвращает выбранную страницу с определенным в запросе количеством комментариев в соответствии с поисковым запросом. Требует авторизации. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __POST__ __/admin/set/admin/{id}__ назначает пользователя по его идентификатору администратором. Требует авторизации под ролью администратора. Формат возвращаемого сообщения  ```{
  "status": 200,
  "timestamp": "2024-02-29T20:42:35.5421876",
  "message": "The role from user have been successful changed"
  }```
* __POST__ __/admin/set/journalist/{id}__ назначает пользователя по его идентификатору журналистом. Требует авторизации под ролью администратора. Формат возвращаемого сообщения  ```{
  "status": 200,
  "timestamp": "2024-02-29T20:42:35.5421876",
  "message": "The role from user have been successful changed"
  }```
* __GET__ __/api/admin/users__ возвращает список всех зарегистрированных пользователей. Требует авторизации под ролью администратора. Формат возвращаемого сообщения  ```[
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
* __POST__ __/api/auth__ создает jwt-токен на основе полученной информации о пользователе. Формат возвращаемого сообщения  ```{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIl0sInN1YiI6ImFkbWluIiwiZXhwIjoxNzA5MjI1MTc0fQ.DRYv0YNex4x_A_1KmN2dNX7BLZDNxtZCLgImuLn9xxI"
  }```
* __POST__ __/api/registration__ регистрирует нового пользователя. Формат возвращаемого сообщения  ```{
  "status": 200,
  "timestamp": "2024-02-29T20:35:40.5155621",
  "message": "The user have been successful created"
  }```

<h4>**Open Api Swagger:</h4>
* документация доступна по адресу __/documentation__
* api-doc доступны по адресу __/api-doc__

<h4>Параметры запуска:</h4>
* Выбор осуществляется через файл конфигурации, настройка profiles
