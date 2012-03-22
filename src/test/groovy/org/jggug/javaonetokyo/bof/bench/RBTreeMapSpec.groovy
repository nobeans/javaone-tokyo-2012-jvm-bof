package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class RdBTreeMapSpec extends Specification {

    def map

    def setup() {
        map = RBTreeMap.newInstance()
    }

    def "エントリを追加しない場合は高さ1"() {
        expect:
        map.height() == 1
        map.toString() == "BLACK(empty)"
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
        map.toString() == "BLACK(a=Value of a){L:BLACK(empty)}{R:BLACK(empty)}"
    }

    def "エントリを2つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(a=Value of a)
                {L:BLACK(empty)}
                {R:RED(b=Value of b)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する。平衡になるように追加する。"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(a=Value of a)
                {L:RED(X=Value of X)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:RED(b=Value of b)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを4つ追加する。親と親の兄弟が両方とも赤の場合は、親の親を赤に、親と親の兄弟を黒に変更する。ただし親の親が根の場合は黒のままとする。"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')
        map.put('c', 'Value of c')

        then:
        map.height() == 3
        map.toString() == '''
            BLACK(a=Value of a)
                {L:BLACK(X=Value of X)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:BLACK(b=Value of b)
                    {L:BLACK(empty)}
                    {R:RED(c=Value of c)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}}
            '''.readLines().collect{ it.trim() }.join()
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
        map.toString() == '''
            BLACK(b=Value of b)
                {L:RED(a=Value of a)
                    {L:BLACK(X=Value of X)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:RED(c=Value of c)
                    {L:BLACK(empty)}
                    {R:RED(d=Value of d)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを6つ追加する。親と親の兄弟が赤の場合は、親の親を赤に、親と親の兄弟を黒に変更する"() {
        when:
        map.put('a', 'Value of a')
        map.put('c', 'Value of c')
        map.put('X', 'Value of X')
        map.put('b', 'Value of b')
        map.put('d', 'Value of d')
        map.put('e', 'Value of e')

        then:
        map.height() == 4
        map.toString() == '''
            BLACK(b=Value of b)
                {L:BLACK(a=Value of a)
                    {L:BLACK(X=Value of X)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:BLACK(c=Value of c)
                    {L:BLACK(empty)}
                    {R:RED(d=Value of d)
                        {L:BLACK(empty)}
                        {R:RED(e=Value of e)
                            {L:BLACK(empty)}
                            {R:BLACK(empty)}}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。右、右"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(b=Value of b)
                {L:RED(a=Value of a)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:RED(c=Value of c)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。左、左"() {
        when:
        map.put('c', 'Value of c')
        map.put('b', 'Value of b')
        map.put('a', 'Value of a')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(b=Value of b)
                {L:RED(a=Value of a)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:RED(c=Value of c)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。右、左"() {
        when:
        map.put('a', 'Value of a')
        map.put('c', 'Value of c')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(b=Value of b)
                {L:RED(a=Value of a)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:RED(c=Value of c)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する。直列に偏るように追加されると平衡化を行う。左、右"() {
        when:
        map.put('c', 'Value of c')
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == '''
            BLACK(b=Value of b)
                {L:RED(a=Value of a)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:RED(c=Value of c)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "右側にエントリを4つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')
        map.put('d', 'Value of d')

        then:
        map.height() == 3
        map.toString() == '''
            BLACK(b=Value of b)
                {L:BLACK(a=Value of a)
                    {L:BLACK(empty)}
                    {R:BLACK(empty)}}
                {R:BLACK(c=Value of c)
                    {L:BLACK(empty)}
                    {R:RED(d=Value of d)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "右側にエントリを5つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')
        map.put('d', 'Value of d')
        map.put('e', 'Value of e')

        then:
        map.height() == 3
        map.toString() == '''
            BLACK(c=Value of c)
                {L:RED(b=Value of b)
                    {L:BLACK(a=Value of a)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:RED(d=Value of d)
                    {L:BLACK(empty)}
                    {R:RED(e=Value of e)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "右側にエントリを6つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')
        map.put('d', 'Value of d')
        map.put('e', 'Value of e')
        map.put('f', 'Value of f')

        then:
        map.height() == 4
        map.toString() == '''
            BLACK(c=Value of c)
                {L:BLACK(b=Value of b)
                    {L:BLACK(a=Value of a)
                        {L:BLACK(empty)}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:BLACK(d=Value of d)
                    {L:BLACK(empty)}
                    {R:RED(e=Value of e)
                        {L:BLACK(empty)}
                        {R:RED(f=Value of f)
                            {L:BLACK(empty)}
                            {R:BLACK(empty)}}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "右側にエントリを7つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')
        map.put('d', 'Value of d')
        map.put('e', 'Value of e')
        map.put('f', 'Value of f')
        map.put('g', 'Value of g')

        then:
        map.height() == 4
        map.toString() == '''
            BLACK(d=Value of d)
                {L:RED(c=Value of c)
                    {L:BLACK(b=Value of b)
                        {L:BLACK(a=Value of a)
                            {L:BLACK(empty)}
                            {R:BLACK(empty)}}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:RED(e=Value of e)
                    {L:BLACK(empty)}
                    {R:RED(f=Value of f)
                        {L:BLACK(empty)}
                        {R:RED(g=Value of g)
                            {L:BLACK(empty)}
                            {R:BLACK(empty)}}}}
            '''.readLines().collect{ it.trim() }.join()
    }

    def "右側にエントリを8つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('c', 'Value of c')
        map.put('d', 'Value of d')
        map.put('e', 'Value of e')
        map.put('f', 'Value of f')
        map.put('g', 'Value of g')
        map.put('h', 'Value of h')

        then:
        map.height() == 5
        map.toString() == '''
            BLACK(d=Value of d)
                {L:BLACK(c=Value of c)
                    {L:BLACK(b=Value of b)
                        {L:BLACK(a=Value of a)
                            {L:BLACK(empty)}
                            {R:BLACK(empty)}}
                        {R:BLACK(empty)}}
                    {R:BLACK(empty)}}
                {R:BLACK(e=Value of e)
                    {L:BLACK(empty)}
                    {R:RED(f=Value of f)
                        {L:BLACK(empty)}
                        {R:RED(g=Value of g)
                            {L:BLACK(empty)}
                            {R:RED(h=Value of h)
                                {L:BLACK(empty)}
                                {R:BLACK(empty)}}}}}
            '''.readLines().collect{ it.trim() }.join()
    }

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
