
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

            Thread.sleep(60000);
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
        System.out.println("[Terminal] We're closing now! Bye bye guys :]");
        System.out.println("====================================================");
        i.stopInspection();

        synchronized (i.lock) {
            i.lock.notify();
        }
        MinibusTerminal.isClosed.set(true);
    }
}
