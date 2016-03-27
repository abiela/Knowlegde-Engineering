package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class ValueNode extends Node implements Comparable<ValueNode>, Resetable {

    private Double value;
    private Double wage;
    private List<RecordNode> recordNodeList;
    private AttributeNode attributeNode;

    /**
     * Constructor, getter & setter.
     */

    public ValueNode(Double value) {
        this.value = value;
        this.recordNodeList = new ArrayList<>();
    }


    public void setAttributeNode(AttributeNode attributeNode) {
        this.attributeNode = attributeNode;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Double getValue() {
        return value;
    }

    public List<RecordNode> getRecordNodeList() {
        return recordNodeList;
    }

    /**
     * Adding calculated wage to all associated record nodes.
     * @param wage
     */
    public void addCalcuatedWageToAllRecords(Double wage) {
        setWage(wage);
        for (RecordNode recordNode : recordNodeList) {
            recordNode.addToTotalWage(wage);
        }
    }

    /**
     * Adding new record node.
     * @param recordNode
     */
    public void addRecordNode(RecordNode recordNode) {
        recordNodeList.add(recordNode);
    }

    /**
     * Interface methods
     */

    @Override
    public int compareTo(ValueNode o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public void onResetValue() {
        wage = 0.0d;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueNode that = (ValueNode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
