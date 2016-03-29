package knn;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KNNClassifableObject object = (KNNClassifableObject) o;
        return Objects.equals(name, object.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
