/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

/**
 *
 * @author FREE
 */
public class util {
    public static String retornarStringPorNumero(int a){
        String inicial = Integer.toString(a);
        if (inicial.length()==1){
            return("00"+inicial);
        }else if (inicial.length()==2){
            return("0"+inicial);
        }else{
            return(inicial);
        }
    }
}
