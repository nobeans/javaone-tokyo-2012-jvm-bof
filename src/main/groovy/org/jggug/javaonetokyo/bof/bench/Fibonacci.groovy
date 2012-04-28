package org.jggug.javaonetokyo.bof.bench

class Fibonacci {
    def calc = { n ->
      return (n == 0) ?  0 :
             (n == 1) ?  1 :
             call(n - 1) + call(n - 2)
    }

    static void main(String[] args) {
        def num = args[0] as int
        println "Fib($num) = ${new Fibonacci().calc(num)}"
    }
}