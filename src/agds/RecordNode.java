package agds;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class RecordNode extends Node<String> {

    private Double averageWage = 0.0;

    public RecordNode(String name, ClassValueNode classNode) {
        super(name);
        addNode(classNode);
    }

    public void addToAverageWage(double wage) {
        averageWage = averageWage + wage;
    }

    public Double getAverageWage() {
        return averageWage;
    }

    @Override
    public int compareTo(Node<String> o) {
        double lhsValue = getAverageWage();
        double rhsValue = 0.0;
        if(o instanceof RecordNode) {
            rhsValue = ((RecordNode) o).getAverageWage();
        }

        if (lhsValue < rhsValue)
            return -1;

        else if (lhsValue == rhsValue)
            return 0;

        return 1;
    }
}
