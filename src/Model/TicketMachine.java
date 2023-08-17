/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author mingl
 */
public class TicketMachine implements Runnable {

    private MinibusTerminal terminal;

    public TicketMachine(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            Customer customer = terminal.getFirstWaitingCustomer();
            if (customer != null) {
                System.out.println("[Customer] Customer " + customer.getID() + " found Ticket Machine available");
                System.out.println("[Customer] Customer " + customer.getID() + " is buying ticket from Ticket Machine");
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
