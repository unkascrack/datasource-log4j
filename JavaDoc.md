**DataSourceAppender** permite escribir los logs a una base de datos, mediante la definición de un datasource.

Cada llamada añade el log a un **ArrayList buffer**, cuando el buffer esta completo cada evento log es lanzado como una sentencia SQL (configurable) a la base de datos

El appender a invocar para utilizar esta librería debe ser **`es.map.sgtic.fw.log4j.DataSourceAppender`**

Parámetros definidos en la configuración del appender:
  * **datasource**, [obligatorio](obligatorio.md) nombre del datasource.
  * **sql**, [obligatorio](obligatorio.md) consulta que lanzar contra la base de datos.
  * **layout**, [obligatorio](obligatorio.md) Es necesario definir el layout, siendo posible utilizar cualquiera, aunque es conveniente utilizar el propio layout del appender, DataSourceLayout, el cual dispone de nuevos patrones %e y %m, para la gestión de excepciones.
  * **bufferSize**, [opcional](opcional.md) número de logs que debe haberse producido para guardarlos en la base de datos, por defecto es 1 (inmediato).

Si durante la carga del datasource, se produce un error, se muestra un mensaje por la consola y el appender deja de funcionar.

Si al realizar una insercción en la base de datos se produce un error, muestra la descripción del error por la consola, pero continua con la inserción del resto de logs.

Ejemplo de entrada apender en log4j.properties:

---

`log4j.rootCategory=ERROR, DATASOURCE`<br>

<code>log4j.appender.DATASOURCE=es.map.sgtic.fw.log4j.DataSourceAppender</code><br>
<code>log4j.appender.DATASOURCE.datasource=java:jdbc/datasource</code><br>
<code>log4j.appender.DATASOURCE.sql=INSERT INTO TABLE_LOG (DE_NIVEL, DE_LOG, DE_EXCEPCION, FE_LOG) VALUES ('%p', '%m', '%e', TO_DATE('%d{yyyy-MM-dd HH:mm:ss}','YYYY-MM-DD HH24:MI:SS'))</code><br>
<code>log4j.appender.DATASOURCE.bufferSize=1</code><br>
<code>log4j.appender.DATABASE.layout=es.map.sgtic.fw.log4j.DataSourceLayout</code><br>
<code>log4j.appender.DATABASE.layout.maxSizeMessage=1000</code><br>
<code>log4j.appender.DATABASE.layout.maxSizeException=4000</code><br>
<code>log4j.appender.DATABASE.layout.maxTraceException=5</code><br>

<code>log4j.appender.DATABASE.layout.maxSizeException=4000</code>

<code>log4j.appender.DATABASE.layout.maxTraceException=5</code>
<hr />