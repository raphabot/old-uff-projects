package trabed2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TabelaHash {

    private static final double DEFlimiteLU = 0.65;
    private static final double DEFlimiteLoad = 0.7;
    private static final double DEFlimiteInfLoad = 0.1;
    private static final int DEFtable_size = 512;
    private static final int DEFbucket_size = 4;

    private final int bucket_size;
    private double limiteLU;
    private double limiteLoad;
    private double limiteInfLoad;
    private int table_size;
    private int bucket_cap;
    private int oriSize;
    //private Bucket[] bucket;
    private double luFactor;
    private double loadFactor;
    private int indice;
    private static int numIndices;
    private RandomAccessFile AR;
    private RandomAccessFile TH;
    private RandomAccessFile AC;
    private RegistroTemplate fRegistro;



    //Declaração da classe Bucket(Compartimento)
    private class Bucket{
        public EntradaHash[] elems;
        public int transbordado;
        public int cheio;

        public Bucket(int bucket_size) {
            elems = new EntradaHash[bucket_size];
            transbordado = 0;
            cheio = 0;
            for (int j = 0; j < bucket_size; j++) {
                elems[j] = new EntradaHash();
            }
        }
        
        public void le(RandomAccessFile arq) throws IOException{
            for (int i = 0; i<bucket_cap; i++){
                this.elems[i].le(arq);
            }
            this.transbordado = arq.readInt();
            this.cheio = arq.readInt();
        }
        
        public void escreve(RandomAccessFile arq) throws IOException{

            for (int i = 0; i<bucket_cap; i++){
                elems[i].escreve(arq);
            }
            arq.writeInt(transbordado);
            arq.writeInt(cheio);
        }
        
        //public static int SIZE(){
        //    return Integer.SIZE+16+EntradaHash.SIZE();
        //}

        
    }

    public TabelaHash(int ind){
        indice = ind;
        limiteLU = DEFlimiteLU;
        limiteLoad = DEFlimiteLoad;
        limiteInfLoad = DEFlimiteInfLoad;
        try {
            //Abrindo o Arquivo de Hash.
            TH = new RandomAccessFile("TabelaHash"+indice+".dat", "rw");
            TH.seek(0);
            AR = new RandomAccessFile("Registros"+indice+".dat", "rw");
            AR.seek(0);
            //Abrindo o cabeçalho
            AC = new RandomAccessFile("Cabecalho"+indice+".dat", "rw");
            AC.seek(0);
            int numCampos = AC.readInt();
            fRegistro = new RegistroTemplate(numCampos);
            fRegistro.le(AC);
            table_size = AC.readInt();
            bucket_cap = AC.readInt();
            luFactor = AC.readDouble();
            loadFactor = AC.readDouble();
            oriSize = AC.readInt();
        } catch (IOException ex) {ex.printStackTrace(); }
        bucket_size = (((2*Integer.SIZE)/8)+(bucket_cap*EntradaHash.SIZE));       
    }
    public TabelaHash(int ind, RegistroTemplate rt){
        indice = ind;
        limiteLU = DEFlimiteLU;
        limiteLoad = DEFlimiteLoad;
        limiteInfLoad = DEFlimiteInfLoad;
        try {
            //Abrindo o Arquivo de Hash.
            TH = new RandomAccessFile("TabelaHash"+indice+".dat", "rw");
            TH.seek(0);
            AR = new RandomAccessFile("Registros"+indice+".dat", "rw");
            AR.seek(0);
            //Abrindo o cabeçalho
            AC = new RandomAccessFile("Cabecalho"+indice+".dat", "rw");
            AC.seek(0);
            if (AC.length() == 0) {
                fRegistro = rt;
                table_size = DEFtable_size;
                bucket_cap = DEFbucket_size;
                luFactor = 0;
                loadFactor = 0;
                oriSize = table_size;
                if (fRegistro != null) {
                    AC.writeInt(fRegistro.getNumCampos());
                    fRegistro.escreve(AR);
                    AC.writeInt(table_size);
                    AC.writeInt(bucket_cap);
                    AC.writeDouble(luFactor);
                    AC.writeDouble(loadFactor);
                    AC.writeInt(oriSize);
                }
            } else {
                if (fRegistro != null) {
                    int numCampos = AC.readInt();
                    fRegistro = new RegistroTemplate(numCampos);
                    fRegistro.le(AC);
                    table_size = AC.readInt();
                    bucket_cap = AC.readInt();
                    luFactor = AC.readDouble();
                    loadFactor = AC.readDouble();
                    oriSize = AC.readInt();
                }
            }
            
        } catch (IOException ex) {ex.printStackTrace(); }
        bucket_size = (((2*Integer.SIZE)/8)+(bucket_cap*EntradaHash.SIZE));       
    }
    
    public TabelaHash(RegistroTemplate r) {
        table_size = DEFtable_size;
        bucket_cap = DEFbucket_size;
        limiteLU = DEFlimiteLU;
        limiteLoad = DEFlimiteLoad;
        limiteInfLoad = DEFlimiteInfLoad;
        fRegistro = r;
        oriSize = table_size;
        luFactor = 0;
        loadFactor = 0;
        indice = numIndices++;
        bucket_size = (((2*Integer.SIZE)/8)+(bucket_cap*EntradaHash.SIZE));
        try {
            TH = new RandomAccessFile("TabelaHash"+indice+".dat", "rw");
            AR = new RandomAccessFile("Registros"+indice+".dat", "rw");
            AR.setLength(0);
            TH.setLength(bucket_size*table_size);
            TH.seek(0);
            while(TH.getFilePointer()<TH.length()) {
                Bucket bucket = new Bucket(bucket_cap);
                bucket.escreve(TH);
            }
            AC = new RandomAccessFile("Cabecalho"+indice+".dat", "rw");
            AC.seek(0);
            AC.writeInt(fRegistro.getNumCampos());
            r.escreve(AC);
            AC.writeInt(table_size);
            AC.writeInt(bucket_cap);
            AC.writeDouble(luFactor);
            AC.writeDouble(loadFactor);
            AC.writeInt(oriSize);
        } catch (Exception e) {}
    }

    //Função Hash utilizada para o Hash e para o Rehasing
    private int hashf(int key) {
        String str = String.valueOf(Math.abs(key));
        int b = 378551;
        long a = 63689;
        long hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * a + str.charAt(i);
            a = a * b;
        }

        int resp = (Math.abs((int) hash) % table_size);
        if (resp == key)
            resp++;
        return resp;
    }
    /* End Of RS Hash Function
    A simple hash function from Robert Sedgwicks Algorithms in C book.
    I've added some simple optimizations to the algorithm in order to
    speed up its hashing process.*/

    //Função que retorna o registro da chave testada. Retorna nulo se não encontrar
    public Registro get(int key) {
        int hash = hashf(key);
        int oriHash = hash;
        int contador = 0;
        do {
            try {
                contador++;
                TH.seek(bucket_size*hash);
                Bucket bucket = new Bucket(bucket_cap);
                bucket.le(TH);
                for (int i = 0; i < bucket_cap; i++) {
                    if (bucket.elems[i] != null && bucket.elems[i].getKey() == key) {
                        return buscaRegistro(bucket.elems[i].getPos(),AR);
                    }
                }
                if (bucket.transbordado == 0) {
                    return null;
                }
                hash = hashf(hash);
            } catch (IOException ex) {
                Logger.getLogger(TabelaHash.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (hash != oriHash || contador>=table_size);
        return null;
    }

    //Deleta o registro da chave passada.
    public boolean deleteHT(int key) {
        int hash = hashf(key);
        int oriHash = hash;
        do {
            try {
                TH.seek(bucket_size*hash);
                long algumacoisa = TH.length();
                Bucket bucket = new Bucket(bucket_cap);
                bucket.le(TH);
                for (int i = 0; i < bucket_cap; i++) {
                    if (bucket.elems[i] != null && bucket.elems[i].getKey() == key) {
                        if (bucket.cheio == bucket_cap) {
                            loadFactor -= (1.0 / table_size);
                            escreveLoadFactor();
                        }
                        if (bucket.cheio == bucket_cap && bucket.transbordado == 1) {
                            luFactor += (1.0 / table_size);
                            escreveLuFactor();
                        }
                        //Deleta do arquivo de registros
                        if (!deleteAR(bucket.elems[i].getPos(),fRegistro.getTam()))
                            return false;
                        bucket.elems[i] = new EntradaHash();
                        bucket.cheio--;
                        if ((luFactor > limiteLU) || ((loadFactor < limiteInfLoad) && table_size > oriSize)) {
                            if ((loadFactor < limiteInfLoad) && table_size > oriSize){
                                table_size/=2;
                                escreveTableSize();
                            }
                            reconstroi();
                        }
                        TH.seek(bucket_size*hash);
                        bucket.escreve(TH);
                        return true;
                    }
                }
                if (bucket.transbordado == 0) {
                    return false;
                }
                hash = hashf(hash);
            } catch (IOException ex) {return false;}
        } while (hash != oriHash);

        return false;

    }

    //Função a ser chamada para inserir o Registro de chave key
    public boolean put(int key, Registro value){
        int pos = putAR(value);
        if (pos == -1)
            return false;
        try {
            return putHT(key,pos);
        } catch (Exception e){return false;}
    }


    //Tenta, de fato, inserir o registro na tabela hash
    public boolean putHT(int key, int pos){
        int hash = hashf(key);
        int oriHash = hash;
        int contador = 0;
        boolean insert = false;
        do {
            try {
                int seek = (bucket_size*hash);
                //APAGAR!
                long algumacoisa = TH.length();
                if (seek >= TH.length())
                    TH.setLength(seek);
                TH.seek(seek);
                Bucket bucket = new Bucket(bucket_cap);
                bucket.le(TH);
                for (int i = 0; i < bucket_cap; i++) {
                    contador++;
                    if (bucket.elems[i].getKey() == Integer.MIN_VALUE || bucket.elems[i].getKey() == key) {
                        bucket.elems[i] = new EntradaHash(key, pos);
                        insert = true;
                        bucket.cheio++;
                        if (bucket.cheio == bucket_cap) {
                            loadFactor += (1.0 / table_size);
                            escreveLoadFactor();
                            if (loadFactor > limiteLoad) {
                                table_size = 2*table_size;
                                escreveTableSize();
                                reconstroi();
                            }
                        }                
                        break;
                    }
                }
                if (!insert) {
                    bucket.transbordado = 1;
                    hash = hashf(hash);
                    //TH.seek(hash*fRegistro.getTam());
                }
                TH.seek(seek);
                bucket.escreve(TH);
            } catch (IOException ex) {return false;}
        } while ((!insert) && (hash != oriHash) && (contador<table_size));
        if (insert)
            return true;
        else{
            table_size*=2;
            reconstroi();
            return putHT(key, pos);
        }
    }

    public boolean putHT(EntradaHash eh) {
        return this.putHT(eh.getKey(), eh.getPos());
    }

    //Reconstroi a tabela
    private void reconstroi() {
        try {
            RandomAccessFile ARTemp = new RandomAccessFile("ReconstroiTemp", "rw");
            ARTemp.setLength(0);
            ARTemp.seek(0);
            AR.seek(0);
            while(AR.getFilePointer()<AR.length()){
                ARTemp.writeByte(AR.readByte()); 
            }
            luFactor = 0;
            loadFactor = 0;
            AR.setLength(0);
            TH.setLength(bucket_size*table_size);
            TH.seek(0);
            while(TH.getFilePointer()<TH.length()) {
                Bucket bucket = new Bucket(bucket_cap);
                bucket.escreve(TH);
            }
            escreveTableSize();
            escreveLoadFactor();
            escreveLuFactor();
            Registro registro;
            int k =0;
            while (k < ARTemp.length() / fRegistro.getTam()) {
                registro = buscaRegistro(k, ARTemp);
                if (registro.isOcupado() == 1)
                    put((Integer) registro.getInfo(0), registro);
                k++;
            }
            
        } catch (IOException ex) {
            Logger.getLogger(TabelaHash.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    //Relativos ao arquivo de registros
    private Registro buscaRegistro(int pos, RandomAccessFile arq) {
        Registro registro = null;
        try {
            int seek =pos*fRegistro.getTam();
            arq.seek(seek);
            registro = new Registro();
            for (int i = 0; i< fRegistro.getNumCampos(); i++){
                int tipo = fRegistro.getTipoCampo(i);
                switch (tipo) {
                //Inteiro
                case 1:
                    registro.setInfo(arq.readInt());
                break;
                //Double
                case 2:
                    registro.setInfo(arq.readDouble());
                break;
                //String
                case 3:
                    registro.setInfo(arq.readUTF());
                break;
                }
            }
            return registro;
        } catch (FileNotFoundException ex) {return null;}
          catch (IOException ex) {return null;}
    }

    //Insere o registro no arquivo de Registros.
    private int putAR(Registro registro){
        try {
            //Ajeitar, aqui apenas para debugar
            AR.seek(this.AR.length());
            long qualquercoisa = this.AR.length();
            for (int i = 0; i< fRegistro.getNumCampos(); i++){
                int tipo = fRegistro.getTipoCampo(i);
                switch (tipo) {
                //Inteiro
                case 1:
                    AR.writeInt((Integer)registro.getInfo(i));
                break;
                //Double
                case 2:
                    AR.writeDouble((Double)registro.getInfo(i));
                break;
                //String
                case 3:
                    String aux = (String)registro.getInfo(i);
                    for (int k = aux.length(); k<30; k++){
                        aux += " ";
                    }
                    AR.writeUTF(aux);
                break;
                }
            }
            //Escreve o booleano que seta o registro como ocupado
            AR.writeInt(1);
            int resp = (int)(this.AR.length()-fRegistro.getTam())/fRegistro.getTam();
            return resp;
        } catch (FileNotFoundException ex) {}
          catch (IOException ex) {}
        return -1;
    }
    

    //Deleta o Registro do arquivo de Registros.
    private boolean deleteAR(int pos, int tamanho){
        try {
            AR.seek((pos+1)*tamanho-(Integer.SIZE/8));
            AR.writeInt(0);
            return (true);
        } catch (IOException ex) {return false;}
    }
    
    private void escreveTableSize(){
        try {
            AC.seek((Integer.SIZE/8)+fRegistro.getTam());
            AC.writeInt(table_size);
        } catch (IOException ex) {}
    }
    
    private void escreveLoadFactor(){
        try {
            AC.seek((3*Integer.SIZE/8)+fRegistro.getTam()+(Double.SIZE/8));
            AC.writeDouble(loadFactor);
        } catch (IOException ex) {
            Logger.getLogger(TabelaHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void escreveLuFactor(){
        try {
            AC.seek((3*Integer.SIZE/8)+fRegistro.getTam());
            AC.writeDouble(luFactor);
        } catch (IOException ex) {
            Logger.getLogger(TabelaHash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setfRegistro(RegistroTemplate fRegistro) {
        this.fRegistro = fRegistro;
    }


}