package data_mining;

import java.io.File;
import java.util.*;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 06.03.2016.
 */
public class DataMining {

    public static final double MINSUP_THRESHOLD = 0.3;
    public static final double CONFIDENCE_THRESHOLD = 0.4;

    public List<Itemset> candidates;
    public Map<String, List<Transaction>> transactionList;

    public DataMining() {
        transactionList = new HashMap<>();
        candidates = new ArrayList<>();
    }

    public double countSupport(String modelKeyname, Itemset itemset) {

        if(!transactionList.containsKey(modelKeyname))
            throw new IllegalArgumentException("Model hasn't been read yet.");

        List<Transaction> transactionList = this.transactionList.get(modelKeyname);
        int supportedTransactions = 0;

        for (Transaction transaction : transactionList) {
            if(transaction.containsAtLeastOneItem(itemset))
                supportedTransactions++;
        }
        return (double) supportedTransactions / transactionList.size();
    }

    public double countConfidence(String modelKeyname, AssociationRule associationRule) {
        if(!transactionList.containsKey(modelKeyname))
            throw new IllegalArgumentException("Model hasn't been read yet.");

        List<Transaction> transactionList = this.transactionList.get(modelKeyname);
        int ancedentAmount = 0;
        int consequentAmount = 0;

        for (Transaction transaction : transactionList) {
            if(transaction.getTransactionItemset().getItems().containsAll(associationRule.getAntecedentItemset().getItems())) {
                ancedentAmount++;
                if(transaction.getTransactionItemset().getItems().containsAll(associationRule.getConsequentItemset().getItems()))
                    consequentAmount++;
            }

        }
        return (double) consequentAmount/ancedentAmount;
    }

    public static final void main(String[] args) {
        DataMining dataMining = new DataMining();
        TransactionReader.readDataFromFile(dataMining, new File(TransactionReader.TRANSACTION_DATA_FILEPATH));
        System.out.println("Transaction added: " + dataMining.transactionList.get(TransactionReader.TRANSACTION_DATA_FILEPATH).size());
        Set<String> itemSet = new HashSet<>();
        itemSet.add("kawa");
        itemSet.add("cukier");

        Set<String> itemSet2 = new HashSet<>();
        itemSet2.add("orzeszki");
        AssociationRule associationRule = new AssociationRule(new Itemset(itemSet), new Itemset(itemSet2));
        System.out.println("Counted support: " + dataMining.countSupport(TransactionReader.TRANSACTION_DATA_FILEPATH, new Itemset(itemSet)));
        System.out.println("Counted confidence: " + dataMining.countConfidence(TransactionReader.TRANSACTION_DATA_FILEPATH, associationRule));
    }
}
