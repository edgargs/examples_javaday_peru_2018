@Grapes([
    @Grab(group='org.postgresql', module='postgresql', version='9.4.1212.jre7'),
    @Grab(group='com.microsoft.sqlserver', module='sqljdbc4', version='4.0'),
    @GrabConfig(systemClassLoader = true)
])
import groovy.sql.Sql

def conPostgis = Sql.newInstance("jdbc:postgresql://dbpostgis.c2bchrbtgmoa.us-east-1.rds.amazonaws.com:5432/gis","channels","Lima2018","org.postgresql.Driver")
def conMatrix = Sql.newInstance("jdbc:sqlserver://dbchannels.c2bchrbtgmoa.us-east-1.rds.amazonaws.com:1433;databaseName=gis","channels","Lima2018","com.microsoft.sqlserver.jdbc.SQLServerDriver")

void copiaData(int gid_ini, int gid_fin, Sql conPostgis, Sql conMatrix) {
    
    conPostgis.eachRow("""
    SELECT *,ST_AsText(Shape) the_geom_import FROM "red_vial_nacional_dic16_01" WHERE Id between ${gid_ini} and ${gid_fin}
    """) {
    fila ->
          sql_insert = """ INSERT INTO red_vial_nacional_dic16_01 (Id ,cCodRuta ,the_geom_import)
              VALUES(?,?,? ) """
          param_delete = [fila.Id]    
          param_insert = [fila.cCodRuta ,
                          fila.the_geom_import ]

           sql_update = """ UPDATE red_vial_nacional_dic16_01
                  SET the_geom = geometry::STGeomFromText(the_geom_import,4326)
                  WHERE Id = ?
            """
            //println param_insert
          conMatrix.execute(sql_insert,param_delete + param_insert)
          conMatrix.execute(sql_update,param_delete)
          conMatrix.commit()
          
          println "inserted:"+fila.Id    
  }
}

//copiaData(1,1000,conPostgis, conMatrix)
//copiaData(1001,2000,conPostgis, conMatrix)

conMatrix.eachRow("""
    SELECT Id,the_geom.STSrid the_geom_srid FROM "red_vial_nacional_dic16_01"
    """) {
    fila ->
        println fila
}

//Cerrar conexiones
conPostgis.close()
conMatrix.close()