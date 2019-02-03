# log4j-datasource
Permite escribir los logs a una base de datos, mediante la definición de un datasource.

Cada llamada añade el log a un **ArrayList buffer**, cuando el buffer esta completo cada evento log es lanzado como una sentencia SQL (configurable) a la base de datos.

El appender a invocar para utilizar esta librería debe ser *org.apache.log4j.jndi.datasource.DataSourceAppender*

Parámetros definidos en la configuración del appender: 
 * **datasource** [obligatorio], nombre del datasource. 
 * **layout** [obligatorio], layout del appender, siendo posible utilizar cualquiera, aunque es conveniente utilizar el propio layout del appender, DataSourceLayout, el cual dispone de nuevos patrones %e y %m, para la gestión de excepciones. 
 * *bufferSize* [opcional], número de logs que debe haberse producido para guardarlos en la base de datos, por defecto es 1 (inmediato).

Tambien está disponible el layout, *org.apache.log4j.jndi.datasource.DataSourceLayout*, el cual dispone los comodines
 * **%h**, nombre del host que produce la traza de log.
 * **%m**, mensaje de la traza formatea a SQL, al que se le puede definir su tamaño máximo (maxSizeMessage).
 * **%e**, mensaje de la traza de la excepción.
 
Los parámetros del layout *org.apache.log4j.jndi.datasource.DataSourceLayout* son: 
 * *sqlPattern* [opcional], consulta que lanzar contra la base de datos.
 * *maxSizeMessage* [opcional], tamaño máximo del mensaje (%m). 
 * *maxSizeException* [opcional], tamaño máximo del mensaje de excepción (%e).
 * *maxTraceException* [opcional], número máximo de las trazas de excepción (%e).

Si durante la carga del datasource, se produce un error, se muestra un mensaje por la consola y el appender no se activará. 

Si al realizar una insercción en la base de datos se produce un error, muestra la descripción del error por la consola, pero continua con la inserción del resto de logs. 

Ejemplo de entrada apender en log4j.properties: 

    log4j.rootCategory=ERROR, DATABASE

    log4j.appender.DATABASE=org.apache.log4j.jndi.datasource.DataSourceAppender
    log4j.appender.DATABASE.datasource=java:jdbc/datasource
    log4j.appender.DATABASE.bufferSize=1
    log4j.appender.DATABASE.layout=org.apache.log4j.jndi.datasource.DataSourceLayout
    log4j.appender.DATABASE.layout.sqlPattern=INSERT INTO LOG (LEVEL, HOSTNAME, MESSAGE, EXCEPTION, DATE_LOG) VALUES ('%p', '%h', '%m', '%e', '%d{yyyy-MM-dd HH:mm:ss}')
    log4j.appender.DATABASE.layout.maxSizeMessage=1000
    log4j.appender.DATABASE.layout.maxSizeException=4000
    log4j.appender.DATABASE.layout.maxTraceException=5


Ejemplo de entrada apender en log4j.xml:

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
    <log4j:configuration debug="false">
        <appender name="DATABASE" class="org.apache.log4j.jndi.datasource.DataSourceAppender">
            <param name="datasource" value="jdbc/datasource" />
            <!-- <param name="threshold" value="WARN" /> -->
            <layout class="org.apache.log4j.jndi.datasource.DataSourceLayout">
                <param name="sqlPattern" value="INSERT INTO LOG (LEVEL, HOSTNAME, MESSAGE, EXCEPTION, DATE_LOG) VALUES ('%p', '%h', '%m', '%e', '%d{yyyy-MM-dd HH:mm:ss}')" />
                <param name="maxSizeMessage" value="4000" />
                <param name="maxSizeException" value="4000" />
            </layout>
        </appender>
        <root>
            <appender-ref ref="DATABASE" />
        </root>
    </log4j:configuration>