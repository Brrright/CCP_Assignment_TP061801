
import java.util.concurrent.*;

public class TinyBusTerminal {

    private static final int TERMINAL_CAPACITY = 15;
    private static final int WAITING_AREA_CAPACITY = 10;

    private static final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_CAPACITY);
    private static final BlockingQueue<Customer> waitingAreaQueue = new ArrayBlockingQueue<>(WAITING_AREA_CAPACITY);

    private static final Semaphore ticketBooths = new Semaphore(2, true);  // two ticket booths
    private static boolean ticketMachineWorking = true;

    static class Customer implements Runnable {

        private final int id;

        public Customer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // Simulate sometimes the ticket machine isn't working
                if (new java.util.Random().nextInt(10) > 7) {  // 30% chance
                    ticketMachineWorking = false;
                } else {
                    ticketMachineWorking = true;
                }

                // Enter terminal if not full
                if (terminalQueue.remainingCapacity() > 0) {
                    System.out.println("Customer " + id + " entered the terminal.");
                    terminalQueue.put(this);
                } else {
                    System.out.println("Terminal is full! Customer " + id + " is waiting outside.");
                    while (terminalQueue.remainingCapacity() <= 0) {
                        Thread.sleep(1000);  // wait outside for a while
                    }
                    terminalQueue.put(this);
                }

                if (ticketMachineWorking) {
                    System.out.println("Customer " + id + " is buying a ticket from the machine.");
                    Thread.sleep(500);
                } else {
                    System.out.println("Customer " + id + " is waiting for the ticket booth.");
                    ticketBooths.acquire();
                    System.out.println("Customer " + id + " is buying a ticket from the booth.");
                    Thread.sleep(1000);  
                    ticketBooths.release();
                }

                if (waitingAreaQueue.remainingCapacity() > 0) {
                    waitingAreaQueue.put(this);
                } else {
                    System.out.println("Waiting area full! Customer " + id + " is waiting in the terminal.");
                    while (waitingAreaQueue.remainingCapacity() <= 0) {
                        Thread.sleep(1000);  // wait inside the terminal
                    }
                    waitingAreaQueue.put(this);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Minibus implements Runnable {

        @Override
        public void run() {
            try {
                // Simulate bus delays
                Thread.sleep(new java.util.Random().nextInt(10) * 1000);

                // Pick up customers
                for (int i = 0; i < 10 && !waitingAreaQueue.isEmpty(); i++) {
                    // Load customer to bus
                    Customer customer = waitingAreaQueue.take();
                    System.out.println("Loading customer " + customer.id + " into the minibus.");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService customerExecutor = Executors.newFixedThreadPool(80); // to handle all customers
        ExecutorService minibusExecutor = Executors.newSingleThreadExecutor();  // assuming one minibus for simplicity

        TinyBusTerminal terminal = new TinyBusTerminal();

        // Simulate customer arrivals
        for (int i = 0; i < 80; i++) {
            customerExecutor.execute(new Customer(i));
            Thread.sleep(new java.util.Random().nextInt(3) * 1000 + 1000); // Sleep for 1, 2, or 3 seconds
        }

        // Start the minibus
        minibusExecutor.execute(new Minibus());

        customerExecutor.shutdown();
        customerExecutor.awaitTermination(1, TimeUnit.HOURS);  // or an appropriate duration
        minibusExecutor.shutdown();
        minibusExecutor.awaitTermination(1, TimeUnit.HOURS);
    }
}
