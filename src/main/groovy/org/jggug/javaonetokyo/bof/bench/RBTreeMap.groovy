package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    private Node root = Node.EMPTY

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value)
        root.toRootColor()
    }

    String get(String key) {
        if (key == null) throw new IAE("key is null")
        root.get(key)
    }

    int height() { root.height() }

    @Override
    String toString() { root.toString() }

    static RBTreeMap newMap() { new RBTreeMap() }
}

abstract class Node {
    protected final static int BLACK = 1
    protected final static int RED = 0
    protected final static Node EMPTY = new EmptyNode(color:BLACK)

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
        int compare = (this.key <=> key)
        if (compare > 0) {
            left = left.put(key, value)
            return balanceLeft(this)
        }
        if (compare < 0) {
            right = right.put(key, value)
            return balanceRight(this)
        }
        this.value = value
        return this
    }

    void toRootColor() { setColor(BLACK) }

    @Override
    String toString() { toTreeString(0) }

    String toTreeString(int level) {
        def indent = " "
        def buff = []
        if (right instanceof FillNode) {
            buff << right.toTreeString(level + 1)
        }
        buff << indent * level + (color == BLACK ? 'B' : 'R') + "($key)"
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
        if (node.color == BLACK && left.color == RED) {
            if (right.color == BLACK) {
                if (left.right.color == RED) {
                    node.setLeft(rotateLeft(left))
                }
                // both are black
                else if (left.left.color == BLACK) {
                    return node
                }
                node = rotateRight(node)
            }
            right = node.right
            left = node.left
            if (node.color == BLACK && right.color == RED && left.color == RED) {
                node.setColor(RED)
                left.setColor(BLACK)
                right.setColor(BLACK)
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
        if (node.color == BLACK && right.color == RED) {
            if (left.color == BLACK) {
                if (right.left.color == RED) {
                    node.setRight(rotateRight(right))
                }
                // both are black
                else if (right.right.color == BLACK) {
                    return node
                }
                node = rotateLeft(node)
            }
            right = node.right
            left = node.left
            if (node.color == BLACK && right.color == RED && left.color == RED) {
                node.setColor(RED)
                left.setColor(BLACK)
                right.setColor(BLACK)
            }
        }
        //println ">"*10 + "balanceRight:AFTER"
        //println node
        return node
    }
}

class FillNode extends Node {
    FillNode(int color, String key, String value) {
        setColor(color)
        setKey(key)
        setValue(value)
    }

    @Override
    String get(String key) {
        int compare = (this.key <=> key)
        if (compare > 0) return left.get(key)
        if (compare < 0) return right.get(key)
        return value
    }

    @Override
    int height() {
        left.height() + color // because of BLACK = 1 and RED = 0
    }
}

class EmptyNode extends Node {
    @Override
    Node put(String key, String value) {
        return new FillNode(RED, key, value)
    }

    @Override
    String get(String key) { null }

    @Override
    int height() { 0 } // empty node shouldn't be count
}
