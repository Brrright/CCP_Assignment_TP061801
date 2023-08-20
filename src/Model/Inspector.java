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
            resumeInspection();
        }
    }

    public void stopInspection() {
        shouldStop = true;
    }

    public void resumeInspection() {
        shouldStop = false;
    }

    public boolean shouldStop() {
        return shouldStop;
    }

    private void checkTicketsInWaitingArea(WaitingArea area) {
        Queue<Customer> queue = area.getQueue();
        for (Customer customer : queue) {
            Ticket ticket = customer.getTicket();
            if (!ticket.isCheckedByInspector()) {
                ticket.setCheckedByInspector(true);
                System.out.println("[Inspector] Checked ticket for Customer " + customer.getID() + " in " + area.getName());
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void moveToAnotherGate(WaitingArea wa) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[Inspector] Inspector moving to Departure Gate " + wa.getName() + ". (" + wa.getQueue().size() + "/10)");
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                try {
                    lock.wait(); // Inspector waits until a bus arrives or it is told to stop.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (shouldStop()) {
                continue;
            }

            while (!terminal.isClosed.get()) {
                System.out.println("[Inspector] Bus arrived, queue up please!");
                moveToAnotherGate(terminal.getWaitingAreaA());
                checkTicketsInWaitingArea(terminal.getWaitingAreaA());
                moveToAnotherGate(terminal.getWaitingAreaB());
                checkTicketsInWaitingArea(terminal.getWaitingAreaB());
                moveToAnotherGate(terminal.getWaitingAreaC());
                checkTicketsInWaitingArea(terminal.getWaitingAreaC());
                moveToAnotherGate(terminal.getWaitingAreaB());
                checkTicketsInWaitingArea(terminal.getWaitingAreaB());
                moveToAnotherGate(terminal.getWaitingAreaA());
                checkTicketsInWaitingArea(terminal.getWaitingAreaA());

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
