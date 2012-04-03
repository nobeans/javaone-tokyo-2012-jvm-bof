package org.jggug.javaonetokyo.bof.dsl

import spock.lang.*

class ConfigSlurperSpec extends Specification {

    def slurper

    def setup() {
        slurper = new ConfigSlurper()
    }

    def "入れ子構造でドット区切りが表現できる"() {
        setup:
        def dsl = """\
            |x {
            |    a = "value of a"
            |    y {
            |        b = "value of b"
            |        z = "value of z"
            |    }
            |    c = "value of c"
            |}
            |""".stripMargin()

        when:
        def config = slurper.parse(dsl)

        then:
        config.x.a == "value of a"
        config.x.y.b == "value of b"
        config.x.y.z == "value of z"
        config.x.c == "value of c"
    }

    def "Propertiesへの変換も可能"() {
        setup:
        def dsl = """\
            |x {
            |    a = "value of a"
            |    y {
            |        b = "value of b"
            |        z = "value of z"
            |    }
            |    c = "value of c"
            |}
            |""".stripMargin()

        when:
        def props = slurper.parse(dsl).toProperties()

        then:
        props."x.a" == "value of a"
        props."x.y.b" == "value of b"
        props."x.y.z" == "value of z"
        props."x.c" == "value of c"
        props.size() == 4
    }
}
