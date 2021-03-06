package de.fau.amos.virtualledger.android.dagger.component;

import dagger.Component;
import de.fau.amos.virtualledger.android.dagger.module.AppModule;
import de.fau.amos.virtualledger.android.dagger.module.BankingDataModule;
import de.fau.amos.virtualledger.android.dagger.module.BankingModule;
import de.fau.amos.virtualledger.android.dagger.module.ContactsDataModule;
import de.fau.amos.virtualledger.android.dagger.module.ContactsModule;
import de.fau.amos.virtualledger.android.dagger.module.DatabaseModule;
import de.fau.amos.virtualledger.android.dagger.module.NetModule;
import de.fau.amos.virtualledger.android.dagger.module.SavingsAccountsDataModule;
import de.fau.amos.virtualledger.android.dagger.module.SavingsModule;
import de.fau.amos.virtualledger.android.data.BankingDataManager;
import de.fau.amos.virtualledger.android.data.ContactsDataManager;
import de.fau.amos.virtualledger.android.data.SavingsAccountsDataManager;
import de.fau.amos.virtualledger.android.localStorage.BankAccessCredentialDB;
import de.fau.amos.virtualledger.android.views.bankingOverview.addBankAccess.AddBankAccessActivity;
import de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.Fragment.ExpandableBankFragment;
import de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.Fragment.NoBankingAccessesFragment;
import de.fau.amos.virtualledger.android.views.calendar.CaldroidBankingFragment;
import de.fau.amos.virtualledger.android.views.contacts.ContactsFragment;
import de.fau.amos.virtualledger.android.views.contacts.ContactsSupplier;
import de.fau.amos.virtualledger.android.views.contacts.NoContactsFragment;
import de.fau.amos.virtualledger.android.views.contacts.add.AddContactsActivity;
import de.fau.amos.virtualledger.android.views.contacts.delete.ContactsDeleteAction;
import de.fau.amos.virtualledger.android.views.menu.MainMenu;
import de.fau.amos.virtualledger.android.views.savings.Delete.SavingsAccountDeleteAction;
import de.fau.amos.virtualledger.android.views.savings.NoSavingsAccountsFragment;
import de.fau.amos.virtualledger.android.views.savings.SavingAccountsFragment;
import de.fau.amos.virtualledger.android.views.savings.SavingsSupplier;
import de.fau.amos.virtualledger.android.views.savings.add.AddSavingsAccountAccountsFragment;
import de.fau.amos.virtualledger.android.views.savings.add.AddSavingsAccountActivity;
import de.fau.amos.virtualledger.android.views.shared.totalAmount.TotalAmountFragment;
import de.fau.amos.virtualledger.android.views.shared.transactionList.BankTransactionSupplierImplementation;
import de.fau.amos.virtualledger.android.views.shared.transactionList.TransactionListFragment;
import de.fau.amos.virtualledger.android.views.transactionOverview.TransactionOverviewFragment;

/**
 * Created by Simon on 07.05.2017. taken from https://adityaladwa.wordpress.com/2016/05/09/
 * dagger-2-with-retrofit-and-okhttp-and-gson/)
 */

@NetComponentScope
@Component(modules = {AppModule.class, NetModule.class, BankingModule.class, DatabaseModule.class, BankingDataModule.class, SavingsModule.class, SavingsAccountsDataModule.class, ContactsModule.class, ContactsDataModule.class}, dependencies = {OidcAuthenticationComponent.class})
public interface NetComponent {

    BankAccessCredentialDB bankAccessCredentialDB();
    BankingDataManager bankingDataManager();
    SavingsAccountsDataManager savingsAccountsDataManager();
    ContactsDataManager contactsDataManager();

    void inject(MainMenu activity);

    void inject(ExpandableBankFragment expandableBankFragment);

    void inject(AddBankAccessActivity addBankAccessActivity);

    void inject(TransactionOverviewFragment transactionOverviewFragment);

    void inject(NoBankingAccessesFragment noBankingAccessesFragment);

    void inject(TotalAmountFragment totalAmountFragment);

    void inject(CaldroidBankingFragment caldroidBankingFragment);

    void inject(TransactionListFragment caldroidBankingFragment);

    void inject(BankTransactionSupplierImplementation caldroidBankingFragment);

    void inject(SavingAccountsFragment savingAccountsFragment);

    void inject(NoSavingsAccountsFragment noSavingsAccountsFragment);

    void inject(AddSavingsAccountActivity addSavingsAccountActivity);

    void inject(SavingsSupplier addSavingsAccountActivity);

    void inject(ContactsFragment contactsFragment);

    void inject(ContactsSupplier contactsSupplier);

    void inject(NoContactsFragment noContactsFragment);

    void inject(AddContactsActivity addContactsActivity);

    void inject(AddSavingsAccountAccountsFragment addSavingsAccountAccountsFragment);

    void inject(ContactsDeleteAction deleteAction);

    void inject(SavingsAccountDeleteAction deleteAction);
}
