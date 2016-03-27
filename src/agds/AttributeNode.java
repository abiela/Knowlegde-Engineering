package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class AttributeNode extends Node {

    private final String value;
    private List<ValueNode> valueNodeList;
    private ValueNode minValueNode;
    private ValueNode maxValueNode;

    /**
     * Constuctrors, getters & setters
     */

    public AttributeNode(String value) {
        this.value = value;
        this.valueNodeList = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<ValueNode> getValueNodeList() {
        return valueNodeList;
    }

    public ValueNode getMinValueNode() {
        return minValueNode;
    }

    public ValueNode getMaxValueNode() {
        return maxValueNode;
    }

    /**
     * Adding new value node to attribute list.
     * @param valueNode - value candidate
     */

    public void addNode(ValueNode valueNode) {

        if (valueNodeList.contains(valueNode)) {
            ValueNode foundValueNode = valueNodeList.get(valueNodeList.indexOf(valueNode));
            foundValueNode.getRecordNodeList().addAll(valueNode.getRecordNodeList());
        } else
            valueNodeList.add(valueNode);
    }


    /**
     * Sorting value nodes in ascending order.
     */
    public void sortValueNodes() {
        Collections.sort(valueNodeList);
        assignMinValueNode();
        assignMaxValueNode();
    }

    /**
     * Calculating wages based on most similar element in index
     */
    public void calculateWages(int indexValue) {
        for (ValueNode valueNode : getValueNodeList()) {
            double wageValue = 1 - (Math.abs(valueNode.getValue() - getValueNodeList().get(indexValue).getValue())) / (maxValueNode.getValue() - minValueNode.getValue());
            valueNode.addCalcuatedWageToAllRecords(wageValue);
        }
    }

    /**
     * Reseting value nodes wage value.
     */
    public void resetValueNodes() {
        for (ValueNode valueNode : valueNodeList)
            valueNode.onResetValue();
    }

    /**
     * Keeping reference to minimal value node.
     */
    private void assignMinValueNode() {
        minValueNode = valueNodeList.get(0);
    }

    /**
     * Keeping reference to maximal value node.
     */
    private void assignMaxValueNode() {
        maxValueNode = valueNodeList.get(valueNodeList.size() - 1);
    }
}
