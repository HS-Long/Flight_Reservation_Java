import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReservationSystem {
    private List<Flight> flights;
    private List<Passenger> passengers;
    private Scanner scanner;
    private static Connection connection;

    public ReservationSystem() {
        this.flights = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.scanner = new Scanner(System.in);

        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/flight";
            String username = "root";
            String password = "@seaklong2001@";
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        addFlightByUser();
    }

    public void addFlight(Flight flight) {
        try {
            // Add flight to the database
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO flights (flight_number, departure_city, destination_city, departure_time, arrival_time, available_seats) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, flight.getFlightNumber());
            preparedStatement.setString(2, flight.getDepartureCity());
            preparedStatement.setString(3, flight.getDestinationCity());
            preparedStatement.setString(4, flight.getDepartureTime());
            preparedStatement.setString(5, flight.getArrivalTime());
            preparedStatement.setInt(6, flight.getAvailableSeats());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                flight.setId(generatedKeys.getInt(1));
            }

            flights.add(flight);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFlight(Flight flight) {
        try {
            // Remove flight from the database
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM flights WHERE id = ?"
            );
            preparedStatement.setInt(1, flight.getId());
            preparedStatement.executeUpdate();

            flights.remove(flight);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchFlight(String flightNumber) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM flights WHERE flight_number = ?"
            );
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Flight flight = createFlightFromResultSet(resultSet);
                flight.displayDetails();
            } else {
                System.out.println("Flight not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    ****************************************************************************************************
    public void displayFlightDetails() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM flights"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Flight flight = createFlightFromResultSet(resultSet);
                flight.displayDetails();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    ****************************************************************************************************
public void makeReservation() {
    System.out.println("Enter passenger details:");
    System.out.print("Name: ");
    String name = scanner.nextLine();
    System.out.print("Age: ");
    int age = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character
    System.out.print("Gender: ");
    String gender = scanner.nextLine();
    System.out.print("Contact Number: ");
    String contactNumber = scanner.nextLine();

    // Get available flights from the database
    System.out.println("Available Flights:");
    displayFlightDetails();

    System.out.print("Enter the flight number to make a reservation: ");
    String flightNumber = scanner.nextLine();

    Flight selectedFlight = null;

    try {
        // Retrieve flight details from the database
        PreparedStatement selectFlightStatement = connection.prepareStatement(
                "SELECT * FROM flights WHERE flight_number = ? FOR UPDATE"
        );
        selectFlightStatement.setString(1, flightNumber);
        ResultSet resultSet = selectFlightStatement.executeQuery();

        if (resultSet.next()) {
            selectedFlight = createFlightFromResultSet(resultSet);

            // Check if there are available seats
            if (selectedFlight.getAvailableSeats() > 0) {
                // Update availableSeats in the database
                PreparedStatement updateSeatsStatement = connection.prepareStatement(
                        "UPDATE flights SET available_seats = available_seats - 1 WHERE flight_number = ?"
                );
                updateSeatsStatement.setString(1, flightNumber);
                updateSeatsStatement.executeUpdate();
            } else {
                System.out.println("No available seats for the selected flight. Reservation failed.");
                return;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    if (selectedFlight != null) {
        // Create a new passenger and make a reservation
        Passenger newPassenger = new Passenger(name, age, gender, contactNumber, flightNumber, connection);
        selectedFlight.bookSeat(newPassenger);
        passengers.add(newPassenger);
        System.out.println("Reservation successful. Your booking reference: " + newPassenger.getBookingReference());
    } else {
        System.out.println("Invalid flight number. Reservation failed.");
    }
}


    //********************************************************************************************************8
    public void cancelReservation() {
        System.out.println("Enter passenger details:");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        Passenger passengerToRemove = null;

        for (Passenger passenger : passengers) {
            if (passenger.getName().equals(name)) {
                passengerToRemove = passenger;
                break;
            }
        }

        if (passengerToRemove != null) {
            try {
                // Remove passenger from the database
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM passengers WHERE id = ?"
                );
                preparedStatement.setInt(1, passengerToRemove.getId());
                preparedStatement.executeUpdate();

                passengers.remove(passengerToRemove);
                System.out.println("Reservation canceled successfully.");
            } catch (SQLException e) {
                handleSQLException(e);
                System.out.println("Failed to cancel reservation.");
            }
        } else {
            System.out.println("Passenger not found. Cancellation failed.");
        }
    }




//    ******************************************************************************************************
    public void displayPassengerDetails() {
        try {
            System.out.print("Enter passenger name: ");
            String name = scanner.nextLine();

            try {
                // Try fetching passenger details from the database
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT * FROM passengers WHERE name = ?"
                );
                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Passenger passenger = createPassengerFromResultSet(resultSet);
                    passenger.displayDetails();
                    return;
                }
            } catch (SQLException e) {
                // Handle SQLException when interacting with the database
                e.printStackTrace();
            }

            // If not found in the database, search in the in-memory list
            for (Passenger passenger : passengers) {
                if (passenger.getName().equals(name)) {
                    passenger.displayDetails();
                    return;
                }
            }

            System.out.println("Passenger not found.");
        } catch (Exception e) {
            // Handle any other exceptions that might occur during the process
            e.printStackTrace();
        }
    }


    // ... existing code ...
//    ***************************************************************************************

    public void addFlightByUser() {

        System.out.println("Are you sure you want to add the flight?");
        System.out.print("Enter 'Yes' to confirm, 'No' to cancel: ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Yes")) {

            System.out.println("Enter details for the new flight:");
            System.out.print("Flight Number: ");
            String flightNumber = scanner.nextLine();
            System.out.print("Departure City: ");
            String departureCity = scanner.nextLine();
            System.out.print("Destination City: ");
            String destinationCity = scanner.nextLine();
            System.out.print("Departure Time: ");
            String departureTime = scanner.nextLine();
            System.out.print("Arrival Time: ");
            String arrivalTime = scanner.nextLine();
            System.out.print("Available Seats: ");
            int availableSeats = scanner.nextInt();

            scanner.nextLine(); // Consume the newline character
            System.out.println("Flight Number: " + flightNumber);
            System.out.println("Departure City: " + departureCity);
            System.out.println("Destination City: " + destinationCity);
            System.out.println("Departure Time: " + departureTime);
            System.out.println("Arrival Time: " + arrivalTime);
            System.out.println("Available Seats: " + availableSeats);
            Flight newFlight = new Flight(flightNumber, departureCity, destinationCity, departureTime, arrivalTime, availableSeats, connection);
            addFlight(newFlight);
            System.out.println("New flight added successfully.");
        } else {
            System.out.println("Flight addition canceled.");
        }
//        System.out.println("Are you sure you want to add the following flight?");
//        System.out.println("Flight Number: " + flightNumber);
//        System.out.println("Departure City: " + departureCity);
//        System.out.println("Destination City: " + destinationCity);
//        System.out.println("Departure Time: " + departureTime);
//        System.out.println("Arrival Time: " + arrivalTime);
//        System.out.println("Available Seats: " + availableSeats);
//        System.out.print("Enter 'Yes' to confirm, 'No' to cancel: ");
//
//        String confirmation = scanner.nextLine();
//
//        if (confirmation.equalsIgnoreCase("Yes")) {
//            Flight newFlight = new Flight(flightNumber, departureCity, destinationCity, departureTime, arrivalTime, availableSeats, connection);
//            addFlight(newFlight);
//            System.out.println("New flight added successfully.");
//        } else {
//            System.out.println("Flight addition canceled.");
//        }
    }



//*******************************************************************************************************
    private boolean isFlightNumberExists(String flightNumber) {
        for (Flight flight : flights) {
            if (flight.getFlightNumber().equals(flightNumber)) {
                return true; // Flight with the given flight number already exists
            }
        }
        return false;
    }


//*******************************************************************************************************
    private Flight createFlightFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String flightNumber = resultSet.getString("flight_number");
        String departureCity = resultSet.getString("departure_city");
        String destinationCity = resultSet.getString("destination_city");
        String departureTime = resultSet.getString("departure_time");
        String arrivalTime = resultSet.getString("arrival_time");
        int availableSeats = resultSet.getInt("available_seats");

        return new Flight(flightNumber, departureCity, destinationCity, departureTime, arrivalTime, availableSeats, connection);
    }

    private Passenger createPassengerFromResultSet(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        String gender = resultSet.getString("gender");
        String contactNumber = resultSet.getString("contact_number");
        String bookingReference = resultSet.getString("booking_reference");
        int flightId = resultSet.getInt("flightId");

        // Validate that the flightId exists in the flights table
        if (isFlightIdValid(flightId)) {
            return new Passenger(name, age, gender, contactNumber, bookingReference, flightId, connection);
        } else {
            // Handle the case where the flightId is not valid
            System.out.println("Invalid flightId for passenger: " + name);
            return null;
        }
    }

