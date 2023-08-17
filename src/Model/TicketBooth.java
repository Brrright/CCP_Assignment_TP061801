/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author mingl
 */
public class TicketBooth implements Runnable {

    private MinibusTerminal terminal;
    private String name;

    public TicketBooth(MinibusTerminal terminal, String name) {
        this.terminal = terminal;
        this.name = name;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            Customer customer = terminal.getNextCustomer();
            if (customer != null) {
                System.out.println("[Customer] Customer " + customer.getID() + " found Ticket Booth " + this.name + " available");
                System.out.println("[Customer] Customer " + customer.getID() + " is buying ticket from Ticket Booth " + this.name);
                try {
                    //TODO: remove later
                    Thread.sleep(600);
//                    Thread.sleep(3000);  // Simulate time for buying a ticket
                    customer.buyTicket();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // If no customer, they can wait a bit before checking again.
                try {
                    Thread.sleep(200);  // Sleep for 200ms before checking again
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
//public class TicketBooth {
//
//    private String boothID;
//    private static final AtomicBoolean isStaffBreak = new AtomicBoolean(false);
//
//    public TicketBooth(String boothID) {
//        this.boothID = boothID;
//    }
//
//    public String getID() {
//        return this.boothID;
//    }
//
//    public void setID(String id) {
//        this.boothID = id;
//    }
//    
//    static boolean isAvailable()
//    {
//        return isStaffBreak.get();
//    }
//    
//    static void setAvailable(boolean status) {
//        isStaffBreak.set(status);
//    }
//
//    static void buyTicket(Customer customer) throws InterruptedException {
//        System.out.println("[T_Booth] Customer " + customer.getID() + " is buying a ticket from the Ticket Booth ");
//        Thread.sleep(500);
//        customer.hasTicket = true;
//        
//    }
//}
