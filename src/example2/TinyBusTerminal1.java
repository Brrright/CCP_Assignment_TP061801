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
    private static final AtomicBoolean ticketMachineWorking = new AtomicBoolean(true);

    static class Customer implements Runnable {

        private final int id;
        private boolean hasTicket = false;

        public Customer(int id) {
            this.id = id;
        }

        private void buyTicket() throws InterruptedException {
            if (ticketMachineWorking.get()) {
                System.out.println("[Customer] Customer " + id + " is buying a ticket from the machine.");
                Thread.sleep(500);
                hasTicket = true; // customer now has a ticket

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
                // Simulate sometimes the ticket machine isn't working
                if (new java.util.Random().nextInt(10) > 7) {  // 30% chance
                    ticketMachineWorking.set(false);
                } else {
                    ticketMachineWorking.set(true);
                }

                // Enter terminal if not full
                if (terminalQueue.remainingCapacity() > 0) {
//                    System.out.println("[Customer] Customer " + id + " entered the terminal.");
                    terminalQueue.put(this);
                } else {
                    System.out.println("[Terminal] Terminal is full! Customer " + id + " is waiting outside.");
                    while (terminalQueue.remainingCapacity() <= 0) {
                        Thread.sleep(1000);  // wait outside for a while
                    }
                    terminalQueue.put(this);
                }

                if (ticketMachineWorking.get()) {
                    System.out.println("[Customer] Customer " + id + " is buying a ticket from the machine.");
                    Thread.sleep(500);
                } else {
                    System.out.println("[Customer] Customer " + id + " is waiting for the ticket booth.");
                    ticketBooths.acquire();
                    System.out.println("[Customer] Customer " + id + " is buying a ticket from the booth.");
                    Thread.sleep(1000);
                    ticketBooths.release();
                }

                if (waitingAreaQueue.remainingCapacity() > 0) {
                    System.out.println("[Customer] Customer " + id + " moved to the waiting area.");
                    terminalQueue.remove(this);  // Remove from terminal
                    waitingAreaQueue.put(this);  // Move to waiting area
                } else {
                    System.out.println("[Waiting Area] Waiting area full! Customer " + id + " is waiting in the terminal.");
                    while (waitingAreaQueue.remainingCapacity() <= 0) {
                        Thread.sleep(1000);  // wait inside the terminal
                    }
                    waitingAreaQueue.put(this);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public void enterTerminalFromEntrance(int entrance) throws InterruptedException {
            Queue<Customer> entranceQueue = entrance == WEST_ENTRANCE ? westEntranceQueue : eastEntranceQueue;
            synchronized (entranceQueue) {
                if (terminalQueue.remainingCapacity() > 0) {
                    System.out.println("[Customer] Customer " + id + " entered the terminal from " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                    terminalQueue.put(this);
                } else {
                    System.out.println("[Terminal] Terminal is full! Customer " + id + " is waiting at " + (entrance == WEST_ENTRANCE ? "West" : "East") + " entrance.");
                    entranceQueue.add(this);
                    entranceQueue.wait();  // Customer waits at the entrance
                }
            }
        }

    }

    static class TicketInspector implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    // Iterate through each minibus to inspect tickets
                    for (BlockingQueue<Minibus> minibusQueue : minibusQueues) {
                        if (!minibusQueue.isEmpty()) {
                            Minibus minibus = minibusQueue.take();
                            minibus.checkTickets();
                            minibusQueue.offer(minibus);
                        }
                    }
                    Thread.sleep(1000); // sleep for a while before checking again
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
            while (!onboardingQueue.isEmpty()) {
                Customer customer = onboardingQueue.poll();
                System.out.println("[Inspector] Inspector is now checking ticket of Customer " + customer.id);
                if (customer.hasTicket) {
                    System.out.println("[Customer] Customer " + customer.id + " has a valid ticket.");
                } else {
                    System.out.println("[Customer] Customer " + customer.id + " was denied boarding due to not having a ticket.");
                }
            }
        }

        @Override // run of Terminal
        public void run() {
            try {
                while (true) {
                    if (waitingAreaQueue.size() < 10) {
                        int numLeft = 10 - waitingAreaQueue.size();
                        System.out.println("[Mini Bus] Minibus waiting for " + numLeft + " customers...");
                        Thread.sleep(MINIBUS_CHECK_INTERVAL_MS);  // Check again after 10 seconds
                    } else {
                        for (int i = 0; i < 10 && !waitingAreaQueue.isEmpty(); i++) {
                            Customer customer = waitingAreaQueue.take();
                            onboardingQueue.offer(customer);
                            System.out.println("[Customer] Customer " + customer.id + " has queue up to enter the bus.");
                        }

                        // Request ticket inspector for ticket checking
                        ownQueue.offer(this);
                        ownQueue.poll();  // wait for the ticket inspector to finish

                        System.out.println("[Mini Bus] Minibus departing...");
                        Thread.sleep(MINIBUS_TRAVEL_TIME_MS);
                        System.out.println("[Mini Bus] Minibus returned to the terminal.");
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
                ticketMachineWorking.set(false);
                System.out.println("The ticket machine has stopped working.");
            } else {
                if (!ticketMachineWorking.get()) {
                    System.out.println("The ticket machine is now back in service.");
                }
                ticketMachineWorking.set(true);
            }
        }, 10, 30, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService customerExecutor = Executors.newFixedThreadPool(80);
        ExecutorService minibusExecutor = Executors.newSingleThreadExecutor();

        // <editor-fold>
        // Occasionally simulate booth staff on break
//        ScheduledExecutorService staffBreaks = Executors.newScheduledThreadPool(1);
//
//        ScheduledExecutorService machineBreaks = Executors.newScheduledThreadPool(1);
//        occasionallyBreakTicketMachine(machineBreaks);
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
// </editor-fold>
//        TinyBusTerminal1 terminal = new TinyBusTerminal1();
        // Initialize and start the minibuses
        for (int i = 0; i < NUMBER_OF_MINIBUSES; i++) {
            Minibus minibus = new Minibus(minibusQueues[i]);
            minibusQueues[i].offer(minibus);
            new Thread(minibus).start();
        }
        TicketInspector ticketInspector = new TicketInspector();
        new Thread(ticketInspector).start();

        ScheduledExecutorService entranceExecutor = Executors.newScheduledThreadPool(2);

        handleEntrances(entranceExecutor);

//        // Start the minibus
//        minibusExecutor.submit(new Minibus(ticketInspectorQueue));
        // Simulate customer arrivals
        for (int i = 1; i <= MAX_CUSTOMERS; i++) {
            Customer c = new Customer(i);
            if (new java.util.Random().nextBoolean()) {
                new Thread(() -> {
                    try {
                        c.enterTerminalFromEntrance(WEST_ENTRANCE);
                        c.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                new Thread(() -> {
                    try {
                        c.enterTerminalFromEntrance(EAST_ENTRANCE);
                        c.run();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            Thread.sleep(new java.util.Random().nextInt(3) * 1000 + 1000);
        }

        customerExecutor.shutdown();
        customerExecutor.awaitTermination(1, TimeUnit.HOURS);  // or an appropriate duration
        minibusExecutor.shutdown();
        minibusExecutor.awaitTermination(1, TimeUnit.HOURS);
        entranceExecutor.shutdown();
//        staffBreaks.shutdown();
//        machineBreaks.shutdown();
    }
}
