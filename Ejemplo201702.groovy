@Grapes([
    @Grab(group='org.postgresql', module='postgresql', version='9.4.1212.jre7'),
    @Grab(group='com.microsoft.sqlserver', module='sqljdbc4', version='4.0'),
    @GrabConfig(systemClassLoader = true)
])
import groovy.sql.Sql

def conPostgis = Sql.newInstance("jdbc:postgresql://192.168.100.8:5432/postgis","admin","789456","org.postgresql.Driver")
def conMatrix = Sql.newInstance("jdbc:sqlserver://192.168.100.5:1433;databaseName=NUEVO","sa","456123","com.microsoft.sqlserver.jdbc.SQLServerDriver")

void copiaData(int gid_ini, int gid_fin, Sql conPostgis, Sql conMatrix) {
    println "consulta entre ${gid_ini} and ${gid_fin}"
    conPostgis.eachRow("""
    SELECT *,ST_AsText(the_geom) the_geom_import FROM "Limites" WHERE gid between ${gid_ini} and ${gid_fin}
    """) {
    fila ->
          sql_insert = """ INSERT INTO MTXLimites (gid ,discod95 ,procod95 ,depcod ,disnom95 ,pronom95 ,depnom ,
              the_geom_import)
              VALUES(?,?,?,?,?,?,?,? ) """
          param_delete = [fila.gid]    
          param_insert = [fila.discod95 ,fila.procod95 ,fila.depcod ,fila.disnom95 ,fila.pronom95 ,fila.depnom ,
                          fila.the_geom_import ]

           sql_update = """ UPDATE MTXLimites_Peru
                  SET the_geom = geometry::STGeomFromText(the_geom_import,4326)
                  WHERE gid = ?
            """

          conMatrix.execute(sql_insert,param_delete + param_insert)
          conMatrix.execute(sql_update,param_delete)
          conMatrix.commit()

          println "inserted:"+fila.gid    
  }
}

copiaData(1,1000,conPostgis, conMatrix)
copiaData(1001,2000,conPostgis, conMatrix)

//Cerrar conexiones
conPostgis.close()
conMatrix.close()