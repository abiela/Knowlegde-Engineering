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

    public Double getRange() {
        return range;
    }

    public void sortNodes() {
        Collections.sort(getNodesList());
        assignMinValueNode();
        assignMaxValueNode();
        calculateRange();
    }

    public void calculateWages(int index) {
        for (Node node : getNodesList()) {
            if(node instanceof ValueNode) {
                double valueWage = 1 - (Math.abs(((ValueNode) node).getValue() - (double) getNodesList().get(index).getValue()))/range;
                System.out.println("Wage: " + valueWage);
                ((ValueNode) node).setWage(valueWage);
            }
        }
    }

    private void assignMinValueNode() {
        minValueNode = getNodesList().get(0);
    }

    private void assignMaxValueNode() {
        maxValueNode = getNodesList().get(getNodesList().size() - 1);
    }

    private void calculateRange() {
        range = ((double) maxValueNode.getValue()) - ((double) minValueNode.getValue());
    }
}
