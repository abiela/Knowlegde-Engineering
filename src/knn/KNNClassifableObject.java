package knn;

/**
 * Model of object that can be classified.
 * Created by biela.arek@gmail.com (Arek Biela) on 12.03.2016.
 */
public class KNNClassifableObject {

    private double[] comparableValues;
    private String name;

    public KNNClassifableObject(double[] comparableValues) {
        this.comparableValues = comparableValues;
    }

    public KNNClassifableObject(double[] comparableValues, String name) {
        this.comparableValues = comparableValues;
        this.name = name;
    }

    public double[] getComparableValues() {
        return comparableValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
