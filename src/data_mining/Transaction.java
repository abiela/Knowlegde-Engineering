package data_mining;

/**
 * Abstract class for each transaction type.
 * Created by biela.arek@gmail.com (Arek Biela) on 06.03.2016.
 */
public class Transaction {

    private Itemset transactionItemset;

    public Transaction(Itemset transactionItemset) {
        this.transactionItemset = transactionItemset;
    }

    public boolean containsAtLeastOneItem(Itemset itemset) {
        for (String item : itemset.getItems()) {
            if(transactionItemset.getItems().contains(item))
                return true;
        }
        return false;
    }

    public Itemset getTransactionItemset() {
        return transactionItemset;
    }
}
