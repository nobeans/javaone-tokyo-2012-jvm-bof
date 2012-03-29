=====
Usage
=====

How to benchmark
----------------

To use src/main/resources/rbtree_map_input.csv (default):

    $ ./gradlew benchmark

To use specified file:

    $ ./gradlew benchmark -Dinput=<INPUT_FILE_PATH>

To use full arguments:

    $ ./gradlew benchmark [-Dinput=<INPUT_FILE_PATH>] [-DwarmUpTime=<SECOND>] [-Dverbose=true] [-Dtrials=<COUNT>] [-DwithPutOnly=true]


How to translate and tweet
--------------------------

    $ cd script
    $ echo こんにちは、Groovy! | groovy translateAndTweet.groovy

