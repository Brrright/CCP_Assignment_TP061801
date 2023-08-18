/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.Queue;

/**
 *
 * @author mingl
 */
public class Inspector implements Runnable {

    private MinibusTerminal terminal;
    public final Object lock = new Object();
    private boolean shouldStop = false;

    public Inspector(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    public void signalArrival() {
        synchronized (lock) {
            lock.notify(); // Notify the inspector when a bus arrives
        }
    }

    public void stopInspection() {
        shouldStop = true;
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    private void checkTicketsInWaitingArea(WaitingArea area) {
        Queue<Customer> queue = area.getQueue();
        for (Customer customer : queue) {
            Ticket ticket = customer.getTicket();
            if (!ticket.isCheckedByInspector()) {
                // Assuming Ticket has a method to set it as checked
                ticket.setCheckedByInspector(true);
                System.out.println("[Inspector] Checked ticket for Customer " + customer.getID() + " in " + area.getName());
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (!terminal.isClosed.get() && !shouldStop()) {
            synchronized (lock) {
                while (!terminal.isClosed.get()) {
                    try {
                        lock.wait(); // Inspector waits until a bus arrives
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!shouldStop()) {
                return;
            }
            checkTicketsInWaitingArea(terminal.getWaitingAreaA());
            checkTicketsInWaitingArea(terminal.getWaitingAreaB());
            checkTicketsInWaitingArea(terminal.getWaitingAreaC());
            checkTicketsInWaitingArea(terminal.getWaitingAreaB());
            checkTicketsInWaitingArea(terminal.getWaitingAreaA());

            try {
                Thread.sleep(1000); // Sleep for 1 seconds before next check
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
