package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class NewAttributeNode extends NewNode {

    private final String value;
    private List<NewValueNode> valueNodeList;
    private NewValueNode minValueNode;
    private NewValueNode maxValueNode;

    /**
     * Constuctrors, getters & setters
     */

    public NewAttributeNode(String value) {
        this.value = value;
        this.valueNodeList = new ArrayList<>();
    }

    public String getValue() {
        return value;
    }

    public List<NewValueNode> getValueNodeList() {
        return valueNodeList;
    }

    public NewValueNode getMinValueNode() {
        return minValueNode;
    }

    public NewValueNode getMaxValueNode() {
        return maxValueNode;
    }

    /**
     * Adding new value node to attribute list.
     * @param valueNode - value candidate
     */

    public void addNode(NewValueNode valueNode) {

        if (valueNodeList.contains(valueNode)) {
            NewValueNode foundValueNode = valueNodeList.get(valueNodeList.indexOf(valueNode));
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
        for (NewValueNode valueNode : getValueNodeList()) {
            double wageValue = 1 - (Math.abs(valueNode.getValue() - getValueNodeList().get(indexValue).getValue())) / (maxValueNode.getValue() - minValueNode.getValue());
            valueNode.addCalcuatedWageToAllRecords(wageValue);
        }
    }

    public void resetValueNodes() {
        for (NewValueNode valueNode : valueNodeList)
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
