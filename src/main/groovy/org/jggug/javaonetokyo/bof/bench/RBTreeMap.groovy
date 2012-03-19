package org.jggug.javaonetokyo.bof.bench

import java.lang.IllegalArgumentException as IAE

class RBTreeMap {
    def root = new EmptyNode()

    private RBTreeMap() {}

    void put(String key, String value) {
        if (key == null) throw new IAE("key is null")
        root = root.put(key, value).root
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

    int color
    String key
    String value
    Node parent
    Node left
    Node right

    abstract Node put(String key, String value)
    abstract String get(String key)
    abstract int height()

    final Node getBrother() {
        if (isLefty()) {
            return parent.right
        } else {
            return parent.left
        }
    }

    final void replace(Node node) {
        if (parent == null) {
            node.parent = null
        } else if (isLefty()) {
            parent.left = node
        } else {
            parent.right = node
        }
    }

    final boolean isLefty() { parent.left == this }
    final boolean isRighty() { parent.right == this }

    final void setLeft(Node left) {
        this.left = left
        left.parent = this
    }
    final void setRight(Node right) {
        this.right = right
        right.parent = this
    }

    final Node getRoot() {
        if (parent == null) {
            return this
        }
        return parent.getRoot()
    }
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
            case  0: return this
            case  1: return left.put(key, value)
            case -1: return right.put(key, value)
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
        // as root
        if (parent == null) {
            return new FillNode(BLACK, key, value)
        }

        // parent is black
        if (parent.color == BLACK) {
            Node node = new FillNode(RED, key, value)
            this.replace(node)
            return node
        }

        // parent is red
        def brother = parent.brother
        if (brother.color == RED) {
            parent.color = BLACK
            brother.color = BLACK
            def grandParent = parent.parent
            if (grandParent != null && grandParent.parent != null) { // if grandparent isn't root
                grandParent.color = RED
            }
            Node node = new FillNode(RED, key, value)
            this.replace(node)
            return node
        }

        // parent is red and parent's brother is black
        boolean isLefty = this.isLefty()
        boolean parentIsLefty = parent.isLefty()
        if (parentIsLefty) {
            if (isLefty) {
                def oldSubRoot = parent.parent
                def newSubRoot = parent
                newSubRoot.color = BLACK
                oldSubRoot.replace(newSubRoot)
                oldSubRoot.left = newSubRoot.right
                oldSubRoot.color = RED
                newSubRoot.right = oldSubRoot
                newSubRoot.left = new FillNode(RED, key, value)
                return newSubRoot
            } else {
                def oldSubRoot = parent.parent
                def newSubRoot = new FillNode(BLACK, key, value)
                oldSubRoot.replace(newSubRoot)
                oldSubRoot.left = newSubRoot.right
                oldSubRoot.color = RED
                parent.right = newSubRoot.left
                parent.color = RED
                newSubRoot.right = oldSubRoot
                newSubRoot.left = parent
                return newSubRoot
            }
        } else {
            if (isLefty) {
                def oldSubRoot = parent.parent
                def newSubRoot = new FillNode(BLACK, key, value)
                oldSubRoot.replace(newSubRoot)
                oldSubRoot.right = newSubRoot.left
                oldSubRoot.color = RED
                parent.left = newSubRoot.right
                parent.color = RED
                newSubRoot.left = oldSubRoot
                newSubRoot.right = parent
                return newSubRoot
            } else {
                def oldSubRoot = parent.parent
                def newSubRoot = parent
                newSubRoot.color = BLACK
                oldSubRoot.replace(newSubRoot)
                oldSubRoot.right = newSubRoot.left
                oldSubRoot.color = RED
                newSubRoot.left = oldSubRoot
                newSubRoot.right = new FillNode(RED, key, value)
                return newSubRoot
            }
        }
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

