package org.jggug.javaonetokyo.bof.bench

enum Color {
    BLACK, RED
}

class RedBlackTreeMap {

    def root = new Empty()

    void put(String key, String value) {
        root = root.put(key, value)
    }

    int height() {
        root.height()
    }
}

class Entry {
    String key
    String value
}

abstract class Node {
    Color color
    Entry entry
    Node left
    Node right

    int height() {
        left.height() + (color == Color.BLACK ? 1 : 0)
    }
}

class FillNode extends Node {
    Node put(String key, String value) {
        if (entry == null) {
            entry = new Entry(key:key, value:value)
            return
        }
        if (entry.key == key) {
            entry = new Entry(key:key, value:value)
            return
        }
        if (entry.key > key) {
            left.put(key, value)
        }
        if (entry.key < key) {
            right.put(key, value)
        }
    }
}

class Empty extends Node {

    Empty() {
        color = Color.BLACK
    }

    Node put(String key, String value) {
        entry = new Entry(key:key, value:value)
        return new FillNode(color:Color.RED, entry:entry, left:new Empty(), right:new Empty())
    }

    int height() {
        1
    }
}

