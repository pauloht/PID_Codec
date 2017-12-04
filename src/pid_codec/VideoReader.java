/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author FREE
 */
public class VideoReader {
    private FileInputStream file;
    private int frameIndex = -1;
    private int frameDeLeitura = -1;
    private int lastFrame = 0;
    private boolean firstFrame = true;
    private int width = -1;
    private int height = -1;
    private JFrame frame;
    private imagemPanel imgP = new imagemPanel(null);
    private Imagem[] bufferDeImages = new Imagem[10];
    private volatile boolean[] flagBuffer = new boolean[10];
    private int posEscrevendo = 0;
    private int posLendo = 0;
    private Imagem imgBuffer = null;
    private Imagem img;
    byte[] bytesArrayInicial = null;
    byte[] bytesArrayDiff = null;
    
    public VideoReader(File hueFile){
        try {
            file = new FileInputStream(hueFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoReader.class.getName()).log(Level.SEVERE, null, ex);
            file = null;
        }
        frame = new JFrame();
        frame.add(imgP);
        frame.setVisible(true);
        
        for (int i=0;i<flagBuffer.length;i++){
            flagBuffer[i] = false;
        }
        
        Thread t1 = new Thread(){
            @Override
            public void run(){
                carregarBuffer();
            }
        };
        
        t1.start();
    }
    
    public Imagem carregarImagem(){
        //PTimer pt = new PTimer("Tempo de carregar imagem");
        //pt.startTimer();
        if (firstFrame){
            byte[] intArray = new byte[4];
            try {
                file.read(intArray);
                lastFrame = ByteBuffer.wrap(intArray).getInt();
                file.read(intArray);
                width = ByteBuffer.wrap(intArray).getInt();
                file.read(intArray);
                height = ByteBuffer.wrap(intArray).getInt(); 
                bytesArrayInicial = new byte[(width*height)*3];
                bytesArrayDiff = new byte[(width*height)*2];
                System.out.println("lastFrame : " + lastFrame + ",width : " + width + ",height : " + height);
            } catch (IOException ex) {
                Logger.getLogger(VideoReader.class.getName()).log(Level.SEVERE, null, ex);
            }
            firstFrame = false;
        }
        if (frameIndex < lastFrame){
            try {
                //PTimer t1 = new PTimer("Tempo de ler bytes do disco");
                //t1.startTimer();
                //t1.endTimer();
                frameIndex++;
                //PTimer t2 = new PTimer("Tempo de proc");
                //t2.startTimer();
                //
                if (img==null){
                    file.read(bytesArrayInicial);
                    img = new Imagem(bytesArrayInicial,width,height);
                    imgBuffer = new Imagem(img);
                }else{
                    file.read(bytesArrayDiff);
                    //System.out.println(imgBuffer.getSize() + " = " + (bytesArrayDiff.length/4));
                    img.setPixels(imgBuffer,bytesArrayDiff);
                    imgBuffer = new Imagem(img);
                }
                //t2.endTimer();
                //pt.endTimer();
                
                //
                //long tempoTotal = pt.getElapsed();
                //long tempDisco = t1.getElapsed();
                //long tempProc = t2.getElapsed();
                
                //double pDisco = (tempDisco+0.0)/(tempoTotal+0.0)*100.00;
                //double pProc = (tempProc+0.0)/(tempoTotal+0.0)*100.00;
                
                //System.out.println("tempo total : " + (tempoTotal/1E9) + " seg");
                //System.out.println("percentage disco : " + pDisco);
                //System.out.println("percentage procc : " + pProc);
                
                //
                //System.out.println("nova imagem : " + this.img.testPrint());
                return(this.img);
            } catch (IOException ex) {
                Logger.getLogger(VideoReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //pt.endTimer();
        //System.out.println(pt);
        return(null);
    }
    
    public void carregarBuffer(){
        try{
        while (true){
            if (flagBuffer[posEscrevendo]==false){
                //System.out.println("posE : " + posEscrevendo);
                Imagem img = carregarImagem();
                if (img==null){
                    System.out.println("produtor terminou");
                    break;
                }
                bufferDeImages[posEscrevendo] =  img;
                flagBuffer[posEscrevendo] = true;
                if (posEscrevendo==9){
                    posEscrevendo = 0;
                }else{
                    posEscrevendo++;
                }
            }else{
                //faz nada
                //System.out.println("produtor parado em pos " + posEscrevendo);
            }
        }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Erro thread");
        }
        /*

        */
    }
    
    public boolean nextFrame(){
        if (frameDeLeitura>=lastFrame){
            System.out.println("ultimo frame!");
            return(true);
        }
        frame.pack();
        if (file!=null){
            if (flagBuffer[posLendo]==true){
                frameDeLeitura++;
                Imagem imgLocal = bufferDeImages[posLendo];
                //System.out.println(imgLocal.testPrint());
                imgP.changeImage(imgLocal,posLendo);
                flagBuffer[posLendo] = false;
                if (posLendo==9){
                    posLendo = 0;
                }else{
                    posLendo++;
                }
                //System.out.println("setando " + posLendo + " true");
                return(true);
            }
        }
        //System.out.println("Consumidor parado em " + posLendo);
        return(false);
    }

    public boolean hasVideoEnded() {
        //return (frameDeLeitura>=lastFrame);
        return (frameDeLeitura>=lastFrame);
    }
    
    
}
