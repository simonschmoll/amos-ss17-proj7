package de.fau.amos.virtualledger.android.views.shared.transactionList;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.fau.amos.virtualledger.android.views.transactionOverview.transactionfilter.TransactionFilter;

/**
 * Created by sebastian on 18.06.17.
 */

public class BankTransactionSuplierFilter implements BankTransactionSupplier {

    private BankTransactionSupplier wrappedSupplier;
    private TransactionFilter filter;


    public BankTransactionSuplierFilter(BankTransactionSupplier wrappedSupplier, TransactionFilter filter) {
        this.wrappedSupplier = wrappedSupplier;
        this.filter = filter;
    }


    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> allTransactions = new LinkedList<>(this.wrappedSupplier.getAllTransactions());/** Linked lists supports remove(T<?>)*/
        int sizeBefore = allTransactions.size();
        for (Transaction t : new LinkedList<>(allTransactions)) {
            if (filter.shouldBeRemoved(t)) {
                allTransactions.remove(t);
            }
        }
        logger().info("Filtering Transactions: " + sizeBefore + ">>" + allTransactions.size());
        return allTransactions;
    }

    @Override
    public void onResume() {
        wrappedSupplier.onResume();
    }

    @Override
    public void addDataListeningObject(DataListening observer) {
        wrappedSupplier.addDataListeningObject(observer);
    }

    @Override
    public void deregister(DataListening observer) {
        this.wrappedSupplier.deregister(observer);
    }

    @Override
    public void onPause() {
        wrappedSupplier.onPause();
    }

    private Logger logger() {
        return Logger.getLogger(this.getClass().getCanonicalName() + "{" + this.toString() + " Filter{" + this.filter + "}}");
    }
}
