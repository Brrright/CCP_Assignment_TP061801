/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import static Model.MinibusTerminal.EAST_ENTRANCE;
import static Model.MinibusTerminal.WEST_ENTRANCE;
import static Model.MinibusTerminal.terminalQueue;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author mingl
 */
public class Customer implements Runnable {

    private int customerID;
    private MinibusTerminal terminal;
    public boolean hasTicket;

    public Customer(MinibusTerminal terminal, int id) {
        this.terminal = terminal;
        this.customerID = id;
    }

    public int getID() {
        return this.customerID;
    }

    @Override
    public void run() {
        setEntrance(this);
    }

    private synchronized void buyTicket() {

    }

    public void setEntrance(Customer c) {
        if (new java.util.Random().nextBoolean()) {
            Thread newCust = new Thread(() -> {
                try {
                    c.enterTerminalFromEntrance(WEST_ENTRANCE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            newCust.start();
            try {
                newCust.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        } else {
            Thread newCust = new Thread(() -> {
                try {
                    c.enterTerminalFromEntrance(EAST_ENTRANCE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            newCust.start();
        }
    }

    public synchronized void enterTerminalFromEntrance(int entrance) throws InterruptedException {
        while (!MinibusTerminal.isClosed.get()) {
            if (terminalQueue.remainingCapacity() < 1) {
                MinibusTerminal.isFull.set(true);
            }
            System.out.println("[Customer] Customer " + customerID + " is coming from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
            if (MinibusTerminal.isFull.get()) {
                if (MinibusTerminal.terminalQueue.remainingCapacity() < MinibusTerminal.MIN_AVAILABLE_CAPACITY) {
//                    System.out.println("terminal queue size: " + terminalQueue.size());
//                    System.out.println("min_available_capacity: " + MinibusTerminal.MIN_AVAILABLE_CAPACITY);
//                    System.out.println("[Terminal] Still a lot of people, please wait until the terminal capacity become lesser than " + (terminalQueue.size() / MinibusTerminal.MIN_AVAILABLE_CAPACITY) + "customers.");
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
            if (terminalQueue.remainingCapacity() > 0) {
                terminalQueue.add(this);
                System.out.println("[Customer] Customer " + customerID + " has entered the terminal foyer from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                System.out.println("[Terminal] Foyer's capacity: " + terminalQueue.size() + " / " + MinibusTerminal.TERMINAL_MAX_CAPACITY);
                run();  // Directly proceed with the rest of the logic
                break;
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
// <editor-fold>
//        try {
//            if (terminalQueue.remainingCapacity() > 0) {
//                terminalQueue.put(this);
//                buyTicket();
//
//                int waitingAreaIndex = new Random().nextInt(3);
////                if (waitingAreas[waitingAreaIndex].offer(this)) {
////                    System.out.println("[Customer] Customer " + id + " moved to waiting area " + (char) ('A' + waitingAreaIndex));
////                } else {
////                    System.out.println("[Waiting Area] Waiting area " + (char) ('A' + waitingAreaIndex) + " is full! Customer " + id + " is waiting in the terminal.");
////                    waitingAreas[waitingAreaIndex].put(this);
////                }
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
// enter terminal (destination)
// buyTicket
// go to waiting area
// inspect ticket
// enter bus

// </editor-fold>
