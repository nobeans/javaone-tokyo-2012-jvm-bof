package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class RdBTreeMapSpec extends Specification {

    def map

    def setup() {
        map = new RBTreeMap()
    }

    def "エントリを追加しない場合は高さ1"() {
        expect:
        map.height() == 1

        map.root in EmptyNode
        map.root.parent == null
    }

    def "エントリを1つだけ追加する"() {
        when:
        map.put('a', 'A')

        then:
        map.height() == 2

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'A'

        map.root.left in EmptyNode
        map.root.left.parent == map.root

        map.root.right in EmptyNode
        map.root.right.parent == map.root
    }

    def "エントリを2つ追加する"() {
        when:
        map.put('a', 'A')
        map.put('b', 'B')

        then:
        map.height() == 2

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'A'

        map.root.left in EmptyNode
        map.root.left.parent == map.root

        map.root.right in FillNode
        map.root.right.parent == map.root
        map.root.right.color == Color.RED
        map.root.right.entry.key == 'b'
        map.root.right.entry.value == 'B'
    }
}
