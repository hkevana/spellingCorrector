package spell;

public class Node implements INode {
    private int count;
    private Node[] children;

    public Node() {
        this.count = 0;
        this.children = new Node[26];
    }
    @Override
    public int getValue() { return this.count; }

    @Override
    public void incrementValue() { this.count++; }

    @Override
    public INode[] getChildren() { return this.children; }

    public boolean hasChildAt(int i) { return this.children[i] != null; }
    public Node at(int i) { return this.children[i]; }
    public void createChild(int i) { this.children[i] = new Node(); }
}
