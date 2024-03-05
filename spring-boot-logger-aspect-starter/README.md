<h2 align="center">Стартер логирования _spring-boot-logger-aspect-starter_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Библиотека для логирования</h3>


<h4>Включена возможность логирования следующих слоев:</h4>
* controller:
    * запрос (уровень лога - ```info```)
    * ответ (уровень лога - ```info```)
* service:
  * ошибки уровня _error_ (уровень лога - ```error```)
* repository:
  * ошибки уровня _error_ (уровень лога - ```error```)

<h4>Стек:</h4>
* java 17
* gradle
* springframework

<h4>Инициализация:</h4>
* Библиотека разворачивается в локальном репозитории. Требует предварительной сборки* и добавления в Ваш проект следующих зависимостей:
  * в блок repositories - ```mavenLocal()```
  * в блок dependencies - ```implementation 'by.clevertec:spring-boot-logger-aspect-starter:0.0.1-SNAPSHOT'```
* *Для сборки проекта введите команду ```./gradlew build``` в директории стартера

<h4>Использование:</h4>
* В слое controller
    *  пометьте метод аннотацией ```@ControllerAspectLogger```
* В слое service
    *  пометьте метод аннотацией ```@ServiceAspectLogger```
* В слое repository
    *  пометьте метод аннотацией ```@RepositoryAspectLogger```
