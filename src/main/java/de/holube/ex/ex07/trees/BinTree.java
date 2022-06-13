package de.holube.ex.ex07.trees;

public class BinTree {

    private Node root;

    public BinTree() {
        this.root = null;
    }

    public BinTree(String v) {
        this.root = new Node(v);
    }

    public BinTree(BinTree l, String v, BinTree r) {
        this.root = new Node(v);
        if (l != null) {
            this.root.setLeft(l.root);
        }
        if (r != null) {
            this.root.setRight(r.root);
        }
    }

    private BinTree(Node n) {
        this.root = n;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public String getValue() {
        if (this.isEmpty()) {
            return null; // error
        }
        return this.root.getContent();
    }

    public BinTree getLeft() {
        if (this.isEmpty()) {
            return null; // error
        }
        return new BinTree(this.root.getLeft());
    }

    public void setLeft(BinTree tree) {
        if (this.isEmpty()) {
            return; // error
        }
        this.root.setLeft(tree.root);
    }

    public BinTree getRight() {
        if (this.isEmpty()) {
            return null; // error
        }
        return new BinTree(this.root.getRight());
    }

    public void setRight(BinTree tree) {
        if (this.isEmpty()) {
            return; // error
        }
        this.root.setRight(tree.root);
    }

    public void inorder() {
        if (!this.isEmpty()) {
            this.getLeft().inorder();
            System.out.print(this.getValue());
            this.getRight().inorder();
        }
    }

    public void preorder() {
        if (!this.isEmpty()) {
            System.out.print(this.getValue());
            this.getLeft().preorder();
            this.getRight().preorder();
        }
    }

    public void postorder() {
        if (!this.isEmpty()) {
            this.getLeft().postorder();
            this.getRight().postorder();
            System.out.print(this.getValue());
        }
    }

    public void depthFirstTraversal() { // == preorder
        BinTreeStack stack = new BinTreeStack();
        if (!this.isEmpty()) {
            stack.push(this);
        }
        while (!stack.isEmpty()) {
            BinTree tree = stack.pop();
            do {
                System.out.print(tree.getValue());
                if (!tree.getRight().isEmpty()) {
                    stack.push(tree.getRight());
                }
                tree = tree.getLeft();
            } while (!tree.isEmpty());
        }
    }

    public void breadthFirstTraversal() {
        BinTreeQueue queue = new BinTreeQueue();
        if (!this.isEmpty()) {
            queue.enque(this);
        }
        while (!queue.isEmpty()) {
            BinTree tree = queue.deque();
            System.out.print(tree.getValue());
            if (!tree.getLeft().isEmpty()) {
                queue.enque(tree.getLeft());
            }
            if (!tree.getRight().isEmpty()) {
                queue.enque(tree.getRight());
            }
        }
    }

    public BinTree dfs(String data) {
        if (isEmpty()) {
            return null;
        }
        if (getValue().equals(data)) {
            return this;
        }
        BinTree result = null;
        if (getLeft() != null) {
            result = getLeft().dfs(data);
            if (result != null) {
                return result;
            }
        }
        if (getRight() != null) {
            result = getRight().dfs(data);
        }
        return result;
    }

    public BinTree dfsIterative(String data) {
        BinTreeStack stack = new BinTreeStack();
        if (!isEmpty()) {
            stack.push(this);
        }
        while (!stack.isEmpty()) {
            BinTree tree = stack.pop();
            do {
                if (tree.getValue().equals(data)) {
                    return tree;
                }
                if (!tree.getRight().isEmpty()) {
                    stack.push(tree.getRight());
                }
                tree = tree.getLeft();
            } while (!tree.isEmpty());
        }
        return null;
    }

    public BinTree bfs(String data) {
        BinTreeQueue queue = new BinTreeQueue();
        if (!isEmpty()) {
            queue.enque(this);
        }
        while (!queue.isEmpty()) {
            BinTree tree = queue.deque();
            if (tree.getValue().equals(data)) {
                return tree;
            }
            if (!tree.getLeft().isEmpty()) {
                queue.enque(tree.getLeft());
            }
            if (!tree.getRight().isEmpty()) {
                queue.enque(tree.getRight());
            }
        }
        return null;
    }

    public void insertDF(String value) { // depth first
        if (this.isEmpty()) {
            this.root = new Node(value);
            return;
        }
        BinTree tree = this;
        while (!tree.getLeft().isEmpty()) {
            tree = tree.getLeft();
        }
        tree.setLeft(new BinTree(value));

    }

    public void insertBF(String value) { // breadth first
        if (this.isEmpty()) {
            this.root = new Node(value);
            return;
        }
        BinTreeQueue queue = new BinTreeQueue();
        queue.enque(this);
        while (!queue.isEmpty()) {
            BinTree tree = queue.deque();
            if (tree.getLeft().isEmpty()) {
                tree.setLeft(new BinTree(value));
                return;
            }
            queue.enque(tree.getLeft());
            if (tree.getRight().isEmpty()) {
                tree.setRight(new BinTree(value));
                return;
            }
            queue.enque(tree.getRight());
        }
    }

    public int getNumberOfNodes() {
        if (this.isEmpty()) {
            return 0;
        }
        return 1 + this.getLeft().getNumberOfNodes() + this.getRight().getNumberOfNodes();
    }

    public int getHeight() {
        if (this.isEmpty()) {
            return 0;
        }
        return 1 + Math.max(this.getLeft().getHeight(), this.getRight().getHeight());
    }

}
