package agds;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public abstract class Node<T> {

    private T value;
    private List<Node> nodesList;

    public Node(T name) {
        this.value = name;
        nodesList = new LinkedList<>();
    }

    public T getValue() {
        return value;
    }

    public List<Node> getNodesList() {
        return nodesList;
    }

    public void addNode(Node node) {
        nodesList.add(node);
    };

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
}
