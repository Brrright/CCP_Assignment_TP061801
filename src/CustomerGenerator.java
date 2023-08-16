
import Model.Customer;
import Model.MinibusTerminal;
import static Model.MinibusTerminal.EAST_ENTRANCE;
import static Model.MinibusTerminal.WEST_ENTRANCE;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mingl
 */
public class CustomerGenerator implements Runnable {

    private MinibusTerminal terminal;
    private static final int MAX_CUSTOMERS = 80;
    private AtomicInteger counter = new AtomicInteger(0);

    public CustomerGenerator(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!terminal.isClosed.get() && counter.incrementAndGet() < MAX_CUSTOMERS) {
            Customer c = new Customer(terminal, counter.get());
            new Thread(c).start();
            // interval
            try {
                Thread.sleep((long) (Math.random() * 2) * 500);
                //TODO: change later
//                Thread.sleep((long) ((Math.random()) * 2) * 1000 + 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (terminal.isClosed.get()) {
            try {
//                if (MinibusTerminal.isClosed.get()) {
//                    MinibusTerminal.releaseEntranceQueue();
//                    return;
//                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
