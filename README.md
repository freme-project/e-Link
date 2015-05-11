# FREME e-Link service

## Building

### Requirements

* Java >= 1.7
* Maven
* Git

### Compile

    cd e-Services/e-link
    mvn clean install

## Run

Compiled jar can be found in the `target` subdirectory. To run the service:

    cd target
    java -jar e-link-1.0-SNAPSHOT.jar

Upon successful start the service will run on `localhost` at `e-link` and port `8080`

## Try it

    curl -X POST -d @../src/main/resources/data/data.ttl "http://localhost:8080/e-link/?outformat=turtle&templateid=1" -H "Content-Type: text/turtle"
