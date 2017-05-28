package de.fau.amos.virtualledger.server.api;

import de.fau.amos.virtualledger.dtos.BankAccess;
import de.fau.amos.virtualledger.server.banking.BankingOverviewController;
import de.fau.amos.virtualledger.server.banking.model.BankingException;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Georg on 28.05.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class BankingOverviewApiEndpointTest {

    @Mock
    BankingOverviewController bankingOverviewController;


    @Test
    public void getBankingOverviewEndpoint_securityContextPrincipalNameNull() throws BankingException {
        // SETUP
        SecurityContext context = mock(SecurityContext.class);
        Principal principal = mock(Principal.class);
        when(principal.getName())
                .thenReturn(null);
        when(context.getUserPrincipal())
                .thenReturn(principal);
        BankingOverviewApiEndpoint bankingOverviewApiEndpoint = new BankingOverviewApiEndpoint(bankingOverviewController);

        // ACT
        Response reponse = bankingOverviewApiEndpoint.getBankingOverviewEndpoint(context);

        // ASSERT
        int expectedStatusCode = 403;
        Assert.isTrue(reponse.getStatus() == expectedStatusCode, "Wrong status code applied! Expected " + expectedStatusCode + ", but got " + reponse.getStatus());
        verify(bankingOverviewController, times(0))
                .getBankingOverview(any(String.class));
    }

    @Test
    public void getBankingOverviewEndpoint_securityContextPrincipalNameEmpty() throws BankingException {
        // SETUP
        SecurityContext context = mock(SecurityContext.class);
        Principal principal = mock(Principal.class);
        when(principal.getName())
                .thenReturn("");
        when(context.getUserPrincipal())
                .thenReturn(principal);
        BankingOverviewApiEndpoint bankingOverviewApiEndpoint = new BankingOverviewApiEndpoint(bankingOverviewController);

        // ACT
        Response reponse = bankingOverviewApiEndpoint.getBankingOverviewEndpoint(context);

        // ASSERT
        int expectedStatusCode = 403;
        Assert.isTrue(reponse.getStatus() == expectedStatusCode, "Wrong status code applied! Expected " + expectedStatusCode + ", but got " + reponse.getStatus());
        verify(bankingOverviewController, times(0))
                .getBankingOverview(any(String.class));
    }

    @Test
    public void getBankingOverviewEndpoint_validInput() throws BankingException {
        // SETUP
        SecurityContext context = mock(SecurityContext.class);
        Principal principal = mock(Principal.class);
        when(principal.getName())
                .thenReturn("mock");
        when(context.getUserPrincipal())
                .thenReturn(principal);
        when(bankingOverviewController.getBankingOverview(any(String.class)))
                .thenReturn(new ArrayList<BankAccess>());
        BankingOverviewApiEndpoint bankingOverviewApiEndpoint = new BankingOverviewApiEndpoint(bankingOverviewController);

        // ACT
        Response reponse = bankingOverviewApiEndpoint.getBankingOverviewEndpoint(context);

        // ASSERT
        int expectedStatusCode = 200;
        Assert.isTrue(reponse.getStatus() == expectedStatusCode, "Wrong status code applied! Expected " + expectedStatusCode + ", but got " + reponse.getStatus());
        verify(bankingOverviewController, times(1))
                .getBankingOverview(any(String.class));
    }

    @Test
    public void getBankingOverviewEndpoint_controllerThrows() throws BankingException {
        // SETUP
        SecurityContext context = mock(SecurityContext.class);
        Principal principal = mock(Principal.class);
        when(principal.getName())
                .thenReturn("mock");
        when(context.getUserPrincipal())
                .thenReturn(principal);
        when(bankingOverviewController.getBankingOverview(any(String.class)))
                .thenThrow(new BankingException("mock"));
        BankingOverviewApiEndpoint bankingOverviewApiEndpoint = new BankingOverviewApiEndpoint(bankingOverviewController);

        // ACT
        Response reponse = bankingOverviewApiEndpoint.getBankingOverviewEndpoint(context);

        // ASSERT
        int expectedStatusCode = 400;
        Assert.isTrue(reponse.getStatus() == expectedStatusCode, "Wrong status code applied! Expected " + expectedStatusCode + ", but got " + reponse.getStatus());
        verify(bankingOverviewController, times(1))
                .getBankingOverview(any(String.class));
    }
}
