package de.fau.amos.virtualledger.android.views.savings.add;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import de.fau.amos.virtualledger.R;
import de.fau.amos.virtualledger.android.api.sync.Toaster;
import de.fau.amos.virtualledger.android.dagger.App;
import de.fau.amos.virtualledger.android.data.SavingsAccountsDataManager;
import de.fau.amos.virtualledger.dtos.SavingsAccount;

public class AddSavingsAccountActivity extends AppCompatActivity {
    @SuppressWarnings("unused")
    private static final String TAG = AddSavingsAccountActivity.class.getSimpleName();
    private SavingsAccount savingsAccountState = new SavingsAccount();

    private static final Integer ADD_AMOUNT_PAGE = 3;

    @Inject
    SavingsAccountsDataManager savingsAccountsDataManager;

    @BindView(R.id.add_savings_account_pager)
    ViewPager viewPager;

    @BindView(R.id.add_savings_account_button_previous)
    Button buttonPrevious;

    @BindView(R.id.add_savings_account_button_next)
    Button buttonNext;

    private AddSavingsAccountPagerAdapter pagerAdapter;
    private final List<AddSavingsAccountPage> pages = new ArrayList<>();

    private final AddSavingsAccountAmountFragment addSavingsAccountAmountFragment = new AddSavingsAccountAmountFragment();
    private final AddSavingsAccountAmountSplitFragment addSavingsAccountAmountSplitFragment = new AddSavingsAccountAmountSplitFragment();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getNetComponent().inject(this);

        setContentView(R.layout.saving_accounts_activity_add);
        ButterKnife.bind(this);

        pages.add(new AddSavingsAccountNameFragment());
        pages.add(new AddSavingsAccountFinalDateFragment());
        pages.add(new AddSavingsAccountAmountTypeFragment());
        pages.add(addSavingsAccountAmountFragment);
        pages.add(new AddSavingsAccountAssignPeopleFragment());
        pages.add(new AddSavingsAccountAccountsFragment());

        for (AddSavingsAccountPage page : this.pages) {
            page.consumeDataModel(this.savingsAccountState);
        }

        pagerAdapter = new AddSavingsAccountPagerAdapter(getSupportFragmentManager(), pages);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        updateBottomBar();
    }

    public void setSplitGoals(final boolean splitGoals) {
        pages.remove(addSavingsAccountAmountFragment);
        pages.remove(addSavingsAccountAmountSplitFragment);
        pages.add(ADD_AMOUNT_PAGE, splitGoals ? addSavingsAccountAmountSplitFragment : addSavingsAccountAmountFragment);
        pagerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.add_savings_account_button_previous)
    void onClickPrevious() {
        final int currentItem = viewPager.getCurrentItem();
        if (currentItem > 0) {
            viewPager.setCurrentItem(currentItem - 1);
        }
    }

    @OnClick(R.id.add_savings_account_button_next)
    void onClickNext() {
        final int currentItem = viewPager.getCurrentItem();
        logger().info("Filling in data of:" + this.pages.get(currentItem));
        this.pages.get(currentItem).fillInData(this.savingsAccountState);

        if (!this.pages.get(currentItem).navigatePossible()) {
            logger().info("Navigation is not possible because of missing data in the assistant page.");
            return;
        }

        if (currentItem < pagerAdapter.getCount() - 1) {
            this.pages.get(currentItem + 1).consumeDataModel(this.savingsAccountState);
            viewPager.setCurrentItem(currentItem + 1);
        } else {
            for (AddSavingsAccountPage page : this.pages) {
                if (!page.navigatePossible()) {
                    return;
                }
            }
            submit();
        }
    }

    private Logger logger() {
        return Logger.getLogger(this.getClass().getCanonicalName());
    }

    @OnPageChange(R.id.add_savings_account_pager)
    void onPagerPageChange() {
        updateBottomBar();
    }

    private void updateBottomBar() {
        final int position = viewPager.getCurrentItem();
        buttonNext.setText(position >= pagerAdapter.getCount() - 1
                ? R.string.add_savings_account_button_submit
                : R.string.add_savings_account_button_next);
        buttonPrevious.setVisibility(position <= 0
                ? View.INVISIBLE
                : View.VISIBLE);
    }

    private void submit() {
        Toaster toaster = new Toaster(getApplicationContext())//
                .pushSuccessMessage(String.format(Locale.getDefault(), "Savings account \"%s\" added.",
                        this.savingsAccountState.getName()))
                .pushTechnicalErrorMessage(
                        "No connection to server, please try again");

        savingsAccountsDataManager.add(this.savingsAccountState, toaster);
        finish();
    }
}
