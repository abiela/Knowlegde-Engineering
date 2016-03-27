package knn;

/**
 * utils used in complex calculation.
 * Created by biela.arek@gmail.com (Arek Biela) on 05.03.2016.
 */
public class MathUtils {

    public static double calculateEuclideanDistance(KNNClassifableObject lhs, KNNClassifableObject rhs) {
        double variableDistanceSum = 0;
        for (int i = 0; i < lhs.getComparableValues().length; i++) {
            variableDistanceSum = variableDistanceSum + Math.pow(lhs.getComparableValues()[i] - rhs.getComparableValues()[i], 2);
        }
        return Math.sqrt(variableDistanceSum);
    }
}
