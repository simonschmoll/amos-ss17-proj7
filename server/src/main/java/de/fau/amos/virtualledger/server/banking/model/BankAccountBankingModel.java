package de.fau.amos.virtualledger.server.banking.model;

/**
 * Created by Georg on 20.05.2017.
 */
public class BankAccountBankingModel {

    private String bankAccessId = null;
    private BankAccountBalanceBankingModel bankAccountBalance = null;
    private String bicHbciAccount = null;
    private String blzHbciAccount = null;
    private String countryHbciAccount = null;
    private String currencyHbciAccount = null;
    private String ibanHbciAccount = null;
    private String id = null;
    private String nameHbciAccount = null;
    private String numberHbciAccount = null;
    private String type = null;

    public BankAccountBankingModel() {
    }

    public String getBankAccessId() {
        return bankAccessId;
    }

    public void setBankAccessId(String bankAccessId) {
        this.bankAccessId = bankAccessId;
    }

    public BankAccountBalanceBankingModel getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(BankAccountBalanceBankingModel bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }

    public String getBicHbciAccount() {
        return bicHbciAccount;
    }

    public void setBicHbciAccount(String bicHbciAccount) {
        this.bicHbciAccount = bicHbciAccount;
    }

    public String getBlzHbciAccount() {
        return blzHbciAccount;
    }

    public void setBlzHbciAccount(String blzHbciAccount) {
        this.blzHbciAccount = blzHbciAccount;
    }

    public String getCountryHbciAccount() {
        return countryHbciAccount;
    }

    public void setCountryHbciAccount(String countryHbciAccount) {
        this.countryHbciAccount = countryHbciAccount;
    }

    public String getCurrencyHbciAccount() {
        return currencyHbciAccount;
    }

    public void setCurrencyHbciAccount(String currencyHbciAccount) {
        this.currencyHbciAccount = currencyHbciAccount;
    }

    public String getIbanHbciAccount() {
        return ibanHbciAccount;
    }

    public void setIbanHbciAccount(String ibanHbciAccount) {
        this.ibanHbciAccount = ibanHbciAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameHbciAccount() {
        return nameHbciAccount;
    }

    public void setNameHbciAccount(String nameHbciAccount) {
        this.nameHbciAccount = nameHbciAccount;
    }

    public String getNumberHbciAccount() {
        return numberHbciAccount;
    }

    public void setNumberHbciAccount(String numberHbciAccount) {
        this.numberHbciAccount = numberHbciAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
