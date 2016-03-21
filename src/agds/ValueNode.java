package agds;

import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class ValueNode extends Node<Double> implements Comparable<Node<Double>> {

    private double wage;

    public ValueNode(Double value, RecordNode recordNode) {
        super(value);
        addNode(recordNode);
    }

    @Override
    public int compareTo(Node<Double> o) {
        double lhsValue = getValue();
        double rhsValue = o.getValue();

        if (lhsValue < rhsValue)
            return -1;

        else if (lhsValue == rhsValue)
            return 0;

        return 1;
    }

    public void setWage(double wage) {
        this.wage = wage;
        for(Node node : getNodesList()) {
            if(node instanceof RecordNode) {
                ((RecordNode) node).addToAverageWage(wage);
            }
        }
    }
}
