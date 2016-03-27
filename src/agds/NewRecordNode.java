package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class NewRecordNode implements Comparable<NewRecordNode>, Resetable {

    private String name;
    private Double totalWage;
    private NewClassNode classNode;
    private List<NewValueNode> valueNodeList;

    public NewRecordNode(String name) {
        this.name = name;
        this.totalWage = 0.0d;
        this.valueNodeList = new ArrayList<>();
    }

    public void setValueNodeList(List<NewValueNode> valueNodeList) {
        this.valueNodeList = valueNodeList;
    }

    public String getName() {
        return name;
    }

    public Double getTotalWage() {
        return totalWage/valueNodeList.size();
    }

    public NewClassNode getClassNode() {
        return classNode;
    }

    @Override
    public int compareTo(NewRecordNode o) {
        return totalWage.compareTo(o.getTotalWage());
    }

    @Override
    public void onResetValue() {
        totalWage = 0.0d;
    }

    public void addClassNode(NewClassNode classNode) {
        if (this.classNode == null)
            this.classNode = classNode;
    }

    public void addToTotalWage(Double wage) {
        totalWage = totalWage + wage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewRecordNode that = (NewRecordNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
