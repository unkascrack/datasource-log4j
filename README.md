# log4j-datasource
Permite escribir los logs a una base de datos, mediante la definición de un datasource.

Cada llamada añade el log a un **ArrayList buffer**, cuando el buffer esta completo cada evento log es lanzado como una sentencia SQL (configurable) a la base de datos.

El appender a invocar para utilizar esta librería debe ser *org.apache.log4j.datasource.DataSourceAppender*

Parámetros definidos en la configuración del appender: 
 * **datasource**, [obligatorio] nombre del datasource. 
 * **sql**, [obligatorio] consulta que lanzar contra la base de datos. 
 * **layout**, [obligatorio] Es necesario definir el layout, siendo posible utilizar cualquiera, aunque es conveniente utilizar el propio layout del appender, DataSourceLayout, el cual dispone de nuevos patrones %e y %m, para la gestión de excepciones. 
 * *bufferSize*, [opcional] número de logs que debe haberse producido para guardarlos en la base de datos, por defecto es 1 (inmediato). 

Si durante la carga del datasource, se produce un error, se muestra un mensaje por la consola y el appender deja de funcionar. 

Si al realizar una insercción en la base de datos se produce un error, muestra la descripción del error por la consola, pero continua con la inserción del resto de logs. 

Ejemplo de entrada apender en log4j.properties: 

    log4j.rootCategory=ERROR, DATASOURCE

    log4j.appender.DATASOURCE=org.apache.log4j.datasource.DataSourceAppender
    log4j.appender.DATASOURCE.datasource=java:jdbc/datasource
    log4j.appender.DATASOURCE.sql=INSERT INTO TABLE_LOG (DE_NIVEL, DE_LOG, DE_EXCEPCION, FE_LOG) VALUES ('%p', '%m', '%e', TO_DATE('%d{yyyy-MM-dd HH:mm:ss}','YYYY-MM-DD HH24:MI:SS'))
    log4j.appender.DATASOURCE.bufferSize=1
    log4j.appender.DATASOURCE.layout=org.apache.log4j.datasource.DataSourceLayout
    log4j.appender.DATASOURCE.layout.maxSizeMessage=1000
    log4j.appender.DATASOURCE.layout.maxSizeException=4000
    log4j.appender.DATASOURCE.layout.maxTraceException=5


Ejemplo de entrada apender en log4j.xml:

    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
    <log4j:configuration debug="false">
        <appender name="DATABASE" class="org.apache.log4j.datasource.DataSourceAppender">
            <param name="datasource" value="jdbc/datasource" />
            <!-- <param name="threshold" value="WARN" /> -->
            <layout class="org.apache.log4j.datasource.DataSourceLayout">
                <param name="sqlPattern" value="INSERT INTO LOG (ID, LEVEL, CLASSNAME, MESSAGE, EXCEPTION, DATE_MESSAGE) VALUES (SEQ_LOG.nextval, '%p', '%c', '%m', '%e', SYSTIMESTAMP)" />
                <param name="maxSizeMessage" value="4000" />
                <param name="maxSizeException" value="4000" />
            </layout>
        </appender>
        <root>
            <appender-ref ref="DATABASE" />
        </root>
   </log4j:configuration>