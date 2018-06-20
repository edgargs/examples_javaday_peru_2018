
@Grab('com.xlson.groovycsv:groovycsv:1.1')
import static com.xlson.groovycsv.CsvParser.parseCsv

for(line in parseCsv(new FileReader('countries.csv'), separator: ',')) {
    println "\nRecevive=$line.RECEIVE, Message=$line.MESSAGE"
    sendSMS2(line.RECEIVE,line.MESSAGE);
}

@Grapes([
    @Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
])

import java.io.*
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.EncoderRegistry
import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

void sendSMS2 (sms_receive,sms_message) {
    
    //http://192.168.1.2:81/sendmsg?user=1&passwd=Pass2017&cat=1&priority=1&to=987654321&text=Hola_Mundo

    String sms_cat = "1" //alertMessage.getSms_cat();
    String sms_passwd = "Pass2017" //alertMessage.getSms_passwd();
    String sms_user = "smsuser" //alertMessage.getSms_user();
            
    String base = 'http://192.168.1.2:81'
    def http = new groovyx.net.http.HTTPBuilder(base)
    
    http.request(GET, TEXT ) { req ->
        
        uri.path = '/sendmsg'
        uri.query = [user:sms_user,
                         passwd:sms_passwd,
                         cat:sms_cat,
                         priority:1,
                         to:sms_receive,
                         text:sms_message]

        response.success = { resp, reader ->
            println "Content-Type: ${resp.headers.'Content-Type'}"
            println reader.text
            println "$resp.statusLine   Respond rec"
        }
    }
        
}