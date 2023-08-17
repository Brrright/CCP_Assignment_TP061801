/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import static Model.MinibusTerminal.WEST_ENTRANCE;
import static Model.MinibusTerminal.terminalQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author mingl
 */
public class Customer implements Runnable {

    public enum Status {
        WAITING,
        BEING_SERVED,
        SERVED
    }

    private Status status = Status.WAITING;
    private int customerID;
    private MinibusTerminal terminal;
    private Ticket ticket;
    private final long timestamp;
    private AtomicBoolean hasTicket = new AtomicBoolean(false);

    public Customer(MinibusTerminal terminal, int id) {
        this.terminal = terminal;
        this.customerID = id;
        this.timestamp = System.currentTimeMillis();
    }

    public int getID() {
        return this.customerID;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public AtomicBoolean getHasTicket() {
        return this.hasTicket;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public void setHasTicket(boolean value) {
        this.hasTicket.set(value);
    }

    public void resetTicketStatus() {
        this.status = Customer.Status.WAITING;
        this.ticket = null;
        this.hasTicket.set(false);
    }

    @Override
    public void run() {
    }

    public void buyTicket() {
        if (hasTicket.get() || status != Status.BEING_SERVED) {
            return;
        }
        Destination destination = Destination.values()[new Random().nextInt(Destination.values().length)];
        this.ticket = new Ticket(destination, this);
        this.hasTicket.set(true);
        System.out.println("[Customer] Customer " + customerID + " bought a ticket for " + destination);
    }

    public synchronized void enterTerminalFromEntrance(int entrance) throws InterruptedException {
        if (MinibusTerminal.isClosed.get()) {
            return; // if terminal is closed, don't process the customer
        }
        while (!MinibusTerminal.isClosed.get()) {
            if (terminalQueue.remainingCapacity() < 1) {
                MinibusTerminal.isFull.set(true);
            }

            System.out.println("[Customer] Customer " + customerID + " is coming from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
            if (MinibusTerminal.isFull.get()) {
                if (MinibusTerminal.terminalQueue.size() > MinibusTerminal.MIN_AVAILABLE_CAPACITY) {
                    System.out.println("[Terminal] Still a lot of people, please wait for a while.");
                    System.out.println("[Customer] Customer " + customerID + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                    if (entrance == WEST_ENTRANCE) {
                        MinibusTerminal.westEntranceQueue.add(this);
                    } else {
                        MinibusTerminal.eastEntranceQueue.add(this);
                    }
                    break;
                } else {
                    System.out.println("[Terminal]: Terminal capacity has been dropped to " + terminalQueue.size() + ". Welcome next customer!");
                    MinibusTerminal.isFull.set(false);
                }
            }
            synchronized (MinibusTerminal.terminalQueue) {
                if (terminalQueue.remainingCapacity() > 0) {
                    terminalQueue.add(this);
                    System.out.println("[Customer] Customer " + customerID + " has entered the terminal foyer from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance. (Terminal: " + MinibusTerminal.terminalQueue.size() + "/" + MinibusTerminal.TERMINAL_MAX_CAPACITY + ").");
                    run();  // Directly proceed with the rest of the logic
                    return;
                } else {
                    System.out.println("[Terminal] Terminal is full!");
                    System.out.println("[Customer] Customer " + customerID + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                    if (entrance == WEST_ENTRANCE) {
                        MinibusTerminal.westEntranceQueue.add(this);
                    } else {
                        MinibusTerminal.eastEntranceQueue.add(this);
                    }
                    break;
                }
            }
        }
    }
}
