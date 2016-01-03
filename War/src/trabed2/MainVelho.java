/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trabed2;

import javax.swing.JOptionPane;

/**
 *
 * @author Raphael Bottino, Bernardo Junca, Marcos Paulino
 */
public class MainVelho {

    /**
     * @param args the command line arguments
     */
    public static final int STR_LENGHT = 64;

    public static void main(String[] args) throws Exception {

        boolean criou = false;
        Menu menu = new Menu();
        //boolean erro = menu.criaTabela();
        //menu.criaRegistro();
        int op = -1;
        try{
        while (op != 0) {
            op = Integer.parseInt(JOptionPane.showInputDialog("Qual a opção desejada?\n1- Criar outra tabela\n2- Listar tabelas\n3- Listar registros de uma tabela\n0- Sair do programa"));
            switch (op) {
                //Inteiro
                case 1:
                    if (menu.criaTabela() == true) {
                        menu.criaCampos();
                        criou = true;
                    } else {
                        JOptionPane.showMessageDialog(null, "A tabela já existe!");
                    }
                    break;
                //Double
                case 2:
                    if (menu.getCatalogo().getTamanhoCatalogo() == 0) {
                        JOptionPane.showMessageDialog(null, "Não existe nenhuma tabela!");
                    } else {
                        menu.listarTabela();
                    }
                    break;
                //String
                case 3:
                    if (menu.getCatalogo().getTamanhoCatalogo() == 0) {
                        JOptionPane.showMessageDialog(null, "Não existe nenhuma tabela!");
                    } else {
                        op = Integer.parseInt(JOptionPane.showInputDialog("Qual o número da Lista a ter seus registros listados?"));
                        menu.listaCampos(op);
                        op = 3; //Para ter certeza que não será o op que equivale a sair do loop.
                    }
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }

        }
        JOptionPane.showMessageDialog(null, "Obrigado por utilizar nosso programa ;)");
        if ( criou)
        menu.destroi();
    }catch ( Exception e)
    {
        JOptionPane.showMessageDialog(null, "Programa cancelado");
    }
    }
}
