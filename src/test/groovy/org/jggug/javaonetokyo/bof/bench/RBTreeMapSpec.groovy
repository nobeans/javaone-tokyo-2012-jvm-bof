package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class RdBTreeMapSpec extends Specification {

    def map

    def setup() {
        println "-"*50
        map = RBTreeMap.newInstance()
    }

    def "エントリを追加しない場合は高さ1"() {
        expect:
        map.height() == 1
        map.toString() == "B(null)"
    }

    def "nullをputすると例外をスローする"() {
        when:
        map.put(null, "value")

        then:
        thrown(IllegalArgumentException)
    }

    def "エントリを1つだけ追加する"() {
        when:
        map.put('a', 'Value of a')

        then:
        map.height() == 2
        map.toString() == "B(a)"
    }

    def "エントリを2つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == ''' R(b)
                            |B(a)'''.stripMargin()
    }

    def "エントリを3つ追加する。平衡になるように追加する。"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')

        then:
        map.height() == 2
        map.toString() == ''' R(b)
                            |B(a)
                            | R(X)'''.stripMargin()
    }

    def "エントリを4つ追加する。親と親の兄弟が両方とも赤の場合は、親の親を赤に、親と親の兄弟を黒に変更する。ただし親の親が根の場合は黒のままとする。"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')
        map.put('c', 'Value of c')

        then:
        map.height() == 3
        map.toString() == '''  R(c)
                            | B(b)
                            |B(a)
                            | B(X)'''.stripMargin()
    }

    def "エントリを5つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('c', 'Value of c')
        map.put('X', 'Value of X')
        map.put('b', 'Value of b')
        map.put('d', 'Value of d')

        then:
        map.height() == 3
        map.toString() == '''  B(X)
                            | R(a)
                            |B(b)
                            | R(c)
                            |  R(d)'''.stripMargin()
    }

//    def "エントリを6つ追加する。親と親の兄弟が赤の場合は、親の親を赤に、親と親の兄弟を黒に変更する"() {
//        when:
//        map.put('a', 'Value of a')
//        map.put('c', 'Value of c')
//        map.put('X', 'Value of X')
//        map.put('b', 'Value of b')
//        map.put('d', 'Value of d')
//        map.put('e', 'Value of e')
//
//        then:
//        map.height() == 4
//        map.toString() == '''  B(X)
//                            | R(a)
//                            |B(b)
//                            | R(c)
//                            |  R(d)'''.stripMargin()
//    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。右、右"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')

        then:
        map.height() == 2
        map.toString() == ''' R(c)
                            |B(b)
                            | R(a)'''.stripMargin()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。左、左"() {
        when:
        map.put('c', 'Value of c')
        map.put('b', 'Value of b')
        map.put('a', 'Value of a')

        then:
        map.height() == 2
        map.toString() == ''' R(c)
                            |B(b)
                            | R(a)'''.stripMargin()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。右、左"() {
        when:
        map.put('a', 'Value of a')
        map.put('c', 'Value of c')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == ''' R(c)
                            |B(b)
                            | R(a)'''.stripMargin()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。左、右"() {
        when:
        map.put('c', 'Value of c')
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == ''' R(c)
                            |B(b)
                            | R(a)'''.stripMargin()
    }

    def "右側にエントリを4つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('d', 'Value of d')
        map.put('c', 'Value of c')

        then:
        map.height() == 3
        map.toString() == ''' B(d)
                            |  R(c)
                            |B(b)
                            | B(a)'''.stripMargin()
    }

//    def "右側にエントリを5つ追加する"() {
//        when:
//        map.put('a', 'Value of a')
//        map.put('b', 'Value of b')
//        map.put('c', 'Value of c')
//        map.put('d', 'Value of d')
//        map.put('e', 'Value of e')
//
//        then:
//        map.height() == 3
//        map.toString() == '''  B(e)
//                            | B(d)
//                            |  B(c)
//                            |B(b)
//                            | R(a)'''.stripMargin()
//    }
//
//    def "右側にエントリを6つ追加する"() {
//        when:
//        map.put('a', 'Value of a')
//        map.put('b', 'Value of b')
//        map.put('c', 'Value of c')
//        map.put('d', 'Value of d')
//        map.put('e', 'Value of e')
//        map.put('f', 'Value of f')
//
//        then:
//        map.height() == 4
//        map.toString() == '''
//            '''.readLines().collect{ it.trim() }.join()
//    }
//
//    def "右側にエントリを7つ追加する"() {
//        when:
//        map.put('a', 'Value of a')
//        map.put('b', 'Value of b')
//        map.put('c', 'Value of c')
//        map.put('d', 'Value of d')
//        map.put('e', 'Value of e')
//        map.put('f', 'Value of f')
//        map.put('g', 'Value of g')
//
//        then:
//        map.height() == 4
//        map.toString() == '''
//            '''.readLines().collect{ it.trim() }.join()
//    }
//
//    def "右側にエントリを8つ追加する"() {
//        when:
//        map.put('a', 'Value of a')
//        map.put('b', 'Value of b')
//        map.put('c', 'Value of c')
//        map.put('d', 'Value of d')
//        map.put('e', 'Value of e')
//        map.put('f', 'Value of f')
//        map.put('g', 'Value of g')
//        map.put('h', 'Value of h')
//
//        then:
//        map.height() == 5
//        map.toString() == '''
//            '''.readLines().collect{ it.trim() }.join()
//    }

   def "get: 指定したキーの値を取得する"() {
       setup:
       map.put('a', 'Value of a')
       map.put('b', 'Value of b')
       map.put('c', 'Value of c')
       map.put('d', 'Value of d')
       map.put('e', 'Value of e')

       expect:
       map.get(key) == value

       where:
       key | value
       'a' | 'Value of a'
       'b' | 'Value of b'
       'c' | 'Value of c'
       'd' | 'Value of d'
       'e' | 'Value of e'
       'f' | null
   }

    def "nullをgetすると例外をスローする"() {
        when:
        map.get(null)

        then:
        thrown(IllegalArgumentException)
    }
}
