package data_mining;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 06.03.2016.
 */
public class AssociationRule {

    private Itemset antecedentItemset;
    private Itemset consequentItemset;

    public AssociationRule(Itemset antecedentItemset, Itemset consequentItemset) {
        this.antecedentItemset = antecedentItemset;
        this.consequentItemset = consequentItemset;
    }

    public Itemset getAntecedentItemset() {
        return antecedentItemset;
    }

    public Itemset getConsequentItemset() {
        return consequentItemset;
    }
}
