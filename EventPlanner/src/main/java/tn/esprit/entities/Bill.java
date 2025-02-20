package tn.esprit.entities;

import java.util.Date;

public class Bill {
    private int BillId,Amount,Archived,EventID;
    private Date DueDate;
    private String Description, PaymentStatus;

    public Bill() {
    }

    public Bill( int billId,int archived,  int amount, Date dueDate, String description, String paymentStatus,int eventID) {
        EventID = eventID;
        Archived = archived;
        BillId = billId;
        Amount = amount;
        DueDate = dueDate;
        Description = description;
        PaymentStatus = paymentStatus;
    }

    public int getBillId() {
        return BillId;
    }

    public void setBillId(int billId) {
        BillId = billId;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getArchived() {
        return Archived;
    }

    public void setArchived(int archived) {
        Archived = archived;
    }

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public Date getDueDate() {
        return DueDate;
    }

    public void setDueDate(Date dueDate) {
        DueDate = dueDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "BillId=" + BillId +
                ", Amount=" + Amount +
                ", Archived=" + Archived +
                ", EventID=" + EventID +
                ", DueDate=" + DueDate +
                ", Description='" + Description + '\'' +
                ", PaymentStatus='" + PaymentStatus + '\'' +
                '}';
    }
}

