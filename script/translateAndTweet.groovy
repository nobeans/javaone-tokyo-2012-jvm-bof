// Twitter4JのJarを自動ダウンロード＆クラスパス設定する。
@Grab('org.twitter4j:twitter4j-core:[2.1,)')
import twitter4j.*

import java.net.URLEncoder
import groovy.util.XmlParser

// Bing TranslatorのAPIコードを取得する。
def getApiId = {
    new File(System.properties["user.home"], ".bing-api-id").text.trim()
}

// URLエンコードする動的メソッドをStringに追加する。
String.metaClass.encodeAsURL = {
    URLEncoder.encode(delegate)
}

// Bing Translator APIで翻訳する動的メソッドをStringに追加する。
String.metaClass.translate = { from, to ->
    def text =  delegate.encodeAsURL()

    def apiId = getApiId()
    def translateUrl = "https://api.microsofttranslator.com/V2/Http.svc/Translate?appId=${apiId}&text=${text}&from=${from}&to=${to}"

    def response = translateUrl.toURL().text
    return new XmlParser().parseText(response).text()

    // もっとシンプルにも書ける。
    //return new XmlParser().parse(translateUrl).text()
}

// Twitterにツイートする。
String.metaClass.tweet = {
    new TwitterFactory().instance.updateStatus(delegate)
}

// ------------------------------------------------
// エレガントに実行する。

System.in.text.translate("ja", "en").tweet()

