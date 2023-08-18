
import Model.Inspector;
import Model.MinibusTerminal;

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

    public Timer(CustomerGenerator cg, MinibusTerminal t, Inspector i) {
        this.cg = cg;
        this.t = t;
        this.i = i;
    }

    public void run() {
        try {

            Thread.sleep(50000);
            t.printTerminalAndWaitingAreaCapacity();

            // clear entrance queue, block customer from coming in, say that we are closing soon, no more customer will be accepted
            // if still customer in foyer, continue to serve them, 
            // if still customer in waiting area, schedule another bus to fetch them
            // wait until the bus returned.
            closeTerminal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void closeTerminal() {
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
                t.printTerminalAndWaitingAreaCapacity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        i.stopInspection();

        synchronized (i.lock) {
            i.lock.notify();
        }
        if (t.terminalQueue.size() == 0) {
            System.out.println("omg it invoked");
            MinibusTerminal.isClosed.set(true);
        }
    }
}
