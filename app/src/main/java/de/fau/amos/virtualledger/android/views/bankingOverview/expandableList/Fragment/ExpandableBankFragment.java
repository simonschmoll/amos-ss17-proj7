package de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.android.api.auth.AuthenticationProvider;
import de.fau.amos.virtualledger.android.api.banking.BankingProvider;
import de.fau.amos.virtualledger.android.dagger.App;
import de.fau.amos.virtualledger.android.data.BankingDataManager;
import de.fau.amos.virtualledger.android.data.BankingSyncFailedException;
import de.fau.amos.virtualledger.android.localStorage.BankAccessCredentialDB;
import de.fau.amos.virtualledger.android.views.bankingOverview.deleteBankAccessAccount.BankAccessNameExtractor;
import de.fau.amos.virtualledger.android.views.bankingOverview.deleteBankAccessAccount.DeleteBankAccessAction;
import de.fau.amos.virtualledger.android.views.bankingOverview.deleteBankAccessAccount.LongClickDeleteListenerList;
import de.fau.amos.virtualledger.android.views.bankingOverview.deleteBankAccessAccount.functions.BiConsumer;
import de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.Adapter.ExpandableAdapterBanking;
import de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.BankingOverviewHandler;
import de.fau.amos.virtualledger.android.views.bankingOverview.expandableList.model.Group;
import de.fau.amos.virtualledger.android.views.menu.MainMenu;
import de.fau.amos.virtualledger.android.views.shared.totalAmount.TotalAmountFragment;
import de.fau.amos.virtualledger.dtos.BankAccess;
import de.fau.amos.virtualledger.dtos.BankAccount;

public class ExpandableBankFragment extends Fragment implements Observer {
    @SuppressWarnings("unused")
    private static final String TAG = "BankAccessListFragment";

    @Inject
    BankingProvider bankingProvider;
    @Inject
    AuthenticationProvider authenticationProvider;
    @Inject
    BankAccessCredentialDB bankAccessCredentialDB;
    @Inject
    BankingDataManager bankingDataManager;

    @BindView(R.id.expandableView)
    ExpandableListView listView;
    @BindView(R.id.banking_overview_finishButton)
    Button finishButton;
    @BindView(R.id.banking_overview_enable_all_accounts_checkbox)
    CheckBox enableAllCheckBox;

    @OnClick(R.id.banking_overview_finishButton)
    void onClickShowAllTransactions() {
        if(BankingOverviewHandler.hasItemsChecked(mappingCheckBoxes)) {
            ((MainMenu) getActivity()).switchToTransactionOverview(mappingCheckBoxes);
        }
    }

    @OnClick(R.id.banking_overview_enable_all_accounts_checkbox)
    void onClickEnableAllCheckbox() {
        final BankingOverviewHandler bankingOverview = BankingOverviewHandler.getInstance();
        if(!enableAllCheckBox.isChecked()) {
            mappingCheckBoxes = bankingOverview.setAllAccountsCheckedOrUnchecked(mappingCheckBoxes, false);
        } else {
            mappingCheckBoxes = bankingOverview.setAllAccountsCheckedOrUnchecked(mappingCheckBoxes, true);
        }
        adapter.setMappingCheckBoxes(mappingCheckBoxes);
        listView.setAdapter(adapter);
    }

    private ExpandableAdapterBanking adapter;
    private final SparseArray<Group> groups = new SparseArray<>();
    private List<BankAccess> bankAccessList;
    private HashMap<String, Boolean> mappingCheckBoxes = new HashMap<>();

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // This line needs to stay right here!!! Otherwise bankingDataManager is null when passed to adapter
        ((App) getActivity().getApplication()).getNetComponent().inject(this);

        adapter = new ExpandableAdapterBanking(getActivity(),
                groups, bankingDataManager, mappingCheckBoxes);
    }

    @Override
    public void onResume() {
        super.onResume();
        bankingDataManager.addObserver(this);

        switch (bankingDataManager.getSyncStatus()) {
            case NOT_SYNCED:
                bankingDataManager.sync();
                break;
            case SYNCED:
                onBankingDataChanged();
                break;
        }
    }

    private void onBankAccessesUpdated() {
        createData();
        adapter.setMappingCheckBoxes(mappingCheckBoxes);
        listView.setAdapter(adapter);
        final BankAccessNameExtractor getName = new BankAccessNameExtractor();
        listView.setOnItemLongClickListener(
                new LongClickDeleteListenerList(adapter, getActivity(),
                        bankAccessList,
                        getName,
                        new BiConsumer<BankAccess, BankAccount>() {
                            @Override
                            public void accept(final BankAccess item1, final BankAccount item2) {
                                new DeleteBankAccessAction(bankingDataManager).accept(item1, item2);
                                final Intent intent = new Intent(getActivity(), MainMenu.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                        }
                )

        );
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.banking_overview_expandablelist_main_view, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    private void createData() {
        int i = 0;
        final BankingOverviewHandler bankingOverview = BankingOverviewHandler.getInstance();
        bankAccessList = bankingOverview.sortAccesses(bankAccessList);
        groups.clear();
        mappingCheckBoxes.clear();
        for (final BankAccess access : bankAccessList) {
            final Group group = new Group(access);
            final List<BankAccount> accountList = bankingOverview.sortAccounts(access.getBankaccounts());
            access.setBankaccounts(accountList);
            for (final BankAccount account : access.getBankaccounts()) {
                group.children.add(account);
                mappingCheckBoxes.put(account.getBankid(), false);
            }
            groups.append(i, group);
            i++;
        }

    }

    /**
     * opens a fragment through replacing another fragment
     */
    private void openFragment(final Fragment fragment) {
        if (null != fragment) {
            final FragmentManager manager = getFragmentManager();
            final FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_menu_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void update(final Observable o, final Object arg) {
        onBankingDataChanged();
    }

    public void onBankingDataChanged() {
        try {
            bankAccessList = bankingDataManager.getBankAccesses();
            if ((bankAccessList == null || bankAccessList.size() == 0)) {
                final Fragment fragment = new NoBankingAccessesFragment();
                openFragment(fragment);
            }
            onBankAccessesUpdated();

        } catch (final BankingSyncFailedException ex) {
            Toast.makeText(getActivity(), "Failed connecting to the server, try again later", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bankingDataManager.deleteObserver(this);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        // add total amount fragment programmatically (bad practice in xml -> empty LinearLayout as wrapper)
        final FragmentManager fm = getFragmentManager();
        final TotalAmountFragment totalAmountFragment = new TotalAmountFragment();
        final FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.banking_overview_total_amount_fragment_wrapper, totalAmountFragment, "banking_overview_total_amount_fragment");
        ft.commit();
    }

}
