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

    public List<NewAttributeNode> param;
    public Map<String, NewClassNode> newClassValues;

    public AGDS() {
        this.param = new ArrayList<>();
        this.newClassValues = new HashMap<>();
    }

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
                        param.add(new NewAttributeNode(objectRawValues[i]));
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
                        NewClassNode classValueNode = new NewClassNode(className);
                        newClassValues.put(className, classValueNode);
                    }

                    //Matching record node to class node.
                    NewClassNode classValueNode = newClassValues.get(className);
                    NewRecordNode recordNode = new NewRecordNode("Record " + String.valueOf(itemCounter));
                    recordNode.addClassNode(classValueNode);
                    classValueNode.addRecordNode(recordNode);

                    List<NewValueNode> valueNodeList = new ArrayList<>();

                    //Adding value nodes.
                    for (int i = 0; i < objectRawValues.length - 1; i++) {
                        objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();

                        NewValueNode newValueNode = new NewValueNode(objectDoubleValues[i]);
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

    public void launchAGDSStructureFromFile(File file) {
        readFromFile(file);
        prepareValueNodes();
        resetNodesWages();
    }

    public NewRecordNode findMostSimilarElement(double[] scannedValues) {
        NewClassNode mostSimilarClass = null;
        resetNodesWages();

        for (NewAttributeNode attributeNode : param) {
            int foundIndex = findClosestAttributeValueIndex(attributeNode, new NewValueNode(scannedValues[param.indexOf(attributeNode)]));
            attributeNode.calculateWages(foundIndex);
        }

        for (NewClassNode newClassNode : newClassValues.values()) {
            newClassNode.sortNodes();

            if (mostSimilarClass == null)
                mostSimilarClass = newClassNode;

            else if (newClassNode.getRecordNodeList().get(0).getTotalWage() > mostSimilarClass.getRecordNodeList().get(0).getTotalWage())
                mostSimilarClass = newClassNode;
        }
        return mostSimilarClass.getRecordNodeList().get(0);
    }

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

    private int findClosestAttributeValueIndex(NewAttributeNode attributeNode, NewValueNode searchedValue) {
        List<NewValueNode> newValueNodes = attributeNode.getValueNodeList();
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

    private void prepareValueNodes() {
        for (NewAttributeNode attributeNode : param) {
            attributeNode.sortValueNodes();
        }
    }

    private void resetNodesWages() {
        for (NewAttributeNode attributeNode : param)
            attributeNode.resetValueNodes();

        for (NewClassNode newClassNode : newClassValues.values())
            newClassNode.resetRecordNodes();
    }

    public static void main(String[] args) {
        AGDS irisAgds = new AGDS();
        irisAgds.launchAGDSStructureFromFile(new File(IRIS_DATA_PATH));
        NewRecordNode mostSimilarClassNode = irisAgds.findMostSimilarElement(irisAgds.loadNextDoubleRecord());
        System.out.println("Most similar class: " + mostSimilarClassNode.getClassNode().getClassName() + "(" + mostSimilarClassNode.getTotalWage() + ")");
    }
}
