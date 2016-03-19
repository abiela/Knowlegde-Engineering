package agds;

import knn.KNNClassifableObject;

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
    public Set<ClassValueNode> classValues;

    public AGDS() {
        //TODO: add atribute nodes names
        this.atributeNodes = new ArrayList<>();
        this.classValues = new HashSet<>();
        atributeNodes.add(new AtributeNode("leaf-length"));
        atributeNodes.add(new AtributeNode("leaf-width"));
        atributeNodes.add(new AtributeNode("petal-length"));
        atributeNodes.add(new AtributeNode("petal-width"));
    }

    public List<KNNClassifableObject> readFromFileClassAtFirst(List<AtributeNode> atributeNodes, File file) {
        List<KNNClassifableObject> knnClassifableObjects = new ArrayList<>();
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String nextObjectLine;
            while((nextObjectLine = bufferedReader.readLine()) != null) {
                String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                double[] objectDoubleValues = new double[objectRawValues.length - 1];
                for (int i = 1; i < objectRawValues.length - 1; i++) {
                    objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();
                }
                for (AtributeNode atributeNode : atributeNodes) {
                    ClassValueNode classValueNode = new ClassValueNode(objectRawValues[objectRawValues.length - 1]);
                    classValues.add(classValueNode);
                }
//                KNNClassifableObject nextObject = new KNNClassifableObject(objectDoubleValues, objectRawValues[0]);
//                knnClassifableObjects.add(nextObject);
            }
        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        catch (ParseException e) {
            e.printStackTrace();
        }

        return knnClassifableObjects;
    }

    public static void main(String[] args) {
        AGDS agds = new AGDS();
        agds.readFromFileClassAtFirst(agds.atributeNodes, new File(agds.IRIS_DATA_PATH));
        System.out.println("Founded classes: " + agds.classValues.size());
    }
}
