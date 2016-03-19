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
        this.atributeNodes = new ArrayList<>();
        this.classValues = new HashSet<>();
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
            while((nextObjectLine = bufferedReader.readLine()) != null) {
                String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                double[] objectDoubleValues = new double[objectRawValues.length - 1];

                ClassValueNode classValueNode = new ClassValueNode(objectRawValues[objectRawValues.length - 1]);
                classValues.add(classValueNode);
                RecordNode recordNode = new RecordNode("Record " + String.valueOf(itemCounter), classValueNode);

                for (int i = 0; i < objectRawValues.length - 1; i++) {
                    objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();
                    ValueNode valueNode = new ValueNode(objectDoubleValues[i], recordNode);
                    atributeNodes.get(i).addNode(valueNode);
                }
                itemCounter++;
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
    }

    public static void main(String[] args) {
        AGDS agds = new AGDS();
        agds.readFromFileClassAtFirst(agds.atributeNodes, new File(agds.IRIS_DATA_PATH));
        System.out.println("Founded classes: " + agds.classValues.size());
        System.out.println("Founded attr #1: " + agds.atributeNodes.get(0).getNodesList().size());
    }
}
