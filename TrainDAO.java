package dao;
import db.DBConnection;
import model.Train;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TrainDAO {

    public ArrayList<Train> getAllTrains() {

        ArrayList<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM trains";

        try {
            Connection con = db.DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_number"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getDouble("fare"),
                    rs.getInt("available_seats")
                );

                trains.add(train);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trains;
    }
     public ArrayList<Train> searchTrains(String source, String destination) {

        ArrayList<Train> trains = new ArrayList<>();

        String sql = "SELECT * FROM trains WHERE source = ? AND destination = ?";

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, source);
            ps.setString(2, destination);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Train train = new Train(
                    rs.getInt("train_id"),
                    rs.getString("train_number"),
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("departure_time"),
                    rs.getString("arrival_time"),
                    rs.getDouble("fare"),
                    rs.getInt("available_seats")
                );

                trains.add(train);
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return trains;
    }
}
