
import Model.Customer;
import Model.MinibusTerminal;
import java.util.Date;

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

    public CustomerGenerator(MinibusTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void run() {
        Customer customer = new Customer(terminal);
        customer.setInTime(new Date());
        Thread thcustomer = new Thread(customer);
        customer.setID("Customer " + thcustomer.getId());
        thcustomer.start();
    }

}
