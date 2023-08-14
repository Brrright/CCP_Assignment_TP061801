/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author mingl
 */
public class MinibusTerminal {

    private static final  int TERMINAL_MAX_CAPACITY = 15;
    private int MIN_AVAILABLE_CAPACITY = (int) (15 * 0.8);
    private int WAITING_AREA_CAPACITY = 10;
    private boolean isFull = false;
    
    // Shared resources
    private final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_MAX_CAPACITY);
    private final BlockingQueue<Customer> waitingAreaQueue = new ArrayBlockingQueue<>(WAITING_AREA_CAPACITY);
    
    List<Customer> terminalCustomerList;
    List<Customer> westCustomerList;
    List<Customer> eastCustomerList;

    public MinibusTerminal() {
        westCustomerList = new LinkedList<Customer>();
    }

    public void add(Customer customer) {
        synchronized (westCustomerList) {
            if (westCustomerList.size() == TERMINAL_MAX_CAPACITY) {
                isFull = true;
                System.out.println("No chair available for customer " + customer.getID());
                return;
            }
            
            if(isFull){
                System.out.println("Please wait until the terminal capacity reach 80%.");
                if(westCustomerList.size() <= MIN_AVAILABLE_CAPACITY){
                    isFull = false;
                }
                return;
            }
        }
        ((LinkedList<Customer>) westCustomerList).offer(customer);

        if (westCustomerList.size() == 1) {
            westCustomerList.notify(); // This will resolves the wait(), which also resolve the deadlock occuring before.
        }
    }

}
