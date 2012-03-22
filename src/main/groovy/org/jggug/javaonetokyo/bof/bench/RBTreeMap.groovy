package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    def root = new EmptyNode()

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
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

class NodeUtil {
    final static int BLACK = 1
    final static int RED = 0

    static Node rotateRight(Node node) {
        def left = node.left
        node.left = left.right
        left.right = node
        left.color = node.color
        node.color = RED
        return left
    }

    static Node rotateLeft(Node node) {
        def right = node.right
        node.right = right.left
        right.left = node
        right.color = node.color
        node.color = RED
        return right
    }

    static void split(Node node) {
        node.color = RED
        node.left.color = BLACK
        node.right.color = BLACK
    }

    static Node balanceLeft(Node node) {
        if (node.color == BLACK) {
            if (node.left.right.color == RED) {
                node.left = rotateLeft(node.left)
            }
            if (node.left.left.color == RED) {
                if (node.right.color == RED) {
                    split(node)
                } else {
                    return rotateRight(node)
                }
            }
        }
        return node
    }

    static Node balanceRight(node) {
        if (node.color == BLACK) {
            if (node.right.left.color == RED) {
                node.right = rotate_right(node.right)
            }
            if (node.right.right.color == RED)
                if (node.left.color == RED) {
                    split(node)
                } else {
                    return rotate_left(node)
                }
        }
        return node
    }
}

abstract class Node {
    final static int BLACK = 1
    final static int RED = 0

    int color
    String key
    String value
    Node left
    Node right

    abstract Node put(String key, String value)
    abstract String get(String key)
    abstract int height()
}

class FillNode extends Node {
    FillNode(int color, String key, String value) {
        this.color = color
        this.key = key
        this.value = value
        this.left = new EmptyNode()
        this.right = new EmptyNode()
    }

    @Override
    Node put(String key, String value) {
        switch (this.key <=> key) {
            case  0: this.value = value; return this
            case  1: return NodeUtil.balanceLeft(left.put(key, value))
            case -1: return NodeUtil.balanceRight(right.put(key, value))
        }
        assert false
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
        left.height() + color // BLACK = 1, RED = 0
    }

    @Override
    String toString() {
        "[${parent?.key ?: 'ROOT'}]->${color == 1 ? 'BLACK' : 'RED'}(${key}=${value}){L:${left?.toString()}}{R:${right?.toString()}}"
    }
}

class EmptyNode extends Node {
    EmptyNode() {
        this.color = BLACK
    }

    @Override
    Node put(String key, String value) {
        return new FillNode(RED, key, value)
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
    String toString() {
        "[${parent?.key}]->BLACK(empty)"
    }
}

