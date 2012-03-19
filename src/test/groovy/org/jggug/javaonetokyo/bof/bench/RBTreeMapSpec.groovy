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
        map.put('a', 'Value of a')

        then:
        map.height() == 2

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'Value of a'

        map.root.left in EmptyNode
        map.root.left.parent == map.root

        map.root.right in EmptyNode
        map.root.right.parent == map.root
    }

    def "エントリを2つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'Value of a'

        map.root.left in EmptyNode
        map.root.left.parent == map.root

        map.root.right in FillNode
        map.root.right.parent == map.root
        map.root.right.color == Color.RED
        map.root.right.entry.key == 'b'
        map.root.right.entry.value == 'Value of b'

        map.root.right.right in EmptyNode
        map.root.right.right.parent == map.root.right
        map.root.right.left in EmptyNode
        map.root.right.left.parent == map.root.right
    }

    def "エントリを3つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')

        then:
        map.height() == 2

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'Value of a'

        map.root.left in FillNode
        map.root.left.parent == map.root
        map.root.left.color == Color.RED
        map.root.left.entry.key == 'X'
        map.root.left.entry.value == 'Value of X'

        map.root.left.right in EmptyNode
        map.root.left.right.parent == map.root.left
        map.root.left.left in EmptyNode
        map.root.left.left.parent == map.root.left

        map.root.right in FillNode
        map.root.right.parent == map.root
        map.root.right.color == Color.RED
        map.root.right.entry.key == 'b'
        map.root.right.entry.value == 'Value of b'

        map.root.right.right in EmptyNode
        map.root.right.right.parent == map.root.right
        map.root.right.left in EmptyNode
        map.root.right.left.parent == map.root.right
    }

    def "エントリを4つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')
        map.put('c', 'Value of c')

        then:
        map.height() == 3

        map.root in FillNode
        map.root.parent == null
        map.root.color == Color.BLACK
        map.root.entry.key == 'a'
        map.root.entry.value == 'Value of a'

        map.root.left in FillNode
        map.root.left.parent == map.root
        map.root.left.color == Color.BLACK
        map.root.left.entry.key == 'X'
        map.root.left.entry.value == 'Value of X'

        map.root.left.right in EmptyNode
        map.root.left.right.parent == map.root.left
        map.root.left.left in EmptyNode
        map.root.left.left.parent == map.root.left

        map.root.right in FillNode
        map.root.right.parent == map.root
        map.root.right.color == Color.BLACK
        map.root.right.entry.key == 'b'
        map.root.right.entry.value == 'Value of b'

        map.root.right.right in FillNode
        map.root.right.right.parent == map.root.right
        map.root.right.right.color == Color.RED
        map.root.right.right.entry.key == 'c'
        map.root.right.right.entry.value == 'Value of c'

        map.root.right.right.right in EmptyNode
        map.root.right.right.right.parent == map.root.right.right
        map.root.right.right.left in EmptyNode
        map.root.right.right.left.parent == map.root.right.right
    }
}
