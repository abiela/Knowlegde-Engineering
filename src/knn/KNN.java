package knn;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.io.File;
import java.util.*;

/**
 * K Nearest Neighbour method class.
 * Created by biela.arek@gmail.com (Arek Biela) on 05.03.2016.
 */
public class KNN {

    public Map<String, List<KNNClassifableObject>> classifableDataMap;

    private Scanner scanner;

    public KNN() {
        classifableDataMap = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public KNNClassifableObject loadNextCandidate(String dataModelKey) {
        if(!classifableDataMap.containsKey(dataModelKey))
            throw new IllegalArgumentException("Data model hasn't been read yet.");

        double[] candidateValues = new double[classifableDataMap.get(dataModelKey).get(0).getComparableValues().length];

        for (int i = 0; i < candidateValues.length; i++) {
            System.out.print("Load next value(" + (i + 1) + "): ");
            candidateValues[i] = scanner.nextDouble();
        }
        return new KNNClassifableObject(candidateValues);
    }

    public int loadKValue() {
        System.out.println("Load K value: ");
        return scanner.nextInt();
    }

    public boolean loadMode() {
        System.out.println("Weighted mode: ");
        return scanner.nextBoolean();
    }

    public String classifyObject(String dataModelKey, final KNNClassifableObject objectCandidate, int kValue, boolean weightedMode) {

        if(!classifableDataMap.containsKey(dataModelKey))
            throw new IllegalArgumentException("Data model hasn't been read yet.");

        List<KNNClassifableObject> objectList = classifableDataMap.get(dataModelKey);

        String objectCandidateName = new String();

        //Sort elements using euclidean distance
        Collections.sort(objectList, new Comparator<KNNClassifableObject>() {
            @Override
            public int compare(KNNClassifableObject o1, KNNClassifableObject o2) {
                return (int) Math.signum(MathUtils.calculateEuclideanDistance(o1, objectCandidate) - MathUtils.calculateEuclideanDistance(o2, objectCandidate));
            }
        });

        List<KNNClassifableObject> neighbourObjectList = objectList.subList(0, kValue);

        //KNN weigthed mode
        if (weightedMode) {
            Map<String, KNNWeightedHelper> votableObjectMap = new HashMap<>();
            for (KNNClassifableObject object : neighbourObjectList) {
                String objectKeyname = object.getName();
                if (votableObjectMap.containsKey(objectKeyname))
                    votableObjectMap.put(objectKeyname, votableObjectMap.get(objectKeyname).refreshValues(MathUtils.calculateEuclideanDistance(objectCandidate, object)));
                else
                    votableObjectMap.put(objectKeyname, new KNNWeightedHelper(MathUtils.calculateEuclideanDistance(objectCandidate, object),1));
            }

            double lowestVote = 0d;
            for (String objectName : votableObjectMap.keySet()) {
                System.out.println("Vote value for: " + objectName + " is: " + votableObjectMap.get(objectName).countVoteValue());

                if (votableObjectMap.get(objectName).countVoteValue() < lowestVote || lowestVote == 0) {
                    objectCandidateName = objectName;
                    lowestVote = votableObjectMap.get(objectName).countVoteValue();
                }
            }
        }

        //KNN classic mode
        else {
            Map<String, Integer> objectFrequenceMap = new HashMap<>();

            for (KNNClassifableObject object : neighbourObjectList) {
                String objectKeyname = object.getName();
                if (objectFrequenceMap.containsKey(objectKeyname)) {
                    int currentAmount = objectFrequenceMap.get(objectKeyname);
                    objectFrequenceMap.put(objectKeyname, ++currentAmount);
                } else {
                    objectFrequenceMap.put(objectKeyname, 1);
                }
            }

            int moreFrequentElement = 0;
            for (String objectName : objectFrequenceMap.keySet()) {
                if (objectFrequenceMap.get(objectName) > moreFrequentElement) {
                    moreFrequentElement = objectFrequenceMap.get(objectName);
                    objectCandidateName = objectName;
                }
            }
        }

        objectCandidate.setName(objectCandidateName);
        objectList.add(objectCandidate);
        classifableDataMap.put(dataModelKey, objectList);
        return objectCandidateName;
    }

    class KNNWeightedHelper {
        private Double distanceSum;
        private Integer amount;

        public KNNWeightedHelper(Double distanceSum, Integer amount) {
            this.distanceSum = distanceSum;
            this.amount = amount;
        }

        private double countVoteValue() {
            return (double) distanceSum/amount;
        }

        private KNNWeightedHelper refreshValues(Double distanceSum) {
            this.distanceSum =+ distanceSum;
            amount++;
            return this;
        }
    }

    public static void main(String[] args) {
        KNN knn = new KNN();
        KNNFileReader.readFromFileClassAtLast(knn, new File(KNNFileReader.IRIS_DATA_PATH));
        System.out.println("Number of objects: " + knn.classifableDataMap.get(KNNFileReader.IRIS_DATA_PATH).size());

        System.out.println("Candidate has been classified as: " + knn.classifyObject(KNNFileReader.IRIS_DATA_PATH, knn.loadNextCandidate(KNNFileReader.IRIS_DATA_PATH), knn.loadKValue(), knn.loadMode()));
    }
}
