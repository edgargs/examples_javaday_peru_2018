@Grapes([
    @Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7')
])

import static groovyx.net.http.Method.*
import static groovyx.net.http.ContentType.*

String base = 'http://localhost:5050/'

  def http = new groovyx.net.http.HTTPBuilder(base)

  
  http.request(GET, TEXT ) { req ->

    uri.path = '/api/v1'
    /*uri.query = [user:sms_user,
                    passwd:sms_passwd,
                    cat:sms_cat,
                    priority:1,
                    to:sms_receives,
                    text:sms_message]
    */
    response.success = { resp, reader ->
        println "Content-Type: ${resp.headers.'Content-Type'}"
        println reader.text
        println "$resp.statusLine   Respond rec"
    }
  }
  
  http.post(path: '/api/v1/1') {
  
      resp, reader ->
 
      println reader.text
      println "POST Success: ${resp.statusLine}"
      
  }
  
    http.request(POST) { 

        uri.path = '/api/v2/2'

        response.success = { resp, reader ->
            println "Content-Type: ${resp.headers.'Content-Type'}"
            println reader.text
            println "$resp.statusLine   Respond rec"
        }
    }