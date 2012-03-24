package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    def root = Node.EMPTY

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
        root.color = Node.BLACK
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
    boolean shouldBlance = false

    abstract String get(String key)
    abstract int height()

    Node put(String key, String value) {
        if (this == EMPTY) {
            return new FillNode(RED, key, value)
        }
        switch (this.key <=> key) {
            case  0:
                this.value = value
                return this
            case  1:
                left = left.put(key, value)
                println("L:" + left?.shouldBlance)
                if (left.shouldBlance) {
                    return balanceLeft(this)
                } else {
                    return this
                }
            case -1:
                right = right.put(key, value)
                println("R:" + right?.shouldBlance)
                if (right.shouldBlance) {
                    return balanceRight(this)
                } else {
                    return this
                }
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

    private static Node rotateRight(Node node) {
        def left = node.left
        node.left = left.right
        left.right = node
        left.color = node.color
        node.color = RED
        return left
    }

    private static Node rotateLeft(Node node) {
        def right = node.right
        node.right = right.left
        right.left = node
        right.color = node.color
        node.color = RED
        return right
    }

    private static void split(Node node) {
        node.color = RED
        node.left.color = BLACK
        node.right.color = BLACK
    }

    private static Node balanceLeft(Node node) {
        if (node.color == BLACK) {
            node.shouldBlance = false
            if (node.left.right.color == RED) {
                node.left = rotateLeft(node.left)
            }
            if (node.left.left.color == RED) {
                if (node.right.color == RED) {
                    split(node)
                    node.shouldBlance = true
                } else {
                    return rotateRight(node)
                }
            }
        }
        return node
    }

    private static Node balanceRight(Node node) {
        if (node.color == BLACK) {
            node.shouldBlance = false
            if (node.right.left.color == RED) {
                node.right = rotateRight(node.right)
            }
            if (node.right.right.color == RED) {
                if (node.left.color == RED) {
                    split(node)
                    node.shouldBlance = true
                } else {
                    return rotateLeft(node)
                }
            }
        }
        return node
    }
}

class FillNode extends Node {
    FillNode(int color, String key, String value) {
        this.color = color
        this.key = key
        this.value = value
        this.shouldBlance = true
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
        1 // empty node is always black.
    }

    @Override
    boolean asBoolean() {
        false
    }
}
