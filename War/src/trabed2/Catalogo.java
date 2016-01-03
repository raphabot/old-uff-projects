/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabed2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author raphabot
 */
public class Catalogo {

    private RandomAccessFile catalogo;

    public Catalogo() throws FileNotFoundException, IOException {
        this.catalogo = new RandomAccessFile("Catalogo.dat", "rw");
        //Utilizado pois queremos que o arquivo seja zerado cada vez que é rodado o programa. Comentar, caso contrário
        //this.catalogo.setLength(0);
    }

    public RandomAccessFile getCatalogo() {
        return catalogo;
    }

    

    public long getTamanhoCatalogo() throws IOException {
        return catalogo.length();
    }

    /*public void seekTabela(int valor){
        this.catalogo.seek(valor/VALORSTRING);
    }*/

    public void fechaCatalogo() throws IOException{
        this.catalogo.close();
    }

    public boolean inserirTabela(String nomeTabela) throws FileNotFoundException, IOException {
        String aux;
        try {
            catalogo.seek(0);
            while (true) {
                aux = this.catalogo.readUTF();
                if (aux.compareTo(nomeTabela) == 0) {
                    return false;
                }
            }
        } catch (Exception e) {} 
        this.catalogo.writeUTF(nomeTabela);
        return true;
    }

    public int retornaTabela(String nomeTabela) throws Exception {
        this.catalogo.seek(0);
        int chave = -1;
        String nomeAtual = "";
        while (!nomeAtual.equals(nomeTabela)) {
            nomeAtual = this.catalogo.readUTF();
            chave++;
            if (nomeAtual.equals(nomeTabela)) {
                return chave;
            }
        }
        return -1;
    }

    public String listaTabelas() throws Exception {
        this.catalogo.seek(0);
        int cont = -1;
        String resp = "";
        try {
            while (true) {
                cont++;
                resp = resp + "Tabela " + cont + ": " + catalogo.readUTF() + "\n";
            }

        } catch (Exception e) {
        }
        return resp;
    }
}
