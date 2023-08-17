
import Model.MinibusTerminal;
import Model.TicketBooth;
import Model.TicketMachine;
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

        TicketBooth booth1 = new TicketBooth(terminal, "1");
        TicketBooth booth2 = new TicketBooth(terminal, "2");
        TicketMachine machine = new TicketMachine(terminal);
        EntranceQueueProcessor queueProcessor = new EntranceQueueProcessor(terminal); 

        // start ticket booths and machine
        new Thread(booth1).start();
        new Thread(booth2).start();
        new Thread(machine).start();
        new Thread(queueProcessor).start();

        // create thread
        // start  operation + timer
        CustomerGenerator custGenerator = new CustomerGenerator(terminal);
        new Thread(custGenerator).start();
        Timer timer = new Timer(custGenerator, terminal);
        new Thread(timer).start();
    }
}
