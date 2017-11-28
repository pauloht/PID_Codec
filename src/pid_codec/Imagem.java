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
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elixandrebaldi
 */
public class Imagem {
    private int width;
    
    private int height;
    
    private int deslocamento;
    
    private byte[] pixel;

    public Imagem(byte[] byteArray,int width,int height){
        //pixel = new ArrayList<>();
        pixel = byteArray;
        this.width = width;
        this.height = height;
        this.deslocamento = -1;
    }
    
    public Imagem(Imagem img){
        this.width = img.width;
        this.height = img.height;
        this.deslocamento = img.deslocamento;
        this.pixel = new byte[img.pixel.length];
        System.arraycopy(img.pixel, 0, this.pixel, 0, img.pixel.length);
    }
    
    public int getSize(){
        return(this.height*this.width);
    }
    
    public static byte[] diffImagem(Imagem a,Imagem b){
        if (a.height!=b.height || a.width!=b.width){
            throw new IllegalArgumentException("Tamanhos incompativeis em metodo diffImagem de classe Imagem!");
        }
        int numeroDePixels = a.width*a.height;
        byte[] retorno = new byte[(a.height*a.width)*4];
        int difRed,difGreen,difBlue;
        byte sinal = 0;
        for (int i=0;i<numeroDePixels;i=i+1){
            sinal = (byte)0;
            difBlue = (b.pixel[i*3]&0xFF) - (a.pixel[i*3]&0xFF);
            difGreen = (b.pixel[i*3+1]&0xFF) - (a.pixel[i*3+1]&0xFF);
            difRed = (b.pixel[i*3+2]&0xFF) - (a.pixel[i*3+2]&0xFF);
            
            if (difBlue<0){
                sinal = (byte) (sinal | (1 << 7));
                difBlue = difBlue*-1;
            }
            if (difGreen<0){
                sinal = (byte) (sinal | (1 << 6));
                difGreen = difGreen*-1;
            }
            if (difRed<0){
                sinal = (byte) (sinal | (1 << 5));
                difRed = difRed*-1;
            }
            retorno[i*4] = sinal;
            retorno[i*4+1] = (byte)difBlue;
            retorno[i*4+2] = (byte)difGreen;
            retorno[i*4+3] = (byte)difRed;
        }
        return(retorno);
    }
    
    public void setPixels(byte[] byteArray){
        if (byteArray.length!=pixel.length){
            throw new IllegalArgumentException("tamanhos incompativeis!");
        }
        pixel = byteArray;
    }
    
    public Imagem(Imagem img, byte[] bytesDeDiferenca){
        this.width = img.width;
        this.height = img.height;
        this.deslocamento = img.deslocamento;
        this.pixel = new byte[img.pixel.length];
        setPixels(img, bytesDeDiferenca);
    }
    
    public static String getInterpretacaoDiferanca(byte[] b){
        byte sinal,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<1;i=i+1){
            aux.append('(');
            sinal = b[i*4];
            sinalBlue = (byte)((sinal >> 7) & 1);
            sinalGreen = (byte)((sinal >> 6) & 1);
            sinalRed = (byte)((sinal >> 5) & 1);
            blue = b[i*4+1];
            green = b[i*4+2];
            red = b[i*4+3];
            if ((sinalBlue&0xFF)==1){//subtrair
                aux.append('-');
            }else{
                aux.append('+');
            }
            aux.append((blue&0xFF));
            if ((sinalGreen&0xFF)==1){
                aux.append('-');
            }else{
                aux.append('+');
            }
            aux.append((green&0xFF));
            if ((sinalRed&0xFF)==1){
                aux.append('-');
            }else{
                aux.append('+');
            }
            aux.append((red&0xFF));
            aux.append(")\n");
        }
        return(aux.toString());
    }
    
    public void setPixels(Imagem img, byte[] bytesDeDiferenca){
        if ((img.getHeight()*img.getWidth())!=(bytesDeDiferenca.length/4) || (bytesDeDiferenca.length/4)!=(this.pixel.length/3)){
            throw new IllegalArgumentException("tamanhos incompativeis!");
        }
        byte[] imgB = img.getBytes();
        byte sinal,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        int vRed,vGreen,vBlue;
        for (int i=0;i<img.getSize();i=i+1){
            sinal = bytesDeDiferenca[i*4];
            sinalBlue = (byte)((sinal >> 7) & 1);
            sinalGreen = (byte)((sinal >> 6) & 1);
            sinalRed = (byte)((sinal >> 5) & 1);
            blue = bytesDeDiferenca[i*4+1];
            green = bytesDeDiferenca[i*4+2];
            red = bytesDeDiferenca[i*4+3];
            vBlue = (blue&0xFF);
            vGreen =  (green&0xFF);
            vRed = (red&0xFF);
            if ((sinalBlue&0xFF)==1){//subtrair
                vBlue = vBlue*-1;
            }
            if ((sinalGreen&0xFF)==1){
                vGreen = vGreen*-1;
            }
            if ((sinalRed&0xFF)==1){
                vRed = vRed*-1;
            }
            vBlue = vBlue + (imgB[i*3]&0xFF);
            vGreen = vGreen + (imgB[i*3+1]&0xFF);
            vRed = vRed + (imgB[i*3+2]&0xFF);
            
            this.pixel[i*3] = (byte)vBlue;
            this.pixel[i*3+1] = (byte)vGreen;
            this.pixel[i*3+2] = (byte)vRed;
        }
    }
    
    public Imagem(File file,boolean resetar){
        //pixel = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] intBuffer = new byte[4];
            ByteBuffer bbInt = ByteBuffer.wrap(intBuffer);
            bbInt.order(ByteOrder.LITTLE_ENDIAN);
            fis.skip(10); // deslocamento 10
            fis.read(intBuffer); // deslocamento 14
            int deslocamentoBitMap = bbInt.getInt();//onde esta a tabela bitmap
            this.deslocamento = deslocamentoBitMap;
            bbInt.rewind();
            fis.skip(4); // deslocamento 18
            fis.read(intBuffer); // deslocamento 22
            width = bbInt.getInt();//bruxaria para "criar" unsigned short, java feelings =(
            bbInt.rewind();
            fis.read(intBuffer);//deslocamento 26
            height = bbInt.getInt();
            bbInt.rewind();
            pixel = new byte[3*width*height];
            fis.close();
            setImagem(file,resetar);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void setImagem(File file,boolean resetar){
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            fis.skip(deslocamento);
            int linhaTamanho = (width+width%4)*3;
            byte red;
            byte green;
            byte blue;
            fis.read(pixel);
            fis.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Imagem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public byte[] getBytes(){
        return(pixel);
    }
    
    public String testPrint(){
        StringBuilder aux = new StringBuilder();
        aux.append('(');
        byte[] b = new byte[3];
        System.arraycopy(this.pixel, 0, b, 0, 3);
        aux.append(b[0]&0xFF).append(',');
        aux.append(b[1]&0xFF).append(',');
        aux.append(b[2]&0xFF).append(')');
        return(aux.toString());
    }

}
