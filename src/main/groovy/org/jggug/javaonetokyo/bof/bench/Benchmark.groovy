package org.jggug.javaonetokyo.bof.bench

import gbench.*

class Benchmark {
    @Typed
    static void putOnly(File file) {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        file.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == cols[2] as int
        }
    }

    @Typed
    static void putAndGet(File file) {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        file.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == cols[2] as int
        }

        // get(key)
        file.eachLine { line ->
            String[] cols = line.split(",") // key,value,height
            assert map.get(cols[0]) == cols[1]
        }
    }

    static void main(String[] args) {
        // input file
        File file = new File(args[0])
        if (!file.exists()) {
            System.err.println "ERROR: file not found: ${file}"
            System.exit(1)
        }

        // benchmarking
        // MEMO: change warmUpTime and trials
        int trials = 5
        trials.times {
            new BenchmarkBuilder().run(warmUpTime:10 /*sec*/, measureCpuTime:false, quiet:true) {
                with "${file}を読み込んで赤黒木を構築する(put only)", {
                    Benchmark.putOnly(file)
                }

                with "${file}を読み込んで赤黒木を構築した後にチェックする(put & get)", {
                    Benchmark.putAndGet(file)
                }
            }.prettyPrint()
        }
    }
}

