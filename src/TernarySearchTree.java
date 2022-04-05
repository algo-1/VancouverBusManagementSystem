import java.util.ArrayList;
import java.util.List;

public class TernarySearchTree {
    private Node root;

    private static class Node
    {
        private final char key;
        private Node left;
        private Node mid;
        private Node right;
        private final Stop value;

        Node(char key, Stop value)
        {
            this.key = key;
            this.value = value;
        }
    }
    TernarySearchTree() {}

    public void insert(String word, Stop value)
    {
        this.root = insert(root, word, value, 0);
    }

    private Node insert(Node node, String word, Stop value, int idx)
    {
        char letter = word.charAt(idx);
        if (node == null)
        {
            node = new Node(letter, value);
        }
        if (letter < node.key) node.left = insert(node.left, word, value, idx);
        else if (letter > node.key) node.right = insert(node.right, word, value, idx);
        else if (idx < word.length() - 1) node.mid = insert(node.mid, word, value, idx + 1);
        return node;
    }

    public List<Stop> find(String prefix)
    {
        return find(root, prefix, 0);
    }

    private List<Stop> find(Node node, String prefix, int idx)
    {
        if (node == null) return null;
        char letter = prefix.charAt(idx);

        if (letter < node.key) return find(node.left, prefix, idx);
        else if (letter > node.key) return find(node.right, prefix, idx);
        else if (idx < prefix.length() - 1)
        {
            return find(node.mid, prefix, idx + 1);
        }
        else
        {
            return getMatchingStops(node, new ArrayList<Stop>());
        }
    }

    private List<Stop> getMatchingStops(Node node, List<Stop> matchingStops)
    {
        if (node.mid == null)
        {
            matchingStops.add(node.value);
            return matchingStops;
        }

        List<Node> subTries = new ArrayList<Node>();
        subTries.add(node.mid);

        subTries = getSubTries(node.mid, subTries);
        for (Node trie : subTries)
        {
            getMatchingStops(trie, matchingStops);
        }

        return matchingStops;
    }

    private List<Node> getSubTries(Node node, List<Node> subTries)
    {
        if (node == null) return subTries;
        if (node.left != null) subTries.add(node.left);
        if (node.right != null) subTries.add(node.right);
        getSubTries(node.left, subTries);
        getSubTries(node.right, subTries);
        return subTries;
    }

}
