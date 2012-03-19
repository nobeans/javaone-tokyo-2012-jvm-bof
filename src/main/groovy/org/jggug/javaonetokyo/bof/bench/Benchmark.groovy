package org.jggug.javaonetokyo.bof.bench

import gbench.*

final FILE = new File("src/main/resources/rbtree_map_input.csv")

new BenchmarkBuilder().run(idle:0, times:1, average:true, trim:true) {
    with "${FILE}を読み込んで赤黒木を構築する", {
        def map = RBTreeMap.newInstance()
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            map.put(key, value)
            assert map.height() == height
        }
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            assert map.get(key) == value
        }
    }
}.prettyPrint()
