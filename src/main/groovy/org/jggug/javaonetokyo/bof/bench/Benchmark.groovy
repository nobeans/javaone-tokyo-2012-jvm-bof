package org.jggug.javaonetokyo.bof.bench

import gbench.*

class Benchmark {
    private static final File FILE = new File("src/main/resources/rbtree_map_input.csv")
    private static final File DEBUG = new File("/tmp/rbtree_map_input.csv")

    @Typed
    static void putOnly() {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        FILE.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == height as int
            //DEBUG << "${key},${value},${map.height()}\n"
            //DEBUG << "${map}\n"
        }
    }

    @Typed
    static void putAndGet() {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        FILE.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == height as int
            //DEBUG << "${key},${value},${map.height()}\n"
            //DEBUG << "${map}\n"
        }

        // get(key)
        FILE.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            assert map.get(cols[0]) == cols[1]
        }
    }

    static void main(String[] args) {
        new BenchmarkBuilder().run(warmUpTime:10, measureCpuTime:false, quiet:true) {

            with "${FILE}を読み込んで赤黒木を構築する(put only)", {
                Benchmark.putOnly()
            }

            with "${FILE}を読み込んで赤黒木を構築した後にチェックする(put & get)", {
                Benchmark.putAndGet()
            }

        }.prettyPrint()
    }
}

