package agds;

import java.util.Collections;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 20.03.2016.
 */
public class ClassValueNode extends Node<String> {

    double similarity;

    public ClassValueNode(String name) {
        super(name);
    }

    public void sortNodes() {
        Collections.sort(getNodesList());
        Node firstNode = getNodesList().get(getNodesList().size() - 1);
        if (firstNode instanceof RecordNode) {
            similarity = ((RecordNode) firstNode).getAverageWage()/4;
        }
    }

    public double getSimilarity() {
        return similarity;
    }
}
