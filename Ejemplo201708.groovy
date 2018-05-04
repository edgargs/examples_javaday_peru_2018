@Grapes([
  @Grab('io.ratpack:ratpack-groovy:1.5.4'),
  @Grab('org.slf4j:slf4j-simple:1.7.25')
])
import static ratpack.groovy.Groovy.ratpack

ratpack {
    handlers {
        get {
            render 'Hello World!'
        }
        post(':name') {
              render "Hello $pathTokens.name!"
        }

        prefix("api") {
            prefix('v1') {
                get {
                    render 'GET-V1'
                }                
                post(':id') {
                      render "POST-V1"
                }
            }
            
            prefix('v2') {
                get {
                    byContent {
                        html {
                            render 'HTML'
                        }  
                        json {
                            render 'JSON'
                        }                        
                    }
                }                
                post(':id') {
                    response.headers.add 'api-version','v2'
                    render "POST: $pathTokens.id"
                }
            }
        }
    }
}
