/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author mingl
 */
public class TicketInspector implements Runnable{
    @Override
        public void run() {
//            try {
//                while (true) {
//                    for (BlockingQueue<Customer> waitingArea : waitingAreas) {
//                        if (!waitingArea.isEmpty()) {
//                            Customer customer = waitingArea.poll();
//                            if (customer.hasTicket) {
//                                System.out.println("[Inspector] Customer " + customer.id + " has a valid ticket and can board the minibus.");
//                            } else {
//                                System.out.println("[Inspector] Customer " + customer.id + " was denied boarding due to not having a ticket.");
//                            }
//                        }
//                        Thread.sleep(1000); // sleep for a while before checking the next waiting area
//                    }
//                }
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
        }
}
