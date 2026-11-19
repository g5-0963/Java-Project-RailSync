import dao.TrainDAO;
import dao.PassengerDAO;
import dao.BookingDAO;
import dao.PaymentDAO;
import model.Train;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        TrainDAO trainDAO = new TrainDAO();
        PassengerDAO passengerDAO = new PassengerDAO();
        BookingDAO bookingDAO = new BookingDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        // =====================================================
        // WELCOME
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println("              WELCOME TO RAILSYNC");
        System.out.println("       Railway Reservation System");
        System.out.println("==============================================");

        // =====================================================
        // SEARCH TRAIN
        // =====================================================

        System.out.println();
        System.out.println("========== SEARCH TRAIN ==========");

        System.out.print("Enter source: ");
        String source = sc.nextLine().trim();

        System.out.print("Enter destination: ");
        String destination = sc.nextLine().trim();

        ArrayList<Train> trains =
                trainDAO.searchTrains(source, destination);

        // =====================================================
        // CHECK TRAIN AVAILABILITY
        // =====================================================

        if (trains == null || trains.size() == 0) {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println("No trains available for this route.");
            System.out.println("----------------------------------------------");

            sc.close();
            return;
        }

        // =====================================================
        // DISPLAY AVAILABLE TRAINS
        // =====================================================

        System.out.println();
        System.out.println("========== AVAILABLE TRAINS ==========");

        for (int i = 0; i < trains.size(); i++) {

            Train train = trains.get(i);

            System.out.println();
            System.out.println(
                    (i + 1) + ". "
                    + train.getTrainName()
                    + " (" + train.getTrainNumber() + ")"
            );

            System.out.println(
                    "   Route          : "
                    + train.getSource()
                    + " -> "
                    + train.getDestination()
            );

            System.out.println(
                    "   Departure      : "
                    + train.getDepartureTime()
            );

            System.out.println(
                    "   Arrival        : "
                    + train.getArrivalTime()
            );

            System.out.println(
                    "   Fare per Seat  : Rs."
                    + train.getFare()
            );

            System.out.println(
                    "   Available Seats: "
                    + train.getAvailableSeats()
            );
        }

        // =====================================================
        // SELECT TRAIN
        // =====================================================

        System.out.println();

        int trainChoice;

        while (true) {

            System.out.print(
                    "Select train number (1-"
                    + trains.size()
                    + "): "
            );

            String input =
                    sc.nextLine().trim();

            try {

                trainChoice =
                        Integer.parseInt(input);

                if (trainChoice >= 1
                        && trainChoice <= trains.size()) {

                    break;

                } else {

                    System.out.println(
                            "Please select a valid train number."
                    );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a number only."
                );
            }
        }

        Train selectedTrain =
                trains.get(trainChoice - 1);

        // =====================================================
        // SELECTED TRAIN
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println("              SELECTED TRAIN");
        System.out.println("==============================================");

        System.out.println(
                "Train        : "
                + selectedTrain.getTrainName()
        );

        System.out.println(
                "Train Number : "
                + selectedTrain.getTrainNumber()
        );

        System.out.println(
                "Route        : "
                + selectedTrain.getSource()
                + " -> "
                + selectedTrain.getDestination()
        );

        System.out.println(
                "Departure    : "
                + selectedTrain.getDepartureTime()
        );

        System.out.println(
                "Arrival      : "
                + selectedTrain.getArrivalTime()
        );

        System.out.println(
                "Fare/Seat    : Rs."
                + selectedTrain.getFare()
        );

        System.out.println(
                "=============================================="
        );

        // =====================================================
        // AVAILABLE SEATS
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println("              SEAT SELECTION");
        System.out.println("==============================================");

        ArrayList<String> bookedSeats =
                bookingDAO.getBookedSeats(
                        selectedTrain.getTrainId()
                );

        ArrayList<String> availableSeats =
                new ArrayList<>();

        // Generate A1-A14, B1-B14, C1-C14
        for (char row = 'A'; row <= 'C'; row++) {

            for (int number = 1; number <= 14; number++) {

                String seat =
                        row + String.valueOf(number);

                boolean alreadyBooked = false;

                for (int i = 0;
                     i < bookedSeats.size();
                     i++) {

                    if (bookedSeats.get(i)
                            .equalsIgnoreCase(seat)) {

                        alreadyBooked = true;
                        break;
                    }
                }

                if (!alreadyBooked) {

                    availableSeats.add(seat);
                }
            }
        }

        // =====================================================
        // NO SEATS AVAILABLE
        // =====================================================

        if (availableSeats.size() == 0) {

            System.out.println();
            System.out.println("----------------------------------------------");
            System.out.println(
                    "Sorry! No seats are available for this train."
            );
            System.out.println("----------------------------------------------");

            sc.close();
            return;
        }

        // =====================================================
        // DISPLAY AVAILABLE SEATS
        // =====================================================

        System.out.println();
        System.out.println("Available Seats:");
        System.out.println();

        for (int i = 0;
             i < availableSeats.size();
             i++) {

            System.out.printf(
                    "%-6s",
                    availableSeats.get(i)
            );

            if ((i + 1) % 7 == 0) {
                System.out.println();
            }
        }

        System.out.println();

        System.out.println("----------------------------------------------");
        System.out.println(
                "You can select multiple seats."
        );
        System.out.println(
                "Example: A1 A4 A5"
        );
        System.out.println("----------------------------------------------");

        // =====================================================
        // SELECT MULTIPLE SEATS
        // =====================================================

        ArrayList<String> selectedSeats =
                new ArrayList<>();

        while (true) {

            System.out.print("Select seat(s): ");

            String input =
                    sc.nextLine()
                      .trim()
                      .toUpperCase();

            // Empty input
            if (input.length() == 0) {

                System.out.println();
                System.out.println(
                        "Please select at least one seat."
                );

                continue;
            }

            String[] seats =
                    input.split("\\s+");

            ArrayList<String> tempSeats =
                    new ArrayList<>();

            boolean validSelection = true;

            // =================================================
            // CHECK EACH SEAT
            // =================================================

            for (int i = 0;
                 i < seats.length;
                 i++) {

                String seat =
                        seats[i];

                boolean seatAvailable = false;

                // Check if seat exists
                for (int j = 0;
                     j < availableSeats.size();
                     j++) {

                    if (availableSeats.get(j)
                            .equalsIgnoreCase(seat)) {

                        seatAvailable = true;

                        // Store correct format
                        seat =
                                availableSeats.get(j);

                        break;
                    }
                }

                // Invalid / booked seat
                if (!seatAvailable) {

                    System.out.println();
                    System.out.println(
                            "X Seat "
                            + seat
                            + " is not available."
                    );

                    validSelection = false;
                    break;
                }

                // =================================================
                // CHECK DUPLICATE SEAT
                // =================================================

                boolean duplicate = false;

                for (int j = 0;
                     j < tempSeats.size();
                     j++) {

                    if (tempSeats.get(j)
                            .equalsIgnoreCase(seat)) {

                        duplicate = true;
                        break;
                    }
                }

                if (duplicate) {

                    System.out.println();
                    System.out.println(
                            "X Seat "
                            + seat
                            + " was selected more than once."
                    );

                    validSelection = false;
                    break;
                }

                tempSeats.add(seat);
            }

            // =================================================
            // INVALID SELECTION
            // =================================================

            if (!validSelection) {

                System.out.println();
                System.out.println(
                        "Please try again."
                );

                System.out.println(
                        "Example: A1 A4 A5"
                );

                System.out.println();

                continue;
            }

            // =================================================
            // SAVE SELECTED SEATS
            // =================================================

            selectedSeats.clear();

            for (int i = 0;
                 i < tempSeats.size();
                 i++) {

                selectedSeats.add(
                        tempSeats.get(i)
                );
            }

            break;
        }

        // =====================================================
        // CALCULATE TOTAL FARE
        // =====================================================

        double totalFare =
                selectedTrain.getFare()
                * selectedSeats.size();

        // =====================================================
        // SEAT SUMMARY
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println("           SEAT SELECTION SUMMARY");
        System.out.println("==============================================");

        System.out.print("Selected Seats : ");

        for (int i = 0;
             i < selectedSeats.size();
             i++) {

            System.out.print(
                    selectedSeats.get(i)
            );

            if (i < selectedSeats.size() - 1) {

                System.out.print(", ");
            }
        }

        System.out.println();

        System.out.println(
                "Number of Seats: "
                + selectedSeats.size()
        );

        System.out.println(
                "Fare per Seat  : Rs."
                + selectedTrain.getFare()
        );

        System.out.println(
                "Total Fare     : Rs."
                + totalFare
        );

        System.out.println(
                "=============================================="
        );

        // =====================================================
        // PASSENGER DETAILS
        // =====================================================

        System.out.println();
        System.out.println("========== PASSENGER DETAILS ==========");

        System.out.print(
                "Enter passenger name: "
        );

        String name =
                sc.nextLine().trim();

        while (name.length() == 0) {

            System.out.println(
                    "Name cannot be empty."
            );

            System.out.print(
                    "Enter passenger name: "
            );

            name =
                    sc.nextLine().trim();
        }

        int age;

        while (true) {

            System.out.print(
                    "Enter age: "
            );

            String ageInput =
                    sc.nextLine().trim();

            try {

                age =
                        Integer.parseInt(ageInput);

                if (age > 0 && age <= 120) {

                    break;
                }

                System.out.println(
                        "Please enter a valid age."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter age as a number."
                );
            }
        }

        System.out.print(
                "Enter gender: "
        );

        String gender =
                sc.nextLine().trim();

        System.out.print(
                "Enter phone: "
        );

        String phone =
                sc.nextLine().trim();

        System.out.print(
                "Enter email: "
        );

        String email =
                sc.nextLine().trim();

        // =====================================================
        // PAYMENT
        // =====================================================

        System.out.println();
        System.out.println("==============================================");
        System.out.println("                 PAYMENT");
        System.out.println("==============================================");

        System.out.println(
                "Number of Seats : "
                + selectedSeats.size()
        );

        System.out.println(
                "Total Amount    : Rs."
                + totalFare
        );

        System.out.println();

        System.out.println("1. UPI");
        System.out.println("2. Card");
        System.out.println("3. NetBanking");

        System.out.println();

        String paymentInput;
        String paymentMethod;

        while (true) {

            System.out.print(
                    "Select payment method (1/2/3): "
            );

            paymentInput =
                    sc.nextLine()
                      .trim()
                      .toUpperCase();

            if (paymentInput.equals("1")
                    || paymentInput.equals("UPI")) {

                paymentMethod =
                        "UPI";

                break;

            } else if (paymentInput.equals("2")
                    || paymentInput.equals("CARD")) {

                paymentMethod =
                        "CARD";

                break;

            }else if (paymentInput.equals("3")
                    || paymentInput.equals("NETBANKING")) {

                paymentMethod =
                        "NetBanking";

                break;

            }
             else {

                System.out.println();
                System.out.println(
                        "Invalid payment method."
                );

                System.out.println(
                        "Please enter 1, 2 or 3."
                );
            }
        }

        // =====================================================
        // SAVE PASSENGER
        // =====================================================

        System.out.println();
        System.out.println(
                "Processing passenger details..."
        );

        int passengerId =
                passengerDAO.addPassenger(
                        name,
                        age,
                        gender,
                        phone,
                        email
                );

        if (passengerId == -1) {

            System.out.println();
            System.out.println(
                    "Passenger registration failed."
            );

            sc.close();
            return;
        }

        System.out.println(
                "Passenger details saved successfully."
        );

        // =====================================================
        // CREATE MULTIPLE BOOKINGS
        // =====================================================

        System.out.println();
        System.out.println(
                "Creating your booking..."
        );

        ArrayList<Integer> bookingIds =
                bookingDAO.createBookings(
                        passengerId,
                        selectedTrain.getTrainId(),
                        selectedSeats,
                        selectedTrain.getFare()
                );

        // =====================================================
        // CHECK BOOKING
        // =====================================================

        if (bookingIds.size()
                != selectedSeats.size()) {

            System.out.println();
            System.out.println(
                    "Booking creation failed."
            );

            System.out.println(
                    "Please try again."
            );

            sc.close();
            return;
        }

        System.out.println(
                "Booking created successfully."
        );

        // =====================================================
        // SAVE PAYMENT
        // =====================================================

        System.out.println(
                "Processing payment..."
        );

        boolean paymentSuccess =
                true;

        double farePerSeat =
                selectedTrain.getFare();

        for (int i = 0;
             i < bookingIds.size();
             i++) {

            boolean success =
                    paymentDAO.makePayment(
                            bookingIds.get(i),
                            farePerSeat,
                            paymentMethod
                    );

            if (!success) {

                paymentSuccess =
                        false;

                break;
            }
        }

        // =====================================================
        // PAYMENT FAILED
        // =====================================================

        if (!paymentSuccess) {

            System.out.println();
            System.out.println(
                    "Payment failed."
            );

            System.out.println(
                    "Please contact support."
            );

            sc.close();
            return;
        }

        // =====================================================
        // BOOKING CONFIRMATION
        // =====================================================

        System.out.println();
        System.out.println();
        System.out.println(
                "=============================================="
        );

        System.out.println(
                "             BOOKING CONFIRMED!"
        );

        System.out.println(
                "=============================================="
        );

        System.out.println();

        System.out.println(
                "Passenger Name   : "
                + name
        );

        System.out.println(
                "Age              : "
                + age
        );

        System.out.println(
                "Gender           : "
                + gender
        );

        System.out.println(
                "Phone            : "
                + phone
        );

        System.out.println(
                "Email            : "
                + email
        );

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "Train            : "
                + selectedTrain.getTrainName()
        );

        System.out.println(
                "Train Number     : "
                + selectedTrain.getTrainNumber()
        );

        System.out.println(
                "Route            : "
                + selectedTrain.getSource()
                + " -> "
                + selectedTrain.getDestination()
        );

        System.out.println(
                "Departure        : "
                + selectedTrain.getDepartureTime()
        );

        System.out.println(
                "Arrival          : "
                + selectedTrain.getArrivalTime()
        );

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "Seats Booked:"
        );

        for (int i = 0;
             i < selectedSeats.size();
             i++) {

            System.out.println(
                    "  "
                    + selectedSeats.get(i)
                    + "  -> Booking ID: "
                    + bookingIds.get(i)
            );
        }

        System.out.println(
                "----------------------------------------------"
        );

        System.out.println(
                "Total Seats      : "
                + selectedSeats.size()
        );

        System.out.println(
                "Amount Paid      : Rs."
                + totalFare
        );

        System.out.println(
                "Payment Method   : "
                + paymentMethod
        );

        System.out.println(
                "Payment Status   : SUCCESS"
        );

        System.out.println(
                "Booking Status   : CONFIRMED"
        );

        System.out.println();

        System.out.println(
                "=============================================="
        );

        System.out.println(
                "       Thank you for booking with RailSync!"
        );

        System.out.println(
                "=============================================="
        );

        sc.close();
    }
}
