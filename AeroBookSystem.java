import java.util.ArrayList;
import java.util.List;

/**
 * AeroBook - Concurrent Airline Reservation Engine
 * Demonstrates Java Multithreading, Synchronization, and Dynamic Pricing.
 */

class Flight {
    private String flightNumber;
    private int totalSeats;
    private int availableSeats;
    private double basePrice;

    public Flight(String flightNumber, int totalSeats, double basePrice) {
        this.flightNumber = flightNumber;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.basePrice = basePrice;
    }

    public String getFlightNumber() { return flightNumber; }
    public int getAvailableSeats() { return availableSeats; }

    /**
     * DYNAMIC PRICING LOGIC:
     * Automatically adjusts ticket fares based on real-time occupancy.
     */
    public double getCurrentPrice() {
        double occupancyRate = (double) (totalSeats - availableSeats) / totalSeats;
        // If 80% or more seats are filled, apply a 20% surge
        if (occupancyRate >= 0.8) {
            return basePrice * 1.20;
        }
        return basePrice;
    }

    /**
     * THREAD-SAFE BOOKING METHOD:
     * The 'synchronized' keyword prevents multiple threads from 
     * booking the same seat at the exact same millisecond.
     */
    public synchronized boolean bookSeat(String passengerName) {
        if (availableSeats > 0) {
            double pricePaid = getCurrentPrice();
            System.out.println("[SUCCESS] " + passengerName + " booked a seat on " + flightNumber + 
                               ". Fare: $" + String.format("%.2f", pricePaid));
            availableSeats--;
            return true;
        } else {
            System.out.println("[FAILED] Booking rejected for " + passengerName + ". " + flightNumber + " is FULL.");
            return false;
        }
    }
}

/**
 * PassengerThread simulates an individual user hitting the booking server.
 */
class PassengerThread extends Thread {
    private Flight flight;
    private String passengerName;

    public PassengerThread(Flight flight, String passengerName) {
        this.flight = flight;
        this.passengerName = passengerName;
    }

    @Override
    public void run() {
        // Simulating a minor network latency for realism
        try { 
            Thread.sleep((int) (Math.random() * 200)); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        flight.bookSeat(passengerName);
    }
}

public class AeroBookSystem {
    public static void main(String[] args) {
        System.out.println("=== AeroBook System: Initializing Simulation ===");
        
        // Flight with limited seats to demonstrate concurrency and pricing surge
        Flight indigoExpress = new Flight("6E-123", 5, 4500.00);
        System.out.println("Flight: " + indigoExpress.getFlightNumber() + " | Base Price: $4500.00");
        System.out.println("---------------------------------------------------");

        // Simulating 8 passengers attempting to book 5 available seats
        List<PassengerThread> users = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            users.add(new PassengerThread(indigoExpress, "User-" + i));
        }

        // Triggering concurrent booking requests
        for (PassengerThread user : users) {
            user.start();
        }

        // Ensuring the main thread waits for all transactions to complete
        for (PassengerThread user : users) {
            try { 
                user.join(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("Final Status: " + indigoExpress.getAvailableSeats() + " seats remaining.");
        System.out.println("=== Simulation Completed ===");
    }
}
