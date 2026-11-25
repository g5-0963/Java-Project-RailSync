import dao.BookingDAO;
import dao.PaymentDAO;
import dao.TrainDAO;

import model.Train;
import model.Traveller;

import java.util.ArrayList;
import java.util.Scanner;


public class App {


    public static void main(String[] args) {


        Scanner sc =
                new Scanner(System.in);


        TrainDAO trainDAO =
                new TrainDAO();


        BookingDAO bookingDAO =
                new BookingDAO();


        PaymentDAO paymentDAO =
                new PaymentDAO();


        // =====================================================
        // MAIN MENU
        // =====================================================

        System.out.println();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "       RAILSYNC RAILWAY SYSTEM"
        );

        System.out.println(
                "===================================="
        );


        System.out.println(
                "1. Book Tickets"
        );


        System.out.println(
                "2. View Ticket"
        );


        System.out.print(
                "Enter choice: "
        );


        int choice =
                Integer.parseInt(
                        sc.nextLine()
                );


        // =====================================================
        // VIEW TICKET
        // =====================================================

        if (choice == 2) {


            System.out.print(
                    "Enter Booking ID: "
            );


            int bookingId =
                    Integer.parseInt(
                            sc.nextLine()
                    );


            bookingDAO.viewTicket(
                    bookingId
            );


            sc.close();

            return;
        }


        // =====================================================
        // BOOK TICKETS
        // =====================================================

