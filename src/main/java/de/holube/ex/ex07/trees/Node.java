package de.holube.ex.ex07.trees;

public class Node {

    private String content;
    private Node left, right;

    public Node(String c) {
        content = c;
        left = null;
        right = null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
