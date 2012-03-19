package org.jggug.javaonetokyo.bof.bench

class RBTreeMap {
    def root = new EmptyNode(null)

    void put(String key, String value) {
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

class Entry {
    String key
    String value
}

abstract class Node {
    Node parent
    Color color
    Entry entry
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

    @Override
    String toString() {
        return "[${parent?.entry?.key ?: 'ROOT'}]->${color}(${entry?.key}=${entry?.value}){L:${left?.toString()}}{R:${right?.toString()}}"
    }
}

class FillNode extends Node {
    FillNode(Node parent, Color color, Entry entry) {
        this.parent = parent
        this.color = color
        this.entry = entry
        this.left = new EmptyNode(this)
        this.right = new EmptyNode(this)
    }

    @Override
    Node put(String key, String value) {
        if (entry == null) {
            entry = new Entry(key:key, value:value)
            return this
        }
        else if (entry.key == key) {
            entry.value == value
            return this
        }
        else if (entry.key > key) {
            left = left.put(key, value)
            return this
        }
        else if (entry.key < key) {
            right = right.put(key, value)
            return this
        }
        else {
            assert false
        }
    }
}

class EmptyNode extends Node {
    EmptyNode(parent) {
        this.parent = parent
        this.color = BLACK
    }

    @Override
    Node put(String key, String value) {
        def parent = this.parent

        entry = new Entry(key:key, value:value)

        // as root
        if (parent == null) {
            return new FillNode(null, BLACK, entry)
        }

        // parent is black
        if (parent.color == BLACK) {
            return new FillNode(parent, RED, entry)
        }

        // parent is red
        def brother = parent.brother
        if (brother.color == RED) {
            parent.color = BLACK
            brother.color = BLACK
            def grandParent = parent.parent
            if (grandParent != null && grandParent.parent != null) { // if grandparent is root
                grandParent.color = RED
            }
            // TODO ここで親にぶら下がるEmptyNode(this)と新規FillNodeを差し替える？
            return new FillNode(parent, RED, entry)
        }

        assert false : "まだテストも書いてない"

        // parent is red and parent's brother is black
        if (parent.isLefty() && this.isLefty()) {
            def newSubRoot = parent
            parent.color = BLACK

        }
        if (parent.isRighty() && this.isRighty()) {
        }


        return node
    }

    @Override
    int height() {
        1 // empty node is always black.
    }

    @Override
    String toString() {
        return "[${parent?.entry?.key}]->${color}(empty)"
    }
}

