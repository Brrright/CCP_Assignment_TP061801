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

    public Ticket(Destination destination, Customer customer) {
        this.isChecked = false;
        this.destination = destination;
        this.customer = customer;
    }

    public Destination getDestination() {
        return destination;
    }

    public boolean isCheckedByInspector() {
        return isChecked;
    }

    public void check() {
        this.isChecked = true;
    }
}
