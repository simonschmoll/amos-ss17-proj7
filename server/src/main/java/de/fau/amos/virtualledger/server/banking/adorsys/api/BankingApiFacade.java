package de.fau.amos.virtualledger.server.banking.adorsys.api;

/**
 *
 * Created by Georg on 18.05.2017.
 */

import de.fau.amos.virtualledger.server.banking.model.BankAccessBankingModel;
import de.fau.amos.virtualledger.server.banking.model.BankAccountBankingModel;
import de.fau.amos.virtualledger.server.banking.model.BankingException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Class that can be used in other controllers to do interaction with banking api.
 */
@ApplicationScoped
public class BankingApiFacade {


    BankingApiBinder binder;

    @Inject
    public BankingApiFacade(BankingApiBinder binder) {
        this.binder = binder;
    }
    // protected empty constructor required for server to init it
    protected BankingApiFacade() { this(null); }




    public void createUser(String userId) throws BankingException
    {
        binder.getUserEndpoint().createUser(userId);
    }

    public List<BankAccessBankingModel> getBankAccesses(String userId) throws BankingException
    {
        return binder.getBankAccessEndpoint().getBankAccesses(userId);
    }

    public List<BankAccountBankingModel> getBankAccounts(String userId, String bankAccessId) throws BankingException
    {
        return binder.getBankAccountEndpoint().getBankAccounts(userId, bankAccessId);
    }

    public void syncBankAccount(String userId, String bankAccessId, String bankAccountId, String pin) throws BankingException
    {
        binder.getBankAccountEndpoint().syncBankAccount(userId, bankAccessId, bankAccountId, pin);
    }

    public void addBankAccess(String userId, BankAccessBankingModel bankAccess) throws BankingException
    {
        binder.getBankAccessEndpoint().addBankAccess(userId, bankAccess);
    }

}
