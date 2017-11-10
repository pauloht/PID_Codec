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
    private int lastFrame = 0;
    private boolean firstFrame = true;
    private int width = -1;
    private int height = -1;
    private JFrame frame;
    private imagemPanel imgP = new imagemPanel(null);
    private boolean videoEnded = false;
    
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
    }
    
    public boolean nextFrame(){
        if (file!=null){
            if (firstFrame){
                byte[] intArray = new byte[4];
                try {
                    file.read(intArray);
                    lastFrame = ByteBuffer.wrap(intArray).getInt();
                    file.read(intArray);
                    width = ByteBuffer.wrap(intArray).getInt();
                    file.read(intArray);
                    height = ByteBuffer.wrap(intArray).getInt(); 
                    System.out.println("lastFrame : " + lastFrame + ",width : " + width + ",height : " + height);
                } catch (IOException ex) {
                    Logger.getLogger(VideoReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                firstFrame = false;
            }
            if (frameIndex < lastFrame){
                byte[] bytesArray = new byte[(width*height)*3];
                try {
                    file.read(bytesArray);
                    Imagem img = new Imagem(bytesArray,width,height);
                    imgP.changeImage(img);
                    frame.pack();
                } catch (IOException ex) {
                    Logger.getLogger(VideoReader.class.getName()).log(Level.SEVERE, null, ex);
                }
                frameIndex++;
                return(true);
            }
        }
        videoEnded = true;
        return(false);
    }

    public boolean hasVideoEnded() {
        return videoEnded;
    }
    
    
}
