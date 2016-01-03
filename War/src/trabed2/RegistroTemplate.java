/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trabed2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daniel
 */
public class RegistroTemplate {
    
    //Tamanho máximo da string nos registros
    private static final int TAMSTRING = 30;
    private int numCampos;
    private int tam;
    private Integer[] tipoCampo;


    public RegistroTemplate(int numCampos){
        this.numCampos = numCampos;
        tipoCampo = new Integer[numCampos];
        tam = 0;
    }
    
    public RegistroTemplate(Integer[] s){
        numCampos = s.length;
        tipoCampo = s;
        tam = 0;
        for (int i=0; i<numCampos; i++){
            if (s[i] == 1){
                tam+= Integer.SIZE;
            }
            if (s[i] == 2){
                tam+= Double.SIZE;
            }
            if (s[i] == 3){
                tam+= (Character.SIZE*TAMSTRING)+2;
            }
        }
    }
    
    public RegistroTemplate(ArrayList<Object> s) {
        numCampos = s.size();
        tipoCampo = new Integer[numCampos];
        tam = 0;
        for (int i=0; i<numCampos; i++){
            if (s.get(i) instanceof Integer){
                tipoCampo[i]=1;
                tam+= Integer.SIZE;
            }
            if (s.get(i) instanceof Double){
                tipoCampo[i]=2;
                tam+= Double.SIZE;
            }
            if (s.get(i) instanceof String){
                tipoCampo[i]=3;
                tam+= (Character.SIZE*TAMSTRING)+2;
            }
        }
        //Para considerar o booleano para saber se está vazio ou não.
        tam+=Integer.SIZE;
        tam/=8;
    }

    public int getNumCampos(){
        return numCampos;
    }

    public int getTam(){
        return tam;
    }

    public int getTipoCampo(int i){
        return tipoCampo[i];
    }

    public void escreve(RandomAccessFile arq){
        try {
            arq.writeInt(numCampos);
            arq.writeInt(tam);
            for (int i = 0; i< tipoCampo.length; i++)
                arq.writeInt(tipoCampo[i]);
        } catch (IOException ex) {
            Logger.getLogger(RegistroTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void le(RandomAccessFile arq){
        try {
            arq.readInt();
            arq.readInt();
            for (int i = 0; i < tipoCampo.length; i++) {
                int lido = arq.readInt();
                tipoCampo[i] = lido;
                if (lido == 1) {
                    tam += Integer.SIZE;
                }
                if (lido == 2) {
                    tam += Double.SIZE;
                }
                if (lido == 3) {
                    tam += Character.SIZE * TAMSTRING;
                }
                
            }           
        } catch (IOException ex) {ex.printStackTrace();}
    }
    

}
