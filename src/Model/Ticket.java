/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author mingl
 */
public class Ticket {
    private boolean isChecked;
    private Destination destination;
    private Customer customer;

    public Ticket(boolean isChecked, Destination destination, Customer customer) {
        this.isChecked = isChecked;
        this.destination = destination;
        this.customer = customer;
    }
}
