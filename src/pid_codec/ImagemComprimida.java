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
public class ImagemComprimida {
    private int width;
    
    private int height;
    
    //private ArrayList<Cor> pixel;
    private byte[] pixel;
    
    public static ImagemComprimida difImagem(Imagem a,Imagem b){
        ImagemComprimida ret = new ImagemComprimida();
        Cor[] aCor = a.getPixel();
        Cor[] bCor = b.getPixel();
        ret.width = a.getWidth();
        ret.height = a.getHeight();
        ret.pixel = new byte[aCor.length];
        for (int i=0;i<ret.pixel.length;i++){
            byte corDif = 0;
            Cor c1 = aCor[i];
            Cor c2 = bCor[i];
            //red
            byte r1 = c1.getRed();
            byte r2 = c2.getRed();
            if (r2 < r1){
                corDif = (byte) (corDif | (1 << 5)); // Indica subtracao de r1 para r2 
            }
            if (Math.abs(r1-r2)>15){
                corDif = (byte) (corDif | (1 << 4)); 
            }
            
            //green
            byte g1 = c1.getGreen();
            byte g2 = c2.getGreen();
            if (g2 < g1){
                corDif = (byte) (corDif | (1 << 3)); // Indica subtracao de g1 para g2 
            }
            if (Math.abs(g1-g2)>15){
                corDif = (byte) (corDif | (1 << 2));
            }
            
            //blue
            byte b1 = c1.getBlue();
            byte b2 = c2.getBlue();
            if (Math.abs(b1-b2)>15){
                corDif = (byte) (corDif | (1 << 1)); // Indica subtracao de g1 para g2 
            }
            if (!(b1==b2)){
                corDif = (byte) (corDif | (1 << 0));
            }
            
            ret.pixel[i] = corDif;
        }
        return(ret);
    }
    
    private ImagemComprimida(){
        
    }
    
    public ImagemComprimida(byte[] bytes){
        ImagemComprimida ret = new ImagemComprimida();
        Cor[] aCor = a.getPixel();
        Cor[] bCor = b.getPixel();
        ret.width = a.getWidth();
        ret.height = a.getHeight();
        ret.pixel = new byte[aCor.length];
        for (int i=0;i<ret.pixel.length;i++){
            byte corDif = 0;
            
            ret.pixel[i] = corDif;
        }
        return(ret???????);
    }
    
    public byte[] getBytes(){
        return(pixel);
    }
}
