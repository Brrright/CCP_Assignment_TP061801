
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
        while (!terminal.isClosed && counter.incrementAndGet() < MAX_CUSTOMERS) {
            //<editor-fold desc="construct customer & determine from west or east">
            Customer c = new Customer(terminal, counter.get());
            setEntrance(c);
            // interval
            try {
                Thread.sleep((long) ((Math.random()) * 2) * 1000 + 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (terminal.isClosed) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public void setEntrance(Customer c) {
        if (new java.util.Random().nextBoolean()) {
            Thread newCust = new Thread(() -> {
                try {
                    c.enterTerminalFromEntrance(WEST_ENTRANCE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            newCust.start();
            try {
                newCust.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        } else {
            Thread newCust = new Thread(() -> {
                try {
                    c.enterTerminalFromEntrance(EAST_ENTRANCE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            newCust.start();
        }
    }
}
