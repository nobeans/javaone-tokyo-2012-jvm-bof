package org.jggug.javaonetokyo.bof.bench

class RBTreeMap {
    def root = new EmptyNode()

    void put(String key, String value) {
        root = root.put(key, value)
    }

    String get(String key) {
        // TODO
    }

    int height() {
        root.height()
    }
}

enum Color {
    BLACK, RED
}

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
        left.height() + (color == Color.BLACK ? 1 : 0)
    }
}

class FillNode extends Node {
    FillNode(Node parent, Color color, Entry entry) {
        this.parent = parent
        this.color = color
        this.entry = entry
        this.left = new EmptyNode(parent:parent)
        this.right = new EmptyNode(parent:parent)
    }

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
    {
        color = Color.BLACK
    }

    Node put(String key, String value) {
        def parent = this.parent

        entry = new Entry(key:key, value:value)

        // as root
        if (parent == null) {
            return new FillNode(null, Color.BLACK, entry)
        }

        // 
        if (parent.color == Color.BLACK) {
            return new FillNode(parent, Color.RED, entry)
        }

        // parent is red
        def brother = parent.brother
        if (brother == Color.RED) {
            parent.color = Color.BLACK
            brother.color = Color.BLACK
            // TODO ここで親にぶら下がるEmptyNode(this)と新規FillNodeを差し替える？
            return new FillNode(parent, Color.RED, entry)
        }

        return node
    }

    int height() {
        1 // empty node is always black.
    }
}

