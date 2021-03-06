package dtos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.fau.amos.virtualledger.dtos.BankAccountBookings;
import de.fau.amos.virtualledger.dtos.BankAccountSyncResult;
import de.fau.amos.virtualledger.dtos.Booking;

/**
 * Created by Simon on 12.06.2017.
 */

public class BankAccountSyncResultTest {

    private static final int EARLY_DATE = 123123;
    private static final int MIDDLE_DATE = 555555;
    private static final int LATE_DATE = 999999;
    private static final double LOW_AMOUNT = 111.1;
    private static final double MIDDLE_AMOUNT = 555.1;
    private static final double HIGH_AMOUNT = 999.1;
    private List<BankAccountBookings> bankAccountBookingsList;
    private List<Booking> bookings = new ArrayList<>();
    private Booking booking1 = new Booking(new Date(EARLY_DATE), LOW_AMOUNT);
    private Booking booking2 = new Booking(new Date(MIDDLE_DATE), MIDDLE_AMOUNT);
    private Booking booking3 = new Booking(new Date(LATE_DATE), HIGH_AMOUNT);
    private BankAccountBookings bankAccountBookings1;
    private BankAccountBookings bankAccountBookings2;
    private BankAccountBookings bankAccountBookings3;

    /**
     *
     */
    @Before
    public void setUp() {
        bankAccountBookingsList = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);
        bookings.add(booking3);
        bankAccountBookings1 = new BankAccountBookings("accessId1", "accountId1", bookings);
        bankAccountBookings2 = new BankAccountBookings("accessId2", "accountId2", bookings);
        bankAccountBookings3 = new BankAccountBookings("accessId3", "accountId3", bookings);
        bankAccountBookingsList.add(bankAccountBookings1);
        bankAccountBookingsList.add(bankAccountBookings2);
        bankAccountBookingsList.add(bankAccountBookings3);
    }

    /**
     *
     */
    @Test
    public void constructorTest() {
        BankAccountSyncResult syncResultTest2 = new BankAccountSyncResult(bankAccountBookingsList);
        Assert.assertNotNull(syncResultTest2);
    }

    /**
     *
     */
    @Test
    public void setAndGetBankAccountBookings() {
        BankAccountBookings bankAccountBookings4 = new BankAccountBookings("accessId4", "accountId4", bookings);
        BankAccountBookings bankAccountBookings5 = new BankAccountBookings("accessId5", "accountId5", bookings);
        BankAccountBookings bankAccountBookings6 = new BankAccountBookings("accessId6", "accountId6", bookings);
        List<BankAccountBookings> bankAccountBookingsList2 = new ArrayList<>();
        bankAccountBookingsList2.add(bankAccountBookings4);
        bankAccountBookingsList2.add(bankAccountBookings5);
        bankAccountBookingsList2.add(bankAccountBookings6);
        BankAccountSyncResult syncResultTest3 = new BankAccountSyncResult(bankAccountBookingsList);
        syncResultTest3.setBankaccountbookings(bankAccountBookingsList2);
        Assert.assertEquals(bankAccountBookingsList2, syncResultTest3.getBankaccountbookings());
    }
}
