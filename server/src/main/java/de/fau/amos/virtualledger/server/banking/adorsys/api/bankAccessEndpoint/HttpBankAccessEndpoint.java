package de.fau.amos.virtualledger.server.banking.adorsys.api.bankAccessEndpoint;

import com.sun.istack.logging.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import de.fau.amos.virtualledger.server.banking.adorsys.api.json.BankAccessJSONBankingModel;
import de.fau.amos.virtualledger.server.banking.adorsys.api.BankingApiUrlProvider;
import de.fau.amos.virtualledger.server.banking.model.BankAccessBankingModel;
import de.fau.amos.virtualledger.server.banking.model.BankingException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Georg on 18.05.2017.
 */
@RequestScoped @Default
public class HttpBankAccessEndpoint implements BankAccessEndpoint {

    @Inject
    BankingApiUrlProvider urlProvider;

    @Override
    public List<BankAccessBankingModel> getBankAccesses(String userId) throws BankingException {

        // Create Jersey client
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        String url = urlProvider.getBankAccessEndpointUrl(userId);
        WebResource.Builder webResourceGET = client.resource(url)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON);
        ClientResponse response = webResourceGET.get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new BankingException("No connection to Adorsys Server!");
        }
        BankAccessJSONBankingModel reponseModel = response.getEntity(BankAccessJSONBankingModel.class);
        if(reponseModel == null || reponseModel.get_embedded() == null)
        {
        	Logger.getLogger(HttpBankAccessEndpoint.class).info("No access found!");
            return new ArrayList<BankAccessBankingModel>();
        }
        List<BankAccessBankingModel> result = reponseModel.get_embedded().getBankAccessEntityList();
        return result;
    }

    @Override
    public void addBankAccess(String userId, BankAccessBankingModel bankAccess) throws BankingException {

        // Create Jersey client
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);

        String url = urlProvider.getBankAccessEndpointUrl(userId);
        WebResource.Builder webResourcePOST = client.resource(url)
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON);
        ClientResponse response = webResourcePOST.post(ClientResponse.class, bankAccess);

        if (response.getStatus() != 201) {
            throw new BankingException("Creating Banking Access failed!");
        }
        return;
    }
}