//****************************************************************************************************

    private boolean isFlightIdValid(int flightId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM flights WHERE id = ?"
            );
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // true if the flightId exists in the flights table
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    private void handleSQLException(SQLException e) {
    }

    public void updatePassenger() {
        System.out.println("Enter passenger details for update:");
        System.out.print("Existing Name: ");
        String existingName = scanner.nextLine();
        System.out.print("New Name: ");
        String newName = scanner.nextLine();
        System.out.print("New Age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        System.out.print("New Gender: ");
        String newGender = scanner.nextLine();
        System.out.print("New Contact Number: ");
        String newContactNumber = scanner.nextLine();

        try {
            // Update passenger information in the database
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE passengers SET name = ?, age = ?, gender = ?, contact_number = ? WHERE name = ?"
            );
            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newAge);
            preparedStatement.setString(3, newGender);
            preparedStatement.setString(4, newContactNumber);
            preparedStatement.setString(5, existingName);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Passenger information updated successfully.");
            } else {
                System.out.println("Passenger not found. Update failed.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

//    ********************************************************************************************
    public void showAllPassengers () {
        System.out.println("All Passengers:");
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM passengers"
            );
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Passenger passenger = createPassengerFromResultSet(resultSet);
                passenger.displayDetails();
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

    }
//****************************************************************************8
    public static void deletePassenger(int id) {
        String query = "delete from passengers where id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, id);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected>0) {
                System.out.println("Passenger deleted successfully");
            }else {
                System.out.println("No Passenger found!!!");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void deleteFlight(int ID) {
        String query = "delete from flights where id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1, ID);
            int rowAffected = preparedStatement.executeUpdate();
            if(rowAffected>0) {
                System.out.println("Flights deleted successfully");
            }else {
                System.out.println("No Flights found!!!");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

