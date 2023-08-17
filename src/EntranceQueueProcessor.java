
import Model.Customer;
import Model.MinibusTerminal;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mingl
 */
public class EntranceQueueProcessor implements Runnable {

    private final MinibusTerminal terminal;

    public EntranceQueueProcessor(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        while (!MinibusTerminal.isClosed.get()) {
            Customer westCustomer = null;
            Customer eastCustomer = null;

            synchronized (MinibusTerminal.westEntranceQueue) {
                if (!MinibusTerminal.westEntranceQueue.isEmpty()) {
                    westCustomer = MinibusTerminal.westEntranceQueue.peek();
                }
            }

            synchronized (MinibusTerminal.eastEntranceQueue) {
                if (!MinibusTerminal.eastEntranceQueue.isEmpty()) {
                    eastCustomer = MinibusTerminal.eastEntranceQueue.peek();
                }
            }

            // comparing timestamp indicating when they arrived
            if (westCustomer != null && (eastCustomer == null || westCustomer.getTimestamp() < eastCustomer.getTimestamp())) {
                try {
                    terminal.add(westCustomer);
                    synchronized (MinibusTerminal.westEntranceQueue) {
                        MinibusTerminal.westEntranceQueue.poll(); // remove customer from queue after processing
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (eastCustomer != null) {
                try {
                    terminal.add(eastCustomer);
                    synchronized (MinibusTerminal.eastEntranceQueue) {
                        MinibusTerminal.eastEntranceQueue.poll(); // remove customer from queue after processing
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
