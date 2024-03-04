<h2 align="center">Стартер обработки ошибок _spring-boot-exception-handler-starter_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Библиотека для отлова,обработки и интерпретации исключений согласно REST</h3>


<h4>Включена обработка следующих исключений:</h4>
* jakarta:
    * EntityNotFoundException
    * PersistenceException
    * ValidationException
* org.springframework:
  * DataIntegrityViolationException
  * HttpMessageNotReadableException
  * MethodArgumentNotValidException
  * MethodArgumentTypeMismatchException
* java.lang
  * IllegalArgumentException
  * ConditionalException
* пользовательские исключения:
  * CustomEntityNotFoundException
  * CustomAccessException
  * CustomNoContentException
  * ConditionalException

<h4>Стек:</h4>
* java 17
* gradle
* springframework
* junit / assertj

<h4>Инициализация:</h4>
* Библиотека разворачивается в локальном репозитории. Требует предварительной сборки* и добавления в Ваш проект следующих зависимостей:
  * в блок repositories - ```mavenLocal()```
  * в блок dependencies - ```implementation 'by.clevertec:spring-boot-exception-handler-starter:0.0.1-SNAPSHOT'```
* *Для сборки проекта введите команду ```./gradlew build``` в директории стартера

<h4>Ответы обработчика ошибок:</h4>
* ExceptionResponse ```{
  "status": int,
  "timestamp": "string",
  "message": "string",
  "type": "string"
  }```
* ErrorValidationResponse ```{
  "status": int,
  "timestamp": "string",
  "errors": [
  "string"
  ],
  "message": "string"
  }```

