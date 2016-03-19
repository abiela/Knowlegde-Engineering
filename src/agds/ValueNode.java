package agds;

import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class ValueNode extends Node<Double> implements Comparable<Node<Double>> {

    public ValueNode(Double value, RecordNode recordNode) {
        super(value);
        addNode(recordNode);
    }

    @Override
    public int compareTo(Node<Double> o) {
        double lhsValue = getValue();
        double rhsValue = o.getValue();

        if(lhsValue < rhsValue)
            return -1;

        else if (lhsValue == rhsValue)
            return 0;

        return 1;
    }
}
