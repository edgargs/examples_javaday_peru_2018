
@Grapes([
    @Grab('com.xlson.groovycsv:groovycsv:1.1'),
    @Grab(group='org.postgresql', module='postgresql', version='9.4.1212.jre7'),
    //@Grab(group='com.microsoft.sqlserver', module='sqljdbc4', version='4.0'),
    @GrabConfig(systemClassLoader = true)
])
import static com.xlson.groovycsv.CsvParser.parseCsv
import groovy.sql.Sql

def conPostgis = Sql.newInstance("jdbc:postgresql://dbpostgis.c2bchrbtgmoa.us-east-1.rds.amazonaws.com:5432/gis","channels","Lima2018","org.postgresql.Driver")

for(line in parseCsv(new FileReader('red_vial_nacional_dic16.csv'), separator: ',')) {
    //println line
    /*FID,Id,dkmInicio,dkmFinal,cCodRuta,dLongitud,cSentido,dNroCarriles,dAncCalzada,
    cClasificado,cCodDepartamento,cDepartamento,cNomRuta,cRegion,cTopografia,dVelProTramo,
    cNroResMinisterial,dTipIntervencion,cSuperficie,cEstado,dSuperficieDic2016,cEstadoDic2016,
    cTipRedDic2016,cCodRutaDic2016,dLongitudDic2016,Shape_STLength__,Shape,ZCL*/
    //println "\nId=$line.Id, $line.cCodRuta, Shape=${line.Shape.length()}"
    copiaData(line, conPostgis)
    updateGeom(line, conPostgis)
}

void copiaData(def fila, Sql conPostgis) {
    sql_insert = """ INSERT INTO red_vial_nacional_dic16_01 (Id ,cCodRuta ,Shape_text)
              VALUES(?,?,? ) """
    param_delete = [Integer.parseInt(fila.Id)]    
    param_insert = [fila.cCodRuta ,fila.Shape ]

    conPostgis.execute(sql_insert,param_delete + param_insert)
    //conPostgis.commit()

    println "inserted:"+fila.Id
}

def updateGeom(def fila, Sql conPostgis) {
    sql_update = """ UPDATE red_vial_nacional_dic16_01
                  SET Shape = ST_GeometryFromText(Shape_text,4326)
                  WHERE Id = ?
            """
    param_delete = [Integer.parseInt(fila.Id)]

    conPostgis.execute(sql_update,param_delete)
}

conPostgis.eachRow("""
    SELECT Id,st_srid(Shape) the_geom_import FROM "red_vial_nacional_dic16_01"
    """) {
    fila ->
        println fila
}

//Cerrar conexiones
conPostgis.close()