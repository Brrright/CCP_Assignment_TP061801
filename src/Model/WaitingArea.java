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

    public BlockingQueue<Customer> getQueue() {
        return this.waitingAreaQueue;
    }

    public boolean isFull() {
        return waitingAreaQueue.size() == WAITING_AREA_CAPACITY;
    }

//    public void addToWaitingArea(Customer customer) throws InterruptedException {
//        waitingAreaQueue.put(customer); // This will wait until there's space available
//    }
    public synchronized void addToWaitingArea(Customer customer) throws InterruptedException {
        while (waitingAreaQueue.size() == WAITING_AREA_CAPACITY) {
            this.wait();  // wait until there's space
        }
        waitingAreaQueue.offer(customer);
        this.notifyAll();
    }

    public synchronized boolean removeFromWaitingArea(Customer customer) {
        boolean result = this.waitingAreaQueue.remove(customer);
        if (result) {
            this.notifyAll(); // Notify all waiting threads that a space has opened up
        }
        return result;
    }

    public String getName() {
        return this.name;
    }
}
