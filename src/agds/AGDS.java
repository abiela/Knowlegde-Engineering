package agds;

import utils.Utils;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 14.03.2016.
 */
public class AGDS {

    private static final String TAB_WHITESPACE = "\t";
    public static final String IRIS_DATA_PATH = "IrisData.txt";

    public List<AttributeNode> param;
    public Map<String, ClassNode> newClassValues;

    public AGDS() {
        this.param = new ArrayList<>();
        this.newClassValues = new HashMap<>();
    }

    /**
     * Reading elements from file.
     *
     * @param file
     */
    private void readFromFile(File file) {
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);
        System.out.println(Utils.getCurrentTimestamp() + "AGDS: Reading file: " + file.getPath() + " started.");

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String nextObjectLine;


            int itemCounter = 0;
            while ((nextObjectLine = bufferedReader.readLine()) != null) {

                //Reading atribute values from the first line.
                if (itemCounter == 0) {
                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        param.add(new AttributeNode(objectRawValues[i]));
                    }
                    itemCounter++;
                }

                //Reading saved records to value, record and class node.
                else {

                    String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                    double[] objectDoubleValues = new double[objectRawValues.length - 1];
                    String className = objectRawValues[objectRawValues.length - 1];

                    //If read class doesn't exist in Map, put it there.
                    if (!newClassValues.containsKey(className)) {
                        ClassNode classValueNode = new ClassNode(className);
                        newClassValues.put(className, classValueNode);
                    }

                    //Matching record node to class node.
                    ClassNode classValueNode = newClassValues.get(className);
                    RecordNode recordNode = new RecordNode("Record " + String.valueOf(itemCounter));
                    recordNode.addClassNode(classValueNode);
                    classValueNode.addRecordNode(recordNode);

                    List<ValueNode> valueNodeList = new ArrayList<>();

                    //Adding value nodes.
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();

                        ValueNode newValueNode = new ValueNode(objectDoubleValues[i]);
                        newValueNode.addRecordNode(recordNode);
                        newValueNode.setAttributeNode(param.get(i));

                        int indexValue = param.get(i).getValueNodeList().indexOf(newValueNode);
                        param.get(i).addNode(newValueNode);
                        valueNodeList.add(newValueNode);
                    }
                    recordNode.setValueNodeList(valueNodeList);
                    itemCounter++;
                }

            }
            System.out.println(Utils.getCurrentTimestamp() + "AGDS: Reading finished.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Running AGDS Structure from file.
     *
     * @param file
     */
    public void launchAGDSStructureFromFile(File file) {
        readFromFile(file);
        prepareValueNodes();
        resetNodesWages();
    }

    /**
     * Database methods
     */

    /**
     * Find the most similar elements based on scanned record value and elements quantity.
     *
     * @param scannedValues  - values to be checked
     * @param elementsAmount - number of elements that we would like to return
     * @return
     */
    public List<RecordNode> findMostSimilarElementsTotalWage(double[] scannedValues, int elementsAmount) {
        resetNodesWages();

        for (AttributeNode attributeNode : param) {
            int foundIndex = findClosestAttributeValueIndex(attributeNode, new ValueNode(scannedValues[param.indexOf(attributeNode)]));
            attributeNode.calculateWages(foundIndex);
        }

        List<RecordNode> recordNodeList = new ArrayList<>();
        for (ClassNode newClassNode : newClassValues.values()) {
            recordNodeList.addAll(newClassNode.getRecordNodeList());
        }

        Collections.sort(recordNodeList, Collections.<RecordNode>reverseOrder());
        return recordNodeList.subList(0, elementsAmount);
    }

    /**
     * Find the least similar elements based on scanned record value and elements quantity
     * @param scannedValues - values to be checked
     * @param elementsAmount - number of least similar elements
     * @return
     */
    public List<RecordNode> findLeastSimilarElementsTotalWage(double[] scannedValues, int elementsAmount) {
        resetNodesWages();

        for (AttributeNode attributeNode : param) {
            int foundIndex = findClosestAttributeValueIndex(attributeNode, new ValueNode(scannedValues[param.indexOf(attributeNode)]));
            attributeNode.calculateWages(foundIndex);
        }

        List<RecordNode> recordNodeList = new ArrayList<>();
        for (ClassNode newClassNode : newClassValues.values()) {
            recordNodeList.addAll(newClassNode.getRecordNodeList());
        }

        Collections.sort(recordNodeList);
        return recordNodeList.subList(0, elementsAmount);
    }

    /**
     * Find the most similar elements based on fixed similarity rate.
     * @param scannedValues - value to be checked
     * @param similarityRate - threshold similarity value (range 0 - 1) - percentage rate
     * @return
     */
    public List<RecordNode> findAboveSimilarityRate (double [] scannedValues, float similarityRate) {
        resetNodesWages();

        for (AttributeNode attributeNode : param) {
            int foundIndex = findClosestAttributeValueIndex(attributeNode, new ValueNode(scannedValues[param.indexOf(attributeNode)]));
            attributeNode.calculateWages(foundIndex);
        }

        List<RecordNode> recordNodeList = new ArrayList<>();
        for (ClassNode newClassNode : newClassValues.values()) {
            recordNodeList.addAll(newClassNode.getRecordNodeList());
        }

        Iterator<RecordNode> recordNodeIterator = recordNodeList.iterator();
        while (recordNodeIterator.hasNext()) {
            if (recordNodeIterator.next().getTotalWage() < similarityRate)
                recordNodeIterator.remove();
        }

        return recordNodeList;
    }

    /**
     * Find the most similar elements based on fixed similarity rate.
     * @param scannedValues - value to be checked
     * @param similarityRate - threshold similarity value
     * @return
     */
    public List<RecordNode> findBelowSimilarityRate (double [] scannedValues, float similarityRate) {
        resetNodesWages();

        for (AttributeNode attributeNode : param) {
            int foundIndex = findClosestAttributeValueIndex(attributeNode, new ValueNode(scannedValues[param.indexOf(attributeNode)]));
            attributeNode.calculateWages(foundIndex);
        }

        List<RecordNode> recordNodeList = new ArrayList<>();
        for (ClassNode newClassNode : newClassValues.values()) {
            recordNodeList.addAll(newClassNode.getRecordNodeList());
        }

        Iterator<RecordNode> recordNodeIterator = recordNodeList.iterator();
        while (recordNodeIterator.hasNext()) {
            if (recordNodeIterator.next().getTotalWage() > similarityRate)
                recordNodeIterator.remove();
        }

        return recordNodeList;
    }

    /**
     * Find the most similar elements based on selected attribute's value range.
     *
     * @param attributeNode - attribute to be checked.
     * @param minValue      - min attribute value.
     * @param maxValue      - max attribute value.
     * @return - list of records that match selected criteria.
     */
    public List<RecordNode> findInAttributeRange(String attributeNode, double minValue, double maxValue) {

        if (param.indexOf(new AttributeNode(attributeNode)) == -1)
            throw new IllegalArgumentException("Selected argument does not exist.");

        AttributeNode selectedAttributeNode = param.get(param.indexOf(new AttributeNode(attributeNode)));
        Set<RecordNode> recordsMatchingCriteria = new HashSet<>();

        for (ValueNode valueNode : selectedAttributeNode.getValueNodeList()) {
            if (valueNode.getValue() >= minValue && valueNode.getValue() <= maxValue)
                recordsMatchingCriteria.addAll(valueNode.getRecordNodeList());
        }

        return new ArrayList<>(recordsMatchingCriteria);
    }

    /**
     * Find max value of selected attribute.
     * @param attributeNode - attribute to be checked
     * @return
     */
    public List<RecordNode> findAttributeMax(String attributeNode) {
        if (param.indexOf(new AttributeNode(attributeNode)) == -1)
            throw new IllegalArgumentException("Selected argument does not exist.");

        AttributeNode selectedAttributeNode = param.get(param.indexOf(new AttributeNode(attributeNode)));
        return selectedAttributeNode.getMaxValueNode().getRecordNodeList();
    }

    /**
     * Find min value of selected attribute.
     * @param attributeNode - attribute to be checked.
     * @return
     */
    public List<RecordNode> findAttributeMin(String attributeNode) {
        if (param.indexOf(new AttributeNode(attributeNode)) == -1)
            throw new IllegalArgumentException("Selected argument does not exist.");

        AttributeNode selectedAttributeNode = param.get(param.indexOf(new AttributeNode(attributeNode)));
        return selectedAttributeNode.getMinValueNode().getRecordNodeList();
    }

    /**
     * Sort database by selected attribute
     * @param attributeNode - attribute values to be sorted
     * @return
     */
    public List<RecordNode> sortByAttribute (String attributeNode, Sort sort) {
        if (param.indexOf(new AttributeNode(attributeNode)) == -1)
            throw new IllegalArgumentException("Selected argument does not exist.");

        AttributeNode selectedAttributeNode = param.get(param.indexOf(new AttributeNode(attributeNode)));
        selectedAttributeNode.sortValueNodes();

        List<RecordNode> recordNodeList = new ArrayList<>();
        for (ValueNode valueNode : selectedAttributeNode.getValueNodeList()) {
            recordNodeList.addAll(valueNode.getRecordNodeList());
        }

        if (sort.equals(Sort.DESCENDING)) {
            Collections.reverse(recordNodeList);
        }

        return recordNodeList;
    }

    /**
     * Load record based on double values to AGDS structure.
     *
     * @return
     */
    private double[] loadNextDoubleRecord() {
        Scanner scanner = new Scanner(System.in);
        int loadTimes = param.size();
        double[] selectedValues = new double[loadTimes];

        for (int i = 0; i < loadTimes; i++) {
            System.out.println("Next value: (" + i + "):");
            selectedValues[i] = scanner.nextDouble();
        }
        return selectedValues;
    }

    /**
     * Finding the closest attribute value based on binary search algorithm.
     *
     * @param attributeNode
     * @param searchedValue
     * @return
     */
    private int findClosestAttributeValueIndex(AttributeNode attributeNode, ValueNode searchedValue) {
        List<ValueNode> newValueNodes = attributeNode.getValueNodeList();
        int foundIndex = Collections.binarySearch(newValueNodes, searchedValue);

        if (foundIndex < 0) {
            int fixedIndex = -foundIndex - 1;
            if (fixedIndex > 0 && fixedIndex < newValueNodes.size()) {
                double lhsValue = newValueNodes.get(fixedIndex - 1).getValue();
                double rhsValue = newValueNodes.get(fixedIndex).getValue();

                foundIndex = Math.abs(lhsValue - searchedValue.getValue()) < Math.abs(rhsValue - searchedValue.getValue()) ? fixedIndex - 1 : fixedIndex;
            } else if (fixedIndex == 0)
                foundIndex = fixedIndex;
            else
                foundIndex = fixedIndex - 1;
        }
        return foundIndex;
    }

    /**
     * Sorting value nodes in AGDS structure.
     */
    private void prepareValueNodes() {
        for (AttributeNode attributeNode : param) {
            attributeNode.sortValueNodes();
        }
    }

    /**
     * Reset node wages - used before next calcuation.
     */
    private void resetNodesWages() {
        for (AttributeNode attributeNode : param)
            attributeNode.resetValueNodes();

        for (ClassNode newClassNode : newClassValues.values())
            newClassNode.resetRecordNodes();
    }

    public static void main(String[] args) {
        AGDS irisAgds = new AGDS();
        irisAgds.launchAGDSStructureFromFile(new File(IRIS_DATA_PATH));

//        double[] loadValues = irisAgds.loadNextDoubleRecord();
//        List<RecordNode> mostSimilarNodes = irisAgds.findMostSimilarElementsTotalWage(loadValues, 10);
//        List<RecordNode> leastSimilarNodes = irisAgds.findLeastSimilarElementsTotalWage(loadValues, 10);
//
//        for (RecordNode mostSimilarNode : mostSimilarNodes) {
//            System.out.println(mostSimilarNode.toString());
//        }
//
//        for (RecordNode leastSimilarNode : leastSimilarNodes) {
//            System.out.println(leastSimilarNode.toString());
//        }

        List<RecordNode> sortNodes = irisAgds.sortByAttribute("leaf-length", Sort.DESCENDING);
        for (RecordNode sortNode : sortNodes) {
            System.out.println(sortNode.toString());
        }

//        List<RecordNode> mostSimilarNodes = irisAgds.findMostSimilarElementsTotalWage(irisAgds.loadNextDoubleRecord(), 5);
//
//        for (RecordNode recordNode : mostSimilarNodes) {
//            System.out.println(String.valueOf(mostSimilarNodes.get(mostSimilarNodes.indexOf(recordNode))) + ". record: " + recordNode.getClassNode().getClassName() + " wage value: " + recordNode.getTotalWage());
//        }

//        List<RecordNode> aboveSimilarityRate = irisAgds.findAboveSimilarityRate(irisAgds.loadNextDoubleRecord(), 0.9f);
//
//        for (RecordNode recordNode : aboveSimilarityRate) {
//            System.out.println(recordNode);
//        }

//        List<RecordNode> belowSimilarityRate = irisAgds.findBelowSimilarityRate(irisAgds.loadNextDoubleRecord(), 0.6f);
//
//        for (RecordNode recordNode : belowSimilarityRate) {
//            System.out.println(recordNode);
//        }

//        List<RecordNode> maxNodes = irisAgds.findAttributeMax("petal-width");
//        for (RecordNode maxNode : maxNodes) {
//            System.out.println(maxNode.toString());
//        }
//        List<RecordNode> inRangeNodes = irisAgds.findInAttributeRange("petal-width", 0.2d, 0.4d);
//
//        for (RecordNode inRangeNode : inRangeNodes) {
//            System.out.println(inRangeNode.toString());
//        }
    }
}

