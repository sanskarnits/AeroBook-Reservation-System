# ✈️ AeroBook - Concurrent Airline Reservation Engine

## 📌 Overview
AeroBook is a high-performance, backend-focused Airline Reservation System built in Core Java. This project addresses critical real-world challenges in enterprise software development, specifically focusing on **thread safety during concurrent transactions** and **algorithmic dynamic pricing**.

Built as a robust simulation, AeroBook ensures data integrity in high-traffic environments, similar to real-world airline or railway booking systems.

## ✨ Advanced Features
- **Thread-Safe Seat Locking:** Implements a synchronized booking engine using Java's `synchronized` keyword. This ensures that even under heavy concurrent load (multiple users booking at the same millisecond), race conditions are prevented and seat inventory remains accurate.
- **Dynamic Pricing Engine:** Features a demand-based pricing algorithm that calculates real-time occupancy. Once a flight reaches **80% capacity**, the system automatically applies a **20% surge multiplier** to ticket fares.
- **Concurrent User Simulation:** Includes a built-in stress-test environment using the `Thread` class to simulate multiple independent users hitting the server simultaneously.
- **Advanced OOP Design:** Leverages core Object-Oriented principles, including **Encapsulation** for data protection and **Abstraction** for scalable system design.

## 🛠️ Technologies Used
- **Language:** Java (JDK 8+)
- **Concurrency:** Java Multithreading (`Thread`, `join`, `synchronized`)
- **Collections:** Java Collections Framework (ArrayList for thread management)
- **Design:** Object-Oriented Design (OOD)

## 🔌 Technical Logic
### 1. Concurrency Control
To prevent "overbooking," the `bookSeat()` method is protected by a monitor lock. This ensures that only one thread can modify the `availableSeats` variable at a time, maintaining a consistent state.

### 2. Surge Pricing Algorithm
The price is determined dynamically at the moment of the request:
```java
double occupancyRate = (double) (totalSeats - availableSeats) / totalSeats;
if (occupancyRate >= 0.8) fare = basePrice * 1.20;
