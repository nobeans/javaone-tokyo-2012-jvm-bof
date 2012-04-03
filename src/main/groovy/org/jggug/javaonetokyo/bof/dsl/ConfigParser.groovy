package org.jggug.javaonetokyo.bof.dsl

/**
 * methodMissingとpropertyMissingを使った自前実装
 */
class ConfigParser {

    private props
    private stack

    Properties parse(Closure dsl) {
        this.props = new Properties()
        this.stack = []

        // delegateを差し替えてmethodMissing/propertyMissingが
        // ConfigParser自身のメソッドを呼び出すように細工する。
        dsl.delegate = this

        // クロージャを実行する。
        dsl.call()

        return props
    }

    def methodMissing(String name, args) {
        def oldStack = stack
        this.stack = stack + name
        args.each { arg ->
            if (arg in Closure) {
                arg.call()
            }
        }
        this.stack = oldStack
    }

    void propertyMissing(String name, value) {
        def key = (stack + name).join(".")
        props[key] = value
    }
}
