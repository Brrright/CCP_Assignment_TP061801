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
public class TicketMachine implements Runnable {

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
                    System.out.println("[TMachine] Customer " + customer.getID() + " is buying a ticket.");
                    customer.buyTicket();  // Customer purchases ticket here
                    System.out.println("[TMachine] Customer " + customer.getID() + " got ticket for " + customer.getTicket().getDestination());
                    Thread.sleep(1000); // simulate time taken to buy a ticket
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

//public class TicketMachine {
//
//    private static final AtomicBoolean working = new AtomicBoolean(true);
//
//    static boolean isWorking() {
//        return working.get();
//    }
//
//    static void setWorking(boolean status) {
//        working.set(status);
//    }
//
////    static void buyTicket(Customer customer) throws InterruptedException {
////        System.out.println("[Customer] Customer " + customer.getID() + " is buying a ticket from the Ticket Machine.");
////        Thread.sleep(500);
////        customer.hasTicket = true;
////    }
//}
