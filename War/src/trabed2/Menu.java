/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabed2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author raphabot
 */
public class Menu {

    private Catalogo catalogo;
    private Tabela tabela;
    private Campo registro;
    private String aux;
    private int tabelaAtual;

    public Menu() throws FileNotFoundException, IOException {
        this.catalogo = new Catalogo();
        this.tabela = new Tabela();
        this.registro = new Campo();
        int cont = -1;
        try {
            while (true) {
                this.catalogo.getCatalogo().readUTF();
                cont++;
            }
        } catch (Exception e) {
        }
        this.tabelaAtual = cont;
    }

    public void destroi() throws IOException {
        this.catalogo.fechaCatalogo();
        this.tabela.destroiTabela();
        for (int i = 0; i < tabelaAtual; i++) {
            this.tabela = new Tabela(i,null);
            this.tabela.destroiTabela();
        }
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public boolean criaTabela() throws Exception {

        this.aux = JOptionPane.showInputDialog("Qual será o nome da Tabela?");
        if (this.catalogo.inserirTabela(aux) == false) {
            return false;
        }
        //O tabelaAtual é um contador para saber qual o arquivo em que se encontra a tabela
        this.tabelaAtual++;
        this.tabela = new Tabela(tabelaAtual,null);
        return true;
    }

    public void criaCampos() throws IOException {
        int quant = Integer.parseInt(JOptionPane.showInputDialog("Quantos serão os campos da tabela?"));
        //tabela.getRt().
        int ehChave = 0;
        ArrayList<Integer> objetos = new ArrayList<Integer>();
        for (int i = 0; i < quant; i++) {
            //while (this.registro.getNome() == null){
            if (i == 0) {
                this.registro.setNome(JOptionPane.showInputDialog("Qual o nome do campo chave? "));
                ehChave = 1;
                objetos.add(1);
            } else {
                this.registro.setNome(JOptionPane.showInputDialog("Qual o nome" +
                        " do campo " + i + "?"));
                ehChave=0;//JOptionPane.showMessageDialog(null, "Nada foi digitado");
            }            //}

            if (tabela.existeCampo(registro.getNome()) == true) {
                //BOTAR PARA RETORNAR O ERRO!
                JOptionPane.showMessageDialog(null, "Campo já existe! " +
                        "O Campo atual e os próximos da tabela não serão inseridos.");
                break;
            }
            if (ehChave == 1) {
                this.registro.setChave(true);
                this.registro.setTipo(1);

            } else {
                this.registro.setChave(false);

                this.registro.setTipo(Integer.parseInt(JOptionPane.showInputDialog("Qual o tipo do campo?" +
                    "\n1 - Inteiro" +
                    "\n2 - Double" +
                    "\n3 - String")));
                objetos.add(this.registro.getTipo());          
                
            }
            this.tabela.setRt(objetos);
            this.tabela.escreveTabela(this.registro);
            this.tabela.setTabelaHash(tabela.getRt(),tabelaAtual);
        }
    }

    public void listarTabela() throws Exception {
        JOptionPane.showMessageDialog(null, this.catalogo.listaTabelas());
    }

    public void listaCampos(int numTabela) throws Exception {
        tabela = new Tabela(numTabela, this.tabela.getRt());
        JOptionPane.showMessageDialog(null, /*catalogo.seekTabela(numTabela)+"\n"+*/ this.tabela.imprimirCampos());
    }
    
    public boolean insereRegistro(int op){
        try {
            this.tabela = new Tabela(op, this.tabela.getRt());
            this.tabela.getTabelaHash().setfRegistro(this.tabela.getRt());
            Campo[] campos = tabela.retornaCampos(tabela.getRt());
            ArrayList<Object> valores = new ArrayList<Object>();
            for (int i = 0; i < tabela.getRt().getNumCampos();i++) {
                String entrada = JOptionPane.showInputDialog("Entre com o valor do campo: "+campos[i].getNome());
                if (tabela.getRt().getTipoCampo(i) == 1) {
                    valores.add(Integer.parseInt(entrada));
                }
                if (tabela.getRt().getTipoCampo(i) == 2) {
                     valores.add(Double.parseDouble(entrada));
                }
                if (tabela.getRt().getTipoCampo(i) == 3) {
                    valores.add((String)entrada);
                }
                //valores.add(entrada);
            }
            Registro r = new Registro(valores);
            tabela.getTabelaHash().put(tabelaAtual, r);
            return true;
        } catch (Exception ex) {ex.printStackTrace();return false;}
    }
}
