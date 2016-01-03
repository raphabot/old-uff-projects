import java.awt.Point;
import java.util.Observable;
import java.util.Observer;


public class Move extends Observable implements Runnable{
	private AvlNode no;
	private Point destino;
	private Point inicial;

	public Move(Observer obs,AvlNode no){
		this.no = no;
		this.inicial = no.getPosicao();
		this.destino = no.getDestino();		
		this.addObserver(obs);
	}
	
	private int reta(int x){
		return (x-destino.x)*(destino.y-inicial.y)/(destino.x-inicial.x)+destino.y;
	}
	
	public void run(){
		this.notifyObservers(new Boolean(true));
		this.setChanged();
		if(this.inicial==null){
			System.out.println("Nope - NULL!");
			this.notifyObservers(new Boolean(false));
			this.setChanged();
			return;
		}
		if(this.inicial.x == this.destino.x && this.inicial.y==this.destino.y){
			System.out.println("Nope!");
			this.notifyObservers(new Boolean(false));
			this.setChanged();
			return;
		}
		System.out.println("start:"+this.inicial.x+" "+this.inicial.y);
		System.out.println("target:"+this.destino.x+" "+this.destino.y);
		
		int dir = 1;
		if(inicial.x>destino.x)dir=-1;
		try{
			int count = 0;
			while(destino.x!=inicial.x){
				//System.out.println(":"+this.no.getX()+" "+this.no.getY());
				inicial.setLocation(inicial.x+dir, reta(inicial.x+dir));
				no.setPosicao(inicial.x, inicial.y);
				
				this.notifyObservers(no);
				this.setChanged();
				//System.out.println("::"+this.no.getX()+" "+this.no.getY());
				Thread.sleep(10);
				count++;
				//if(count>40)break;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		this.notifyObservers(new Boolean(false));
		this.setChanged();
	}
}
