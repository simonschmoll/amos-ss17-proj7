package de.fau.amos.virtualledger.server.banking.adorsys.api.bankAccessEndpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.fau.amos.virtualledger.server.banking.model.BankAccessBankingModel;


public class DummyBankAccessEndpointTest {

    @Test
    public void workflow_working() throws Exception {

        // SETUP
        String testUser = "user";
        String testBankCode = "testCode";
        String testBankLogin = "testLogin";
        BankAccessBankingModel bankAccessBankingModel = new BankAccessBankingModel();
        bankAccessBankingModel.setUserId(testUser);
        bankAccessBankingModel.setBankLogin(testBankLogin);
        bankAccessBankingModel.setBankCode(testBankCode);

        DummyBankAccessEndpoint dummyBankAccessEndpoint = new DummyBankAccessEndpoint();

        // ACT
        dummyBankAccessEndpoint.addBankAccess(testUser, bankAccessBankingModel);
        List<BankAccessBankingModel> bankAccessBankingModelList = dummyBankAccessEndpoint.getBankAccesses(testUser);

        // ASSERT
        assertNotNull(bankAccessBankingModelList);
        assertEquals(bankAccessBankingModelList.size(), 1);
        assertEquals(bankAccessBankingModelList.get(0).getBankCode(), testBankCode);
        assertEquals(bankAccessBankingModelList.get(0).getBankLogin(), testBankLogin);
    }

    @Test
    public void exists_working() throws Exception {

        // SETUP
        String testUser = "user";
        String testId = "test";
        BankAccessBankingModel bankAccessBankingModel = new BankAccessBankingModel();
        bankAccessBankingModel.setUserId(testUser);

        DummyBankAccessEndpoint dummyBankAccessEndpoint = new DummyBankAccessEndpoint();

        // ACT
        dummyBankAccessEndpoint.addBankAccess(testUser, bankAccessBankingModel);
        List<BankAccessBankingModel> bankAccessBankingModelList = dummyBankAccessEndpoint.getBankAccesses(null);

        assertNotNull(bankAccessBankingModelList);
        assertEquals(bankAccessBankingModelList.size(), 1);

        boolean exists_pre_change = dummyBankAccessEndpoint.existsBankAccess(testId);
        bankAccessBankingModelList.get(0).setId(testId);
        boolean exists_post_change = dummyBankAccessEndpoint.existsBankAccess(testId);

        // ASSERT
        assertFalse(exists_pre_change);
        assertTrue(exists_post_change);
    }

}