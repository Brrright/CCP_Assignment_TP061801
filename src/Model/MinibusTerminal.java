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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author mingl
 */
public class MinibusTerminal {

    public static final int TERMINAL_MAX_CAPACITY = 15;
    public static final int FOYER_QUEUE_SIZE = 6;
    public static final int BUY_TICKET_QUEUE_SIZE = 3;

    public static int MIN_AVAILABLE_CAPACITY = (int) (15 * 0.8);
    public static int WAITING_AREA_CAPACITY = 10;
    public static AtomicBoolean isFull = new AtomicBoolean(false);
    public static AtomicBoolean isClosed = new AtomicBoolean(false);

    public static final int WEST_ENTRANCE = 1;
    public static final int EAST_ENTRANCE = 2;

    // Shared resources
    public static final Queue<Customer> westEntranceQueue = new LinkedList<>();
    public static final Queue<Customer> eastEntranceQueue = new LinkedList<>();
    public static final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_MAX_CAPACITY);

    public static void releaseEntranceQueue() {
        System.out.println("Customers going back home...");
        westEntranceQueue.clear();
        eastEntranceQueue.clear();
    }

    public synchronized void add(Customer customer) {
        if (terminalQueue.size() == TERMINAL_MAX_CAPACITY) {
            isFull.set(true);
        } else {
            terminalQueue.add(customer);

        }

    }
}

//      <editor-fold>
//        synchronized (westCustomerList) {
//            if (isFull) {
//                System.out.println("Please wait until the terminal capacity reach 80%.");
//                if (westCustomerList.size() <= MIN_AVAILABLE_CAPACITY) {
//                    isFull = false;
//                }
//                return;
//            }
//        }
//        ((LinkedList<Customer>) westCustomerList).offer(customer);
//        if (westCustomerList.size() == 1) {
//            westCustomerList.notify(); // This will resolves the wait(), which also resolve the deadlock occuring before.
//        }
//       </editor-fold>
