<h2 align="center">Менеджер DTO _dto-box_</h2>
<h3 align="center">Clevertec course</h2>
<h4 align="center">Библиотека объектов для передачи данных приложения - "Система управления новостями"</h3>


<h4>Назначение:</h4>
  * управления DTO-объектами
  * упрощения документирования кода

<h4>Стек:</h4>
* java 17
* gradle
* springframework-validation
* springdoc-openapi

<h4>Инициализация:</h4>
* Библиотека разворачивается в локальном репозитории. Требует предварительной сборки* и добавления в Ваш проект следующих зависимостей:
  * в блок repositories - ```mavenLocal()```
  * в блок dependencies - ``` implementation 'by.clevertec:dto-box:0.0.1-SNAPSHOT'```
* *Для сборки проекта введите команду ```./gradlew build``` в директории dto-box

<h4>Замечания:</h4>
Библиотека является обязательной к установке при работе с приложением "Система управления новостями"