package com.project_team5.ams.flightAPI.model;



import jakarta.persistence.*;

@Entity
@Table(name="passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String bookingReference;
    @ManyToOne
    @JoinColumn(name = "flightid", insertable = false, updatable = false)
    private Flight flight;

    private int flightid;

    public Passenger(int id, String name, int age, String gender, String contactNumber, String bookingReference, int flightid) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.bookingReference = bookingReference;
        this.flightid = flightid;
    }

    public Passenger() {

    }

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

    public int getFlightid() {
        return flightid;
    }

    public void setFlightid(int flightid) {
        this.flightid = flightid;
    }
    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void bookFlight(Passenger passenger) {
    }
    private void generateBookingReference() {
        // Implement your logic to generate a unique booking reference (e.g., using timestamps or random strings)
        this.bookingReference = "BR" + System.currentTimeMillis();
    }

}

