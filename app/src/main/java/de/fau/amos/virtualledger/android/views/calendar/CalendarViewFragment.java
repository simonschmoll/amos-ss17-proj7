package de.fau.amos.virtualledger.android.views.calendar;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.logging.Logger;

import butterknife.ButterKnife;
import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.android.views.shared.transactionList.DataListening;
import de.fau.amos.virtualledger.android.views.shared.transactionList.Supplier;
import de.fau.amos.virtualledger.android.views.shared.transactionList.Transaction;

public class CalendarViewFragment extends Fragment implements DataListening {
    private static final String BUNDLE_PARAMETER_TOTALAMOUNT = "totalamount";
    private boolean active = true;

    // need FragmentActivity because of Caldroid workaround
    private FragmentActivity context;
    private double totalAmount;
    private Supplier<Transaction> bankTransactionSupplier;


    public void pushTransactionSupplier(Supplier<Transaction> bankTransactionSupplier) {
        this.bankTransactionSupplier = bankTransactionSupplier;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_view_fragment, container, false);
        readBundle(getArguments());
        ButterKnife.bind(this, view);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        this.bankTransactionSupplier.addDataListeningObject(this);
        updateCalendar();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.bankTransactionSupplier.deregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.bankTransactionSupplier.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.bankTransactionSupplier.onPause();
    }

    private void updateCalendar() {
        this.logger().info("Update Calendar View Fragment");
        Calendar cal = Calendar.getInstance();
        CaldroidBankingFragment caldroidFragment = CaldroidBankingFragment.newInstance(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR), this.bankTransactionSupplier, totalAmount);
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendar_view_fragment_calendar_wrapper, caldroidFragment, "calendar_view_fragment_calendar");
        transaction.commit();
    }


    @Override
    public void onAttach(Context context) {
        // workaround because we need getSupportFragmentManager() for Caldroid
        this.context = (FragmentActivity) context;
        super.onAttach(context);
    }


    public static CalendarViewFragment newInstance(Supplier<Transaction> bankTransactionSupplier, double totalAmount) {
        Bundle bundle = new Bundle();
        bundle.putDouble(CalendarViewFragment.BUNDLE_PARAMETER_TOTALAMOUNT, totalAmount);
        CalendarViewFragment fragment = new CalendarViewFragment();
        fragment.pushTransactionSupplier(bankTransactionSupplier);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            totalAmount = bundle.getDouble(CalendarViewFragment.BUNDLE_PARAMETER_TOTALAMOUNT);
        } else {
            throw new InvalidParameterException("No data found in bundle! Please check if you instantiate CaldroidBankingFragment with " + CalendarViewFragment.BUNDLE_PARAMETER_TOTALAMOUNT + " !");
        }
    }

    @Override
    public void notifyDataChanged() {
        this.logger().info("Data changed, reloading calendar");
        this.updateCalendar();
    }

    private Logger logger() {
        return Logger.getLogger(this.getClass().getCanonicalName() + "{" + this.toString() + "}");
    }
}
