package spell;

import java.util.Objects;

public class Trie implements ITrie {
    private Node head;
    private int nodeCount;
    private int wordCount;
    private  int sumWordFreq;

    public Trie() {
        this.head = new Node();
        this.nodeCount = 1;
        this.wordCount = 0;
        this.sumWordFreq = 0;
    }

    @Override
    public void add(String word) {
        Node curNode = this.head;
        word = word.toLowerCase();
        for (char c : word.toCharArray()) {
            int i = (int)(c - 'a');
            if (!curNode.hasChildAt(i)) {
                curNode.createChild(i);
                nodeCount++;
            }
            curNode = curNode.at(i);
        }
        curNode.incrementValue();
        if (curNode.getValue() == 1) { wordCount++; }
        sumWordFreq += curNode.getValue();
    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        Node curNode = this.head;

        for (char c : word.toCharArray()) {
            int i = (int)(c - 'a');
            if (curNode.hasChildAt(i)) {
                curNode = curNode.at(i);
            } else { return null; }
        }
        if (curNode.getValue() > 0) { return curNode; }
        return null;
    }

    @Override
    public int getWordCount() { return this.wordCount; }

    @Override
    public int getNodeCount() { return this.nodeCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trie that = (Trie) o;

        if (this.nodeCount != that.nodeCount) { return false; }
        if (this.wordCount != that.wordCount) { return false; }
        return equals_R(this.head, that.head);
    }
    private boolean equals_R(Node n1, Node n2) {
        if (n1.getValue() != n2.getValue()) { return false; }
        for (int i = 0; i < n1.getChildren().length; i++) {
            if (n1.hasChildAt(i) || n2.hasChildAt(i)) {
                if (n1.hasChildAt(i) && n2.hasChildAt(i)) {
                    if (!equals_R(n1.at(i), n2.at(i))) { return false; }
                } else { return false; }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int firstChild = 0;
        for (int i = 0; i < this.head.getChildren().length; i++) {
            if (this.head.hasChildAt(i)) { firstChild = i; break; }
        }
        return this.wordCount * this.nodeCount + this.sumWordFreq + firstChild;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        StringBuilder word = new StringBuilder();
        out.append(toString_R(this.head, word));
        return out.toString();
    }
    private String toString_R(Node n, StringBuilder word) {
        StringBuilder out = new StringBuilder();
        if (n.getValue() > 0) {
            out.append(word);
            out.append('\n');
        }
        for (int i = 0; i < n.getChildren().length; i++) {
            if (n.hasChildAt(i)) {
                word.append((char)(i + 'a'));
                out.append(toString_R(n.at(i), word));
            }
        }
        if (word.length() > 0) { word.deleteCharAt(word.length() - 1); }
        return out.toString();
    }
}
