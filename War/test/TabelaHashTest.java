/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import trabed2.Registro;
import trabed2.RegistroTemplate;
import trabed2.TabelaHash;
import static org.junit.Assert.*;

/**
 *
 * @author lcc
 */
public class TabelaHashTest {

    static RegistroTemplate rt;
    static ArrayList al;
    public TabelaHashTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        al = new ArrayList<Object>();
        al.add(1);
        al.add(2);
        rt = new RegistroTemplate(al);
    }

    @Test
    public void Test1() {
        TabelaHash ht = new TabelaHash(rt);
        assertTrue(ht.put(11, new Registro(11, 11)));
    }

    @Test
    public void Test2() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        ht.put(10, r1);
        assertEquals(r1.getInfo(1), ht.get(10).getInfo(1));
    }

    @Test
    public void Test3() {
        TabelaHash ht = new TabelaHash(rt);
        ht.put(10, new Registro(10, 11));
        assertTrue(ht.put(12, new Registro(12, 14)));
    }

    @Test
    public void Test4() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        ht.put(10, r1);
        assertTrue(ht.deleteHT(10));
        assertEquals(null, ht.get(10));//ht.put(12, 14));
    }

    @Test
    public void Test5() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        Registro r2 = new Registro(21, 57);
        ht.put(10, r1);
        assertTrue(ht.put(21, r2));
        assertEquals(r2.getInfo(1), ht.get(21).getInfo(1));//ht.put(12, 14));
    }

    @Test
    public void Test6() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        Registro r2 = new Registro(21, 57);
        ht.put(10, r1);
        ht.put(21, r2);
        assertTrue(ht.deleteHT(10));
        assertEquals(r2.getInfo(1), ht.get(21).getInfo(1));//ht.put(12, 14));
    }

    @Test
    public void Test7() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        Registro r2 = new Registro(21, 57);
        Registro r3 = new Registro(21, 57);
        Registro r4 = new Registro(124, 57);
        ht.put(10, r1);
        ht.put(21, r2);
        ht.put(21, r3);
        ht.put(124, r4);
        ht.deleteHT(10);
        assertEquals(r4.getInfo(1), ht.get(124).getInfo(1));//ht.put(12, 14));
    }

    @Test
    public void Test8() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        Registro r2 = new Registro(21, 57);
        Registro r3 = new Registro(21, 57);
        ht.put(10, r1);
        ht.put(21, r2);
        assertTrue(ht.put(21, r3));
    }

    @Test
    public void Test9() {
        TabelaHash ht = new TabelaHash(rt);
        Registro r1 = new Registro(10, 11);
        ht.put(10, r1);
        ht.deleteHT(10);
        Registro r2 = new Registro(11, 11);
        ht.put(11, r2);
        ht.deleteHT(11);
        assertTrue((ht.get(11) == null) && (ht.get(10) == null));//ht.put(12, 14));

    }

    @Test
    public void Test10() {
        TabelaHash ht = new TabelaHash(0);
        assertTrue(ht.put(12, new Registro(12, 11)));
    }

    @Test
    public void Test11() {
        TabelaHash ht = new TabelaHash(rt);
        for (int i = 50; i < 576; i++) {
            assertTrue(ht.put(i, new Registro(i, i)));
            System.out.println(i);
        }
        assertTrue(ht.put(576, new Registro(576, 576)));
        assertEquals(67, ht.get(67).getInfo(1));
    }

    @Test
    public void Test12() {
        ArrayList<Object> objetos = new ArrayList<Object>();
        for (int i = 0; i < 10; i++) {
            objetos.add(i);
        }
        RegistroTemplate rt2 = new RegistroTemplate(objetos);
        TabelaHash ht = new TabelaHash(rt2);
        Registro registro = new Registro(objetos);
        assertTrue(ht.put((Integer) registro.getInfo(0), registro));
        assertEquals(registro.getInfo(0), (ht.get((Integer) registro.getInfo(0))).getInfo(0));
    }

    @Test
    public void Test13() {
        ArrayList<Object> objetos = new ArrayList<Object>();
        objetos.add(47);
        objetos.add(1);
        objetos.add("oi");
        RegistroTemplate rt2 = new RegistroTemplate(objetos);
        TabelaHash ht = new TabelaHash(rt2);
        Registro registro = new Registro(objetos);
        assertTrue(ht.put((Integer) registro.getInfo(0), registro));
        String aux = "oi";
        for (int k = aux.length(); k < 30; k++) {
            aux += " ";
        }
        assertTrue((ht.get((Integer) registro.getInfo(0))).getInfo(2).equals(aux));
    }

    @Test
    public void Test14() {
        ArrayList<Object> objetos = new ArrayList<Object>();
        objetos.add(47);
        objetos.add(1);
        objetos.add(0.2);
        RegistroTemplate rt2 = new RegistroTemplate(objetos);
        TabelaHash ht = new TabelaHash(rt2);
        Registro registro = new Registro(objetos);
        assertTrue(ht.put((Integer) registro.getInfo(0), registro));
        assertEquals((Double) (ht.get((Integer) registro.getInfo(0))).getInfo(2), 0.2, 0.0001);
    }
}