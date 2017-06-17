package de.fau.amos.virtualledger.android.views.shared.transactionList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import de.fau.amos.virtualledger.dtos.Booking;

/**
 * Created by sebastian on 05.06.17.
 */

public class Transaction implements Parcelable {

    private String bankName;
    private Booking booking;


    public Transaction(String bankName, Booking booking){
        this.bankName = bankName;
        this.booking = booking;
    }

    public Booking booking(){
        return this.booking;
    }

    public String bankName(){
        return this.bankName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankName);
        dest.writeLong(booking.getDate().getTime());
        dest.writeDouble(booking.getAmount());
        dest.writeString(booking.getUsage());
    }


    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    protected Transaction(Parcel in) {
        bankName = in.readString();
        Date date = new Date(in.readLong());
        double amount = in.readDouble();
        String usage = in.readString();
        booking = new Booking(date, amount);
        booking.setUsage(usage);
    }
}