package trabed2;

/**
 *
 * @author Raphael Bottino, Bernardo Junca, Marcos Paulino
 */
public class Campo {
    private boolean chave;
    private int tipo;
    private String nome;
    //private Object info;

    Campo (){
        
    }

    Campo(boolean c, int t, String n){
        this.chave = c;
        this.tipo = t;
        this.nome = n;
    }

    public void setChave(boolean chave) {
        this.chave = chave;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getTipo() {
        return tipo;
    }

    public boolean isChave() {
        return chave;
    }

    @Override
    public String toString() {
        String resp = "";
        if (this.isChave())
            resp = "CHAVE ";
        resp += "Nome: "+this.getNome();
        switch (this.getTipo()) {
            //Inteiro
            case 1:
            resp+= " Tipo: Inteiro";
            break;
            //Double
            case 2:
            resp+= " Tipo: Double";
            break;
            //String
            case 3:
            resp+= " Tipo: String";
            break;
            }
        return resp;
    }

}
