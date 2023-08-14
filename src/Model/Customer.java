/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import static Model.MinibusTerminal.WEST_ENTRANCE;
import static Model.MinibusTerminal.terminalQueue;
import java.util.Date;

/**
 *
 * @author mingl
 */
public class Customer implements Runnable {

    private int customerID;
    private MinibusTerminal terminal;

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

        // enter terminal (destination)
        // buyTicket
        // go to waiting area
        // inspect ticket
        // enter bus
    }

    private synchronized void buyTicket() {
        terminal.add(this);
    }

    public void enterTerminalFromEntrance(int entrance) throws InterruptedException {
        if (terminalQueue.remainingCapacity() > 0) {
            System.out.println("[Customer] Customer " + customerID + " entered the terminal from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
            terminalQueue.put(this);
            run();  // Directly proceed with the rest of the logic
        } else {
            System.out.println("[Terminal] Terminal is full! Customer " + customerID + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
        }
    }

}
