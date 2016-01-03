package trabed2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class EntradaHash {
      private int key;
      private int pos;
      public static final int SIZE = 2*Integer.SIZE;

      public EntradaHash(int key, int pos) {
            this.key = key;
            this.pos = pos;
      }
      
      public EntradaHash() {
          key = Integer.MIN_VALUE;
          pos = Integer.MIN_VALUE;
      }

      public int getKey() {
            return key;
      }

      public int getPos() {
            return pos;
      }
      
      public EntradaHash le(RandomAccessFile arq) throws IOException{
          this.key = arq.readInt();
          this.pos = arq.readInt();
          return this;
      }
      
      public void escreve(RandomAccessFile arq) throws IOException{
          arq.writeInt(key);
          arq.writeInt(pos);
      }
      
}
