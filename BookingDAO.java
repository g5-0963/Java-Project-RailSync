
package dao;

import db.DBConnection;
import model.Traveller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

public class BookingDAO {

    // =========================================================
    // GET BOOKED SEATS FOR A TRAIN
    // =========================================================

    public ArrayList<String> getBookedSeats(int trainId) {

        ArrayList<String> bookedSeats = new ArrayList<>();

        String sql =
                "SELECT bp.seat_number " +
                "FROM booking_passengers bp " +
                "JOIN bookings b " +
                "ON bp.booking_id = b.booking_id " +
                "WHERE b.train_id = ? " +
                "AND b.status = 'CONFIRMED'";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, trainId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                bookedSeats.add(
                        rs.getString("seat_number")
                );
            }

            rs.close();

        } catch (Exception e) {

            System.out.println(
                    "Error while getting booked seats."
            );

            e.printStackTrace();
        }

        return bookedSeats;
    }


    // =========================================================
    // CREATE GROUP BOOKING
    // =========================================================

    public int createGroupBooking(
            int trainId,
            ArrayList<Traveller> travellers) {

        Connection con = null;

        int bookingId = -1;

        try {

            con = DBConnection.getConnection();

            // Start transaction
            con.setAutoCommit(false);


            // -------------------------------------------------
            // CHECK TRAIN EXISTS
            // -------------------------------------------------

String trainCheckSQL =
        "SELECT train_id, fare " +
        "FROM trains " +
        "WHERE train_id = ?";

PreparedStatement trainCheck =
        con.prepareStatement(trainCheckSQL);

trainCheck.setInt(
        1,
        trainId
);

ResultSet trainRS =
        trainCheck.executeQuery();

if (!trainRS.next()) {

    System.out.println(
            "Train not found."
    );

    trainRS.close();
    trainCheck.close();

    con.rollback();

    return -1;
}


// Get fare of the selected train
double fare =
        trainRS.getDouble("fare");


// Calculate total booking amount
double totalAmount =
        fare * travellers.size();


trainRS.close();
trainCheck.close();



            // -------------------------------------------------
            // INSERT ALL TRAVELLERS INTO PASSENGERS TABLE
            // -------------------------------------------------

            String passengerSQL =
                    "INSERT INTO passengers " +
                    "(name, age, gender, phone, email) " +
                    "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement passengerPS =
                    con.prepareStatement(
                            passengerSQL,
                            PreparedStatement.RETURN_GENERATED_KEYS
                    );


            ArrayList<Integer> passengerIds =
                    new ArrayList<>();


            for (Traveller traveller : travellers) {

                passengerPS.setString(
                        1,
                        traveller.getName()
                );

                passengerPS.setInt(
                        2,
                        traveller.getAge()
                );

                passengerPS.setString(
                        3,
                        traveller.getGender()
                );

                passengerPS.setString(
                        4,
                        traveller.getPhone()
                );

                passengerPS.setString(
                        5,
                        traveller.getEmail()
                );

                passengerPS.executeUpdate();


                ResultSet generatedKeys =
                        passengerPS.getGeneratedKeys();

                if (generatedKeys.next()) {

                    int passengerId =
                            generatedKeys.getInt(1);

                    passengerIds.add(
                            passengerId
                    );

                } else {

                    throw new Exception(
                            "Passenger ID could not be generated."
                    );
                }

                generatedKeys.close();
            }

            passengerPS.close();


            // -------------------------------------------------
            // CREATE ONE MASTER BOOKING
            // -------------------------------------------------
            
String bookingSQL =
        "INSERT INTO bookings " +
        "(train_id, booking_date, status, total_amount) " +
        "VALUES (?, ?, ?, ?)";

PreparedStatement bookingPS =
        con.prepareStatement(
                bookingSQL,
                PreparedStatement.RETURN_GENERATED_KEYS
        );

bookingPS.setInt(
        1,
        trainId
);

bookingPS.setDate(
        2,
        Date.valueOf(
                LocalDate.now()
        )
);

bookingPS.setString(
        3,
        "CONFIRMED"
);

bookingPS.setDouble(
        4,
        totalAmount
);

bookingPS.executeUpdate();

ResultSet bookingKeys =
        bookingPS.getGeneratedKeys();

if (bookingKeys.next()) {

    bookingId =
            bookingKeys.getInt(1);

} else {

    throw new Exception(
            "Booking ID could not be generated."
    );
}

bookingKeys.close();
bookingPS.close();
            

          
            // INSERT TRAVELLERS + SEATS
           
            String bookingPassengerSQL =
                    "INSERT INTO booking_passengers " +
                    "(booking_id, passenger_id, seat_number) " +
                    "VALUES (?, ?, ?)";

            PreparedStatement bpPS =
                    con.prepareStatement(
                            bookingPassengerSQL
                    );


            for (int i = 0;
                 i < travellers.size();
                 i++) {

                Traveller traveller =
                        travellers.get(i);


                bpPS.setInt(
                        1,
                        bookingId
                );

                bpPS.setInt(
                        2,
                        passengerIds.get(i)
                );

                bpPS.setString(
                        3,
                        traveller.getSeatNumber()
                );

                bpPS.executeUpdate();
            }

            bpPS.close();


            // -------------------------------------------------
            // NO available_seats UPDATE
            //
            // Seat availability is calculated from
            // booking_passengers.
            // -------------------------------------------------


            // -------------------------------------------------
            // COMMIT TRANSACTION
            // -------------------------------------------------

            con.commit();

            con.setAutoCommit(true);

            return bookingId;


        } catch (Exception e) {

            System.out.println(
                    "Booking failed!"
            );

            e.printStackTrace();


            // -------------------------------------------------
            // ROLLBACK
            // -------------------------------------------------

            if (con != null) {

                try {

                    con.rollback();

                } catch (Exception rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            return -1;


        } finally {

            if (con != null) {

                try {

                    con.close();

                } catch (Exception closeError) {

                    closeError.printStackTrace();
                }
            }
        }
    }


    // VIEW TICKET
  

    public void viewTicket(int bookingId) {

        String sql =
                "SELECT " +
                "b.booking_id, " +
                "b.booking_date, " +
                "b.status, " +

                "t.train_number, " +
                "t.train_name, " +
                "t.source, " +
                "t.destination, " +
                "t.departure_time, " +
                "t.arrival_time, " +
                "t.fare, " +

                "p.name, " +
                "p.age, " +
                "p.gender, " +
                "p.phone, " +
                "p.email, " +

                "bp.seat_number " +

                "FROM bookings b " +

                "JOIN trains t " +
                "ON b.train_id = t.train_id " +

                "JOIN booking_passengers bp " +
                "ON b.booking_id = bp.booking_id " +

                "JOIN passengers p " +
                "ON bp.passenger_id = p.passenger_id " +

                "WHERE b.booking_id = ? " +

                "ORDER BY bp.booking_passenger_id";


        try (
                Connection con =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        con.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    bookingId
            );


            ResultSet rs =
                    ps.executeQuery();


            if (!rs.next()) {

                System.out.println(
                        "\nBooking not found."
                );

                return;
            }


          
            // FIRST ROW
         

            String bookingDate =
                    rs.getDate(
                            "booking_date"
                    ).toString();

            String status =
                    rs.getString(
                            "status"
                    );

            String trainNumber =
                    rs.getString(
                            "train_number"
                    );

            String trainName =
                    rs.getString(
                            "train_name"
                    );

            String source =
                    rs.getString(
                            "source"
                    );

            String destination =
                    rs.getString(
                            "destination"
                    );

            String departure =
                    rs.getString(
                            "departure_time"
                    );

            String arrival =
                    rs.getString(
                            "arrival_time"
                    );

            double fare =
                    rs.getDouble(
                            "fare"
                    );


           
            // PRINT TICKET HEADER
            
            System.out.println();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "          RAILSYNC E-TICKET"
            );

            System.out.println(
                    "=========================================="
            );


            System.out.println(
                    "Booking ID : " + bookingId
            );

            System.out.println(
                    "Booking Date : " + bookingDate
            );

            System.out.println(
                    "Status : " + status
            );


            System.out.println(
                    "------------------------------------------"
            );


            System.out.println(
                    "Train : " + trainName
            );

            System.out.println(
                    "Train Number : " + trainNumber
            );

            System.out.println(
                    "Route : " +
                    source +
                    " -> " +
                    destination
            );

            System.out.println(
                    "Departure : " + departure
            );

            System.out.println(
                    "Arrival : " + arrival
            );

            System.out.println(
                    "Fare Per Traveller : Rs." +
                    fare
            );


            System.out.println(
                    "------------------------------------------"
            );


           
            // TRAVELLERS
           

            int travellerCount = 0;

            do {

                travellerCount++;

                System.out.println();

                System.out.println(
                        "TRAVELLER " +
                        travellerCount
                );


                System.out.println(
                        "Name : " +
                        rs.getString("name")
                );

                System.out.println(
                        "Age : " +
                        rs.getInt("age")
                );

                System.out.println(
                        "Gender : " +
                        rs.getString("gender")
                );

                System.out.println(
                        "Phone : " +
                        rs.getString("phone")
                );

                System.out.println(
                        "Email : " +
                        rs.getString("email")
                );

                System.out.println(
                        "Seat : " +
                        rs.getString("seat_number")
                );


                System.out.println(
                        "------------------------------------------"
                );


            } while (rs.next());


            // -------------------------------------------------
            // TOTAL
            // -------------------------------------------------

            double totalAmount =
                    fare * travellerCount;


            System.out.println(
                    "Total Travellers : " +
                    travellerCount
            );

            System.out.println(
                    "Total Amount : Rs." +
                    totalAmount
            );


            System.out.println(
                    "=========================================="
            );


        } catch (Exception e) {

            System.out.println(
                    "Error while viewing ticket."
            );

            e.printStackTrace();
        }
    }
}
