package org.jggug.javaonetokyo.bof.bench

class RBTreeMap {
    def root = new EmptyNode()

    void put(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key is null")
        }
        root = root.put(key, value).root
    }

    String get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null")
        }
        root.get(key)
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

    abstract String get(String key)

    int height() {
        left.height() + (color == BLACK ? 1 : 0)
    }

    Node getBrother() {
        if (isLefty()) {
            return parent.right
        } else if (isRighty()) {
            return parent.left
        } else {
            assert false
        }
    }

    void replace(Node node) {
        if (parent == null) {
            node.parent = null
        } else if (isLefty()) {
            parent.left = node
        } else if (isRighty()) {
            parent.right = node
        } else {
            assert false
        }
    }

    boolean isLefty() { parent.left == this }
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
            return left.put(key, value)
        }
        else if (this.key < key) {
            return right.put(key, value)
        }
        else {
            assert false
        }
    }

    @Override
    String get(String key) {
        if (this.key == key) {
            return value
        }
        else if (this.key > key) {
            return left.get(key)
        }
        else if (this.key < key) {
            return right.get(key)
        }
        else {
            assert false
        }
    }

    @Override
    String toString() {
        "[${parent?.key ?: 'ROOT'}]->${color}(${key}=${value}){L:${left?.toString()}}{R:${right?.toString()}}"
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
        if (parent.isLefty() && this.isLefty()) {
            def oldSubRoot = parent.parent
            def newSubRoot = parent
            newSubRoot.color = BLACK
            oldSubRoot.replace(newSubRoot)
            oldSubRoot.left = newSubRoot.right
            oldSubRoot.color = RED
            newSubRoot.right = oldSubRoot
            newSubRoot.left = new FillNode(RED, key, value)
            return newSubRoot.left
        }
        if (parent.isRighty() && this.isRighty()) {
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
        if (parent.isRighty() && this.isLefty()) {
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
        }
        if (parent.isLefty() && this.isRighty()) {
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

        assert false
    }

    @Override
    int height() {
        1 // empty node is always black.
    }

    @Override
    String get(String key) {
        null
    }

    @Override
    String toString() {
        "[${parent?.key}]->${color}(empty)"
    }
}

