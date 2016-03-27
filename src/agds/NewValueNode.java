package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class NewValueNode extends NewNode implements Comparable<NewValueNode>, Resetable {

    private Double value;
    private Double wage;
    private List<NewRecordNode> recordNodeList;
    private NewAttributeNode attributeNode;

    public NewValueNode(Double value) {
        this.value = value;
        this.recordNodeList = new ArrayList<>();
    }


    public void setAttributeNode(NewAttributeNode attributeNode) {
        this.attributeNode = attributeNode;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Double getValue() {
        return value;
    }

    public List<NewRecordNode> getRecordNodeList() {
        return recordNodeList;
    }

    @Override
    public int compareTo(NewValueNode o) {
        return value.compareTo(o.getValue());
    }

    @Override
    public void onResetValue() {
        wage = 0.0d;
    }

    public void addCalcuatedWageToAllRecords(Double wage) {
        setWage(wage);
        for (NewRecordNode recordNode : recordNodeList) {
            recordNode.addToTotalWage(wage);
        }
    }

    public void addRecordNode(NewRecordNode recordNode) {
        recordNodeList.add(recordNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewValueNode that = (NewValueNode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
