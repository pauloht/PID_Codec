/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FREE
 */
public class VideoWriter {
    public static void write(File diretorio,String baseNome,String extensao,int ultimoID,File nameOut){
        int ultimoIdFixo = 10;
        FileOutputStream out = null;
        try {
            System.out.println("write start");
            out = new FileOutputStream(nameOut);
            File fileImagemInicial = new File((diretorio.getPath()+"//"+baseNome+util.retornarStringPorNumero(0)+extensao));
            Imagem imagemInicial = new Imagem(fileImagemInicial,true);
            int width = imagemInicial.getWidth();
            int height = imagemInicial.getHeight();
            
            PTimer t2 = new PTimer("tempo total");
            t2.startTimer();
            PTimer ptstart = new PTimer("primeiroFrame");
            ptstart.startTimer();
            byte[] cabecalho = new byte[12];
            byte[] ultimoIDBytes = ByteBuffer.allocate(4).putInt(ultimoID).array();
            byte[] widthBytes = ByteBuffer.allocate(4).putInt(width).array();
            byte[] heightBytes = ByteBuffer.allocate(4).putInt(height).array();
            System.arraycopy(ultimoIDBytes, 0, cabecalho, 0, 4);
            System.arraycopy(widthBytes, 0, cabecalho, 4, 4);
            System.arraycopy(heightBytes, 0, cabecalho, 8, 4);
            out.write(cabecalho);
            out.write(imagemInicial.getBytes());
            Imagem imagemBuffer = new Imagem(imagemInicial);
            ptstart.endTimer();
            byte[] aux;
            System.out.println(ptstart);
            for (int i=1;i<=ultimoID;i++){
                PTimer ptloop= new PTimer("frame "+Integer.toString(i));
                ptloop.startTimer();
                File file = new File( (diretorio.getPath()+"//"+baseNome+util.retornarStringPorNumero(i)+extensao) );
                imagemInicial.setImagem(file,false);
                aux = Imagem.diffImagem(imagemBuffer, imagemInicial);
                imagemInicial.setPixels(imagemBuffer, aux);
                
                //byte[] test = Quadtree.getQuadBytes(width, height, aux);
                
                System.out.println("tamanho diff : " + aux.length);
                //System.out.println("tamanho arvore : " + test.length);
                ptloop.endTimer();
                
                imagemBuffer = new Imagem(imagemInicial);
                out.write(aux);
                //break;
                //System.out.println(ptloop);
            }
            t2.endTimer();
            System.out.println(t2);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VideoWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VideoWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            System.out.println("write fim");
        }
    }
}
