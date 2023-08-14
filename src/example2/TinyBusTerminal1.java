package example2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TinyBusTerminal1 {

    private static final int WEST_ENTRANCE = 1;
    private static final int EAST_ENTRANCE = 2;
    private static final int TERMINAL_CAPACITY = 15;
    private static final int WAITING_AREA_CAPACITY = 10;

    private static final int NUMBER_OF_MINIBUSES = 3;
    private static final int MINIBUS_CHECK_INTERVAL_MS = 10000;
    private static final int MINIBUS_TRAVEL_TIME_MS = 5000;
    private static final int MAX_CUSTOMERS = 80;

    private static final Queue<Customer> westEntranceQueue = new LinkedList<>();
    private static final Queue<Customer> eastEntranceQueue = new LinkedList<>();
    private static final BlockingQueue<Customer> terminalQueue = new ArrayBlockingQueue<>(TERMINAL_CAPACITY);
    private static final BlockingQueue<Customer> waitingAreaQueue = new ArrayBlockingQueue<>(WAITING_AREA_CAPACITY);
    private static final BlockingQueue<Minibus>[] minibusQueues = new LinkedBlockingQueue[NUMBER_OF_MINIBUSES];

    private static final BlockingQueue<Customer>[] waitingAreas = new ArrayBlockingQueue[3];

    // Initialize waiting areas
    static {
        for (int i = 0; i < 3; i++) {
            waitingAreas[i] = new ArrayBlockingQueue<>(WAITING_AREA_CAPACITY);
        }
    }

    // Initialize minibuses
    static {
        for (int i = 0; i < NUMBER_OF_MINIBUSES; i++) {
            minibusQueues[i] = new LinkedBlockingQueue<>();
        }
    }

    private static final Semaphore ticketBooths = new Semaphore(2, true);  // two ticket booths
//    private static boolean ticketMachineWorking = true;

    static class TicketMachine {

        private static final AtomicBoolean working = new AtomicBoolean(true);

        static boolean isWorking() {
            return working.get();
        }

        static void setWorking(boolean status) {
            working.set(status);
        }

        static void buyTicket(Customer customer) throws InterruptedException {
            System.out.println("[Customer] Customer " + customer.id + " is buying a ticket from the machine.");
            Thread.sleep(500);
            customer.hasTicket = true;
        }
    }

    static class Customer implements Runnable {

        private final int id;
        private boolean hasTicket = false;

        public Customer(int id) {
            this.id = id;
        }

        private void buyTicket() throws InterruptedException {
            if (TicketMachine.isWorking()) {
                TicketMachine.buyTicket(this);
            } else {
                System.out.println("[Customer] Customer " + id + " is waiting for the ticket booth.");
                ticketBooths.acquire();
                System.out.println("[Customer] Customer " + id + " is buying a ticket from the booth.");
                Thread.sleep(1000);
                ticketBooths.release();
                hasTicket = true; // customer now has a ticket
            }
        }

        @Override
        public void run() {
            try {
                if (terminalQueue.remainingCapacity() > 0) {
                    terminalQueue.put(this);
                    buyTicket();

                    int waitingAreaIndex = new Random().nextInt(3);
                    if (waitingAreas[waitingAreaIndex].offer(this)) {
                        System.out.println("[Customer] Customer " + id + " moved to waiting area " + (char) ('A' + waitingAreaIndex));
                    } else {
                        System.out.println("[Waiting Area] Waiting area " + (char) ('A' + waitingAreaIndex) + " is full! Customer " + id + " is waiting in the terminal.");
                        waitingAreas[waitingAreaIndex].put(this);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void enterTerminalFromEntrance(int entrance) throws InterruptedException {
            if (terminalQueue.remainingCapacity() > 0) {
                System.out.println("[Customer] Customer " + id + " entered the terminal from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                terminalQueue.put(this);
                run();  // Directly proceed with the rest of the logic
            } else {
                System.out.println("[Terminal] Terminal is full! Customer " + id + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                // Let's remove the customer from entrance queues for now for simplicity. This can be re-added later if necessary
            }
        }

    }

    static class TicketInspector implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    for (BlockingQueue<Customer> waitingArea : waitingAreas) {
                        if (!waitingArea.isEmpty()) {
                            Customer customer = waitingArea.poll();
                            if (customer.hasTicket) {
                                System.out.println("[Inspector] Customer " + customer.id + " has a valid ticket and can board the minibus.");
                            } else {
                                System.out.println("[Inspector] Customer " + customer.id + " was denied boarding due to not having a ticket.");
                            }
                        }
                        Thread.sleep(1000); // sleep for a while before checking the next waiting area
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Minibus implements Runnable {

        private final BlockingQueue<Minibus> ownQueue;
        private final Queue<Customer> onboardingQueue = new LinkedList<>();

        public Minibus(BlockingQueue<Minibus> ownQueue) {
            this.ownQueue = ownQueue;
        }

        public void checkTickets() throws InterruptedException {
            Queue<Customer> allowedCustomers = new LinkedList<>();
            while (!onboardingQueue.isEmpty()) {
                Customer customer = onboardingQueue.poll();
                System.out.println("[Inspector] Inspector is now checking ticket of Customer " + customer.id);
                if (customer.hasTicket) {
                    System.out.println("[Customer] Customer " + customer.id + " has a valid ticket.");
                    allowedCustomers.add(customer);
                } else {
                    System.out.println("[Customer] Customer " + customer.id + " was denied boarding due to not having a ticket.");
                }
            }
            onboardingQueue.addAll(allowedCustomers);
        }

        @Override // run of Terminal
        public void run() {
            try {
                int waitingAreaIndex = new Random().nextInt(3); // Randomly choose one of the three waiting areas (A, B, or C)
                while (true) {
                    if (waitingAreas[waitingAreaIndex].size() < 10) {
                        Thread.sleep(MINIBUS_CHECK_INTERVAL_MS);
                    } else {
                        for (int i = 0; i < 10 && !waitingAreas[waitingAreaIndex].isEmpty(); i++) {
                            Customer customer = waitingAreas[waitingAreaIndex].take();
                            onboardingQueue.offer(customer);
                            System.out.println("[Customer] Customer " + customer.id + " has queued up to enter the bus from waiting area " + (char) ('A' + waitingAreaIndex) + ".");
                        }
                        System.out.println("[Mini Bus] Minibus departing from waiting area " + (char) ('A' + waitingAreaIndex) + "...");
                        Thread.sleep(MINIBUS_TRAVEL_TIME_MS);
                        System.out.println("[Mini Bus] Minibus returned to the terminal from waiting area " + (char) ('A' + waitingAreaIndex) + ".");
                        Thread.sleep(20000);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void handleEntrances(ScheduledExecutorService entranceExecutor) {

        entranceExecutor.scheduleAtFixedRate(() -> {
            synchronized (westEntranceQueue) {
                if (!westEntranceQueue.isEmpty() && terminalQueue.remainingCapacity() > 0) {
                    Customer customer = westEntranceQueue.poll();
                    try {
                        terminalQueue.put(customer);
                        westEntranceQueue.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 2, TimeUnit.SECONDS);

        entranceExecutor.scheduleAtFixedRate(() -> {
            synchronized (eastEntranceQueue) {
                if (!eastEntranceQueue.isEmpty() && terminalQueue.remainingCapacity() > 0) {
                    Customer customer = eastEntranceQueue.poll();
                    try {
                        terminalQueue.put(customer);
                        eastEntranceQueue.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private static void occasionallyBreakTicketMachine(ScheduledExecutorService machineBreaks) {
        machineBreaks.scheduleAtFixedRate(() -> {
            if (new Random().nextInt(10) > 7) {  // 30% chance
                TicketMachine.setWorking(false);
                System.out.println("[TMachine] The ticket machine has stopped working.");
            } else {
                if (!TicketMachine.isWorking()) {
                    System.out.println("[TMachine] The ticket machine is now back in service.");
                }
                TicketMachine.setWorking(true);
            }
        }, 10, 30, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService customerExecutor = Executors.newFixedThreadPool(MAX_CUSTOMERS);
        ScheduledExecutorService machineBreaks = Executors.newScheduledThreadPool(1);
        occasionallyBreakTicketMachine(machineBreaks);

        // Initiate ticket inspector thread
        new Thread(new TicketInspector()).start();

        // Create and start minibuses
        for (int i = 0; i < NUMBER_OF_MINIBUSES; i++) {
            new Thread(new Minibus(minibusQueues[i])).start();
        }

        // Simulate customer arrivals
        for (int i = 1; i <= MAX_CUSTOMERS; i++) {
            Customer c = new Customer(i);
            customerExecutor.execute(c);
            Thread.sleep(new java.util.Random().nextInt(3) * 1000 + 1000);
        }

        customerExecutor.shutdown();
        customerExecutor.awaitTermination(1, TimeUnit.HOURS);
        machineBreaks.shutdown();
//        ExecutorService customerExecutor = Executors.newFixedThreadPool(80);
//        ExecutorService minibusExecutor = Executors.newSingleThreadExecutor();
//
//        // <editor-fold>
////         Occasionally simulate booth staff on break
//        ScheduledExecutorService staffBreaks = Executors.newScheduledThreadPool(1);
//        ScheduledExecutorService machineBreaks = Executors.newScheduledThreadPool(1);
//        occasionallyBreakTicketMachine(machineBreaks);
//
//        staffBreaks.scheduleAtFixedRate(() -> {
//            if (ticketBooths.availablePermits() > 0) {
//                try {
//                    ticketBooths.acquire(); // simulate staff going on break
//                    System.out.println("One ticket booth staff is on a break!");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 10, 30, TimeUnit.SECONDS);
//
//        staffBreaks.scheduleAtFixedRate(() -> {
//            if (ticketBooths.availablePermits() < 2) {
//                ticketBooths.release(); // staff back from break
//                System.out.println("Ticket booth staff is back from break!");
//            }
//        }, 15, 30, TimeUnit.SECONDS);
//// </editor-fold>
//        //        TinyBusTerminal1 terminal = new TinyBusTerminal1();
//        // Initialize and start the minibuses
//        for (int i = 0; i < NUMBER_OF_MINIBUSES; i++) {
//            Minibus minibus = new Minibus(minibusQueues[i]);
//            minibusQueues[i].offer(minibus);
//            new Thread(minibus).start();
//        }
//        TicketInspector ticketInspector = new TicketInspector();
//        new Thread(ticketInspector).start();
//
//        ScheduledExecutorService entranceExecutor = Executors.newScheduledThreadPool(2);
//        handleEntrances(entranceExecutor);
//
//        // Simulate customer arrivals
//        for (int i = 1; i <= MAX_CUSTOMERS; i++) {
//            Customer c = new Customer(i);
//            if (new java.util.Random().nextBoolean()) {
//                new Thread(() -> {
//                    try {
//                        c.enterTerminalFromEntrance(WEST_ENTRANCE);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
//            } else {
//                new Thread(() -> {
//                    try {
//                        c.enterTerminalFromEntrance(EAST_ENTRANCE);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }).start();
//            }
//            Thread.sleep(new java.util.Random().nextInt(3) * 1000 + 1000);
//        }
//
//        customerExecutor.shutdown();
//        customerExecutor.awaitTermination(1, TimeUnit.HOURS);  // or an appropriate duration
//        minibusExecutor.shutdown();
//        minibusExecutor.awaitTermination(1, TimeUnit.HOURS);
//        entranceExecutor.shutdown();
////        staffBreaks.shutdown();
////        machineBreaks.shutdown();
    }
}
