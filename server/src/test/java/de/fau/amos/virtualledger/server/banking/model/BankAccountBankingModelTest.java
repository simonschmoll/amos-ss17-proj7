package de.fau.amos.virtualledger.server.banking.model;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Simon on 24.06.2017.
 */
public class BankAccountBankingModelTest {

    private BankAccountBankingModel testModel;

    @Before
    public void setUp() {
        testModel = new BankAccountBankingModel();
    }

    @Test
    public void setAndGetBankAccessId() {
        String testString = "testId";
        testModel.setBankAccessId(testString);
        Assertions.assertThat(testModel.getBankAccessId()).isEqualTo(testString);
    }

    @Test
    public void setAndGetBankAccountBalance() {
        BankAccountBalanceBankingModel testBalanceModel = new BankAccountBalanceBankingModel();
        testModel.setBankAccountBalance(testBalanceModel);
        Assertions.assertThat(testModel.getBankAccountBalance()).isEqualTo(testBalanceModel);
    }

    @Test
    public void setAndGetBicHbciAccount() {
        String testString = "testBicHbci";
        testModel.setBicHbciAccount(testString);
        Assertions.assertThat(testModel.getBicHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetBlzHbciAccount() {
        String testString = "testBlzHbci";
        testModel.setBlzHbciAccount(testString);
        Assertions.assertThat(testModel.getBlzHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetCountryHbciAccount() {
        String testString = "testCountryHbci";
        testModel.setCountryHbciAccount(testString);
        Assertions.assertThat(testModel.getCountryHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetCurrencyHbciAccount() {
        String testString = "testCurrencyHbci";
        testModel.setCurrencyHbciAccount(testString);
        Assertions.assertThat(testModel.getCurrencyHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetIbanHbciAccount() {
        String testString = "testIbanHbci";
        testModel.setIbanHbciAccount(testString);
        Assertions.assertThat(testModel.getIbanHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetId() {
        String testString = "testId";
        testModel.setId(testString);
        Assertions.assertThat(testModel.getId()).isEqualTo(testString);
    }

    @Test
    public void setAndGetNameHbciAccount() {
        String testString = "testNameHbci";
        testModel.setNameHbciAccount(testString);
        Assertions.assertThat(testModel.getNameHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetNumberHbciAccount() {
        String testString = "testNumberHbci";
        testModel.setNumberHbciAccount(testString);
        Assertions.assertThat(testModel.getNumberHbciAccount()).isEqualTo(testString);
    }

    @Test
    public void setAndGetTypeHbciAccount() {
        String testString = "testTypeHbci";
        testModel.setType(testString);
        Assertions.assertThat(testModel.getType()).isEqualTo(testString);
    }








}
