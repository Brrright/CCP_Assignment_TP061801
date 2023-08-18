/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author mingl
 */
public class Minibus implements Runnable {

    public enum BusStatus {
        ARRIVING,
        DEPARTING,
        RETURNING,
        WAITING
    }

    private AtomicBoolean hasArrived = new AtomicBoolean(false);

    public void setArrivalStatus(boolean status) {
        this.hasArrived.set(status);
    }

    public void arrive(Inspector inspector) {
        // Bus arrival logic...
        setArrivalStatus(true);
        inspector.signalArrival(); // Notifying the inspector that a bus has arrived
    }

    private static final int MAX_CAPACITY = 10;

    private String id;
    private Destination destination;
    private MinibusTerminal terminal;
    private BusStatus status;
    private long delay = 0;
    private boolean isDelayed;
    private WaitingArea waitingArea;
    private Inspector inspector;

    public Minibus(String id, Destination destination, MinibusTerminal terminal, Inspector inspector) {
        this.id = id;
        this.destination = destination;
        this.terminal = terminal;
        this.status = BusStatus.ARRIVING;
        this.isDelayed = false;
        this.inspector = inspector;
        switch (destination) {
            case DESTINATION_A:
                waitingArea = terminal.getWaitingAreaA();
                break;
            case DESTINATION_B:
                waitingArea = terminal.getWaitingAreaB();
                break;
            case DESTINATION_C:
                waitingArea = terminal.getWaitingAreaC();
                break;
        }
    }

    public void setDelay(long delay) {
        this.isDelayed = true;
        this.delay = delay;
    }

    public void setNoDelay() {
        this.isDelayed = false;
        this.delay = 0;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            try {
                switch (status) {
                    case ARRIVING:

                        System.out.println("[Bus " + id + "] is coming in 5 seconds.");
                        Thread.sleep(5000);
                        if (new Random().nextInt(10) > 2) {
                            System.out.println("[Bus " + id + "] is DELAYED for another 5 seconds.");
                            this.setDelay(5000);
                            Thread.sleep(delay);
                            this.setNoDelay(); // resetting to no delay
                        }
                        if (!isDelayed) {
                            arrive(inspector);
                        }
                        System.out.println("[Bus " + id + "] Arrived at terminal (Departure gate " + waitingArea.getName() + ") for " + destination);
                        status = BusStatus.WAITING;
                        break;
                    case WAITING: // INITIAL STATE
//                        arrive(inspector);
                        System.out.println("[Bus " + id + "] Waiting at terminal (Departure gate " + waitingArea.getName() + ") for departure to " + destination + ".  Departing in 10 seconds.");
                        // TODO: remove later
//                        Thread.sleep(3000);
                        Thread.sleep(10000);
                        status = BusStatus.DEPARTING;
                        break;
                    case DEPARTING:
                        // Take the customers from the waiting area
                        int customersServed = 0;
//                        while (waitingArea.getQueue().size() > 0) {
                        for (int i = 0; i < waitingArea.getQueue().size(); i++) {
                            // later for inspector to check here maybe?
                            Customer customer = waitingArea.getQueue().poll();
                            if (!customer.getTicket().isCheckedByInspector()) {
                                System.out.println("[Bus " + id + "] Customer " + customer.getID() + "'s ticket haven't check by inspector, go back to waiting area.");
                                waitingArea.addToWaitingArea(customer);
                                continue;
                            }
                            System.out.println("[Bus " + id + "] Customer " + customer.getID() + " onboarding the bus " + this.id);
                            customersServed++;
                        }
//                        if(customersServed == 0){
//                            System.out.println("[Bus " + id + "] Hmm, seems like there is no customer, let's wait next round.");
//                            continue;
                        // this need to wait and get notify once the waiting area got 10 ppl is checked by inpector :v oh nooo
//                        }
                        System.out.println("[Bus " + id + "] Lesgo! Departing to " + destination + " (Customer onboarded: " + customersServed + ")");
                        Thread.sleep(5000);
                        System.out.println("[Bus " + id + "] Delivered " + customersServed + " customers to " + destination);
                        status = BusStatus.RETURNING;
                        break;
                    case RETURNING:
                        System.out.println("[Bus " + id + "] Returning from " + destination);
                        Thread.sleep(5000);
                        status = BusStatus.WAITING;
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
