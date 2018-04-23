# GraphQL Schema & Service Generator

A web application that generates, automatically, a GraphQL Schema and its corresponding GraphQL service from a given RDF Ontology.

## Technologies used
- HTML,CSS,JavaScript, JSP: To make the interface of the tool.
- GraphQL schema language: Language used to describe the GraphQL Schema.
- Java: Main programming language of the tool.
- Java Servlets: To handle http requests.
- RDF: To define the graphql schema in triples.
- Virtuoso server: External service that stores RDF datasets.
- SPARQL: To retrieve data from the RDF datasets.
- Apache Tomcat server: To deploy the tool and the GraphQL services that it generates.
- GraphQL-Java: Library used to have the implementation of GraphQL in Java (https://github.com/graphql-java/graphql-java).
- GraphQL-Java-tools: Library that makes much easier to implement a Java GraphQL service (https://github.com/graphql-java/graphql-java-tools).
- Javapoet: Library used to create dinamically GraphQL services (https://github.com/square/javapoet).

## Deployment instructions

- Load a GCQL-annotated RDF ontology file (e.g. "utils/ontology*.ttl") on a Virtuoso server (tested version: 7.2.4).
- Deploy the "utils/UltimaVersio.war" file on an Apache Tomcat web server (tested version: 8.5 with JDK 1.8, hot-deploy mode enabled).

## Credits

Code written by Robert Almar (@rodergas) under the supervision of Carles Farré and Jovan Varga.