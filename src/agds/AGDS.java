package agds;

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

    public List<AtributeNode> atributeNodes;
    public Map<String, ClassValueNode> classValues;

    public AGDS() {
        this.atributeNodes = new ArrayList<>();
        this.classValues = new HashMap<>();
        atributeNodes.add(new AtributeNode("leaf-length"));
        atributeNodes.add(new AtributeNode("leaf-width"));
        atributeNodes.add(new AtributeNode("petal-length"));
        atributeNodes.add(new AtributeNode("petal-width"));
    }

    public void readFromFileClassAtFirst(List<AtributeNode> atributeNodes, File file) {
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String nextObjectLine;
            int itemCounter = 0;
            while ((nextObjectLine = bufferedReader.readLine()) != null) {
                String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                double[] objectDoubleValues = new double[objectRawValues.length - 1];
                String className = objectRawValues[objectRawValues.length - 1];

                if(!classValues.containsKey(className)) {
                    ClassValueNode classValueNode = new ClassValueNode(className);
                    classValues.put(className, classValueNode);
                }

                ClassValueNode classValueNode = classValues.get(className);
                RecordNode recordNode = new RecordNode("Record " + String.valueOf(itemCounter), classValueNode);
                classValueNode.addNode(recordNode);


                for (int i = 0; i < objectRawValues.length - 1; i++) {
                    objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();
                    ValueNode valueNode = new ValueNode(objectDoubleValues[i], recordNode);
                    int indexValue = atributeNodes.get(i).getNodesList().indexOf(valueNode);

                    if (indexValue == -1)
                        atributeNodes.get(i).addNode(valueNode);
                    else {
                        Node existingValueNode = atributeNodes.get(i).getNodesList().get(indexValue);
                        existingValueNode.addNode(recordNode);
                    }
                }
                itemCounter++;
            }
            sortAttributeNodes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void runAlgorithm() {
        calculateWages();
        findSimilarities();
    }

    private void calculateWages() {
        double [] values = {6.5, 3.2, 5.1, 2};

        for (AtributeNode node : atributeNodes) {
            ValueNode searchedValue = new ValueNode(values[atributeNodes.indexOf(node)], null);
            int indexFromBinarySearch = findClosestAttributeValue(node, searchedValue);
            node.calculateWages(indexFromBinarySearch);
        }
    }

    private void findSimilarities() {
        for(String keyName : classValues.keySet()) {
            classValues.get(keyName).sortNodes();
            System.out.println("Similarity for: " + keyName + " :" + classValues.get(keyName).getSimilarity());
        }
    }

    private int findClosestAttributeValue(Node atributeNode, ValueNode searchedValue) {
        int foundIndex = Collections.binarySearch(atributeNode.getNodesList(), searchedValue);
        if(foundIndex < 0) {
            int fixedIndex = - foundIndex - 1;
            if(atributeNode instanceof AtributeNode && fixedIndex > 0 && fixedIndex < atributeNode.getNodesList().size()) {
                double lhsValue = (double) ((AtributeNode)atributeNode).getNodesList().get(fixedIndex - 1).getValue();
                double rhsValue = (double) ((AtributeNode)atributeNode).getNodesList().get(fixedIndex).getValue();

                foundIndex = Math.abs(lhsValue - searchedValue.getValue()) < Math.abs(rhsValue - searchedValue.getValue()) ? fixedIndex - 1 : fixedIndex;
            }
            else if (fixedIndex == 0)
                foundIndex = fixedIndex;
            else
                foundIndex = fixedIndex - 1;
            foundIndex = fixedIndex;
        }
        return foundIndex;
    }

    private void sortAttributeNodes() {
        for (AtributeNode atributeNode : atributeNodes)
            atributeNode.sortNodes();
    }

    public static void main(String[] args) {
        AGDS agds = new AGDS();
        agds.readFromFileClassAtFirst(agds.atributeNodes, new File(agds.IRIS_DATA_PATH));
        System.out.println("Founded classes: " + agds.classValues.size());
        System.out.println("Founded attr #1: " + agds.atributeNodes.get(0).getNodesList().size());

        for (Node valueNode : agds.atributeNodes.get(0).getNodesList()) {
            System.out.println("Next attr value: " + valueNode.getValue() + " Elements: " + valueNode.getNodesList().size());
        }
        agds.runAlgorithm();
    }
}
