package src.main.groovy.org.jggug.javaonetokyo.bof.dsl

import spock.lang.*

class ConfigBuilderSpec extends Specification {

    def builder

    def setup() {
        builder = new ConfigBuilder()
    }

    def "入れ子構造でドット区切りが表現できる"() {
        setup:
        def config = {
            x {
                y {
                    z "value of z"
                }
            }
        }

        when:
        def props = builder.build(config)

        then:
        props."x.y.z" == "value of z"
        props.size() == 1
    }

    def "入れ子構造でドット区切りが表現できる。複数キーの場合"() {
        setup:
        def config = {
            x {
                a "value of a"
                y {
                    b "value of b"
                    z "value of z"
                }
                c "value of c"
            }
        }

        when:
        def props = builder.build(config)

        then:
        props."x.a" == "value of a"
        props."x.y.b" == "value of b"
        props."x.y.z" == "value of z"
        props."x.c" == "value of c"
        props.size() == 4
    }
}
