/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.BorderLayout;
import javax.swing.JFrame;
/**
 *
 * @author santoslopez
 */
public class Ventana extends JFrame{
    
    //constructor
    public Ventana(String name,SuperficieDeDibujo superficieDibujo){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle(name);
       // this.setSize(600,600);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        
        this.add(superficieDibujo);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
