package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class RecordNode implements Comparable<RecordNode>, Resetable {

    private String name;
    private Double totalWage;
    private ClassNode classNode;
    private List<ValueNode> valueNodeList;

    /**
     * Constructor, getter & setter.
     */

    public RecordNode(String name) {
        this.name = name;
        this.totalWage = 0.0d;
        this.valueNodeList = new ArrayList<>();
    }

    public void setValueNodeList(List<ValueNode> valueNodeList) {
        this.valueNodeList = valueNodeList;
    }

    public String getName() {
        return name;
    }

    public Double getRawTotalWage() {return totalWage;}

    public Double getTotalWage() {
        return totalWage/valueNodeList.size();
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    @Override
    public int compareTo(RecordNode o) {
        return totalWage.compareTo(o.getRawTotalWage());
    }

    @Override
    public void onResetValue() {
        totalWage = 0.0d;
    }

    /**
     * Adding new class node.
     * @param classNode
     */
    public void addClassNode(ClassNode classNode) {
        if (this.classNode == null)
            this.classNode = classNode;
    }

    /**
     * Adding next value node to total wage value.
     * @param wage
     */
    public void addToTotalWage(Double wage) {
        totalWage = totalWage + wage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordNode that = (RecordNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RecordNode{" +
                "name='" + name + '\'' +
                ", totalWage=" + totalWage +
                ", classNode=" + classNode +
                '}';
    }
}
