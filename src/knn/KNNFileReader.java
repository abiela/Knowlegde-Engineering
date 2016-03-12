package knn;

import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * File reader for KNN text input files.
 * Created by biela.arek@gmail.com (Arek Biela) on 12.03.2016.
 */
public class KNNFileReader {

    private static final String TAB_WHITESPACE = "\t";

    public static final String IRIS_DATA_PATH = "IrisData.txt";
    public static final String WINE_DATA_PATH = "Wine.txt";

    public static List<KNNClassifableObject> readFromFileClassAtLast(KNN knn, File file) {
        List<KNNClassifableObject> knnClassifableObjects = new ArrayList<>();
        NumberFormat commaDelimiterFormat = NumberFormat.getInstance(Locale.GERMAN);

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String nextObjectLine;
            while((nextObjectLine = bufferedReader.readLine()) != null) {
                String[] objectRawValues = nextObjectLine.split(TAB_WHITESPACE);
                double[] objectDoubleValues = new double[objectRawValues.length - 1];
                for (int i = 0; i < objectRawValues.length - 1; i++) {
                    objectDoubleValues[i] = commaDelimiterFormat.parse(objectRawValues[i]).doubleValue();
                }
                KNNClassifableObject nextObject = new KNNClassifableObject(objectDoubleValues, objectRawValues[objectRawValues.length - 1]);
                knnClassifableObjects.add(nextObject);
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

        System.out.print(file.getName());
        knn.classifableDataMap.put(file.getName(), knnClassifableObjects);

        return knnClassifableObjects;
    }

    public static List<KNNClassifableObject> readFromFileClassAtFirst(KNN knn, File file) {
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
                KNNClassifableObject nextObject = new KNNClassifableObject(objectDoubleValues, objectRawValues[0]);
                knnClassifableObjects.add(nextObject);
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

        knn.classifableDataMap.put(file.getName(), knnClassifableObjects);
        return knnClassifableObjects;
    }
}
