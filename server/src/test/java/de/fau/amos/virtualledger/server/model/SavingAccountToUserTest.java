package de.fau.amos.virtualledger.server.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Created by Simon on 27.06.2017.
 */
public class SavingAccountToUserTest {

    @Test
    public void constructorTest() {
        SavingsAccountToUser testAccount = new SavingsAccountToUser();
        Assertions.assertThat(testAccount).isNotNull();
    }

}
