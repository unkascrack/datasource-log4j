# datasource-log4j
Permite escribir los logs a una base de datos, mediante la definición de un datasource.

Cada llamada añade el log a un **ArrayList buffer**, cuando el buffer esta completo cada evento log es lanzado como una sentencia SQL (configurable) a la base de datos.

El appender a invocar para utilizar esta librería debe ser *es.map.sgtic.fw.log4j.DataSourceAppender*

Parámetros definidos en la configuración del appender: 
 * **datasource**, [obligatorio] nombre del datasource. 
 * **sql**, [obligatorio] consulta que lanzar contra la base de datos. 
 * **layout**, [obligatorio] Es necesario definir el layout, siendo posible utilizar cualquiera, aunque es conveniente utilizar el propio layout del appender, DataSourceLayout, el cual dispone de nuevos patrones %e y %m, para la gestión de excepciones. 
 * *bufferSize*, [opcional] número de logs que debe haberse producido para guardarlos en la base de datos, por defecto es 1 (inmediato). 

Si durante la carga del datasource, se produce un error, se muestra un mensaje por la consola y el appender deja de funcionar. 

Si al realizar una insercción en la base de datos se produce un error, muestra la descripción del error por la consola, pero continua con la inserción del resto de logs. 

Ejemplo de entrada apender en log4j.properties: 

    log4j.rootCategory=ERROR, DATASOURCE

    log4j.appender.DATASOURCE=es.map.sgtic.fw.log4j.DataSourceAppender
    log4j.appender.DATASOURCE.datasource=java:jdbc/datasource
    log4j.appender.DATASOURCE.sql=INSERT INTO TABLE_LOG (DE_NIVEL, DE_LOG, DE_EXCEPCION, FE_LOG) VALUES ('%p', '%m', '%e', TO_DATE('%d{yyyy-MM-dd HH:mm:ss}','YYYY-MM-DD HH24:MI:SS'))
    log4j.appender.DATASOURCE.bufferSize=1
    log4j.appender.DATASOURCE.layout=es.map.sgtic.fw.log4j.DataSourceLayout
    log4j.appender.DATASOURCE.layout.maxSizeMessage=1000
    log4j.appender.DATASOURCE.layout.maxSizeException=4000
    log4j.appender.DATASOURCE.layout.maxTraceException=5

