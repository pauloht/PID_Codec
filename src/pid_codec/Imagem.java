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
    private static final int min = 0;
    
    private int width;
    
    private int height;
    
    private int deslocamento;
    
    private byte[] pixel;
    
    private byte[] auxBuffer;

    public static String getInterpretacaoDiferanca(byte[] b){
        byte valor1,valor2,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        StringBuilder aux = new StringBuilder();
        for (int i=0;i<b.length/2;i=i+1){
            valor1 = b[i*2];
            valor2 = b[i*2+1];
            sinalBlue = (byte)((valor1 >> 7) & 1);
            sinalGreen = (byte)((valor1 >> 6) & 1);
            sinalRed = (byte)((valor1 >> 5) & 1);
            
            blue = (byte)0;
            green = (byte)0;
            red = (byte)0;
            
            //((ID >> position) & 1);
            
            blue = (byte) (blue | (byte)(((valor1 >> 4) & 1) << 5));
            blue = (byte) (blue | (byte)(((valor1 >> 3) & 1) << 4));
            blue = (byte) (blue | (byte)(((valor1 >> 2) & 1) << 3));
            blue = (byte) (blue | (byte)(((valor1 >> 1) & 1) << 2));
            
            green = (byte) (green | (byte)(((valor2 >> 7) & 1) << 5));
            green = (byte) (green | (byte)(((valor2 >> 6) & 1) << 4));
            green = (byte) (green | (byte)(((valor2 >> 5) & 1) << 3));
            green = (byte) (green | (byte)(((valor2 >> 4) & 1) << 2));
            
            red = (byte) (red | (byte)(((valor2 >> 3) & 1) << 5));
            red = (byte) (red | (byte)(((valor2 >> 2) & 1) << 4));
            red = (byte) (red | (byte)(((valor2 >> 1) & 1) << 3));
            red = (byte) (red | (byte)(((valor2) & 1) << 2));
            
            aux.append('(');
            if ((blue&0xFF)!=0){
                if (sinalBlue!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((blue&0xFF));
            }else{
                aux.append("+0");
            }
            aux.append(',');
            
            if ((green&0xFF)!=0){
                if (sinalGreen!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((green&0xFF));
            }else{
                aux.append("+0");
            }
            aux.append(',');
            
            if ((red&0xFF)!=0){
                if (sinalRed!=0){
                    aux.append('-');
                }else{
                    aux.append('+');
                }
                aux.append((red&0xFF));
            }else{
                aux.append("+0");
            }

            aux.append(")\n");
        }
        return(aux.toString());
    }
    
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
        byte[] retorno = new byte[(a.height*a.width)*2];
        int difRed,difGreen,difBlue;
        byte valor1 = 0;
        byte valor2 = 0;
        for (int i=0;i<numeroDePixels;i=i+1){
            valor1 = (byte)0;
            valor2 = (byte)0;
            difBlue = (b.pixel[i*3]&0xFF) - (a.pixel[i*3]&0xFF);
            difGreen = (b.pixel[i*3+1]&0xFF) - (a.pixel[i*3+1]&0xFF);
            difRed = (b.pixel[i*3+2]&0xFF) - (a.pixel[i*3+2]&0xFF);
            
            if (difBlue<0){
                valor1 = (byte) (valor1 | (1 << 7));
                difBlue = -difBlue;
            }
            if (difBlue>(min)){
                if (difBlue>=32){
                    valor1 = (byte) (valor1 | (1 << 4));
                    difBlue = difBlue-32;
                }
                if (difBlue>=16){
                    valor1 = (byte) (valor1 | (1 << 3));
                    difBlue = difBlue-16;
                }
                if (difBlue>=8){
                    valor1 = (byte) (valor1 | (1 << 2));
                    difBlue = difBlue-8;
                }
                if (difBlue>=4){
                    valor1 = (byte) (valor1 | (1 << 1));
                    difBlue = difBlue-4;
                }
            }
            if (difGreen<0){
                valor1 = (byte) (valor1 | (1 << 6));
                difGreen = -difGreen;
            }
            if (difGreen>(min)){
                if (difGreen>=32){
                    valor2 = (byte) (valor2 | (1 << 7));
                    difGreen = difGreen-32;
                }
                if (difGreen>=16){
                    valor2 = (byte) (valor2 | (1 << 6));
                    difGreen = difGreen-16;
                }
                if (difGreen>=8){
                    valor2 = (byte) (valor2 | (1 << 5));
                    difGreen = difGreen-8;
                }
                if (difGreen>=4){
                    valor2 = (byte) (valor2 | (1 << 4));
                }
            }
            if (difRed<0){
                valor1 = (byte) (valor1 | (1 << 5));
                difRed = -difRed;
            }
            if (difRed>(min)){
                if (difRed>=32){
                    valor2 = (byte) (valor2 | (1 << 3));
                    difRed = difRed-32;
                }
                if (difRed>=16){
                    valor2 = (byte) (valor2 | (1 << 2));
                    difRed = difRed-16;
                }
                if (difRed>=8){
                    valor2 = (byte) (valor2 | (1 << 1));
                    difRed = difRed-8;
                }
                if (difRed>=4){
                    valor2 = (byte) (valor2 | (1));
                }
            }
            retorno[i*2] = valor1;
            retorno[i*2+1] = valor2;
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
    
    public void setPixels(Imagem img, byte[] bytesDeDiferenca){
        if ((img.getHeight()*img.getWidth())!=(bytesDeDiferenca.length/2) || (bytesDeDiferenca.length/2)!=(this.pixel.length/3)){
            throw new IllegalArgumentException("tamanhos incompativeis!");
        }
        byte[] imgB = img.getBytes();
        byte valor1,valor2,sinalRed,sinalGreen,sinalBlue;
        byte blue,green,red;
        int vRed,vGreen,vBlue;
        //System.out.println("antes : " + img.testPrint());
        for (int i=0;i<img.getSize();i=i+1){
            valor1 = bytesDeDiferenca[i*2];
            valor2 = bytesDeDiferenca[i*2+1];
            sinalBlue = (byte)((valor1 >> 7) & 1);
            sinalGreen = (byte)((valor1 >> 6) & 1);
            sinalRed = (byte)((valor1 >> 5) & 1);
            
            blue = (byte)0;
            green = (byte)0;
            red = (byte)0;
            
            //((ID >> position) & 1);
            
            blue = (byte) (blue | (byte)(((valor1 >> 4) & 1) << 5));
            blue = (byte) (blue | (byte)(((valor1 >> 3) & 1) << 4));
            blue = (byte) (blue | (byte)(((valor1 >> 2) & 1) << 3));
            blue = (byte) (blue | (byte)(((valor1 >> 1) & 1) << 2));
            
            green = (byte) (green | (byte)(((valor2 >> 7) & 1) << 5));
            green = (byte) (green | (byte)(((valor2 >> 6) & 1) << 4));
            green = (byte) (green | (byte)(((valor2 >> 5) & 1) << 3));
            green = (byte) (green | (byte)(((valor2 >> 4) & 1) << 2));
            
            red = (byte) (red | (byte)(((valor2 >> 3) & 1) << 5));
            red = (byte) (red | (byte)(((valor2 >> 2) & 1) << 4));
            red = (byte) (red | (byte)(((valor2 >> 1) & 1) << 3));
            red = (byte) (red | (byte)(((valor2) & 1) << 2));
            
            if ((blue&0xFF)!=0){
                if ((sinalBlue&0xFF)!=0){//subtrair
                    vBlue = -(blue&0xFF);
                }else{
                    vBlue = (blue&0xFF);
                }
                vBlue = vBlue + (imgB[i*3]&0xFF);
                this.pixel[i*3] = (byte)vBlue;
            }else{
                this.pixel[i*3] = imgB[i*3];
            }

            if ((green&0xFF)!=0){
                if ((sinalGreen&0xFF)!=0){
                    vGreen = -(green&0xFF);
                }else{
                    vGreen = (green&0xFF);
                }
                vGreen = vGreen + (imgB[i*3+1]&0xFF);
                this.pixel[i*3+1] = (byte)vGreen;
            }else{
                this.pixel[i*3+1] = imgB[i*3+1];
            }
            
            if ((red&0xFF)!=0){
                if ((sinalRed&0xFF)!=0){
                    vRed = -(red&0xFF);
                }else{
                    vRed = (red&0xFF);
                }
                vRed = vRed + (imgB[i*3+2]&0xFF);
                this.pixel[i*3+2] = (byte)vRed;
            }else{
                this.pixel[i*3+2] = imgB[i*3+2];
            }
        }
        //System.out.println("byte[] : " + Imagem.getInterpretacaoDiferanca(bytesDeDiferenca) + "depois : " + this.testPrint());
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
            int linhaTamanho = width*3 + (width)%4;
            if (resetar){
                auxBuffer = new byte[linhaTamanho*height];
                //System.out.println("tam aux : " + auxBuffer.length + ", tam pixels : " + pixel.length);
                //System.out.println("extra : " + (width%4));
                //System.out.println("deslocamento : " + deslocamento);
            }
            if (auxBuffer.length==pixel.length){
                fis.read(pixel);
            }else{
                fis.read(auxBuffer);
                for (int i=0;i<height;i++){
                    //System.out.println("aux "+(i*linhaTamanho)+"-"+(i*linhaTamanho+width*3-1));
                    //System.out.println("pixel "+(i*width*3)+"-"+(i*width*3+width*3-1));
                    System.arraycopy(auxBuffer, i*linhaTamanho, pixel,i*width*3, width*3);
                }
            }
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
        byte[] b = new byte[3];
        int pixels = width*height;
        for (int i=0;i<pixels;i++){
            System.arraycopy(this.pixel, i*3, b, 0, 3);
            aux.append(i).append("=").append('{');
            aux.append(b[0]&0xFF).append(',');
            aux.append(b[1]&0xFF).append(',');
            aux.append(b[2]&0xFF).append(")\n");
        }
        return(aux.toString());
    }

}
