# Bookin backend 

This is the backend part of the bookin web app. You can configure it and compile it separately from the frontend.

## How to configure the backend

You can write the file `src/main/ressources/application.properties` to configure the application. All information is available in the file.

## How to build the backend

* (1) Run `mvn clean install spring-boot:repackage`
* (2) The jar file is in the target folder

## How to run the backend

* (Option 1) Run the previously created jar file with Java
* (Option 2) Use `mvn clean install spring-boot:run` to run without packaging