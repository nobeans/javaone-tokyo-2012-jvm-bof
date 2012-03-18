package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class RedBlackTreeSpec extends Specification {

    def map

    def setup() {
        map = new RedBlackTreeMap()
    }

    def "topic"() {
        when:
        map.put('a', 'A')
        then:
        map.height() == 1
    }
}
