/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


import Model.MinibusTerminal;

/**
 *
 * @author mingl
 */
public class CCP_Assignment_TP061801 {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MinibusTerminal terminal = new MinibusTerminal();
        CustomerGenerator custGenerater = new CustomerGenerator(terminal);
    }
}
