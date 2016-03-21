package agds;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public abstract class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    private T value;
    private List<Node> nodesList;

    public Node(T name) {
        this.value = name;
        nodesList = new LinkedList<>();
    }

    public T getValue() {
        return value;
    }

    public List<? extends Node> getNodesList() {
        return nodesList;
    }

    public <E extends Node> void addNode(E newNode) {
        nodesList.add(newNode);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node<?> node = (Node<?>) o;

        return value.equals(node.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(Node<T> o) {
        return 0;
    }
}
