package de.fau.amos.virtualledger.android.views.savings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.dtos.SavingsAccount;
import de.fau.amos.virtualledger.dtos.SavingsAccountSubGoal;

/**
 * Created by sebastian on 20.07.17.
 */

public class SavingsDetailsFragment extends Fragment {

    private View view;
    private SavingsAccount account;

    public SavingsDetailsFragment() {
    }

    public void setAccount(SavingsAccount account) {
        this.account = account;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.savings_account_details, container, false);
        ButterKnife.bind(this, view);

        updateText(R.id.id_name, account.getName());
        updateText(R.id.id_time_left, new DaysLeftFunction().apply(this.account));
        updateText(R.id.id_current_balance, String.valueOf(Math.round(this.account.getCurrentbalance())));
        updateText(R.id.id_goal_balance, String.valueOf(Math.round(this.account.getGoalbalance())));

        ListView subGoals = (ListView) view.findViewById(R.id.subgoals_list);
        if (account.getSubGoals().isEmpty()) {
            TextView title = (TextView) view.findViewById(R.id.subgoals_title);
            title.setText(R.string.no_subgoals_defined);
        } else {
            ArrayAdapter<SavingsAccountSubGoal> adapter = new SubgoalAdapter(getActivity(), R.id.subgoals_list, this.account.getSubGoals());
            subGoals.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        ListView contacts = (ListView) view.findViewById(R.id.contacts_list);
        if (account.getAdditionalAssignedUsers().isEmpty()) {
            TextView title = (TextView) view.findViewById(R.id.contacts_title);
            title.setText(R.string.no_contacts_assigned);
        } else {
            ContactsAdapter adapter = new ContactsAdapter(getActivity(), R.id.contacts_list, this.account.getAdditionalAssignedUsers());
            contacts.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        ListView accesses = (ListView) view.findViewById(R.id.access_list);
        if (account.getAssignedBankAccounts().isEmpty()) {
            TextView title = (TextView) view.findViewById(R.id.access_title);
            title.setText(R.string.no_access_assigned);
        } else {
            AccessAdapter adapter = new AccessAdapter(getActivity(), R.id.access_list, this.account.getAssignedBankAccounts());
            accesses.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    private void updateText(int id, String text) {
        TextView textView = (TextView) view.findViewById(id);
        textView.setText(text);
    }

}
