/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pid_codec;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 *
 * @author FREE
 */
public class Arvore{
    private Arvore[] filhos = null;
    private int visitados = -1;
    private int id = 0;
    private Arvore pai = null;
    private byte[] valor = null;
    private int[] valores = null;//width height posX posY
    private static ArrayList< Arvore > lista = new ArrayList<>();
    private static ArrayList< Integer > listaW = new ArrayList<>();
    private static ArrayList< Integer > listaH = new ArrayList<>();
    private static ArrayList< Integer > startX = new ArrayList<>();
    private static ArrayList< Integer > startY = new ArrayList<>();
    private byte[] arvoreBytes;
    
    private Arvore(){
        this.pai = null;
    }
    
    private Arvore(Arvore pai){
        this.pai = pai;
    }
    
    public Arvore(byte[] b,byte[] representaoB){
        valor = b;
        filhos = null;
        arvoreBytes = representaoB;
    }

    public Arvore(Arvore[] f,byte[] representaoB){
        filhos = f;
        valor = null;
        arvoreBytes = representaoB;
    }
    
    public static void setImagem(Imagem img,byte[] bytes){
        byte[] tamB = new byte[4];
        System.arraycopy(bytes,0,tamB,0,4);
        int tam = ByteBuffer.wrap(tamB).getInt();
        int deslocamentoCores=0;
        System.arraycopy(bytes,4,tamB,0,4);
        int quantasCores = ByteBuffer.wrap(tamB).getInt();
        int tamanhoBits = (tam-quantasCores)*2 + quantasCores;
        if (tamanhoBits%8==0){
            deslocamentoCores = 8 + tamanhoBits/8 -1;
        }else{
            deslocamentoCores = 8 + (int)(tamanhoBits/8);
        }
        int contadorByte = 7;
        int count = 8;
        int contadorGlobal = 0;
        int contadorCor = 0;
        byte byteB;
        byte bLido = 0;
        Arvore arvLocal = new Arvore();
        arvLocal.valores = new int[4];
        arvLocal.valores[0] = img.getWidth();
        arvLocal.valores[1] = img.getHeight();
        arvLocal.valores[2] = 0;
        arvLocal.valores[3] = 0;
        byteB = bytes[count];
        int width1,width2,height1,height2;
        int idAux=0;
        while (contadorGlobal<tam){
            bLido = (byte)((byteB >> contadorByte) & 1);
            if (contadorByte==0){
                count++;
                byteB = bytes[count];
                contadorByte = 7;
            }else{
                contadorByte--;
            }
            arvLocal.id = idAux;
            idAux++;
            if (bLido == 0){//tem que ler o proximo para ver se eh 00->4filhos ou 01->2filhos
                bLido = (byte)((byteB >> contadorByte) & 1);
                if (contadorByte==0){
                    count++;
                    byteB = bytes[count];
                    contadorByte = 7;
                }else{
                    contadorByte--;
                }
                if ((bLido&0xFF)==0){//4 filhos
                    if (arvLocal.valores[0]%2==0){ //par
                        width1 = arvLocal.valores[0]/2;
                        width2 = arvLocal.valores[0]/2;
                    }else{
                        width1 = (arvLocal.valores[0]+1)/2;
                        width2 = (arvLocal.valores[0]-1)/2;
                    }
                    if (arvLocal.valores[1]%2==0){
                        height1 = arvLocal.valores[1]/2;
                        height2 = arvLocal.valores[1]/2;
                    }else{
                        height1 = (arvLocal.valores[1]+1)/2;
                        height2 = (arvLocal.valores[1]-1)/2;
                    }
                    System.out.println("("+ arvLocal.id + ") Pai 4 , Para width,height,posX,posY = "+arvLocal.valores[0]+","+arvLocal.valores[1]+","+arvLocal.valores[2]+","+arvLocal.valores[3]+",visitados="+arvLocal.visitados);
                    System.out.println("width1 : " + width1 + ",width2 : " + width2 + ",height1 : " + height1 + ",height2 : " + height2);
                    arvLocal.filhos = new Arvore[4];
                    Arvore a1 = new Arvore(arvLocal);
                    a1.visitados = 0;
                    a1.valores = new int[4];
                    a1.valores[0] = width1;
                    a1.valores[1] = height1;
                    a1.valores[2] = arvLocal.valores[2];
                    a1.valores[3] = arvLocal.valores[3];
                    arvLocal.filhos[0] = a1;
                    Arvore a2 = new Arvore(arvLocal);
                    a2.visitados = 0;
                    a2.valores = new int[4];
                    a2.valores[0] = width2;
                    a2.valores[1] = height1;
                    a2.valores[2] = arvLocal.valores[2]+width1;
                    a2.valores[3] = arvLocal.valores[3];
                    arvLocal.filhos[1] = a2;
                    Arvore a3 = new Arvore(arvLocal);
                    a3.visitados = 0;
                    a3.valores = new int[4];
                    a3.valores[0] = width1;
                    a3.valores[1] = height2;
                    a3.valores[2] = arvLocal.valores[2];
                    a3.valores[3] = arvLocal.valores[3]+height1;
                    arvLocal.filhos[2] = a3;
                    Arvore a4 = new Arvore(arvLocal);
                    a4.visitados = 0;
                    a4.valores = new int[4];
                    a4.valores[0] = width2;
                    a4.valores[1] = height2;
                    a4.valores[2] = arvLocal.valores[2]+width1;
                    a4.valores[3] = arvLocal.valores[3]+height1;
                    arvLocal.filhos[3] = a4;
                    arvLocal.visitados = 1;
                    System.out.println(arvLocal.id + " visitados after : " + arvLocal.visitados);
                    for (int i=0;i<4;i++){
                        System.out.println("("+ arvLocal.id + ") Filho : "+i+": , Para width,height,posX,posY = "+arvLocal.filhos[i].valores[0]+","+arvLocal.filhos[i].valores[1]+","+arvLocal.filhos[i].valores[2]+","+arvLocal.filhos[i].valores[3]);
                    }
                    arvLocal = a1;
                }else{ // 2 filhos
                    System.out.println("("+ arvLocal.id + ") Pai 2 , Para width,height,posX,posY = "+arvLocal.valores[0]+","+arvLocal.valores[1]+","+arvLocal.valores[2]+","+arvLocal.valores[3]);
                    if (arvLocal.valores[0]%2==0){ //par
                        width1 = arvLocal.valores[0]/2;
                        width2 = arvLocal.valores[0]/2;
                    }else{
                        width1 = (arvLocal.valores[0]+1)/2;
                        width2 = (arvLocal.valores[0]-1)/2;
                    }
                    if (arvLocal.valores[1]%2==0){
                        height1 = arvLocal.valores[1]/2;
                        height2 = arvLocal.valores[1]/2;
                    }else{
                        height1 = (arvLocal.valores[1]+1)/2;
                        height2 = (arvLocal.valores[1]-1)/2;
                    }
                    arvLocal.filhos = new Arvore[4];
                    Arvore a1 = new Arvore(arvLocal);
                    a1.visitados = 0;
                    a1.valores = new int[4];
                    a1.valores[0] = width1;
                    a1.valores[1] = height1;
                    a1.valores[2] = arvLocal.valores[2];
                    a1.valores[3] = arvLocal.valores[3];
                    arvLocal.filhos[0] = a1;
                    Arvore a2 = new Arvore(arvLocal);
                    a2.visitados = 0;
                    a2.valores = new int[4];
                    if (width2==0){
                        a2.valores[0] = width1;
                        a2.valores[1] = height2;
                        a2.valores[2] = arvLocal.valores[2];
                        a2.valores[3] = arvLocal.valores[3]+height1;
                    }else{
                        a2.valores[0] = width2;
                        a2.valores[1] = height1;
                        a2.valores[2] = arvLocal.valores[2]+width1;
                        a2.valores[3] = arvLocal.valores[3];
                    }
                    arvLocal.filhos[1] = a2;
                    arvLocal.filhos[2] = null;
                    arvLocal.filhos[3] = null;
                    for (int i=0;i<2;i++){
                        System.out.println("("+ arvLocal.id + ")Filho : "+i+": , Para width,height,posX,posY = "+arvLocal.filhos[i].valores[0]+","+arvLocal.filhos[i].valores[1]+","+arvLocal.filhos[i].valores[2]+","+arvLocal.filhos[i].valores[3]);
                    }
                    arvLocal.visitados = 1;
                    arvLocal = a1;
                }
            }else{ // cor
                byte[] b = new byte[2];
                b[0] = bytes[deslocamentoCores+contadorCor*2+1];
                b[1] = bytes[deslocamentoCores+contadorCor*2];
                System.out.println("("+ arvLocal.id + ") Cor : " + Cor.getInterpretacaoCor(b));
                //System.out.println("Para width,height,posX,posY = "+arvLocal.valores[0]+","+arvLocal.valores[1]+","+arvLocal.valores[2]+","+arvLocal.valores[3]);
                int aux = 0;
                for (int i=0;i<arvLocal.valores[1];i++){
                    for (int j=0;j<arvLocal.valores[0];j++){
                        aux = img.getWidth()*(i+arvLocal.valores[3])+(j+arvLocal.valores[2]);
                        img.getBytes()[aux] = b[1];
                        img.getBytes()[aux+1] = b[0];
                    }
                }
                contadorCor++;
                arvLocal.valor = b;
                while (true){
                    if (arvLocal.pai == null){
                        break;
                    }
                    arvLocal = arvLocal.pai;
                    aux = arvLocal.visitados;
                    //System.out.println("retrocendo para pai "+arvLocal.id);
                    //System.out.println("Para width,height,posX,posY = "+arvLocal.valores[0]+","+arvLocal.valores[1]+","+arvLocal.valores[2]+","+arvLocal.valores[3]);
                    if (aux==0){//visitou nenhum nó ???
                        throw new IllegalArgumentException("visitados == 0 para id = "+arvLocal.id);
                    }else if (aux==1){//visitou primeiro no
                        arvLocal.visitados = 2;
                        arvLocal = arvLocal.filhos[aux];
                        break;
                    }else if (aux==2){//visitou primeiro e segundo no
                        if (arvLocal.filhos[aux]!=null){
                            arvLocal.visitados = 3;
                            arvLocal = arvLocal.filhos[aux];
                            break;
                        }else{
                            arvLocal.visitados = 4;
                        }
                    }else if(aux==3){//visitou primeiro e segundo e terceiro no
                        arvLocal.visitados = 4;
                        arvLocal = arvLocal.filhos[aux];
                        break;
                    }else if (aux==4){
                        //visitou todos os nos
                    }
                    else{
                        throw new IllegalArgumentException("visitados == "+Integer.toString(aux));
                    }
                }
            }
            contadorGlobal++;
        }
        
    }
    
