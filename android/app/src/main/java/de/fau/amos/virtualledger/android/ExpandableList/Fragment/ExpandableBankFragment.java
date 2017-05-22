package de.fau.amos.virtualledger.android.ExpandableList.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.android.App;
import de.fau.amos.virtualledger.android.ExpandableList.Adapter.ExpandableAdapterBanking;
import de.fau.amos.virtualledger.android.ExpandableList.model.Group;
import de.fau.amos.virtualledger.android.api.banking.BankingProvider;
import de.fau.amos.virtualledger.android.deleteaction.BankAccessNameExtractor;
import de.fau.amos.virtualledger.android.deleteaction.DeleteAccessAction;
import de.fau.amos.virtualledger.android.deleteaction.LongClickDeleteListenerList;
import de.fau.amos.virtualledger.dtos.BankAccess;
import de.fau.amos.virtualledger.dtos.BankAccount;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simon on 21.05.2017.
 */

public class ExpandableBankFragment extends Fragment {
    /**
     *
     */
    ExpandableListView listView;
    TextView bankBalanceOverviewText;
    View seperator;
    SparseArray<Group> groups = new SparseArray<Group>();
    List<BankAccess> bankAccessList;
    private static final String TAG = "BankAccessListFragment";
    double bankBalanceOverview;
    /**
     *
     */
    @Inject
    BankingProvider bankingProvider;

    /**
     *
     * @param savedInstanceState - state of the instance
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((App) getActivity().getApplication()).getNetComponent().inject(this);
        final ExpandableBankFragment __self = this;


        bankingProvider.getBankingOverview()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BankAccess>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<BankAccess> bankAccesses) {
                        bankAccessList = bankAccesses;
                        if(bankAccessList == null) {
                            Fragment fragment = new NoBankingAccessesFragment();
                            openFragment(fragment);
                        }
                        createData();
                        ExpandableAdapterBanking adapter = new ExpandableAdapterBanking(getActivity(),
                                groups);
                        listView.setAdapter(adapter);
                        String bankBalanceString = String.format(Locale.GERMAN, "%.2f",bankBalanceOverview);
                        bankBalanceOverviewText.setText(bankBalanceString);
                        seperator.setVisibility(View.VISIBLE);
                        BankAccessNameExtractor  getName = new BankAccessNameExtractor();
                        listView.setOnItemLongClickListener(
                                new LongClickDeleteListenerList<BankAccess>(__self.getActivity(),
                                        bankAccessList,
                                        getName,
                                        new DeleteAccessAction(__self.getActivity(),getName
                                        ))
                        );
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Error occured in Observable from login.");
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     *
     * @param inflater - to inflate the view
     * @param container - Viewgroup
     * @param savedInstanceState - state of the instance
     * @return Current View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_view_main,container,false);
        listView = (ExpandableListView)view.findViewById(R.id.expandableView);
        bankBalanceOverviewText = (TextView)view.findViewById(R.id.BankAccessBalanceOverview);
        seperator = (View)view.findViewById(R.id.BankOverviewSeperator);
        return view;
    }

    /**
     *
     */
    private void createData() {
        int i = 0;
        for(BankAccess access: bankAccessList) {
            Group group = new Group(access);
            for(BankAccount account: access.getBankaccounts()) {
                group.children.add(account);
            }
            bankBalanceOverview+=access.getBalance();
            groups.append(i,group);
            i++;
        }
    }

    /**
     *
     * @param fragment which is opened
     */
    private void openFragment(Fragment fragment) {
        if(null!=fragment) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
