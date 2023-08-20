
import Model.Inspector;
import Model.Minibus;
import Model.MinibusTerminal;
import Model.TicketBooth;
import Model.TicketMachine;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mingl
 */
public class Timer extends Thread {

    private CustomerGenerator cg;
    private MinibusTerminal t;
    private Inspector i;

    private Thread ti;
    private Thread t1;
    private Thread t2;
    private Thread m;
    private Thread m1;
    private Thread m2;
    private Thread m3;

    public Timer(CustomerGenerator cg, MinibusTerminal t, Inspector i,
            Thread ti,
            Thread t1,
            Thread t2,
            Thread m,
            Thread m1,
            Thread m2,
            Thread m3) {
        this.cg = cg;
        this.t = t;
        this.i = i;
        this.ti = ti;
        this.t1 = t1;
        this.t2 = t2;
        this.m = m;
        this.m1 = m1;
        this.m2 = m2;
        this.m3 = m3;
    }

    public void run() {
        try {

            Thread.sleep(50000);
//            t.printTerminalAndWaitingAreaCapacity();

            // clear entrance queue, block customer from coming in, say that we are closing soon, no more customer will be accepted
            // if still customer in foyer, continue to serve them, 
            // if still customer in waiting area, schedule another bus to fetch them
            // wait until the bus returned.
            closingSoonTerminal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void closeTerminal() {
        System.out.println("====================================================");
        System.out.println("[Terminal] We're closing now! Bye bye guys:]");
        System.out.println("====================================================");
        i.stopInspection();

        synchronized (i.lock) {
            i.lock.notify();
        }
        MinibusTerminal.isClosed.set(true);
    }

    public synchronized void closingSoonTerminal() {
        System.out.println("====================================================");
        System.out.println("[Terminal] We're closing soon! No more customers will be accepted :]");
        System.out.println("====================================================");

        MinibusTerminal.isAcceptingNewEntries.set(false);

        // Wait until terminal is empty
        while (t.terminalQueue.size() > 0
                || t.getWaitingAreaA().getQueue().size() > 0
                || t.getWaitingAreaB().getQueue().size() > 0
                || t.getWaitingAreaC().getQueue().size() > 0) {
            try {
                wait(1000);  // wait for 1 second before checking again.
//                t.printTerminalAndWaitingAreaCapacity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        i.stopInspection();

        synchronized (i.lock) {
            i.lock.notify();
        }

        if (t.terminalQueue.size() == 0) {
            MinibusTerminal.isClosed.set(true);
            System.out.println("====================================================");
            System.out.println("[Terminal] We're closing now! Bye bye guys:]");
            System.out.println("====================================================");
            while (t1.isAlive()
                    || t2.isAlive()
                    || ti.isAlive()
                    || m.isAlive()
                    || m2.isAlive()
                    || m3.isAlive()
                    || m1.isAlive()) {
                if (t1.getState() == Thread.State.WAITING) {
                    synchronized (t1) {
                        t1.notify();
                    }
                }
                if (t2.getState() == Thread.State.WAITING) {
                    synchronized (t2) {
                        t2.notify();
                    }
                }
                if (ti.getState() == Thread.State.WAITING) {
                    synchronized (ti) {
                        ti.notify();
                    }
                }
                if (m.getState() == Thread.State.WAITING) {
                    synchronized (m) {
                        m.notify();
                    }
                }
                if (t.getWaitingAreaA().getQueue().size() == 0 && m1.getState() == Thread.State.WAITING) {
                    synchronized (m1) {
                        m1.notify();
                    }
                }
                if (t.getWaitingAreaB().getQueue().size() == 0 && m2.getState() == Thread.State.WAITING) {
                    synchronized (m2) {
                        m2.notify();
                    }
                }
                if (t.getWaitingAreaC().getQueue().size() == 0 && m3.getState() == Thread.State.WAITING) {
                    synchronized (m3) {
                        m3.notify();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Main Thread has been interrupted.");
                }
            }
        }
    }
}
