package org.jggug.javaonetokyo.bof.bench

class RBTreeMap {
    def root = new EmptyNode()

    void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null")
        }
        root = root.put(key, value)
    }

    String get(String key) {
        // TODO
    }

    int height() {
        root.height()
    }

    @Override
    String toString() {
        root.toString()
    }
}

enum Color {
    BLACK, RED
}
import static org.jggug.javaonetokyo.bof.bench.Color.*

abstract class Node {
    Color color
    String key
    String value
    Node parent
    Node left
    Node right

    abstract Node put(String key, String value)

    int height() {
        left.height() + (color == BLACK ? 1 : 0)
    }

    Node getBrother() {
        if (isLefty()) {
            return parent.right
        } else {
            return parent.left
        }
    }

    boolean isLefty()  { parent.left == this }
    boolean isRighty() { parent.right == this }

    void setLeft(Node left) {
        this.left = left
        left.parent = this
    }
    void setRight(Node right) {
        this.right = right
        right.parent = this
    }

    Node getRoot() {
        if (parent == null) {
            return this
        }
        return parent.getRoot()
    }
}

class FillNode extends Node {
    FillNode(Color color, String key, String value) {
        this.color = color
        this.key = key
        this.value = value
        this.left = new EmptyNode()
        this.right = new EmptyNode()
    }

    @Override
    Node put(String key, String value) {
        if (this.key == key) {
            this.value == value
            return this
        }
        else if (this.key > key) {
            return left.put(key, value).root
        }
        else if (this.key < key) {
            return right.put(key, value).root
        }
        else {
            assert false
        }
    }

    @Override
    String toString() {
        return "[${parent?.key ?: 'ROOT'}]->${color}(${key}=${value}){L:${left?.toString()}}{R:${right?.toString()}}"
    }
}

class EmptyNode extends Node {
    EmptyNode() {
        this.color = BLACK
    }

    @Override
    Node put(String key, String value) {
        def parent = this.parent

        // as root
        if (parent == null) {
            return new FillNode(BLACK, key, value)
        }

        // parent is black
        if (parent.color == BLACK) {
            Node node = new FillNode(RED, key, value)
            if (this.isLefty()) {
                parent.left = node
            } else {
                parent.right = node
            }
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
            if (this.isLefty()) {
                parent.left = node
            } else {
                parent.right = node
            }
            return node
        }

        // parent is red and parent's brother is black
        if (parent.isLefty() && this.isLefty()) {
            assert false : "まだテストも書いてない"
            def newSubRoot = parent
            parent.color = BLACK
        }
        if (parent.isRighty() && this.isRighty()) {
            def oldSubRoot = parent.parent
            def newSubRoot = parent

            // TODO
            newSubRoot.parent = oldSubRoot.parent

            parent.color = BLACK

            oldSubRoot.right = newSubRoot.left
            oldSubRoot.color = RED

            newSubRoot.left = oldSubRoot

            newSubRoot.right = new FillNode(RED, key, value)

            return newSubRoot
        }

        return node
    }

    @Override
    int height() {
        1 // empty node is always black.
    }

    @Override
    String toString() {
        return "[${parent?.key}]->${color}(empty)"
    }
}

