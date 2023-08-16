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
public class TicketBooth {

    private String boothID;
    private static final AtomicBoolean isStaffBreak = new AtomicBoolean(false);

    public TicketBooth(String boothID) {
        this.boothID = boothID;
    }

    public String getID() {
        return this.boothID;
    }

    public void setID(String id) {
        this.boothID = id;
    }
    
    static boolean isAvailable()
    {
        return isStaffBreak.get();
    }
    
    static void setAvailable(boolean status) {
        isStaffBreak.set(status);
    }

    static void buyTicket(Customer customer) throws InterruptedException {
        System.out.println("[T_Booth] Customer " + customer.getID() + " is buying a ticket from the Ticket Booth ");
        Thread.sleep(500);
        customer.hasTicket = true;
        
    }
}
