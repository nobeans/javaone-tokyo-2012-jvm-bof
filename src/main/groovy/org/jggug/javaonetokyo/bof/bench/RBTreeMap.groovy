@Typed
package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    private Node root = Node.EMPTY

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
        Node.balanceAsRoot(root)
    }

    String get(String key) {
        if (key == null) throw new IAE("key is null")
        root.get(key)
    }

    int height() {
        root.height()
    }

    @Override
    String toString() {
        root.toString()
    }

    static newInstance() {
        new RBTreeMap()
    }
}

abstract class Node {
    private final static int BLACK = 1
    private final static int RED = 0
    final static Node EMPTY = new EmptyNode(color:BLACK)

    int color
    String key
    String value
    Node left = EMPTY
    Node right = EMPTY

    abstract String get(String key)
    abstract int height()

    Node put(String key, String value) {
        //println ">"*10 + "put($key, $value)"
        //println this
        if (this == EMPTY) {
            return new FillNode(color:RED, key:key, value:value)
        }
        switch (this.key <=> key) {
            case  0:
                this.value = value
                return this
            case  1:
                left = left.put(key, value)
                return balanceLeft(this)
            case -1:
                right = right.put(key, value)
                return balanceRight(this)
        }
        assert false
    }

    boolean isBlack() {
        color == BLACK
    }
    boolean isRed() {
        color == RED
    }

    @Override
    String toString() {
        toTreeString(0)
    }

    String toTreeString(int level) {
        def indent = " "
        def buff = []
        if (right instanceof FillNode) {
            buff << right.toTreeString(level + 1)
        }
        buff << indent * level + (isBlack() ? 'B' : 'R') + "($key)"
        if (left instanceof FillNode) {
            buff << left.toTreeString(level + 1)
        }
        buff.join(System.getProperty("line.separator"))
    }

    private static Node rotateRight(Node node) {
        def left = node.left
        node.setLeft(left.right)
        left.setRight(node)
        left.setColor(node.color)
        node.setColor(RED)
        //println ">"*10 + "rotateRight"
        //println node
        return left
    }

    private static Node rotateLeft(Node node) {
        def right = node.right
        node.setRight(right.left)
        right.setLeft(node)
        right.setColor(node.color)
        node.setColor(RED)
        //println ">"*10 + "rotateLeft"
        //println node
        return right
    }

    private static Node balanceLeft(Node node) {
        //println ">"*10 + "balanceLeft:BEFORE"
        //println node
        Node right = node.right
        Node left = node.left
        if (node.isBlack() && left.isRed()) {
            if (node.right.isBlack()) {
                if (left.right.isRed()) {
                    node.setLeft(rotateLeft(left))
                }
                // both are black
                else if (left.left.isBlack()) {
                    //assert (node.left.right.isBlack() && node.left.left.isBlack())
                    return node
                }
                node = rotateRight(node)
            }
            if (node.isBlack() && node.right.isRed() && node.left.isRed()) {
                node.setColor(RED)
                node.left.setColor(BLACK)
                node.right.setColor(BLACK)
            }
        }
        //println ">"*10 + "balanceLeft:AFTER"
        //println node
        return node
    }

    private static Node balanceRight(Node node) {
        //println ">"*10 + "balanceRight:BEFORE"
        //println node
        Node right = node.right
        Node left = node.left
        if (node.isBlack() && right.isRed()) {
            if (left.isBlack()) {
                if (right.left.isRed()) {
                    node.setRight(rotateRight(right))
                }
                // both are black
                else if (right.right.isBlack()) {
                    //assert (node.right.right.isBlack() && node.right.left.isBlack())
                    return node
                }
                node = rotateLeft(node)
            }
            if (node.isBlack() && node.right.isRed() && node.left.isRed()) {
                node.setColor(RED)
                node.left.setColor(BLACK)
                node.right.setColor(BLACK)
            }
        }
        //println ">"*10 + "balanceRight:AFTER"
        //println node
        return node
    }

    static void balanceAsRoot(Node node) {
        node.setColor(BLACK)
        Node right = node.right
        Node left = node.left
        if (right.isRed() && left.isRed()) {
            left.setColor(BLACK)
            right.setColor(BLACK)
        }
    }
}

class FillNode extends Node {
    @Override
    String get(String key) {
        switch (this.key <=> key) {
            case  0: return value
            case  1: return left.get(key)
            case -1: return right.get(key)
        }
        assert false
    }

    @Override
    int height() {
        left.height() + color // because of BLACK = 1 and RED = 0
    }
}

class EmptyNode extends Node {
    @Override
    String get(String key) {
        null
    }

    @Override
    int height() {
        0 // empty node shouldn't be count
    }
}
