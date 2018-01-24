# Aplicació web principal

Aquesta serà l'aplicació web que generarà automàticament el servidor GraphQL i l'esquema GraphQL.

Aquesta aplicació es pot assegurar que funciona bé en el següent entorn:

- L'ordinador del client ha de tenir un sistema operatiu Windows 
- Un servidor web Apache Tomcat versió 8.5 que tingui JDK 1.8, l'arxiu "utils/principal.war" es desplegarà en aquest servidor. S'ha de dir que aquest servidor haurà de estar en mode "hot deploy", explicat a l'apartat 11 de la memòria "utils/memoria.pdf"
- Un servidor Virtuoso versió 7.2.4 el qual tingui una base de dades que guardi els arxius "utils/ontologia*.ttl"
