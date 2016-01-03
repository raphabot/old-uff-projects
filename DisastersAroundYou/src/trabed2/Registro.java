/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trabed2;

import java.util.ArrayList;

/**
 *
 * @author lcc
 */
public class Registro {

    //private int key;
    private ArrayList<Object> info;
    private int ocupado;
    
    public Registro (){
        info = new ArrayList<Object>();
        ocupado = 1;
    }

    public Registro (int key, int v){
        info = new ArrayList<Object>();
        info.add(key);
        info.add(v);
        ocupado = 1;
    }
    
    public Registro(ArrayList<Object> objetos){
        info = objetos;
        ocupado =1;
        
    }

    private ArrayList<Object> getInfo() {
        return info;
    }

    public void setInfo(Object objeto){
        this.getInfo().add(objeto);
    }

    public Object getInfo(int i){
        return this.getInfo().get(i);
    }

    public int isOcupado() {
        return ocupado;
    }

}
