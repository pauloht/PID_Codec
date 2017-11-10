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
public class PTimer {
    private String name = "nil";
    private long startTime = 0;
    private long endTime = 0;
    private boolean timerOk = false;
    
    public PTimer(String name){
        this.name = name;
    }
    
    public void startTimer(){
        startTime = System.nanoTime();
    }
    
    public void endTimer(){
        endTime = System.nanoTime();
    }
    
    @Override
    public String toString(){
        if (timerOk){
            return("Timer " + name + " ainda em andamento");
        }else{
            double elapsed = (endTime-startTime)/(1E9+0.00);
            return(name+" : "+String.format("%.4f s", elapsed));
        }
    }
}
