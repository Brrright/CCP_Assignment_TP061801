
import Model.MinibusTerminal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
/**
 *
 * @author mingl
 */
public class CCP_Assignment_TP061801 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MinibusTerminal terminal = new MinibusTerminal();
        CustomerGenerator custGenerator = new CustomerGenerator(terminal);
       
//        Thread ticketMachineThread = new Thread(MinibusTerminal.ticketMachine);
//        ticketMachineThread.start();
//        Thread ticketBoothThread1 = new Thread(MinibusTerminal.ticketBooth1);
//        ticketBoothThread1.start();
//        Thread ticketBoothThread2 = new Thread(MinibusTerminal.ticketBooth2);
//        ticketBoothThread2.start();

        // create thread
        Thread threadCg = new Thread(custGenerator);
        Timer timer = new Timer(custGenerator, terminal);
        // start  operation + timer
        threadCg.start();
        new Thread(timer).start();
    }
}
