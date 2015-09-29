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

## License

Copyright 2015 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
               Institut für Angewandte Informatik e. V. an der Universität Leipzig,
               Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](LICENSE-3RD-PARTY).

