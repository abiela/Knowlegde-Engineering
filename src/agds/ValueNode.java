package agds;

import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class ValueNode extends Node<Double> {


    public ValueNode(Double value, RecordNode recordNode) {
        super(value);
        addNode(recordNode);
    }
}