        if (choice != 1) {


            System.out.println(
                    "Invalid choice!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // SOURCE
        // =====================================================

        System.out.print(
                "Enter Source: "
        );


        String source =
                sc.nextLine();


        // =====================================================
        // DESTINATION
        // =====================================================

        System.out.print(
                "Enter Destination: "
        );


        String destination =
                sc.nextLine();


        // =====================================================
        // SEARCH TRAINS
        // =====================================================

        ArrayList<Train> trains =
                trainDAO.searchTrains(
                        source,
                        destination
                );


        if (trains.isEmpty()) {


            System.out.println(
                    "No trains found!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // DISPLAY TRAINS
        // =====================================================

        System.out.println();

        System.out.println(
                "========== AVAILABLE TRAINS =========="
        );


        for (int i = 0;
             i < trains.size();
             i++) {


            Train train =
                    trains.get(i);


            System.out.println();


            System.out.println(
                    "Train " + (i + 1)
            );


            System.out.println(
                    "Train ID : "
                    + train.getTrainId()
            );


            System.out.println(
                    "Train Number : "
                    + train.getTrainNumber()
            );


            System.out.println(
                    "Train Name : "
                    + train.getTrainName()
            );


            System.out.println(
                    "Route : "
                    + train.getSource()
                    + " -> "
                    + train.getDestination()
            );


            System.out.println(
                    "Departure : "
                    + train.getDepartureTime()
            );


            System.out.println(
                    "Arrival : "
                    + train.getArrivalTime()
            );


            System.out.println(
                    "Fare : Rs."
                    + train.getFare()
            );
            ArrayList<String> booked =
        bookingDAO.getBookedSeats(
                train.getTrainId()
        );

int totalSeats =
        train.getTotalSeats();

int availableSeats =
        totalSeats - booked.size();

System.out.println(
        "Total Seats : " + totalSeats
);

System.out.println(
        "Available Seats : " + availableSeats
);

        }
           


        // =====================================================
        // SELECT TRAIN
        // =====================================================

        System.out.println();

        System.out.print(
                "Select Train Number: "
        );


        int trainChoice =
                Integer.parseInt(
                        sc.nextLine()
                );


        if (trainChoice < 1 ||
            trainChoice > trains.size()) {


            System.out.println(
                    "Invalid train choice!"
            );


            sc.close();

            return;
        }


        Train selectedTrain =
                trains.get(
                        trainChoice - 1
                );


        // =====================================================
        // GET BOOKED SEATS
        // =====================================================

        ArrayList<String> bookedSeats =
                bookingDAO.getBookedSeats(
                        selectedTrain.getTrainId()
                );


        // =====================================================
        // DISPLAY SEAT MAP
        // =====================================================

System.out.println();

System.out.println(
        "========== AVAILABLE SEATS  =========="
);

for (int row = 0; row < 3; row++) {

    char rowLetter = (char) ('A' + row);

    for (int seat = 1; seat <= 14; seat++) {

        String seatNumber =
                rowLetter + String.valueOf(seat);

        if (!bookedSeats.contains(seatNumber)) {

            System.out.print(
                    "[" + seatNumber + "] "
            );
        }
    }

    System.out.println();
}

 

        // =====================================================
        // SELECT MULTIPLE SEATS
        // =====================================================

        System.out.println();

        System.out.println(
                "Enter seats separated by space."
        );


        System.out.println(
                "Example: A1 A2 A3"
        );


        System.out.print(
                "Enter seats: "
        );


        String seatInput =
                sc.nextLine();


        String[] seatArray =
                seatInput.trim().split("\\s+");


        ArrayList<String> selectedSeats =
                new ArrayList<>();


        for (int i = 0;
             i < seatArray.length;
             i++) {


            String seat =
                    seatArray[i].toUpperCase();


            if (bookedSeats.contains(seat)) {


                System.out.println(
                        "Seat "
                        + seat
                        + " is already booked!"
                );


                sc.close();

                return;
            }


            if (selectedSeats.contains(seat)) {


                System.out.println(
                        "Duplicate seat selected: "
                        + seat
                );


                sc.close();

                return;
            }


            selectedSeats.add(seat);
        }


        if (selectedSeats.isEmpty()) {


            System.out.println(
                    "No seat selected!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // NUMBER OF TRAVELLERS
        // =====================================================

        System.out.println();

        System.out.print(
                "Enter number of travellers: "
        );


        int numberOfTravellers =
                Integer.parseInt(
                        sc.nextLine()
                );


        // Number must exactly match seats

        if (numberOfTravellers
                != selectedSeats.size()) {


            System.out.println();

            System.out.println(
                    "Number of travellers must "
                    + "match selected seats!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // TRAVELLER LIST
        // =====================================================

        ArrayList<Traveller> travellers =
                new ArrayList<>();


        // =====================================================
        // ENTER TRAVELLER DETAILS
        // =====================================================

        for (int i = 0;
             i < numberOfTravellers;
             i++) {


            System.out.println();

            System.out.println(
                    "===================================="
            );


            System.out.println(
                    "       TRAVELLER "
                    + (i + 1)
                    + " DETAILS"
            );


            System.out.println(
                    "===================================="
            );


            if (i == 0) {


                System.out.println(
                        "Booking Owner / Your Details"
                );

            } else {


                System.out.println(
                        "Friend / Family Member Details"
                );
            }


            // -------------------------------------------------
            // NAME
            // -------------------------------------------------

            System.out.print(
                    "Name: "
            );


            String name =
                    sc.nextLine();


            // -------------------------------------------------
            // AGE
            // -------------------------------------------------

            System.out.print(
                    "Age: "
            );


            int age =
                    Integer.parseInt(
                            sc.nextLine()
                    );


            // -------------------------------------------------
            // GENDER
            // -------------------------------------------------

            System.out.print(
                    "Gender: "
            );


            String gender =
                    sc.nextLine();


            // -------------------------------------------------
            // PHONE
            // -------------------------------------------------

            System.out.print(
                    "Phone: "
            );


            String phone =
                    sc.nextLine();


            // -------------------------------------------------
            // EMAIL
            // -------------------------------------------------

            System.out.print(
                    "Email: "
            );


            String email =
                    sc.nextLine();


            // -------------------------------------------------
            // ASSIGN SEAT
            // -------------------------------------------------

            String seatNumber =
                    selectedSeats.get(i);


            System.out.println(
                    "Assigned Seat: "
                    + seatNumber
            );


            // -------------------------------------------------
            // CREATE TRAVELLER
            // -------------------------------------------------

            Traveller traveller =
                    new Traveller(
                            name,
                            age,
                            gender,
                            phone,
                            email,
                            seatNumber
                    );


            travellers.add(
                    traveller
            );
        }


        // =====================================================
        // PAYMENT METHOD
        // =====================================================

   
System.out.println();

System.out.println(
        "========== PAYMENT =========="
);

System.out.println(
        "1. UPI"
);

System.out.println(
        "2. Card"
);

System.out.println(
        "3. NetBanking"
);

System.out.print(
        "Select payment method: "
);


// Take input as String so both number and name work
String paymentInput =
        sc.nextLine();


String paymentMethod;


if (paymentInput.equals("1") ||
    paymentInput.equalsIgnoreCase("UPI")) {

    paymentMethod = "UPI";

} else if (paymentInput.equals("2") ||
           paymentInput.equalsIgnoreCase("CARD")) {

    paymentMethod = "CARD";

} else if (paymentInput.equals("3") ||
           paymentInput.equalsIgnoreCase("NetBanking")) {

    paymentMethod = "NetBanking";

} else {

    System.out.println(
            "Invalid payment method!"
    );

    sc.close();

    return;
}


System.out.println(
        "Selected payment method: "
        + paymentMethod
);



        // =====================================================
        // TOTAL FARE
        // =====================================================

        double farePerSeat =
                selectedTrain.getFare();


        double totalFare =
                farePerSeat
                * travellers.size();


        System.out.println();

        System.out.println(
                "Fare Per Traveller : Rs."
                + farePerSeat
        );


        System.out.println(
                "Number Of Travellers : "
                + travellers.size()
        );


        System.out.println(
                "Total Fare : Rs."
                + totalFare
        );


        // =====================================================
        // CREATE ONE GROUP BOOKING
        // =====================================================

        System.out.println();

        System.out.println(
                "Creating group booking..."
        );


        int bookingId =
                bookingDAO.createGroupBooking(
                        selectedTrain.getTrainId(),
                        travellers
                );


        if (bookingId == -1) {


            System.out.println(
                    "Booking failed!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // ONE PAYMENT FOR COMPLETE GROUP
        // =====================================================

        System.out.println();

        System.out.println(
                "Processing payment..."
        );


        boolean paymentSuccess =
                paymentDAO.makePayment(
                        bookingId,
                        totalFare,
                        paymentMethod
                );


        if (!paymentSuccess) {


            System.out.println(
                    "Payment failed!"
            );


            sc.close();

            return;
        }


        // =====================================================
        // BOOKING SUCCESS
        // =====================================================

        System.out.println();

        System.out.println(
                "=========================================="
        );


        System.out.println(
                "          BOOKING SUCCESSFUL"
        );


        System.out.println(
                "=========================================="
        );


        System.out.println(
                "Booking ID : "
                + bookingId
        );


        System.out.println(
                "Train : "
                + selectedTrain.getTrainName()
        );


        System.out.println(
                "Train Number : "
                + selectedTrain.getTrainNumber()
        );


        System.out.println(
                "Route : "
                + selectedTrain.getSource()
                + " -> "
                + selectedTrain.getDestination()
        );


        System.out.println(
                "Total Travellers : "
                + travellers.size()
        );


        System.out.println(
                "Total Amount : Rs."
                + totalFare
        );


        System.out.println();

        System.out.println(
                "Traveller Seats:"
        );


        for (int i = 0;
             i < travellers.size();
             i++) {


            Traveller traveller =
                    travellers.get(i);


            System.out.println(
                    (i + 1)
                    + ". "
                    + traveller.getName()
                    + " -> Seat "
                    + traveller.getSeatNumber()
            );
        }


        System.out.println();

        System.out.println(
                "Payment Method : "
                + paymentMethod
        );


        System.out.println();

        System.out.println(
                "You can use Booking ID "
                + bookingId
                + " to View Ticket."
        );


        System.out.println(
                "=========================================="
        );


        sc.close();
    }
}
