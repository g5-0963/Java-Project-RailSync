package dao;
import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PassengerDAO {

    public int addPassenger(String name, int age, String gender,
                            String phone, String email) {

        String sql = "INSERT INTO passengers " +
                "(name, age, gender, phone, email) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(sql,
                            PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, phone);
            ps.setString(5, email);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int passengerId = rs.getInt(1);

                rs.close();
                ps.close();
                con.close();

                return passengerId;
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}
