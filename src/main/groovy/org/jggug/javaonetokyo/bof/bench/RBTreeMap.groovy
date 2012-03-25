package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    def root = Node.EMPTY

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
        root = Node.balanceAsRoot(root)
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
    final static int BLACK = 1
    final static int RED = 0
    final static Node EMPTY = new EmptyNode()

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
            return new FillNode(RED, key, value)
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

    @Override
    String toString() {
        toTreeString(0)
    }

    String toTreeString(int level) {
        def indent = " "
        def buff = []
        if (right) {
            buff << right.toTreeString(level + 1)
        }
        buff << indent * level + (color == 1 ? 'B' : 'R') + "($key)"
        if (left) {
            buff << left.toTreeString(level + 1)
        }
        buff.join(System.getProperty("line.separator"))
    }

    boolean hasRedChild() {
        left.color == RED || right.color == RED
    }

    private static Node rotateRight(Node node) {
        def left = node.left
        node.left = left.right
        left.right = node
        left.color = node.color
        node.color = RED
        //println ">"*10 + "rotateRight"
        //println node
        return left
    }

    private static Node rotateLeft(Node node) {
        def right = node.right
        node.right = right.left
        right.left = node
        right.color = node.color
        node.color = RED
        //println ">"*10 + "rotateLeft"
        //println node
        return right
    }

    private static Node balanceLeft(Node node) {
        //println ">"*10 + "balanceLeft:BEFORE"
        //println node
        if (node.color == BLACK && node.left.color == RED && node.right.color == BLACK) {
            if (node.left.right.color == RED) {
                node.left = rotateLeft(node.left)
            }
            // both are black
            else if (node.left.left.color == BLACK) {
                //assert (node.left.right.color == BLACK && node.left.left.color == BLACK)
                return node
            }
            node = rotateRight(node)
        }
        if (node.color == BLACK && node.right.color == RED && node.left.color == RED) {
            node.color = RED
            node.left.color = BLACK
            node.right.color = BLACK
        }
        //println ">"*10 + "balanceLeft:AFTER"
        //println node
        return node
    }

    private static Node balanceRight(Node node) {
        //println ">"*10 + "balanceRight:BEFORE"
        //println node
        if (node.color == BLACK && node.right.color == RED && node.left.color == BLACK) {
            if (node.right.left.color == RED) {
                node.right = rotateRight(node.right)
            }
            // both are black
            else if (node.right.right.color == BLACK) {
                //assert (node.right.right.color == BLACK && node.right.left.color == BLACK)
                return node
            }
            node = rotateLeft(node)
        }
        if (node.color == BLACK && node.right.color == RED && node.left.color == RED) {
            node.color = RED
            node.left.color = BLACK
            node.right.color = BLACK
        }
        //println ">"*10 + "balanceRight:AFTER"
        //println node
        return node
    }

    static Node balanceAsRoot(node) {
        node.color = BLACK
        if (node.right.color == RED && node.left.color == RED) {
            node.left.color = node.right.color = BLACK
        }
        return node
    }
}

class FillNode extends Node {
    FillNode(int color, String key, String value) {
        this.color = color
        this.key = key
        this.value = value
    }

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
    EmptyNode() {
        this.color = BLACK
    }

    @Override
    String get(String key) {
        null
    }

    @Override
    int height() {
        0 // empty node shouldn't be count
    }

    boolean asBoolean() {
        false
    }
}
