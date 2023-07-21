/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Date;

/**
 *
 * @author mingl
 */
public class Customer implements Runnable {

    private String customerID;
    private Date inTime;
    private MinibusTerminal terminal;

    public Customer(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    public String getID() {
        return this.customerID;
    }

    public Date getInTime() {
        return this.inTime;
    }

    public void setID(String ID) {
        this.customerID = ID;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    @Override
    public void run() {
        System.out.println("Customer "+ this.customerID + " is cominggg.");
        
        // enter terminal (destination)
        // buyTicket
        // go to waiting area
        // inspect ticket
        // enter bus
    }

    private synchronized void buyTicket() {
        terminal.add(this);
    }

}
