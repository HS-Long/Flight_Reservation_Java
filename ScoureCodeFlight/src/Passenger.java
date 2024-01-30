import java.sql.*;

public class Passenger {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String bookingReference;
    private int flightId;
    private Connection connection;

    // Constructor for creating a new passenger during reservation
    public Passenger(String name, int age, String gender, String contactNumber, String flightNumber, Connection connection) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.flightId = getFlightIdFromDatabase(flightNumber, connection);
        this.connection = connection;

        generateBookingReference();  // Generate the booking reference

        try {
            // Insert passenger data into the database
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO passengers (name, age, gender, contact_number, booking_reference, flightId) VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, this.name);
            preparedStatement.setInt(2, this.age);
            preparedStatement.setString(3, this.gender);
            preparedStatement.setString(4, this.contactNumber);
            preparedStatement.setString(5, this.bookingReference);
            preparedStatement.setInt(6, this.flightId);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.id = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Constructor for creating a passenger from the database
    public Passenger(String name, int age, String gender, String contactNumber, String bookingReference, int flightId, Connection connection) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.bookingReference = bookingReference;
        this.flightId = flightId;
        this.connection = connection;
    }

    private void generateBookingReference() {
        // Implement your logic to generate a unique booking reference (e.g., using timestamps or random strings)
        this.bookingReference = "BR" + System.currentTimeMillis();
    }

    // Getter and setter methods...


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void displayDetails() {
        System.out.println("+---------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
        System.out.println("|         Name        |         Age         |       Gender        |   Contact Number    | Booking Reference   |     Flight ID       |");
        System.out.println("+---------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
        System.out.printf("| %-19s | %-19d | %-19s | %-19s | %-19s |%-21s|%n",
                name, age, gender, contactNumber, bookingReference, flightId);
        System.out.println("+---------------------+---------------------+---------------------+---------------------+---------------------+---------------------+");
    }




    // Helper method to get flight ID from the database
    private int getFlightIdFromDatabase(String flightNumber, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT id FROM flights WHERE flight_number = ?"
            );
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return -1; // Return -1 if flight ID is not found (error condition)
    }

    private void handleSQLException(SQLException e) {
        // Handle SQLException, e.g., log the error or display a user-friendly message
        e.printStackTrace();
    }

}
