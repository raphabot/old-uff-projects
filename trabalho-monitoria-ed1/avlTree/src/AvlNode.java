import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class AvlNode {
	protected int height;
	protected int key;
    protected AvlNode left, right;
    private Point posicao;
    private Point destino;
    protected boolean delete = false;
    protected boolean nova = false;
    private static final int RAIO = 15;

    public AvlNode ( int theElement ) {
        this( theElement, null, null );
    }

    public AvlNode ( int theElement, AvlNode lt, AvlNode rt ) {
        key = theElement;
        left = lt;
        right = rt;
        height   = 0;
        posicao = new Point(0,0);
        destino = new Point(0,0);
        
    }
    
    public void calculaPosicao(){
		int x,y;
		x = this.getX()-(RAIO)*(int)Math.pow(2, this.height)-RAIO;
		y = this.getY()+RAIO*4;
		if(getLeft()!=null){
			this.getLeft().setPosicao(x, y);
			this.getLeft().calculaPosicao();
		}
		x = this.getX()+(RAIO)*(int)Math.pow(2, this.height)+RAIO;
		
		if(getRight()!=null){
			this.getRight().setPosicao(x, y);
			this.getRight().calculaPosicao();
		}
	}
    
    public void calculaPosicaoDestino(){
		int x,y;
		x = this.getDestino().x-(RAIO)*(int)Math.pow(2, this.height)-RAIO;
		y = this.getDestino().y+RAIO*4;
		if(getLeft()!=null){
			this.getLeft().setDestino(x, y);
			this.getLeft().calculaPosicaoDestino();
		}
		x = this.getDestino().x+(RAIO)*(int)Math.pow(2, this.height)+RAIO;
		
		if(getRight()!=null){
			this.getRight().setDestino(x, y);
			this.getRight().calculaPosicaoDestino();
		}
	}
    
    public void criaMove(Tela t){
		if(getLeft()!=null){
			if(getLeft().isDiferente())
				t.addProcesso(new Thread(new Move(t, this.getLeft())));
			else
				this.getLeft().setPosicao(this.getLeft().destino.x,this.getLeft().destino.y);
			this.getLeft().criaMove(t);
		}
		
		if(getRight()!=null){
			if(this.getRight().isDiferente())
				t.addProcesso(new Thread(new Move(t, this.getRight())));
			else
				this.getRight().setPosicao(this.getRight().destino.x,this.getRight().destino.y);
			this.getRight().criaMove(t);
		}
	}
    
    public void desenha(Graphics g){
    	//System.out.println("DES "+this.key+":("+this.getX()+","+this.getY()+")" );
		if(getLeft()!=null){			
			g.setColor(Color.BLACK);
			g.drawLine(this.getX()+RAIO, this.getY()+RAIO, getLeft().getX()+RAIO, getLeft().getY()+RAIO);
			getLeft().desenha(g);
		}
		
		if(getRight()!=null){
			g.setColor(Color.BLACK);
			g.drawLine(this.getX()+RAIO, this.getY()+RAIO, getRight().getX()+RAIO, getRight().getY()+RAIO);
			getRight().desenha(g);
		}
		
		g.setColor(Color.BLUE);
		g.fillRoundRect(this.getX(),this.getY(), 2*(RAIO), 2*(RAIO), 2*(RAIO), 2*(RAIO));
		if(delete)
    		g.setColor(Color.RED);
		else if(nova)
			g.setColor(Color.GREEN);
    	else
    		g.setColor(Color.BLACK);
		g.fillRoundRect(this.getX()+2,this.getY()+2, 2*(RAIO-2), 2*(RAIO-2), 2*(RAIO-2), 2*(RAIO-2));
		
		g.setColor(Color.WHITE);		
		g.drawString(String.valueOf(this.key),this.getX()+2*RAIO/2-(this.key<10?4:7) , this.getY()+2*RAIO/2+4);
    }
    
    public int getX(){
    	return posicao.x;
    }
    
    public int getY(){
    	return posicao.y;
    }
    
    public void setPosicao(int x,int y){
    	this.posicao.setLocation(x, y);
    }
    
    public void setDestino(int x,int y){
    	this.destino.setLocation(x, y);
    }
    
    public Point getPosicao(){
    	return posicao;
    }
    
    public Point getDestino(){
    	return destino;
    }
    
    public boolean isDiferente(){
    	return destino.y!=posicao.y;
    }
    
    public AvlNode getLeft(){
    	return this.left;
    }
    
    public AvlNode getRight(){
    	return this.right;
    }
}