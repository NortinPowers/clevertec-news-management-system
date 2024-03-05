<h2 align="center">Сервис _comment_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Сервис комментариев приложения "Система управления новостями"</h3>


<h4>Назначение:</h4>
  * управления комментариями к новостям

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
* Для сборки микросервиса в автономном режиме введите команду ```./gradlew build``` в директории comment
* Порт локального развертывания __8002__ 

<h4>Endpoint:</h4>
* Конечные точки подробно описаны с использованием Open Api** 
* __GET__ __/comments__ возвращает выбранную страницу с определенным в запросе количеством комментариев. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __GET__ __/comments/{id}__ возвращает комментарий по его уникальному идентификатору. Формат возвращаемого комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __POST__ __/comments__ сохраняет новый комментарий на основе предоставленных данных. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }
  }```
* __PUT__ __/comments/{id}__ обновляет данные комментария по его уникальному идентификатору. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }
  }``` 
* __POST__ __/comments/{id}__ частично обновляет некоторые данные комментария по его уникальному идентификатору. POST вместо PATH обусловлен использованием Feign клиента. Формат тела запроса ```{
  "name": "John Gold",
  "requestDto": {
  "text": "I haven't read anything better",
  "username": "Hank Rearden",
  "newsId": 1
  }
  }```
* __DELETE__ __/comments/{id}__ удаляет комментарий по его уникальному идентификатору. Формат возвращаемого сообщения ```{
  "status": 200,
  "timestamp": "2024-02-29T18:02:36.6523104",
  "message": "The comments have been successful deleted"
  }```
* __GET__ __/comments/news/{newsId}__ возвращает выбранную страницу с определенным в запросе количеством комментариев в соответствии с идентификатором новости. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```
* __GET__ __/comments/search/{condition}__ возвращает выбранную страницу с определенным в запросе количеством комментариев в соответствии с поисковым запросом. Формат каждого возвращенного комментария ```{
  "time": "2024-02-28T13:05:00",
  "text": "Отличная статья, спасибо за информацию!",
  "username": "Samael",
  "newsId": 1,
  "author": "Samael"
  }```

<h4>**Open Api Swagger:</h4>
* документация доступна по адресу __/documentation__
* api-doc доступны по адресу __/api-doc__

<h4>Параметры запуска:</h4>
* Доступно две конфигурации кэша на выбор. Для старта необходимо выбрать одну из реализаций.
  * lfu/lru cache (lfu-lru)
  * redis cache (redis)
* Выбор осуществляется через файл конфигурации, настройка profiles
* Используется база данных PostgreSQL
