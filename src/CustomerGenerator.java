
import Model.Customer;
import Model.MinibusTerminal;
import static Model.MinibusTerminal.EAST_ENTRANCE;
import static Model.MinibusTerminal.WEST_ENTRANCE;
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

    public CustomerGenerator(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while(!terminal.isClosed){
            for (int i = 1; i <= MAX_CUSTOMERS; i++) {
                Customer c = new Customer(terminal, i);
                if (new java.util.Random().nextBoolean()) {
                    new Thread(() -> {
                        try {
                            c.enterTerminalFromEntrance(WEST_ENTRANCE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else {
                    new Thread(() -> {
                        try {
                            c.enterTerminalFromEntrance(EAST_ENTRANCE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
                try {
                    Thread.sleep(new java.util.Random().nextInt(2) * 1000 + 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
        }
        if (terminal.isClosed) {
            System.out.println("[Terminal] Terminal is closing now. Bye guys :)");
            return;
        }
    }
}
