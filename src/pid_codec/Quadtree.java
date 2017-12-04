/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author FREE
 */
public class Quadtree {
    private static ArrayList<Byte> arvore = new ArrayList<>();
    private static int arvoreBitCounter = 7;
    private static int arvCounter = 0;
    private static boolean estado = true;
    private static byte byteBuffer = 0;
    private static ArrayList<Byte> cores;
    private static byte[] corGlobal;
    
    public static Arvore arvoreFim(byte[] cor,Arvore[] arvVet){
        if (arvoreBitCounter!=7){
            //System.out.println("2 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
            arvore.add(byteBuffer);
            //System.out.println("byteBufferValor : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
        }
        Arvore ret = null;
        byte[] r2 = new byte[8+cores.size()+arvore.size()];
        ByteBuffer bbInt = ByteBuffer.allocate(4);
        bbInt.putInt(arvCounter);
        System.arraycopy(bbInt.array(), 0, r2, 0, 4);
        bbInt.rewind();
        bbInt.putInt((cores.size()/2));
        //System.out.println("cores.size="+cores.size()+",bbIntToInt : " + ByteBuffer.wrap(bbInt.array()).getInt());
        System.arraycopy(bbInt.array(), 0, r2, 4, 4);
        //System.out.println("arvore.size : " + arvore.size());
        System.out.println("tam arvore : " + arvore.size());
        System.out.println("tam cores : " + cores.size());
        System.out.println("total : " + r2.length);
        for (int j=0;j<arvore.size();j++){
            r2[j+8] = arvore.get(j);
        }
        boolean par = false;
        for (int j=0;j<cores.size();j++){
            if (par){
                par = false;
            }else{
                par = true;
            }
            r2[j+8+arvore.size()] = cores.get(j);
        }
        /*
        for (int i=0;i<r2.length;i++){
            System.out.println("byte : " + i + " = "+ String.format("%8s", Integer.toBinaryString(r2[i] & 0xFF)).replace(' ', '0'));
        }
        */
        if (cor!=null){
            ret = new Arvore(corGlobal,r2);
        }else{
            ret = new Arvore(arvVet,r2);
        }
        return(ret);
    }
    
