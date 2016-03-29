package knn;

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

    /**
     * Loading next candidate from standard input.
     * @param dataModelKey - dataModel raw file name
     * @return
     */
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

    /**
     * Getting object list from datamodels map.
     * @param dataModelKey
     * @return
     */
    public List<KNNClassifableObject> getObjectList(String dataModelKey) {
        if(!classifableDataMap.containsKey(dataModelKey))
            throw new IllegalArgumentException("Data model hasn't been read yet.");

        return classifableDataMap.get(dataModelKey);
    }

    /**
     * Loading k parameter, used in KNN method.
     * @return
     */
    public int loadKValue() {
        System.out.println("Load K value: ");
        return scanner.nextInt();
    }

    /**
     * Loading weighted mode, used in KNN method.
     * @return
     */
    public boolean loadMode() {
        System.out.println("Weighted mode: ");
        return scanner.nextBoolean();
    }

    /**
     * Classifying selected objectss by using KNN method.
     * @param objectList - learning set
     * @param objectCandidate - object selected for classyfing
     * @param kValue - KNN k parameter value
     * @param weightedMode - select weightedMode
     * @return
     */
    public String classifyObject(List<KNNClassifableObject> objectList, final KNNClassifableObject objectCandidate, int kValue, boolean weightedMode) {

        String objectCandidateName = new String();

        //Sort elements using euclidean distance
        Collections.sort(objectList, new Comparator<KNNClassifableObject>() {
            @Override
            public int compare(KNNClassifableObject o1, KNNClassifableObject o2) {
                double distanceDiff = MathUtils.calculateEuclideanDistance(o1, objectCandidate) - MathUtils.calculateEuclideanDistance(o2, objectCandidate);
                return (int) Math.signum(distanceDiff);
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
//        objectList.add(objectCandidate);
        return objectCandidateName;
    }

    /**
     * Helper class facilitating weighted mode algorithm implementation.
     */
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

    /**
     * K Fold cross validation.
     * @param key
     * @param kFoldValue
     * @return
     */
    private double runCrossValidation(String key, int kFoldValue) {

        //Reference to appropriate list of objects
        List<KNNClassifableObject> classifableObjectList = classifableDataMap.get(key);
        int validationSetSize = classifableObjectList.size() / kFoldValue;

        int totalValidations = 0;
        int correctValidations = 0;

        //Increment kFoldValue Times
        for (int i = 0; i < kFoldValue; i++) {

            //Divide dataset into learning and validation part
            List<KNNClassifableObject> learningSet = new LinkedList<>();
            List<KNNClassifableObject> validationSet = new LinkedList<>();

            for (int j = 0; j < classifableObjectList.size(); j++) {
                if((j % kFoldValue) == i)
                    validationSet.add(classifableObjectList.get(j));
                else
                    learningSet.add(classifableObjectList.get(j));
            }

            totalValidations = totalValidations + validationSet.size();

            //Run cross validation by classifying next validation objects
            for (KNNClassifableObject validationObject : validationSet) {
                String classificationResult = classifyObject(learningSet, validationObject, 6, false);
                if (validationObject.getName().equals(classificationResult))
                    correctValidations++;
            }
        }

        return correctValidations/totalValidations;
    }

    public static void main(String[] args) {
        KNN knn = new KNN();

        //IrisData simulation
        KNNFileReader.readFromFileClassAtLast(knn, new File(KNNFileReader.IRIS_DATA_PATH));
        knn.runCrossValidation(KNNFileReader.IRIS_DATA_PATH, 10);

        //WineData simulation
        KNNFileReader.readFromFileClassAtFirst(knn, new File(KNNFileReader.WINE_DATA_PATH));
        knn.runCrossValidation(KNNFileReader.WINE_DATA_PATH, 178);
    }
}
