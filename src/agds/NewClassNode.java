package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 25.03.2016.
 */
public class NewClassNode extends NewNode {

    private String className;
    private List<NewRecordNode> recordNodeList;

    public NewClassNode(String className) {
        this.className = className;
        this.recordNodeList = new ArrayList<>();
    }

    public void addRecordNode(NewRecordNode recordNode) {
        if (!recordNodeList.contains(recordNode))
            recordNodeList.add(recordNode);
    }

    public List<NewRecordNode> getRecordNodeList() {
        return recordNodeList;
    }

    public String getClassName() {
        return className;
    }

    public void sortNodes() {
        Collections.sort(recordNodeList, Collections.<NewRecordNode>reverseOrder());
    }

    public void resetRecordNodes() {
        for (NewRecordNode recordNode : recordNodeList)
            recordNode.onResetValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewClassNode that = (NewClassNode) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }
}
