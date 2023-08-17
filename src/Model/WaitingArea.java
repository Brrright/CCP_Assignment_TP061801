/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author mingl
 */
public class WaitingArea {

    private String name;
    public static int WAITING_AREA_CAPACITY = 10;
    private final BlockingQueue<Customer> waitingAreaQueue = new ArrayBlockingQueue<>(WAITING_AREA_CAPACITY);

    public WaitingArea(String name) {
        this.name = name;
    }
    
    public BlockingQueue<Customer> getQueue(){
        return this.waitingAreaQueue;
    }

    public boolean isFull() {
        return waitingAreaQueue.size() == WAITING_AREA_CAPACITY;
    }

    public void addToWaitingArea(Customer customer) throws InterruptedException {
        waitingAreaQueue.put(customer); // This will wait until there's space available
    }

    public void removeCustomer(Customer customer) {
        waitingAreaQueue.remove(customer);
    }
}
