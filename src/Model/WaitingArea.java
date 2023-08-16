/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author mingl
 */
public class WaitingArea {

    private String name;
    private int maxCapacity = 10;
    private static final BlockingQueue<Customer> waitingAreaQueue = new ArrayBlockingQueue<>(MinibusTerminal.TERMINAL_MAX_CAPACITY);
    
    

}
