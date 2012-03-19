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
        map.toString() == "[ROOT]->BLACK(a=Value of a){L:[a]->BLACK(empty)}{R:[a]->BLACK(empty)}"
    }

    def "エントリを2つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')

        then:
        map.height() == 2
        map.toString() == """
            [ROOT]->BLACK(a=Value of a)
                {L:[a]->BLACK(empty)}
                {R:[a]->RED(b=Value of b)
                    {L:[b]->BLACK(empty)}
                    {R:[b]->BLACK(empty)}}
            """.readLines().collect{ it.trim() }.join()
    }

    def "エントリを3つ追加する"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')

        then:
        map.height() == 2
        map.toString() == """
            [ROOT]->BLACK(a=Value of a)
                {L:[a]->RED(X=Value of X)
                    {L:[X]->BLACK(empty)}
                    {R:[X]->BLACK(empty)}}
                {R:[a]->RED(b=Value of b)
                    {L:[b]->BLACK(empty)}
                    {R:[b]->BLACK(empty)}}
            """.readLines().collect{ it.trim() }.join()
    }

    def "エントリを4つ追加する。親と親の兄弟が赤の場合は、親の親を赤に、親と親の兄弟を黒に変更する。ただし親の親が根の場合は黒のままとする。"() {
        when:
        map.put('a', 'Value of a')
        map.put('b', 'Value of b')
        map.put('X', 'Value of X')
        map.put('c', 'Value of c')

        then:
        map.height() == 3
        map.toString() == """
            [ROOT]->BLACK(a=Value of a)
                {L:[a]->BLACK(X=Value of X)
                    {L:[X]->BLACK(empty)}
                    {R:[X]->BLACK(empty)}}
                {R:[a]->BLACK(b=Value of b)
                    {L:[b]->BLACK(empty)}
                    {R:[b]->RED(c=Value of c)
                        {L:[c]->BLACK(empty)}
                        {R:[c]->BLACK(empty)}}}
            """.readLines().collect{ it.trim() }.join()
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
        map.toString() == """
            [ROOT]->BLACK(a=Value of a)
                {L:[a]->BLACK(X=Value of X)
                    {L:[X]->BLACK(empty)}
                    {R:[X]->BLACK(empty)}}
                {R:[a]->BLACK(c=Value of c)
                    {L:[c]->RED(b=Value of b)
                        {L:[b]->BLACK(empty)}
                        {R:[b]->BLACK(empty)}}
                    {R:[c]->RED(d=Value of d)
                        {L:[d]->BLACK(empty)}
                        {R:[d]->BLACK(empty)}}}
            """.readLines().collect{ it.trim() }.join()
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
        map.height() == 3
        map.toString() == """
            [ROOT]->BLACK(a=Value of a)
                {L:[a]->BLACK(X=Value of X)
                    {L:[X]->BLACK(empty)}
                    {R:[X]->BLACK(empty)}}
                {R:[a]->RED(c=Value of c)
                    {L:[c]->BLACK(b=Value of b)
                        {L:[b]->BLACK(empty)}
                        {R:[b]->BLACK(empty)}}
                    {R:[c]->BLACK(d=Value of d)
                        {L:[d]->BLACK(empty)}
                        {R:[d]->RED(e=Value of e)
                            {L:[e]->BLACK(empty)}
                            {R:[e]->BLACK(empty)}}}}
            """.readLines().collect{ it.trim() }.join()
    }
}
