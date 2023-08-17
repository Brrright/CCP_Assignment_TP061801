/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Model.Customer.Status;
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
            Customer customer = terminal.getFirstWaitingCustomer();


            if (customer != null && customer.getStatus() == Status.WAITING) {
                customer.setStatus(Customer.Status.BEING_SERVED);
                System.out.println("[Customer] Customer " + customer.getID() + " found Ticket Booth " + this.name + " available");
                System.out.println("[Customer] Customer " + customer.getID() + " is buying ticket from Ticket Booth " + this.name);
                try {
                    //TODO: remove later
                    Thread.sleep(600);
//                    Thread.sleep(3000);  // Simulate time for buying a ticket
                    customer.buyTicket();
                    customer.setStatus(Customer.Status.SERVED);
                    terminal.moveCustomerToWaitingArea(customer);
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
