
import Model.Destination;
import Model.Inspector;
import Model.Minibus;
import Model.MinibusTerminal;
import Model.TicketBooth;
import Model.TicketMachine;

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
        System.out.println("[Terminal] \"Good dayyooo, our terminal is operating from now!! :D\"");
        MinibusTerminal terminal = new MinibusTerminal();

        TicketBooth booth1 = new TicketBooth(terminal, "1");
        TicketBooth booth2 = new TicketBooth(terminal, "2");
        TicketMachine machine = new TicketMachine(terminal);
        Inspector inspector = new Inspector(terminal);
        Minibus busA = new Minibus("A", Destination.DESTINATION_A, terminal, inspector);
        Minibus busB = new Minibus("B", Destination.DESTINATION_B, terminal, inspector);
        Minibus busC = new Minibus("C", Destination.DESTINATION_C, terminal, inspector);

        // start ticket booths and machine
        Thread b1 = new Thread(booth1);
        b1.start();
        Thread b2 = new Thread(booth2);
        b2.start();
        Thread m = new Thread(machine);
        m.start();
        Thread i = new Thread(inspector);
        i.start();
        Thread m1 = new Thread(busA);
        m1.start();
        Thread m2 = new Thread(busB);
        m2.start();
        Thread m3 = new Thread(busC);
        m3.start();

        // start  operation + timer
        CustomerGenerator custGenerator = new CustomerGenerator(terminal);
        Thread cgThread = new Thread(custGenerator);
        cgThread.start();

        Timer timer = new Timer(custGenerator, terminal, inspector, i, b1, b2, m, m1, m2, m3);
        new Thread(timer).start();
    }
}
