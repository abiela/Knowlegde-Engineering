package agds;

import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class ValueNode extends Node<Float> {


    public ValueNode(Float value, RecordNode recordNode) {
        super(value);
        addNode(recordNode);
    }
}
