/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author FREE
 */
public class imagemPanel extends javax.swing.JPanel {
    private BufferedImage buffer = null;
    /**
     * Creates new form imagemPanel
     */
    
    public imagemPanel(Object obj){
        if (obj != null){
            if (obj instanceof File){
                imagemPanelConstrutor((File)obj);
            }else if(obj instanceof Imagem){
                imagemPanelConstrutor((Imagem)obj);
            }
        }
    }
    
    public void imagemPanelConstrutor(File file) {
        initComponents();
        changeImage(file);
    }
    
    public void imagemPanelConstrutor(Imagem img){
        initComponents();
        changeImage(img);
    }
    
    public void changeImage(File file){
       try{
            Imagem img = new Imagem(file);
            Cor[] pixels = img.getPixel();
            
            int imagemWidth = img.getWidth();
            int imagemHeight = img.getHeight();
            int quantiaPixels = imagemWidth*imagemHeight;
            System.out.println("quantia pixels : " + quantiaPixels);

            BufferedImage quadro = new BufferedImage(imagemWidth,imagemHeight,BufferedImage.TYPE_INT_ARGB);
            buffer = quadro;
            

            for (int i=0;i<imagemHeight;i++){
                    for (int j=0;j<imagemWidth;j++){
                        Cor c = pixels[(imagemHeight-i-1)*imagemWidth+j];
                        quadro.setRGB(j, i, c.getValorInt());
                    }
                }
            this.setPreferredSize(new Dimension(buffer.getWidth(),buffer.getHeight()));
            this.setSize(this.getPreferredSize());
            this.repaint();
        }
        catch(Exception e){
            System.out.println("Erro imagemPanel");
            e.printStackTrace();
        } 
    }
    
    public void changeImage(Imagem img){
            PTimer inicio = new PTimer("changeImage tempo");
            inicio.startTimer();
            try{
                Cor[] pixels = img.getPixel();

                int imagemWidth = img.getWidth();
                int imagemHeight = img.getHeight();

                if (buffer==null){
                    buffer = new BufferedImage(imagemWidth,imagemHeight,BufferedImage.TYPE_INT_ARGB);
                }

                /*
                for (int i=0;i<imagemHeight;i++)
                {
                    int i2 = imagemHeight-i-1;
                    for (int j=0;j<imagemWidth;j++){
                        int corPreencher = Color.OPAQUE;
                        if (!(pixels==null)){
                            if (i<imagemHeight && j<imagemWidth){
                                int pos = i2*imagemWidth + j;
                                Cor corRelativa = pixels[pos];
                                Color corNoPixel = corRelativa.getValorCor();
                                corPreencher = corNoPixel.getRGB();
                            }
                        }
                        quadro.setRGB(j, i, corPreencher);
                    }
                }
                */
                
                for (int i=0;i<imagemHeight;i++){
                    for (int j=0;j<imagemWidth;j++){
                        Cor c = pixels[(imagemHeight-i-1)*imagemWidth+j];
                        buffer.setRGB(j, i, c.getValorInt());
                    }
                }
                
                
                this.setPreferredSize(new Dimension(buffer.getWidth(),buffer.getHeight()));
                this.setSize(this.getPreferredSize());
                this.repaint();
            }
            catch(Exception e){
                System.out.println("Erro imagemPanel");
                e.printStackTrace();
            }finally{
                inicio.endTimer();
                System.out.println(inicio);
            }
    }
    
    @Override
    public void paint(Graphics g)
    {
        if (!(buffer==null)){
            //System.out.println("DRAWING");
            g.drawImage(buffer, 0, 0, this);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
