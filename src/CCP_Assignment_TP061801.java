
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
        // TODO code application logic here
        MinibusTerminal terminal = new MinibusTerminal();
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.scheduleAtFixedRate(() -> {
//            for (int i = 60; i >= 0; i--) {
//                try {
//                    Thread.sleep(1000);  // sleep for 1 second
//                } catch (InterruptedException ex) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//            terminal.isClosed = true;
//        }, 0, 1, TimeUnit.SECONDS);
        CustomerGenerator custGenerator = new CustomerGenerator(terminal);
        Thread threadCg = new Thread(custGenerator);
        threadCg.start();
    }
}
