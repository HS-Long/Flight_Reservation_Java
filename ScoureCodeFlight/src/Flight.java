import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Flight {
    private int id; // Added to store a unique identifier for the flight
    private String flightNumber;
    private String departureCity;
    private String destinationCity;
    private String departureTime;
    private String arrivalTime;
    private int availableSeats;
    private Connection connection;

    public Flight(String flightNumber, String departureCity, String destinationCity, String departureTime, String arrivalTime, int availableSeats, Connection connection) {
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = availableSeats;
        this.connection = connection;
    }

    // Getters and setters...


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }



    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void bookSeat(Passenger passenger) {
        try {
            if (availableSeats > 0) {
                passenger.setBookingReference(flightNumber + passenger.getName().hashCode());
                availableSeats--;

                // Update the database to reflect the booked seat
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE flights SET available_seats = ? WHERE id = ?"
                );
                preparedStatement.setInt(1, availableSeats);
                preparedStatement.setInt(2, id);
                preparedStatement.executeUpdate();

                System.out.println("Reservation successful. Your booking reference: " + passenger.getBookingReference());
            } else {
                System.out.println("Sorry, no available seats on this flight.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void displayDetails() {
        System.out.println("+------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
        System.out.println("|  Flight Number   |  Departure City     |  Destination City   |  Departure Time     |  Arrival Time       |  Available Seats    |");
        System.out.println("+------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
        System.out.printf("| %-16s | %-19s | %-19s | %-19s | %-19s | %-19d |%n", flightNumber, departureCity, destinationCity, departureTime,arrivalTime, availableSeats);
        System.out.println("+------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
    }

    private void handleSQLException(SQLException e) {
        // Handle SQLException, e.g., log the error or display a user-friendly message
        e.printStackTrace();
    }
}
