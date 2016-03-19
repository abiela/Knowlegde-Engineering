package agds;

import java.util.Collections;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class AtributeNode extends Node<String> {

    private Node minValueNode;
    private Node maxValueNode;
    private Double range;

    public AtributeNode(String name) {
        super(name);
    }

    public void sortNodes() {
        Collections.sort(getNodesList());
    }
}
