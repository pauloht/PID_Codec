/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 *
 * @author elixandrebaldi
 */
public class Imagem {
   
    private int width;
    
    private int height;
    
    //private ArrayList<Cor> pixel;
    private Cor[] pixel;

    /* 
    public ArrayList<Cor> getPixel() {
        return pixel;
    }
    */
    
    public Cor[] getPixel() {
        return(pixel);
    }

    /*
    public void setPixel(ArrayList<Cor> pixel) {
        this.pixel = pixel;
    }
    */
    
    
    
    public Imagem(byte[] byteArray,int width,int height){
        //pixel = new ArrayList<>();
        pixel = new Cor[width*height];
        this.width = width;
        this.height = height;
        
        /*
        for (int i=0;i<byteArray.length;i=i+3){
            this.pixel.add(new Cor(byteArray[i],byteArray[i+1],byteArray[i+2]));
        }
        */
        for (int i=0;i<width*height;i++){
            pixel[i] = new Cor(byteArray[i*3],byteArray[i*3+1],byteArray[i*3+2]);
        }
    }
    
    public Imagem(Imagem imgAnterior,ImagemComprimida imgComp){
        this.pixel = new Cor[imgAnterior.pixel.length];
        this.width = imgAnterior.width;
        this.height = imgAnterior.height;
        
        for (int i=0;i<imgAnterior.pixel.length;i++){
            byte red = 0;
            byte green = 0;
            byte blue = 0;
            Cor pixel = imgAnterior.pixel[i];
            
            byte variacao = imgComp.getBytes()[i];
            
            byte variacaoRed = (byte)((variacao >> 4) & 1);
            if (variacaoRed>0){
                byte sinal = (byte)((variacao >> 5) & 1);
                if (sinal>0){
                   red = (byte)(pixel.getRed() - 16);
                }else{
                   red = (byte)(pixel.getRed() + 16);
                }
            }else{
                red = pixel.getRed();
            }
            
            byte variacaoGreen = (byte)((variacao >> 2) & 1);
            if (variacaoGreen>0){
                byte sinal = (byte)((variacao >> 3) & 1);
                if (sinal>0){
                   green = (byte)(pixel.getGreen() - 16);
                }else{
                   green = (byte)(pixel.getGreen() + 16);
                }
            }else{
                green = pixel.getGreen();
            }
            
            byte variacaoBlue = (byte)((variacao >> 0) & 1);
            if (variacaoBlue>0){
                byte sinal = (byte)((variacao >> 1) & 1);
                if (sinal>0){
                   blue = (byte)(pixel.getBlue() - 16);
                }else{
                   blue = (byte)(pixel.getBlue() + 16);
                }
            }else{
                blue = pixel.getBlue();
            }
            
            this.pixel[i] = new Cor(red,green,blue);
        }
    }
    
    public void setPixels(byte[] byteArray){
        /*
        for (int i=0;i<this.pixel.size();i++){
            this.pixel.get(i).setCor(byteArray[i*3], byteArray[i*3+1], byteArray[i*3+2]);
        }
        */
        for (int i=0;i<pixel.length;i++){
            pixel[i].setCor(byteArray[i*3], byteArray[i*3+1], byteArray[i*3+2]);
        }
    }
    
    public Imagem(File file){
        //pixel = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream(file);
            byte[] intBuffer = new byte[4];
            ByteBuffer bbInt = ByteBuffer.wrap(intBuffer);
            bbInt.order(ByteOrder.LITTLE_ENDIAN);
            fis.skip(10); // deslocamento 10
            fis.read(intBuffer); // deslocamento 14
            int deslocamentoBitMap = bbInt.getInt();//onde esta a tabela bitmap
            bbInt.rewind();
            fis.skip(4); // deslocamento 18
            fis.read(intBuffer); // deslocamento 22
            width = bbInt.getInt();//bruxaria para "criar" unsigned short, java feelings =(
            bbInt.rewind();
            fis.read(intBuffer);//deslocamento 26
            height = bbInt.getInt();
            bbInt.rewind();
            int linhaTamanho = (width+width%4)*3;//
            byte[] linhaBuffer = new byte[linhaTamanho];
            int deslocamentoParcial = 26;
            fis.skip((deslocamentoBitMap-deslocamentoParcial));
            byte red;
            byte green;
            byte blue;
            pixel = new Cor[width*height];
            for (int i=0;i<height;i++){
                fis.read(linhaBuffer);
                for (int j=0;j<width;j++){
                   blue = linhaBuffer[j*3];
                   green = linhaBuffer[j*3+1];
                   red = linhaBuffer[j*3+2];
                   //System.out.println("adicionando = " + (red&0xff) + "," + (green&0xff) + "," + (blue&0xff));
                   Cor cor = new Cor(red,green,blue);
                   //pixel.add(cor);
                   pixel[i*width+j] = cor;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
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
        //byte[] retorno = new byte[pixel.size()*3];
        byte[] retorno = new byte[pixel.length*3];
        int count = 0;
        for (Cor cor : pixel){
            System.arraycopy(cor.getBytes(), 0, retorno, count*3, 3);
            count++;
        }
        return(retorno);
    }
    

}
