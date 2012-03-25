package org.jggug.javaonetokyo.bof.bench

import gbench.*

final FILE = new File("src/main/resources/rbtree_map_input.csv")
final DEBUG = new File("/tmp/rbtree_map_input.csv")

new BenchmarkBuilder().run(warmUpTime:10, measureCpuTime:false, quiet:true) {

    with "${FILE}を読み込んで赤黒木を構築する(put only)", {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            map.put(key, value)
            //assert map.height() == height as int
            //DEBUG << "${key},${value},${map.height()}\n"
            //DEBUG << "${map}\n"
        }
    }

    with "${FILE}を読み込んで赤黒木を構築した後にチェックする(put & get)", {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            map.put(key, value)
            //assert map.height() == height as int
            //DEBUG << "${key},${value},${map.height()}\n"
            //DEBUG << "${map}\n"
        }

        // get(key)
        FILE.eachLine { line ->
            def (key, value, height) = line.split(",", 3)
            assert map.get(key) == value
        }
    }

}.prettyPrint()
