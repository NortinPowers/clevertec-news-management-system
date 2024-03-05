<h2 align="center">Сервис _news_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Сервис новостей приложения "Система управления новостями"</h3>


<h4>Назначение:</h4>
  * управления новостями

<h4>Стек:</h4>
* java 17
* gradle
* springframework (web / data / validation)
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
  * _by.clevertec:spring-boot-exception-handler-starter_ (Стартер обработки ошибок)
  * _by.clevertec:spring-boot-logger-aspect-starter_ (Стартер логирования)
* Является микросервисом приложения "Система управления новостями". __Не предназначен для работы в автономном режиме__ 
* Для сборки микросервиса в автономном режиме введите команду ```./gradlew build``` в директории news
* Порт локального развертывания __8001__ 

<h4>Endpoint:</h4>
* Конечные точки подробно описаны с использованием Open Api** 
* __GET__ __/news__ возвращает выбранную страницу с определенным в запросе количеством новостей. Формат каждой возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```
* __GET__ __/news/{id}__ возвращает новость по ее уникальному идентификатору. Формат возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```
* __POST__ __/news__ сохраняет новую новость на основе предоставленных данных. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }
  }```
* __PUT__ __/news/{id}__ обновляет данные новости по её уникальному идентификатору. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }
  }``` 
* __POST__ __/news/{id}__ частично обновляет некоторые данные новости по её уникальному идентификатору. POST вместо PATH обусловлен использованием Feign клиента. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "title": "An incredible event",
  "text": "This has never happened before and here it is again"
  }
  }```
* __DELETE__ __/news/{id}__ удаляет новость по его уникальному идентификатору. Формат возвращаемого сообщения ```{
  "status": 200,
  "timestamp": "2024-02-29T18:02:36.6523104",
  "message": "The news have been successful deleted"
  }```
* __GET__ __/news/search/{condition}__ возвращает выбранную страницу с определенным в запросе количеством новостей в соответствии с поисковым запросом. Формат каждой возвращаемой новости ```  {
  "time": "2024-02-01T14:00:00",
  "title": "Clickbait title",
  "text": "Very interesting news text",
  "author": "Stephen King"
  }```

<h4>**Open Api Swagger:</h4>
* документация доступна по адресу __/documentation__
* api-doc доступны по адресу __/api-doc__

<h4>Параметры запуска:</h4>
* Доступно две конфигурации кэша на выбор. Для старта необходимо выбрать одну из реализаций.
  * lfu/lru cache (lfu-lru)
  * redis cache (redis)
* Выбор осуществляется через файл конфигурации, настройка profiles
* Используется база данных PostgreSQL, поднятие таблиц происходит при старте сервиса
