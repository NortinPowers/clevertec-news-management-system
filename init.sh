#!/bin/bash

cd dto-box
./gradlew build -x test

cd ../spring-boot-exception-handler-starter
./gradlew build -x test

cd ../spring-boot-logger-aspect-starter
./gradlew build -x test

cd ../news
./gradlew build -x test 

cd ../comment
./gradlew build -x test 

cd ../auth
./gradlew build -x test  

cd ../gateway
./gradlew build -x test 
    
cd ..
docker-compose up --build 
