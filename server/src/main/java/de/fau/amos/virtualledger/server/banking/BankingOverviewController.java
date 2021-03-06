package de.fau.amos.virtualledger.server.banking;

import de.fau.amos.virtualledger.dtos.BankAccess;
import de.fau.amos.virtualledger.dtos.BankAccessCredential;
import de.fau.amos.virtualledger.dtos.BankAccount;
import de.fau.amos.virtualledger.dtos.BankAccountBookings;
import de.fau.amos.virtualledger.dtos.BankAccountSync;
import de.fau.amos.virtualledger.dtos.BankAccountSyncResult;
import de.fau.amos.virtualledger.server.banking.adorsys.api.BankingApiFacade;
import de.fau.amos.virtualledger.server.banking.model.BankAccessBankingModel;
import de.fau.amos.virtualledger.server.banking.model.BankAccountBankingModel;
import de.fau.amos.virtualledger.server.banking.model.BankingException;
import de.fau.amos.virtualledger.server.banking.model.BookingModel;
import de.fau.amos.virtualledger.server.factories.BankAccessBankingModelFactory;
import de.fau.amos.virtualledger.server.factories.BankAccessFactory;
import de.fau.amos.virtualledger.server.factories.BankAccountBookingsFactory;
import de.fau.amos.virtualledger.server.factories.BankAccountFactory;
import de.fau.amos.virtualledger.server.model.DeletedBankAccess;
import de.fau.amos.virtualledger.server.model.DeletedBankAccount;
import de.fau.amos.virtualledger.server.model.DeletedBankAccountId;
import de.fau.amos.virtualledger.server.model.User;
import de.fau.amos.virtualledger.server.persistence.DeletedBankAccessesRepository;
import de.fau.amos.virtualledger.server.persistence.DeletedBankAccountsRepository;
import de.fau.amos.virtualledger.server.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class BankingOverviewController {

    @Autowired
    private BankingApiFacade bankingApiFacade;

    @Autowired
    private BankAccountFactory bankAccountFactory;

    @Autowired
    private BankAccessBankingModelFactory bankAccessBankingModelFactory;

    @Autowired
    private BankAccessFactory bankAccessFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeletedBankAccessesRepository deletedBankAccessesRepository;

    @Autowired
    private DeletedBankAccountsRepository deletedBankAccountRepository;

    @Autowired
    private BankAccountBookingsFactory bankAccountBookingsFactory;

    /**
     * loads all the bank accesses and accounts embedded matching to the userId
     * from adorsys api (or from dummies, depending on configuration) also
     * filters the received data to only return the not deleted ones
     *
     * @param userId
     * @return
     * @throws BankingException
     */
    public List<BankAccess> getBankingOverview(String userId) throws BankingException {
        List<BankAccessBankingModel> bankingModelList = bankingApiFacade.getBankAccesses(userId);
        List<BankAccess> bankAccessesList = bankAccessFactory.createBankAccesses(bankingModelList);

        filterBankAccessWithDeleted(userId, bankAccessesList);

        for (BankAccess bankAccess : bankAccessesList) {
            List<BankAccount> bankAccounts = this.getBankingAccounts(userId, bankAccess.getId());
            filterBankAccountsWithDeleted(userId, bankAccess.getId(), bankAccounts);
            bankAccess.setBankaccounts(bankAccounts);
        }

        return bankAccessesList;
    }

    /**
     * adds a bank access, uses adorsys api if configured to store on user with
     * email as username
     *
     * @param email
     * @param bankAccessCredential
     * @return the added BankAccess with containing all added BankAccounts
     * @throws BankingException
     */
    public BankAccess addBankAccess(String email, BankAccessCredential bankAccessCredential, final String token) throws BankingException {

        BankAccessBankingModel bankAccessBankingModel = bankAccessBankingModelFactory
                .createBankAccessBankingModel(email, bankAccessCredential);
        BankAccessBankingModel addedBankAccessBankingModel = bankingApiFacade.addBankAccess(email, bankAccessBankingModel);

        BankAccess bankAccess = bankAccessFactory.createBankAccess(addedBankAccessBankingModel);
        List<BankAccess> bankAccessList = new ArrayList<>();
        bankAccessList.add(bankAccess);
        filterBankAccessWithDeleted(email, bankAccessList);

        List<BankAccount> bankAccounts = this.getBankingAccounts(email, bankAccess.getId());
        filterBankAccountsWithDeleted(email, bankAccess.getId(), bankAccounts);
        bankAccess.setBankaccounts(bankAccounts);

        return bankAccess;
    }

    /**
     * deletes a bank access. In fact, no real delete is done, but entries are
     * made into db so that filtering works when getBankingOverview is called.
     *
     * @param email
     * @param bankAccessId
     * @throws BankingException
     */
    public void deleteBankAccess(String email, String bankAccessId) throws BankingException {
        User user = userRepository.findOne(email);
        DeletedBankAccess deletedBankAccess = deletedBankAccessesRepository.findOne(bankAccessId);
        if (deletedBankAccess == null) {
            deletedBankAccess = new DeletedBankAccess(bankAccessId);
        }
        deletedBankAccess.getUsers().add(user);
        deletedBankAccessesRepository.save(deletedBankAccess);
    }

    /**
     * deletes a bank account. In fact, no real delete is done, but entries are
     * made into db so that filtering works when getBankingOverview is called.
     *
     * @param email
     * @param bankAccessId
     * @param bankAccountId
     * @throws BankingException
     */
    public void deleteBankAccount(String email, String bankAccessId, String bankAccountId) throws BankingException {
        User user = userRepository.findOne(email);
        DeletedBankAccount deletedBankAccount = deletedBankAccountRepository.findOne(new DeletedBankAccountId(bankAccessId, bankAccountId));
        if (deletedBankAccount == null) {
            deletedBankAccount = new DeletedBankAccount(new DeletedBankAccountId(bankAccessId, bankAccountId));
        }
        deletedBankAccount.getUsers().add(user);
        deletedBankAccountRepository.save(deletedBankAccount);
    }

    public BankAccountSyncResult syncBankAccounts(String email, List<BankAccountSync> bankAccountSyncList)
            throws BankingException {
        this.filterBankAccountSyncWithDeleted(email, bankAccountSyncList);

        final List<BankAccountBookings> resultAccountBookings = new ArrayList<>();
        final BankAccountSyncResult result = new BankAccountSyncResult(resultAccountBookings);
        for (BankAccountSync bankAccountSync : bankAccountSyncList) {
            final List<BookingModel> bookingModels = bankingApiFacade.syncBankAccount(email,
                    bankAccountSync.getBankaccessid(), bankAccountSync.getBankaccountid(), bankAccountSync.getPin());
            resultAccountBookings.add(bankAccountBookingsFactory.createBankAccountBookings(bookingModels,
                    bankAccountSync.getBankaccessid(), bankAccountSync.getBankaccountid()));

        }
        return result;
    }

    /**
     * loads all the bank accounts matching to the email from adorsys api (or
     * from dummies, depending on configuration) also filters the received data
     * to only return the not deleted ones
     *
     * @param email
     * @param bankAccesId
     * @return
     * @throws BankingException
     */
    private List<BankAccount> getBankingAccounts(String email, String bankAccesId) throws BankingException {
        List<BankAccountBankingModel> bankingModel = bankingApiFacade.getBankAccounts(email, bankAccesId);
        List<BankAccount> bankAccounts = bankAccountFactory.createBankAccounts(bankingModel);
        return bankAccounts;
    }

    /**
     * filters a list of bank accesses with the ones in database that are marked
     * as deleted
     *
     * @param email
     * @param bankAccessList
     */
    private void filterBankAccessWithDeleted(String email, List<BankAccess> bankAccessList) {
        List<DeletedBankAccess> deletedAccessList = deletedBankAccessesRepository.findAllByUserEmail(email);

        List<BankAccess> foundBankAccesses = new ArrayList<BankAccess>();
        for (DeletedBankAccess deletedAccess : deletedAccessList) {
            for (BankAccess bankAccess : bankAccessList) {
                if (bankAccess.getId().equals(deletedAccess.getBankAccessId())) {
                    foundBankAccesses.add(bankAccess);
                }
            }
        }
        bankAccessList.removeAll(foundBankAccesses);
    }

    /**
     * filters a list of bank accounts with the ones in database that are marked
     * as deleted
     *
     * @param email
     * @param bankAccessId
     * @param bankAccountList
     */
    private void filterBankAccountsWithDeleted(String email, String bankAccessId, List<BankAccount> bankAccountList) {
        List<DeletedBankAccount> deletedAccountList = deletedBankAccountRepository
                .findAllByUserEmailAndAccessId(email, bankAccessId);

        List<BankAccount> foundBankAccounts = new ArrayList<BankAccount>();
        for (DeletedBankAccount deletedAccount : deletedAccountList) {
            for (BankAccount bankAccount : bankAccountList) {
                if (bankAccount.getBankid().equals(deletedAccount.getId().getBankAccountId())) {
                    foundBankAccounts.add(bankAccount);
                }
            }
        }
        bankAccountList.removeAll(foundBankAccounts);
    }

    /**
     * filters a list of BankAccountSync objects with accesses and accounts that
     * are marked as deleted in database
     *
     * @param email
     * @param bankAccountSyncList
     */
    private void filterBankAccountSyncWithDeleted(String email, List<BankAccountSync> bankAccountSyncList) {
        List<DeletedBankAccess> deletedAccessList = deletedBankAccessesRepository.findAllByUserEmail(email);
        for (DeletedBankAccess deletedBankAccess : deletedAccessList) {
            bankAccountSyncList.removeIf(x -> x.getBankaccessid().equals(deletedBankAccess.getBankAccessId()));
        }

        // use iterator, so we can modify List while iterating over it
        Iterator<BankAccountSync> iterator = bankAccountSyncList.iterator();
        while (iterator.hasNext()) {
            BankAccountSync bankAccountSync = iterator.next();
            List<DeletedBankAccount> deletedAccountList = deletedBankAccountRepository
                    .findAllByUserEmailAndAccessId(email, bankAccountSync.getBankaccessid());
            for (DeletedBankAccount deletedBankAccount : deletedAccountList) {
                if (deletedBankAccount.getId().getBankAccountId().equals(bankAccountSync.getBankaccountid())) {
                    iterator.remove();
                }
            }
        }
    }
}
