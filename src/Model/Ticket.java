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
public class Ticket {

    private Destination destination;
    private Customer customer;
    private AtomicBoolean isCheckedByInspector = new AtomicBoolean(false);

    public Ticket(Destination destination, Customer customer) {
        this.destination = destination;
        this.customer = customer;
    }

    public Destination getDestination() {
        return destination;
    }

    public boolean isCheckedByInspector() {
        return isCheckedByInspector.get();
    }

    public void setCheckedByInspector(boolean value) {
        isCheckedByInspector.set(value);
    }
}
