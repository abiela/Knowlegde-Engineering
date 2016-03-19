package agds;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class RecordNode extends Node<String> {

    public RecordNode(String name, ClassValueNode classNode) {
        super(name);
        addNode(classNode);
    }
}
