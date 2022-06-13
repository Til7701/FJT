// (3 + (4 * 5)) / (8 - 2)
//
//         '/'
//      /       \
//      +       -
//     / \     / \ 
//     3 *     8 2
//      / \
//      4 5
package de.holube.ex.ex07.trees;

public class TraverseTest {

    public static void main(String[] argv) {

        BinTree a = new BinTree("4");
        BinTree b = new BinTree("5");
        BinTree m = new BinTree(a, "*", b);
        BinTree f = new BinTree("3");
        BinTree p = new BinTree(f, "+", m);
        BinTree x = new BinTree("8");
        BinTree y = new BinTree("2");
        BinTree n = new BinTree(x, "-", y);
        BinTree root = new BinTree(p, "/", n);

        System.out.print("Preorder: ");
        root.preorder();
        System.out.println();

        System.out.print("Inorder: ");
        root.inorder();
        System.out.println();

        System.out.print("Postorder: ");
        root.postorder();
        System.out.println();

        System.out.print("DepthFirst: ");
        root.depthFirstTraversal();
        System.out.println();

        System.out.print("BreadthFirst: ");
        root.breadthFirstTraversal();
        System.out.println();

        System.out.print("DepthFirstSearch: ");
        BinTree t = root.dfs("5");
        if (t != null) {
            System.out.println(t.getValue());
        } else {
            System.out.println("nicht gefunden");
        }

        System.out.print("DepthFirstSearchIterative: ");
        t = root.dfsIterative("5");
        if (t != null) {
            System.out.println(t.getValue());
        } else {
            System.out.println("nicht gefunden");
        }

        System.out.print("BreadthFirstSearch: ");
        t = root.bfs("5");
        if (t != null) {
            System.out.println(t.getValue());
        } else {
            System.out.println("nicht gefunden");
        }
    }
}
