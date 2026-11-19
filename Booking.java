package model;

public class Booking {

    private int bookingId;
    private int passengerId;
    private int trainId;
    private String seatNumber;
    private String bookingDate;
    private String status;

    public Booking(int bookingId, int passengerId, int trainId,
                   String seatNumber, String bookingDate,
                   String status) {

        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.trainId = trainId;
        this.seatNumber = seatNumber;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getTrainId() {
        return trainId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {

        return "Booking ID: " + bookingId
                + " | Passenger ID: " + passengerId
                + " | Train ID: " + trainId
                + " | Seat: " + seatNumber
                + " | Date: " + bookingDate
                + " | Status: " + status;
    }
}
