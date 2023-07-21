/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mingl
 */
public class MinibusTerminal {

    private int maxCapacity = 15;
    private int availableCapacity = (int) (15 * 0.8);
    private boolean isFull = false;
    
    List<Customer> customerList;

    public MinibusTerminal() {
        customerList = new LinkedList<Customer>();
    }

    public void add(Customer customer) {
        synchronized (customerList) {
            if (customerList.size() == maxCapacity) {
                isFull = true;
                System.out.println("No chair available for customer " + customer.getID());
                return;
            }
            
            if(isFull){
                System.out.println("Please wait until the terminal capacity reach 80%.");
                if(customerList.size() <= availableCapacity){
                    isFull = false;
                }
                return;
            }
        }
        ((LinkedList<Customer>) customerList).offer(customer);

        if (customerList.size() == 1) {
            customerList.notify(); // This will resolves the wait(), which also resolve the deadlock occuring before.
        }
    }

}
