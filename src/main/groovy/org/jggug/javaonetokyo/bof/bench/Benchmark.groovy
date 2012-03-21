package org.jggug.javaonetokyo.bof.bench

import gbench.*

final FILE = new File("src/main/resources/rbtree_map_input.csv")
final DEBUG = new File("/tmp/rbtree_map_input.csv")

new BenchmarkBuilder().run(warmUpTime:0, measureCpuTime:false, verbose:true) {

    with "${FILE}を読み込んで赤黒木を構築する", {
        def map = RBTreeMap.newInstance()

        // put(key, value)
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            map.put(key, value)
            //assert map.height() == height as int
            //DEBUG << "${key},${value},${map.height()}\n"
        }

        // get(key)
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            assert map.get(key) == value
        }
    }

}.prettyPrint()
