package dao;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

public class BookingDAO {

    // Get seats which are already booked for a particular train
    public ArrayList<String> getBookedSeats(int trainId) {

        ArrayList<String> bookedSeats = new ArrayList<>();

        String sql = "SELECT seat_number FROM bookings " +
                     "WHERE train_id = ? AND status = 'CONFIRMED'";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, trainId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                bookedSeats.add(
                        rs.getString("seat_number")
                );
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookedSeats;
    }


    // Create ONE booking
    public int createBooking(int passengerId,
                             int trainId,
                             String seatNumber,
                             double fare) {

        Connection con = null;

        try {

            con = DBConnection.getConnection();

            String sql =
                    "INSERT INTO bookings " +
                    "(passenger_id, train_id, seat_number, booking_date, status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(
                            sql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            ps.setInt(1, passengerId);
            ps.setInt(2, trainId);
            ps.setString(3, seatNumber);

            ps.setDate(
                    4,
                    new Date(System.currentTimeMillis())
            );

            ps.setString(5, "CONFIRMED");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            int bookingId = -1;

            if (rs.next()) {
                bookingId = rs.getInt(1);
            }

            rs.close();
            ps.close();

            // Reduce available seats by 1
            String updateSql =
                    "UPDATE trains " +
                    "SET available_seats = available_seats - 1 " +
                    "WHERE train_id = ? " +
                    "AND available_seats > 0";

            PreparedStatement updatePs =
                    con.prepareStatement(updateSql);

            updatePs.setInt(1, trainId);

            updatePs.executeUpdate();

            updatePs.close();
            con.close();

            return bookingId;

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return -1;
    }


    // Create MULTIPLE bookings
    public ArrayList<Integer> createBookings(
            int passengerId,
            int trainId,
            ArrayList<String> seatNumbers,
            double fare) {

        ArrayList<Integer> bookingIds =
                new ArrayList<>();

        Connection con = null;

        try {

            con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);

            String sql =
                    "INSERT INTO bookings " +
                    "(passenger_id, train_id, seat_number, booking_date, status) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(
                            sql,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );

            for (String seat : seatNumbers) {

                ps.setInt(1, passengerId);
                ps.setInt(2, trainId);
                ps.setString(3, seat);

                ps.setDate(
                        4,
                        new Date(System.currentTimeMillis())
                );

                ps.setString(5, "CONFIRMED");

                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();

                if (rs.next()) {
                    bookingIds.add(rs.getInt(1));
                }

                rs.close();
            }

            ps.close();


            // Reduce available seats according to
            // number of selected seats
            String updateSql =
                    "UPDATE trains " +
                    "SET available_seats = available_seats - ? " +
                    "WHERE train_id = ? " +
                    "AND available_seats >= ?";

            PreparedStatement updatePs =
                    con.prepareStatement(updateSql);

            int numberOfSeats = seatNumbers.size();

            updatePs.setInt(1, numberOfSeats);
            updatePs.setInt(2, trainId);
            updatePs.setInt(3, numberOfSeats);

            int updated =
                    updatePs.executeUpdate();

            updatePs.close();


            if (updated == 0) {

                con.rollback();

                bookingIds.clear();

                System.out.println(
                        "Not enough seats available!"
                );

                con.close();

                return bookingIds;
            }


            // Save everything
            con.commit();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (con != null) {
                    con.rollback();
                    con.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            bookingIds.clear();
        }

        return bookingIds;
    }
}
