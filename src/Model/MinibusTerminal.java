/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author mingl
 */
public class MinibusTerminal {

    public static final int TERMINAL_MAX_CAPACITY = 15;
    public static int MIN_AVAILABLE_CAPACITY = (int) (15 * 0.8); // 80%

    public static AtomicBoolean isFull = new AtomicBoolean(false);
    public static AtomicBoolean isClosed = new AtomicBoolean(false);

    public static final int WEST_ENTRANCE = 1;
    public static final int EAST_ENTRANCE = 2;
    public static final Queue<Customer> westEntranceQueue = new LinkedList<>();
    public static final Queue<Customer> eastEntranceQueue = new LinkedList<>();

//    public static final TicketMachine ticketMachine = new TicketMachine(this);
//    public static final TicketBooth ticketBooth1 = new TicketBooth();
//    public static final TicketBooth ticketBooth2 = new TicketBooth();

    public static final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_MAX_CAPACITY);

    public static void releaseEntranceQueue() {
        System.out.println("[Customer] Customers outside are going back home...");
        westEntranceQueue.clear();
        eastEntranceQueue.clear();
    }

    public Customer getNextCustomer() {
        return terminalQueue.poll();
    }

    public synchronized void add(Customer customer) throws InterruptedException {
        while (terminalQueue.size() == TERMINAL_MAX_CAPACITY) {
            isFull.set(true);
            wait();
        }
            terminalQueue.add(customer);
            notifyAll();
    }
}
