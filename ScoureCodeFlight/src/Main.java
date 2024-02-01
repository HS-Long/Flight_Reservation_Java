import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ReservationSystem reservationSystem = new ReservationSystem();

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);

        while (!exit) {
            System.out.println("+----------------------------------------+");
            System.out.println("|   ==== Flight Reservation System ====  |");
            System.out.println("+----------------------------------------+\n");

            System.out.println("+----------------------------------------+");
            System.out.println("|   ==> For Passenger Booking    <==     |");
            System.out.println("+----------------------------------------+");
            System.out.println("| 1. Display Available Flights           |");
            System.out.println("| 2. Search Flight                       |");
            System.out.println("| 3. Make Reservation                    |");
            System.out.println("| 4. Cancel Reservation                  |");
            System.out.println("+----------------------------------------+");
            System.out.println("|   ==> For Admin Controll     <==       |");
            System.out.println("+----------------------------------------+");
            System.out.println("| 5. Display Passenger Details           |");
            System.out.println("| 6. Show All Passengers                 |");
            System.out.println("| 7. Update Passenger Information        |");
            System.out.println("| 8. Delete Passenger                    |");
            System.out.println("| 9. Delete Flight                       |");
            System.out.println("| 10. Add Flight                         |");
            System.out.println("| 11. Exit                               |");
            System.out.println("+----------------------------------------+");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    reservationSystem.displayFlightDetails();
                    break;
                case 2:
                    System.out.print("Enter flight number to search: ");
                    String flightNumber = scanner.nextLine();
                    reservationSystem.searchFlight(flightNumber);
                    break;
                case 3:
                    reservationSystem.makeReservation();
                    break;
                case 4:
                    reservationSystem.cancelReservation();
                    break;
                case 5:
                    reservationSystem.displayPassengerDetails();
                    break;
                case 6:
                    reservationSystem.showAllPassengers();
                    break;
                case 7:
                    reservationSystem.updatePassenger();
                    break;
                case 8:
                    System.out.print("Please Enter Passenger ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    reservationSystem.deletePassenger(id);
                    break;
                case 9:
                    System.out.print("Please Enter Flight ID: ");
                    int flightID = scanner.nextInt();
                    scanner.nextLine();
                    reservationSystem.deleteFlight(flightID);
                    break;
                case 10:
                    reservationSystem.addFlightByUser();
                    break;
                case 11:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }

        scanner.close();
        System.out.println("Exiting the Flight Reservation System. Thank you!");
    }
}
