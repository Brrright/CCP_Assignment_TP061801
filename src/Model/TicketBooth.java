/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author mingl
 */
public class TicketBooth implements Runnable{
    private String boothID;

    public TicketBooth(String boothID) {
        this.boothID = boothID;
    }
    
     public String getID(){
         return this.boothID;
     }
     
     public void setID(String id){
         this.boothID = id;
     }
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
