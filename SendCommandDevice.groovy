
void sendSMS (receive,message) {
        String sms_receives = receive //alertMessage.getSms_receives().replaceAll(";", ",");
        String sms_message = message //alertMessage.getSms_message();

        /*
        SMS_SERVER_CAT    1
        SMS_SERVER_PASSWD    5m52017
        SMS_SERVER_URL    http://192.168.1.2:81/sendmsg
        SMS_SERVER_USER    smsmatrix
        */
        String sms_cat = "1" //alertMessage.getSms_cat();
        String sms_passwd = "5m52017" //alertMessage.getSms_passwd();
        String sms_url = "http://192.168.1.2:81/sendmsg" //alertMessage.getSms_url();
        String sms_user = "smsmatrix" //alertMessage.getSms_user();
        
        // 2017-07-20 Envia varios numeros de celular
        //String[] receives = sms_receives.split(",");
        
        //for(String receive: receives) {
        //    if(receive.length()>0) {
                String url = sms_url + "?user=" + sms_user + "&passwd=" + sms_passwd + "&cat=" + sms_cat + "&priority=1&to=" + sms_receives + "&text=" + sms_message;

                String[] parts = url.split(":", 2);
                URI uri = new URI(parts[0], parts[1], null);

                URL obj = new URL(uri.toString());
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();
                println("Sending 'GET' request to URL : " + uri.toString());

                if (responseCode == 400) {
                    println("Response Code : " + responseCode);
                }
                
                BufferedReader inb = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = inb.readLine()) != null) {
                    response.append(inputLine);
                }
                inb.close();

                println("Código de envio SMS: " + response.toString());                
            //}
        //}
}

//sendSMS("Mundo");

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

void sendSMS2 (receive,message) {
        String sms_receives = receive //alertMessage.getSms_receives().replaceAll(";", ",");
        String sms_message = message //alertMessage.getSms_message();

        /*
        SMS_SERVER_CAT    1
        SMS_SERVER_PASSWD    5m52017
        SMS_SERVER_URL    http://192.168.1.2:81/sendmsg
        SMS_SERVER_USER    smsmatrix
        */
        String sms_cat = "1" //alertMessage.getSms_cat();
        String sms_passwd = "5m52017" //alertMessage.getSms_passwd();
        String sms_url = "http://192.168.1.2:81/sendmsg" //alertMessage.getSms_url();
        String sms_user = "smsmatrix" //alertMessage.getSms_user();
        
                
        String base = 'http://192.168.1.2:81'
        def http = new groovyx.net.http.HTTPBuilder(base)
        /*http.get(path : '/sendmsg',
                contentType : TEXT,
                query : [user:sms_user,
                         passwd:sms_passwd,
                         cat:sms_cat,
                         priority:1,
                         to:sms_receives,
                         text:sms_message]
                ) { 
                resp, reader ->
                
                    println "response status: ${resp.statusLine}"
                    System.out << reader
                    println '--------------------'
        }*/
        http.request(GET, TEXT ) { req ->
        
        uri.path = '/sendmsg'
        uri.query = [user:sms_user,
                         passwd:sms_passwd,
                         cat:sms_cat,
                         priority:1,
                         to:sms_receives,
                         text:sms_message]

        response.success = { resp, reader ->
            println "Content-Type: ${resp.headers.'Content-Type'}"
            println reader.text
            println "$resp.statusLine   Respond rec"
        }
    }
        
}