    private static Arvore getArvore(int width,int height,byte[] bytesDeDiff){
        boolean controleLocal = false;
        arvCounter++;
        if (estado){
            estado = false;
            controleLocal = true;
            arvore = new ArrayList<>();
            cores = new ArrayList<>();
            arvCounter = 1;
            arvoreBitCounter = 7;
            byteBuffer = (byte)0;
            corGlobal = null;
        }
        //System.out.println("width : " + width + "height : " + height + ", bytesSize : " + bytesDeDiff.length);
        byte[] cor = null;
        byte[] auxCor = new byte[2];
        boolean ok = true;
        int interacoes = 2*width*height;
        Arvore ret = null;
        for (int i=0;i<interacoes;i=i+2){
            System.arraycopy(bytesDeDiff,i, auxCor, 0, 2);
            //System.out.println("cor  : "+Cor.getInterpretacaoCor(auxCor));
            if (cor==null){
                cor = new byte[2];
                System.arraycopy(auxCor, 0, cor, 0, 2);
            }else{
                if (Arrays.equals(cor, auxCor)==false){
                    ok = false;
                    break;
                }
            }
        }
        if (ok){ // cores iguais = unica representacao
            //System.out.println("cor adicionada quando arvCounter : "+arvCounter+" e arvBitCounter : " + arvoreBitCounter);
            //System.out.println("cor "+(cores.size()/2)+" : " + Cor.getInterpretacaoCor(cor));
            cores.add(cor[1]);
            cores.add(cor[0]);
            //System.out.println("cor OK : "+Cor.getInterpretacaoCor(cor));
            //System.out.println("byteBuffer antes : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
            byteBuffer = (byte) (byteBuffer | (1 << arvoreBitCounter));
            //System.out.println("byteBffer int valor : " + (byteBuffer&0xFF));
            //System.out.println("byteBuffer after : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
            if (arvoreBitCounter==0){
                arvore.add(byteBuffer);
                //System.out.println("1 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
                byteBuffer = (byte)0;
                arvoreBitCounter=7;
            }else{
                arvoreBitCounter--;
            }
            if (controleLocal){
                ret = arvoreFim(cor,null);
            }else{
                ret = new Arvore(cor,null);
            }
        }else{
            int width1,width2,height1,height2;
            if (width%2==0){ //par
                width1 = width/2;
                width2 = width/2;
            }else{
                width1 = (width+1)/2;
                width2 = (width-1)/2;
            }
            if (height%2==0){
                height1 = height/2;
                height2 = height/2;
            }else{
                height1 = (height+1)/2;
                height2 = (height-1)/2;
            }
            // ---------------
            // |  b   |  b   | 
            // |  3   |  4   |
            // |--------------
            // |  b   |  b   |
            // |  1   |  2   |
            // --------------
            
            //casos especiais 
            //height2 = 0
            // ---------------
            // |  b   |  b   | 
            // |  1   |  2   |
            // |--------------
            
            //width2 = 0
            // --------
            // |  b   |
            // |  2   |
            // |-------
            // |  b   |
            // |  1   |
            // --------
            
            //em mem b1b2b1b2b1b2...b1b2b3b4b3b4b3b4...b3b4.
            if (height2!=0 && width2!=0){
                //System.out.println("branching 4, arvBitCounter : " + arvoreBitCounter + ",arvCounter : " + arvCounter + ",size :"+(height*width));
                if (arvoreBitCounter==0){
                    //System.out.println("3 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
                    arvore.add(byteBuffer);
                    byteBuffer = 0;
                    arvoreBitCounter=7;
                }else{
                    arvoreBitCounter--;
                }
                if (arvoreBitCounter==0){
                    //System.out.println("4 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
                    arvore.add(byteBuffer);
                    byteBuffer = 0;
                    arvoreBitCounter=7;
                }else{
                    arvoreBitCounter--;
                }
                final int condicaoB1B2 = (width1*height1+width2*height1)*2;
                final int condicaoB3B4 = condicaoB1B2 + (width1*height2+width2*height2)*2;

                //System.out.println("bytes inicial : "+Cor.getInterpretacaoCor(bytesDeDiff));
                byte[] b1 = new byte[width1*height1*2];// w1 e h1
                byte[] b2 = new byte[width2*height1*2];// w2 e h1
                int i=0;
                int contadorb1 = 0;
                int contadorb2 = 0;
                while (i<condicaoB1B2){
                    System.arraycopy(bytesDeDiff, i, b1, contadorb1, width1*2);
                    contadorb1 = contadorb1+width1*2;
                    i = i+width1*2;
                    System.arraycopy(bytesDeDiff, i, b2, contadorb2, width2*2);
                    contadorb2 = contadorb2+width2*2;
                    i = i+width2*2;
                }
                byte[] b3 = new byte[width1*height2*2];// w1 e h2
                byte[] b4 = new byte[width2*height2*2];// w2 e h2
                int contadorb3 = 0;
                int contadorb4 = 0;
                //System.out.println("width1 : " + width1 + ",width2 : " + width2 + ",height1 : " + height1 + ",height2 : " + height2 + ",c1c2 : " + condicaoB1B2 + ",c3c4 :" + condicaoB3B4);
                while (i<condicaoB3B4){
                    System.arraycopy(bytesDeDiff, i, b3, contadorb3, width1*2);
                    contadorb3 = contadorb3+width1*2;
                    i = i+width1*2;
                    System.arraycopy(bytesDeDiff, i, b4, contadorb4, width2*2);
                    contadorb4 = contadorb4+width2*2;
                    i = i+width2*2;
                }
                bytesDeDiff = null;
                //System.out.println("b1 : "+Cor.getInterpretacaoCor(b1));
                //System.out.println("b2 : "+Cor.getInterpretacaoCor(b2));
                //System.out.println("b3 : "+Cor.getInterpretacaoCor(b3));
                //System.out.println("b4 : "+Cor.getInterpretacaoCor(b4));
                Arvore a1 = getArvore(width1, height1, b1);
                Arvore a2 = getArvore(width2, height1, b2);
                Arvore a3 = getArvore(width1, height2, b3);
                Arvore a4 = getArvore(width2, height2, b4);
                Arvore[] arvVet = new Arvore[4];
                arvVet[0] = a1;
                arvVet[1] = a2;
                arvVet[2] = a3;
                arvVet[3] = a4;
                if (controleLocal){
                    ret = arvoreFim(null,arvVet);
                }else{
                    ret = new Arvore(arvVet,null);
                }
            }else{
                //System.out.println("branching 2, arvBitCounter : " + arvoreBitCounter + ",arvCounter : " + arvCounter + ",size :"+(height*width));
                if (arvoreBitCounter==0){
                    //System.out.println("6 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
                    arvore.add(byteBuffer);
                    byteBuffer = 0;
                    arvoreBitCounter=7;
                }else{
                    arvoreBitCounter--;
                }
                byteBuffer = (byte) (byteBuffer | (1 << arvoreBitCounter));
                if (arvoreBitCounter==0){
                    //System.out.println("7 quando arvCounter : " + arvCounter + ", flush : " + String.format("%8s", Integer.toBinaryString(byteBuffer & 0xFF)).replace(' ', '0'));
                    arvore.add(byteBuffer);
                    byteBuffer = 0;
                    arvoreBitCounter=7;
                }else{
                    arvoreBitCounter--;
                }
                int v1 = width1*height1;
                int v2=0;
                int a=0;
                int b=0;
                if (height2==0){
                    v2 = width2*height1; 
                    a = width2;
                    b = height1;
                }
                if (width2==0){
                    v2 = width1*height2;
                    a = width1;
                    b = height2;
                }
                byte[] b1 = new byte[v1*2];
                byte[] b2 = new byte[v2*2];
                System.arraycopy(bytesDeDiff, 0, b1, 0, v1*2);
                System.arraycopy(bytesDeDiff, v1*2, b2, 0, v2*2);
                if (b1.length==0 || b2.length==0){
                    throw new IllegalArgumentException("valor zero nao esperado!");
                }
                if ((v1*2 + v2*2) != bytesDeDiff.length){
                    throw new IllegalArgumentException("tamanho bytes aparente estar incorreto");
                }
                Arvore a1 = getArvore(width1,height1,b1);
                Arvore a2 = getArvore(a,b,b2);
                Arvore[] arvVet = new Arvore[4];
                arvVet[0] = a1;
                arvVet[1] = a2;
                arvVet[2] = null;
                arvVet[3] = null;
                if (controleLocal){
                    ret = arvoreFim(null,arvVet);
                }else{
                    ret = new Arvore(arvVet,null);
                }
            }
        }
        if (controleLocal){
            estado = true;
        }
        return(ret);
    }
    
    public static String getSignificado(Imagem anterior,byte[] bDiff){
        Arvore av = getArvore(anterior.getWidth(), anterior.getHeight(), bDiff);
        return(Arvore.getSignificado(anterior.getWidth(), anterior.getHeight(), av));
    }
    
    public static byte[] getQuadBytes(int width,int height,byte[] bytesDeDiff){
        Arvore av = getArvore(width, height, bytesDeDiff);
        //System.out.println(Arvore.getSignificado(av.getArvoreBytes()));
        return(av.getArvoreBytes());
    }
    
    public static byte[] writeQuadBytes(int width,int height,byte[] bytesDeDiff){
        return(null);
    }
}
