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

    private final Queue<Customer> customers = new LinkedList<>();
    private final Lock lock = new ReentrantLock();

    public void addCustomer(Customer customer) {
        lock.lock();
        try {
            customers.add(customer);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            lock.lock();
            try {
                if (!customers.isEmpty()) {
                    Customer customer = customers.poll();
                    System.out.println("[T_Booth] Customer " + customer.getID() + " is buying a ticket.");
                    Thread.sleep(2000); // simulate time taken to buy a ticket
//                    customer.hasTicket = true;
                } else {
                    try {
                        Thread.sleep(500); // sleep for half a second before checking the queue again
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
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
