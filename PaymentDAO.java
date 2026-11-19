package dao;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class PaymentDAO {

    public boolean makePayment(int bookingId,
                               double amount,
                               String paymentMethod) {

        String sql = "INSERT INTO payments " +
                "(booking_id, amount, payment_method, " +
                "payment_status, payment_date) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, bookingId);
            ps.setDouble(2, amount);
            ps.setString(3, paymentMethod);
            ps.setString(4, "SUCCESS");
            ps.setDate(5,
                    new Date(System.currentTimeMillis()));

            ps.executeUpdate();

            ps.close();
            con.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
