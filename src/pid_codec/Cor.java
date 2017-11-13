/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.awt.Color;

/**
 *
 * @author elixandrebaldi
 */
public class Cor {
    private byte red;
    private byte green;
    private byte blue;
    
    public Cor(byte r,byte g,byte b){
        red = r;
        green = g;
        blue = b;
    }
    
    public void setCor(byte red,byte green,byte blue){
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    
    public byte[] getBytes(){
        byte[] ret = new byte[3];
        ret[0] = red;
        ret[1] = green;
        ret[2] = blue;
        return(ret);
    }
 
    /*
    public Color getValorCor(){
        try{
            Color retorno = new Color(red&0xff,green&0xff,blue&0xff);
            return(retorno);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
            System.out.println("erro com valores = " + this.toString());
        }
        return(null);
    }
    */
    
    public int getValorInt(){
        return ( ((255 & 0xFF) << 24) |
                ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8)  |
                ((blue & 0xFF) << 0) );
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }
    
    
    
    @Override
    public String toString(){
        return("red="+Byte.toString(red)+",green="+Byte.toString(green)+",blue"+Byte.toString(blue));
    }
}
