
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

    public Timer(CustomerGenerator cg, MinibusTerminal t) {
        this.cg = cg;
        this.t = t;
    }

    public void run() {
        try {
            // Timer for operation hours
            Thread.sleep(60000);
            closeTerminal();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void closeTerminal() {
        System.out.println("====================================================");
        System.out.println("[Terminal] We're Closing!");
        System.out.println("====================================================");
        MinibusTerminal.isClosed = true;
    }
}
