/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Model.Customer.Status;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author mingl
 */
public class TicketBooth implements Runnable {

    private MinibusTerminal terminal;
    private AtomicBoolean toiletBreak = new AtomicBoolean(false);
    private String name;

    public TicketBooth(MinibusTerminal terminal, String name) {
        this.terminal = terminal;
        this.name = name;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            // check if toilet break.
            if (toiletBreak.get() && terminal.terminalQueue.size() != 0) {
                // Staff is on a toilet break, sleep for a while
                try {
                    //TODO: remove later
//                    Thread.sleep(500);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[T_Booth" + this.name + "] Staff: I'm back, next customer comeeee!");
                toiletBreak.set(false);
            }

            // Make the delay for checking for a waiting customer
            try {
                Thread.sleep(100 + new Random().nextInt(200));  // Sleep for a random time between 100ms to 400ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Customer customer = terminal.getFirstWaitingCustomerAndSetBeingServed();

            // set toilet break
            if ((!MinibusTerminal.isClosed.get()) && customer == null && new Random().nextInt(12) == 0 && terminal.terminalQueue.size() != 0) { // 1/15chance for a toilet break if not serving
                System.out.println("***********************************************************************************");
                System.out.println("[T_Booth" + this.name + "] Let's take a toilet break. Ticket Booth " + this.name + " NOT available now, please wait.");
                System.out.println("***********************************************************************************");

                toiletBreak.set(true);
            }

            if (customer != null) {
                customer.setStatus(Customer.Status.BEING_SERVED);
                System.out.println("[Customer] Customer " + customer.getID() + " found Ticket Booth " + this.name + " available");
                System.out.println("[T_Booth] Customer " + customer.getID() + " is buying ticket from Ticket Booth " + this.name);
                try {
                    //TODO: remove later
//                    Thread.sleep(600);
                    Thread.sleep(2000); // buying tickets
                    customer.buyTicket();
                    customer.setStatus(Customer.Status.SERVED);
                    terminal.moveCustomerToWaitingArea(customer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                // If no customer, they can wait a bit before checking again.
                try {
                    Thread.sleep(200); // set a interval
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
