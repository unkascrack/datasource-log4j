**Log4j** appender to registry logs using a **DataSource**

Sample of appender (_log4j.properties_):

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
<hr />