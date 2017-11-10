/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author FREE
 */
public class testJFrameJpanel {
    public static void main(String args[]){
        JFrame frame = new JFrame();
        frame.setVisible(true);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.GREEN);
        panel.setPreferredSize(new Dimension(200,200));
        
        frame.add(panel);
        frame.pack();
    }
}
