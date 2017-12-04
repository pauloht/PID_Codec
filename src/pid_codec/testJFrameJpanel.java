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
    public static int testRGB(byte r,byte g,byte b){
        int ret = ((255 & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF) << 0);
        return(ret);
    }
    
    public static byte getBit(byte ID,int position)
    {
        return (byte)((ID >> position) & 1);
    }
    
    public static void test(String args[]){
        byte a = 0;
        
        a = (byte) (a | (1 << 6));
        a = (byte) (a | (1 << 5));
        a = (byte) (a | (1 << 4));
        a = (byte) (a | (1 << 3));
        a = (byte) (a | (1 << 1));
        
        
        for (int i=0;i<8;i++){
            byte b = getBit(a,i);
            System.out.println(b);
        }
        //a = (byte) (a & ~(1 << 5));
        
        String t = Byte.toString(a);
        String s1 = String.format("%8s", Integer.toBinaryString(a & 0xFF)).replace(' ', '0');
        System.out.println(s1);
    }
    
    public static void main(String args[]){
        byte a = 0;
        a = (byte) (a | (1 << 2));
        a = (byte) (a | (1 << 3));
        a = (byte) (a | (0 << 4));
        a = (byte) (a | (1 << 5));
        System.out.println((a&0xFF));
        
        int deslocamentoCores = 4 + (33+33%8)/8;
        System.out.println("des : " + deslocamentoCores);
        System.out.println("mod : " + ((int)(15/8)));
    }
}
