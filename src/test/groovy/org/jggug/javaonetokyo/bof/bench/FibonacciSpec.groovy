package org.jggug.javaonetokyo.bof.bench

import spock.lang.*

class FibonacciSpec extends Specification {

    def "calculate"() {
        setup:
        def fib = new Fibonacci()

        expect:
        fib.calc(n) == result

        where:
        n | result
        0 |  0
        1 |  1
        2 |  1
        3 |  2
        4 |  3
        5 |  5
    }

}