    public static String getSignificado(int width,int height,Arvore raiz){
        lista.add(raiz);
        listaW.add(width);
        listaH.add(height);
        startX.add(0);
        startY.add(0);
        Arvore aux;
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        int contador2 = 0;
        int width1,width2,height1,height2;
        int pX,pY;
        while (lista.isEmpty()==false){
            aux = lista.get(0);
            width = listaW.get(0);
            height = listaH.get(0);
            pX = startX.get(0);
            pY = startY.get(0);
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
            lista.remove(0);
            listaW.remove(0);
            listaH.remove(0);
            startX.remove(0);
            startY.remove(0);
            if (aux.valor!=null){
                sb.append("[").append(contador).append("]=").append(Cor.getInterpretacaoCor(aux.valor)).append("->(").append(pX).append("-").append((pX+width-1)).append(",").append(pY).append('-').append((pY+height-1)).append(')').append("\n");
            }else{
                sb.append("[").append(contador).append("]=").append("[").append(contador2+1);
                lista.add(aux.filhos[0]);
                listaW.add(width1);
                listaH.add(height1);
                startX.add(pX);
                startY.add(pY);
                
                if (aux.filhos[1]!=null){
                    lista.add(aux.filhos[1]);
                    listaW.add(width2);
                    listaH.add(height1);
                    startX.add(pX+width1);
                    startY.add(pY);
                    sb.append(',').append(contador2+2);
                }else{
                    contador2 = contador2-1;
                }
                
                if (aux.filhos[2]!=null){
                    lista.add(aux.filhos[2]);
                    listaW.add(width1);
                    listaH.add(height2);
                    startX.add(pX);
                    startY.add(pY+height1);
                    sb.append(',').append(contador2+3);
                }else{
                    contador2 = contador2-1;
                }
                
                if (aux.filhos[3]!=null){
                    lista.add(aux.filhos[3]);
                    listaW.add(width2);
                    listaH.add(height2);
                    startX.add(pX+width2);
                    startY.add(pY+height1);
                    sb.append(',').append(contador2+4); 
                }else{
                    contador2 = contador2-1;
                }
                sb.append(']').append("->(").append(pX).append("-").append((pX+width-1)).append(",").append(pY).append('-').append((pY+height-1)).append(')').append("\n");
                contador2 = contador2+4;
            }
            contador = contador+1;
        }
        lista = new ArrayList<>();
        listaW = new ArrayList<>();
        listaH = new ArrayList<>();
        startX = new ArrayList<>();
        startY = new ArrayList<>();
        return(sb.toString());
        /*
        StringBuilder sb = new StringBuilder();
        if (raiz.valor!=null){
            return("n["+Integer.toString(id) + "] = "+Cor.getInterpretacaoCor(raiz.valor)+"\n");
        }else{
            int width1,height1,width2,height2;
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
            sb.append(getSignificado(width1, height1, raiz.filhos[0],auxAltura[altura]-4+1,altura+1));
            sb.append(getSignificado(width2, height1, raiz.filhos[1],auxAltura[altura]-4+2,altura+1));
            sb.append(getSignificado(width1, height2, raiz.filhos[2],auxAltura[altura]-4+3,altura+1));
            sb.append(getSignificado(width2, height2, raiz.filhos[3],auxAltura[altura]-4+4,altura+1));
        }
        return(sb.toString());
        */
    }

    public byte[] getArvoreBytes() {
        return arvoreBytes;
    }
}
