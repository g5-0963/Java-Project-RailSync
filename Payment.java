package model;

public class Payment {

    private int paymentId;
    private int bookingId;
    private double amount;
    private String paymentMethod;
    private String paymentStatus;
    private String paymentDate;

    // Constructor
    public Payment(int paymentId, int bookingId, double amount,
                   String paymentMethod, String paymentStatus,
                   String paymentDate) {

        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getPaymentId() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    // Setters
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    // Display payment details
    public void displayPayment() {
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Amount: ₹" + amount);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Payment Status: " + paymentStatus);
        System.out.println("Payment Date: " + paymentDate);
    }
}
