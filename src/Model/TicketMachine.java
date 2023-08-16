/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author mingl
 */
public class TicketMachine {

    private static final AtomicBoolean working = new AtomicBoolean(true);

    static boolean isWorking() {
        return working.get();
    }

    static void setWorking(boolean status) {
        working.set(status);
    }

    static void buyTicket(Customer customer) throws InterruptedException {
        System.out.println("[Customer] Customer " + customer.getID() + " is buying a ticket from the Ticket Machine.");
        Thread.sleep(500);
        customer.hasTicket = true;
    }
}
