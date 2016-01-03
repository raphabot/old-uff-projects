/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabed2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Raphael Bottino, Bernardo Juncal, Marcos Paulino
 */
public class Tabela {

    private int indice;
    private RandomAccessFile tabela;
    private TabelaHash tabelaHash;
    private Registro registro;
    private RegistroTemplate rt;
    private Campo atributo;
    

    public Tabela(int i, RegistroTemplate rt) throws FileNotFoundException, IOException {
        this.indice = i;
        this.tabela = new RandomAccessFile("Tabela" + this.indice + ".dat", "rw");
        this.tabelaHash = new TabelaHash(i,rt);
        this.rt = rt;
    }

    public Tabela() {
    }

    public void destroiTabela() throws IOException {
        this.tabela.close();
    }

    public void escreveTabela(Campo atributo) {
        try {
            this.tabela.writeBoolean(atributo.isChave());
            this.tabela.writeInt(atributo.getTipo());
            this.tabela.writeUTF(atributo.getNome());

        } catch (Exception e) {
        }

    }

    public String imprimirCampos() throws Exception {
        this.tabela.seek(0);
        String resp = "";
        try {
            while (true) {
                Campo atributo = new Campo(this.tabela.readBoolean(), tabela.readInt(), this.tabela.readUTF());
                resp = resp + atributo.toString() + "\n";
            }
        } catch (IOException ex) {
        }
        return resp;
    }
    
    public Campo[] retornaCampos(RegistroTemplate rt){
        try {
            Campo[] campos = new Campo[rt.getNumCampos()];
            int i = 0;
            this.tabela.seek(0);
            try {
                while (tabela.getFilePointer()<tabela.length()) {
                    Campo campo = new Campo(this.tabela.readBoolean(), tabela.readInt(), this.tabela.readUTF());
                    campos[i] = campo;
                    i++;
                }
            } catch (IOException ex) {return null;}
            return campos;
        } catch (IOException ex) {return null;}
    }

    public void imprimirArquivo() {
        try {
            tabela.seek(0);
            while (true) {
                System.out.println(this.tabela.readBoolean());
                int aux = tabela.readInt();
                System.out.println(aux);
                System.out.println(this.tabela.readUTF());
            }

        } catch (Exception e) {
        }
    }

    public boolean existeCampo(String nome) throws IOException {
        this.tabela.seek(0);
        try {
            while (true) {
                atributo = new Campo(this.tabela.readBoolean(), this.tabela.readInt(), this.tabela.readUTF());
                if (nome.compareTo(atributo.getNome()) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public Registro getRegistro() {
        return registro;
    }

    public TabelaHash getTabelaHash() {
        return tabelaHash;
    }

    public RegistroTemplate getRt() {
        return rt;
    }

    public void setRt(RegistroTemplate rt) {
        this.rt = rt;
    }
    
    public void setRt(ArrayList<Integer> objetos) {
        Integer []tipos = new Integer[objetos.size()];
        objetos.toArray(tipos);
        this.rt = new RegistroTemplate(tipos);
    }

    public void setTabelaHash(RegistroTemplate rt, int ind) {
        this.tabelaHash = new TabelaHash(ind,rt);
    }

    public void setTabela(RandomAccessFile tabela) {
        this.tabela = tabela;
    }

    public void setAtributo(Campo atributo) {
        this.atributo = atributo;
    }
    
    
}
