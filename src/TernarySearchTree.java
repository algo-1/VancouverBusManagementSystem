import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TernarySearchTree {
    private Node root;

    private static class Node
    {
        private final char value;
        private Node left;
        private Node mid;
        private Node right;

        Node(char value)
        {
            this.value = value;
        }
    }
    TernarySearchTree() {}

    public void insert(String word)
    {
        this.root = insert(root, word, 0);
    }

    private Node insert(Node node, String word, int idx)
    {
        char letter = word.charAt(idx);
        if (node == null)
        {
            node = new Node(letter);
        }
        if (letter < node.value) node.left = insert(node.left, word, idx);
        else if (letter > node.value) node.right = insert(node.right, word, idx);
        else if (idx < word.length() - 1) node.mid = insert(node.mid, word, idx + 1);

        return node;
    }

    public List<String> find(String prefix)
    {
        return find(root, prefix, 0);
    }

    private List<String> find(Node node, String prefix, int idx)
    {
        if (node == null) return List.of("No match found.");
        char letter = prefix.charAt(idx);

        if (letter < node.value) return find(node.left, prefix, idx);
        else if (letter > node.value) return find(node.right, prefix, idx);
        else if (idx < prefix.length() - 1)
        {
            return find(node.mid, prefix, idx + 1);
        }
        else
        {
            List<StringBuilder> suffixes = getWords(node, new ArrayList<StringBuilder>(), new StringBuilder());
            return suffixes.stream().map(suffix -> prefix + suffix.toString()).collect(Collectors.toList());
        }
    }

    private List<StringBuilder> getWords(Node node, List<StringBuilder> matchingWords, StringBuilder suffix)
    {
        if (node.mid == null)
        {
            matchingWords.add(suffix);
            return matchingWords;
        }

        List<Node> subTries = new ArrayList<Node>();
        subTries.add(node.mid);

        subTries = getSubTries(node.mid, subTries);
        for (Node trie : subTries)
        {
            getWords(trie, matchingWords, new StringBuilder(suffix).append(trie.value));
        }

        return matchingWords;
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
