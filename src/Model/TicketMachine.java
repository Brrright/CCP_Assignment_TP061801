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
public class TicketMachine implements Runnable {

    private MinibusTerminal terminal;
    private AtomicBoolean broken = new AtomicBoolean(false);

    public TicketMachine(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            System.out.println("...machine working1");
            // if broken 
            if (broken.get()) {
                try {
                    //TODO: remove later
//                    Thread.sleep(800);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                broken.set(false);
                System.out.println("[TMachine] Restarted machine, available now!");
                continue;
            }
            System.out.println("...machine working2");
            try {
                Thread.sleep(100 + new Random().nextInt(200));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("...machine working3");
            Customer customer = terminal.getFirstWaitingCustomerAndSetBeingServed();
            if (customer != null) {
                System.out.println("[Customer] Customer " + customer.getID() + " found Ticket Machine available");
                System.out.println("[TMachine] Customer " + customer.getID() + " is buying ticket from Ticket Machine");
                if (new Random().nextInt(12) < 1) { // 1 in 12 chance for the machine to break
                    System.out.println("****************************************************");
                    System.out.println("[TMachine] Machine CRASHED! Restarting...");
                    broken.set(true);
                    System.out.println("****************************************************");
                    System.out.println("[Customer] Customer " + customer.getID() + ":  \"AIYO HAIYA, I will go to ticket booth now...\"");
                    customer.resetTicketStatus();
                    continue; // skip current iteration
                }
                try {
                    //TODO: remove later
//                    Thread.sleep(600);
                    Thread.sleep(1000);  // buy ticket
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
