# FREME e-Link service

The service is in details described [here](https://github.com/freme-project/technical-discussion/wiki/Broker-API-Calls#user-content-e-link).

## Building

### Requirements

* Java >= 1.7
* Maven
* Git

### Compile

    cd e-Link
    mvn clean install    

## Run through the Broker

Learn how to start the broker [here](https://github.com/freme-project/technical-discussion/wiki/Compile-FREME-from-Source).

## Try it

    curl -X POST -d @../src/main/resources/data/data.ttl "http://localhost:8080/e-link/?outformat=turtle&templateid=1" -H "Content-Type: text/turtle"

### Working on

* support for Linked Data Fragments

### TODOs

* support for Linked Data Fragments
* define more templates (currently only templates for DBpedia)
* develop API for creating/update/listing available templates
  * possible develop simple GUI for template editing 
* document best practices for developing templates

### Complete

* first prototype deployed (11.5.2015)
  * defined templates for DBpedia
  * `templateid` parameters
  * support for NIF (in/out)


