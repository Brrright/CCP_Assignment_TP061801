
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
        new Thread(booth1).start();
        new Thread(booth2).start();
        new Thread(machine).start();
//        new Thread(inspector).start();
        new Thread(busA).start();
        new Thread(busB).start();
        new Thread(busC).start();

        // start  operation + timer
        CustomerGenerator custGenerator = new CustomerGenerator(terminal);
        Thread cgThread = new Thread(custGenerator);
        cgThread.start();

        Timer timer = new Timer(custGenerator, terminal, inspector);
        new Thread(timer).start();
    }
}
