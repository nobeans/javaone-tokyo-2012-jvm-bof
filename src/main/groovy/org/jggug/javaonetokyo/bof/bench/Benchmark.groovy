package org.jggug.javaonetokyo.bof.bench

import gbench.*
import groovy.transform.CompileStatic

class Benchmark {
    @CompileStatic
    static void putOnly(File file) {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        file.eachLine { String line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == cols[2] as int
        }
    }

    @CompileStatic
    static void putAndGet(File file) {
        RBTreeMap map = RBTreeMap.newInstance()

        // put(key, value)
        file.eachLine { String line ->
            String[] cols = line.split(",") // key,value,height
            map.put(cols[0], cols[1])
            //assert map.height() == cols[2] as int
        }

        // get(key)
        file.eachLine { String line ->
            String[] cols = line.split(",") // key,value,height
            assert map.get(cols[0]) == cols[1]
        }
    }

    static void main(String[] args) {
        // arguments
        def (inputFilePath, warmUpTime, verbose, trials) = args
        File file = new File(inputFilePath)
        if (!file.exists()) {
            System.err.println "ERROR: file not found: ${file}"
            System.exit(1)
        }
        warmUpTime = warmUpTime?.toInteger()
        verbose = verbose?.toBoolean()
        trials = trials?.toInteger()

        // benchmarking
        new BenchmarkBuilder().run(warmUpTime:warmUpTime, verbose:verbose, quiet:!verbose, measureCpuTime:false) {
            trials.toInteger().times {
                with "${file}を読み込んで赤黒木を構築した後にチェックする(put & get)", {
                    Benchmark.putAndGet(file)
                }
            }
        }.prettyPrint()
    }
}

