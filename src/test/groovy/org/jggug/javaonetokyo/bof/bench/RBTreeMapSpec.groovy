package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class RdBTreeMapSpec extends Specification {

    def map

    def setup() {
        map = new RBTreeMap()
    }

    def "エントリを追加しない場合は高さ0"() {
        expect:
        map.height() == 0
    }

    def "エントリを1つだけ追加する"() {
        when:
        map.put('a', 'A')

        then:
        map.height() == 1
    }

//    def "エントリを2つ追加する"() {
//        when:
//        map.put('a', 'A')
//        map.put('b', 'B')
//
//        then:
//        map.height() == 1
//    }
}
