/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Iterator;
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
    public static int MIN_AVAILABLE_CAPACITY = (int) (15 * 0.8); // 80% //12

    public static AtomicBoolean isFull = new AtomicBoolean(false);
    public static AtomicBoolean isClosed = new AtomicBoolean(false);
    public static AtomicBoolean isAcceptingNewEntries = new AtomicBoolean(true);

    public static final int WEST_ENTRANCE = 1;
    public static final int EAST_ENTRANCE = 2;
    public static final Queue<Customer> westEntranceQueue = new LinkedList<>();
    public static final Queue<Customer> eastEntranceQueue = new LinkedList<>();

    public static final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_MAX_CAPACITY);
    private final WaitingArea waitingAreaA = new WaitingArea("Area_A");
    private final WaitingArea waitingAreaB = new WaitingArea("Area_B");
    private final WaitingArea waitingAreaC = new WaitingArea("Area_C");

    public static void releaseEntranceQueue() {
        System.out.println("[Customer] Customers outside are going back home...");
        westEntranceQueue.clear();
        eastEntranceQueue.clear();
    }

    public synchronized Customer getFirstWaitingCustomerAndSetBeingServed() {
        Iterator<Customer> iterator = terminalQueue.iterator();
        while (iterator.hasNext()) {
            Customer c = iterator.next();
            if (c.getStatus() == Customer.Status.WAITING) {
                c.setStatus(Customer.Status.BEING_SERVED);
                return c;
            }
        }
        return null;
    }

    public synchronized void moveCustomerToWaitingArea(Customer customer) throws InterruptedException {
        printTerminalAndWaitingAreaCapacity();

        if (null != customer.getTicket().getDestination()) {
            switch (customer.getTicket().getDestination()) {
                case DESTINATION_A:
                    if (!waitingAreaA.addToWaitingArea(customer)) { // If offer returns false, queue is full
                        System.out.println("[WaitingArea] Waiting area for destination " + customer.getTicket().getDestination() + " is full! Customer " + customer.getID() + " is waiting in the foyer.");
                    } else {
                        System.out.println("[Customer] Customer " + customer.getID() + " has entered Waiting Area A. (" + waitingAreaA.getQueue().size() + "/" + WaitingArea.WAITING_AREA_CAPACITY + ")");
                        terminalQueue.remove(customer);
                    }
                    break;
                case DESTINATION_B:
                    if (!waitingAreaB.addToWaitingArea(customer)) {
                        System.out.println("[WaitingArea] Waiting area for destination " + customer.getTicket().getDestination() + " is full! Customer " + customer.getID() + " is waiting in the foyer.");
                    } else {
                        System.out.println("[Customer] Customer " + customer.getID() + " has entered Waiting Area B. (" + waitingAreaB.getQueue().size() + "/" + WaitingArea.WAITING_AREA_CAPACITY + ")");
                        terminalQueue.remove(customer);
                    }
                    break;
                case DESTINATION_C:
                    if (!waitingAreaC.addToWaitingArea(customer)) {
                        System.out.println("[WaitingArea] Waiting area for destination " + customer.getTicket().getDestination() + " is full! Customer " + customer.getID() + " is waiting in the foyer.");
                    } else {
                        System.out.println("[Customer] Customer " + customer.getID() + " has entered Waiting Area C. (" + waitingAreaC.getQueue().size() + "/" + WaitingArea.WAITING_AREA_CAPACITY + ")");
                        terminalQueue.remove(customer);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void add(Customer customer) throws InterruptedException {
        synchronized (this) {
            while (terminalQueue.size() == TERMINAL_MAX_CAPACITY) {
                isFull.set(true);
                wait();
            }
        }
        terminalQueue.add(customer);
        synchronized (this) {
            notifyAll();
        }
    }

    public WaitingArea getWaitingAreaA() {
        return this.waitingAreaA;
    }

    public WaitingArea getWaitingAreaB() {
        return this.waitingAreaB;
    }

    public WaitingArea getWaitingAreaC() {
        return this.waitingAreaC;
    }

    public void printTerminalAndWaitingAreaCapacity() {
        System.out.println("-------------------------------------------------------");
        System.out.println("Summary report for current terminal situation");
        System.out.println("[TMN]  capacity : " + terminalQueue.size());
        System.out.println("[WAA]  capacity : " + waitingAreaA.getQueue().size());
        System.out.println("[WAB]  capacity : " + waitingAreaB.getQueue().size());
        System.out.println("[WAC]  capacity : " + waitingAreaC.getQueue().size());
        System.out.println("-------------------------------------------------------");

    }
}
