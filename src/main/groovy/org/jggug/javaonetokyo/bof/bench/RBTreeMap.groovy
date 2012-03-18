package org.jggug.javaonetokyo.bof.bench

class RBTreeMap {

    def root = new Empty()

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
        }
        else if (entry.key == key) {
            entry.value == value
        }
        else if (entry.key > key) {
            left.put(key, value)
        }
        else if (entry.key < key) {
            right.put(key, value)
        }
        else {
            assert false
        }
    }
}

class Empty extends Node {
    Node put(String key, String value) {
        entry = new Entry(key:key, value:value)
        return new FillNode(color:Color.RED, entry:entry, left:new Empty(), right:new Empty())
    }

    int height() {
        0
    }
}

