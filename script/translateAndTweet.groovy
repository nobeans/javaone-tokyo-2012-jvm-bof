@Grab('org.twitter4j:twitter4j-core:[2.1,)')
import twitter4j.*

import java.net.URLEncoder
import groovy.util.XmlParser

def getApiId = {
    new File(System.properties["user.home"], ".bing-api-id").text.trim()
}

String.metaClass.encodeAsURL = {
    URLEncoder.encode(delegate)
}

String.metaClass.translate = { ->
    def text =  delegate.encodeAsURL()
    //println "Text: ${text}"

    def apiId = getApiId()
    def sourceLang = "ja"
    def targetLang = "en"
    def translateUrl = "https://api.microsofttranslator.com/V2/Http.svc/Translate?appId=${apiId}&text=${text}&from=${sourceLang}&to=${targetLang}"

    def response = translateUrl.toURL().text
    return new XmlParser().parseText(response).text()

    //return new XmlParser().parse(translateUrl).text()
}

String.metaClass.tweet = { ->
    //println delegate
    new TwitterFactory().instance.updateStatus(delegate)
}

System.in.text.translate().tweet()

