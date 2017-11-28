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
    public static int getValorInt(byte[] b){
        return ( ((255 & 0xFF) << 24) |
                ((b[2] & 0xFF) << 16) |
                ((b[1] & 0xFF) << 8)  |
                ((b[0] & 0xFF) << 0) );
    }
}
