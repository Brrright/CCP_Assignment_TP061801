/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

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

//    private void buyTicket() throws InterruptedException
//    {
//        
//    }
    @Override
    public void run() {
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
    }

    private synchronized void buyTicket() {

    }

    private void releaseQueue() {
        System.out.println("Customers going back home...");
        MinibusTerminal.westEntranceQueue.clear();
        MinibusTerminal.eastEntranceQueue.clear();
    }

    public synchronized void enterTerminalFromEntrance(int entrance) throws InterruptedException {
        while (!MinibusTerminal.isClosed) {
            if (terminalQueue.remainingCapacity() > 0) {
                System.out.println("[Customer] Customer " + customerID + " is coming from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                terminalQueue.add(this);
                System.out.println("[Customer] Customer " + customerID + " has entered the terminal foyer from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                System.out.println("[Terminal] Foyer's capacity: " + terminalQueue.size() + " / " + MinibusTerminal.TERMINAL_MAX_CAPACITY);
                run();  // Directly proceed with the rest of the logic
                break;
            } else {
                System.out.println("[Terminal] Terminal is full! Customer " + customerID + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                if (entrance == WEST_ENTRANCE) {
                    MinibusTerminal.westEntranceQueue.add(this);
                } else {
                    MinibusTerminal.eastEntranceQueue.add(this);
                }
                break;
            }
        }
        if (MinibusTerminal.isClosed) {
            releaseQueue();
            return;
        }
    }

}
