import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;


public class Tela extends Applet implements Observer,MouseListener{
	private Image offscreen;

	private boolean first = true;
	private AvlTree tree;
	
	private Vector rotations = new Vector();
	private Vector processos = new Vector();
	
	public void addRotation(AvlNode no){
		rotations.add(no);
	}
	
	public void addProcesso(Thread t){
		processos.add(t);
	}
	
	public void iniciaProcessos(){
		Thread t;
		for(int i=0;i<processos.size();i++){
			t = (Thread)processos.get(i);
			t.start();
		}
	}
	
	public boolean podeInserir(){
		Thread t;
		for(int i=0;i<processos.size();i++){
			t = (Thread)processos.get(i);
			if(t.isAlive())return false;
		}
		processos.removeAllElements();
		return true;
	}
	
	private void doRotation(){
		tree.calculaPosicaoDestino();
		tree.criaMove(this);
		this.iniciaProcessos();
		rotations.removeAllElements();
	}
	
	public void randomInsert(){
		int r = new Random().nextInt(99);
		System.out.println(r);
		insert(r);
	}
	
	public void insert(int valor){
		System.out.println();
		getTree().insertP((valor),this);
		//getTree().altura();
		getTree().calculaPosicao();
		update(this.getGraphics());
		try{
			Thread.sleep(1000);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		getTree().insert((valor),this);
		
		if(rotations.isEmpty()){
			getTree().calculaPosicao();
			update(this.getGraphics());
		}
		else this.doRotation();
	}
	public void remove(int valor){
		System.out.println();
		AvlNode de = getTree().search(valor);
		de.delete=true;
		update(this.getGraphics());
		try{
			Thread.sleep(1000);
		}catch (Exception e) {
			e.printStackTrace();
		}
		getTree().remove((valor),this);
		update(this.getGraphics());
		this.doRotation();
	}
	
	public void init(){
		addMouseListener(this);
		setBackground(Color.white);
		setMinimumSize(new Dimension(800, 400));
		setSize(800, 400);
		tree = (new AvlTree());
		//getTree().insert(1,this);
		//getTree().calculaPosicao();
	}
	public void paint(Graphics g){
		getTree().displayTree(g);
	}
	
	public void update(Graphics g) {
		updateDoubleBufffered(g);
	}
	
	public void update(Observable o, Object arg) {
		
		this.update(this.getGraphics());
	}
	
	private void updateDoubleBufffered(Graphics g) {
		int width = -1;
		int height = -1;
		Dimension d = getSize();
		if (offscreen == null || width != d.width || height != d.height
				|| offscreen == null) {
			width = d.width;
			height = d.height;
			if (width > 0 || height > 0) {
				offscreen = createImage(width, height);
			} else
				offscreen = null;
		}

		if (offscreen == null)
			return;

		Graphics gg = offscreen.getGraphics();

		gg.setColor(getBackground());
		gg.fillRect(0, 0, width, height);
		gg.setColor(getForeground());

		paint(gg);

		gg.dispose();

		g.drawImage(offscreen, 0, 0, null);
	}
	public void mouseClicked(MouseEvent e) {
		/*String op = JOptionPane.showInputDialog(null, "1-Add\n2-Remove");
		if(op.equals("1")){			
			if(this.podeInserir()){
				String valor = JOptionPane.showInputDialog(null, "Entre com valor:");
				this.insert(Integer.parseInt(valor));
			}else{
				JOptionPane.showMessageDialog(null, "Nao pode!","Sorry",JOptionPane.ERROR_MESSAGE);
			}
		}else{
			if(this.podeInserir()){
				String valor = JOptionPane.showInputDialog(null, "Entre com valor:");
				this.remove(Integer.parseInt(valor));
			}else{
				JOptionPane.showMessageDialog(null, "Nao pode!","Sorry",JOptionPane.ERROR_MESSAGE);
			}
		}*/
		if(this.podeInserir())
			this.randomInsert();
			
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public AvlTree getTree() {
		return tree;
	}
}
