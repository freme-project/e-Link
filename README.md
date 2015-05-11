# FREME e-Link service

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
* generate more templates (currently only templates for DBpedia)
* support for dataset specific entity linking
  * user can upload his data and perform entity recognition against this dataset

### Complete

* first prototype deployed (11.5.2015)
  * defined templates for DBpedia
  * `templateid` parameters
  * support for NIF (in/out)


