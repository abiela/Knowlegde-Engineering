package data_mining;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 06.03.2016.
 */
public class TransactionReader {

    public static final String TRANSACTION_DATA_FILEPATH = "Transaction.txt";

    private static final String WHITESPACE = " ";

    public static List<Transaction> readDataFromFile(DataMining dataMining, File file) {
        List<Transaction> transactionList = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(new File(TRANSACTION_DATA_FILEPATH));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String nextTransaction;
            while ((nextTransaction = bufferedReader.readLine()) != null) {
                Transaction transaction = new Transaction(new Itemset(nextTransaction.split(WHITESPACE)));
                transactionList.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataMining.transactionList.put(file.getName(), transactionList);
        return transactionList;
    }
}
