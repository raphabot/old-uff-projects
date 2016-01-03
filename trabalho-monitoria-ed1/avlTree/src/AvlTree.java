import java.awt.Graphics;
import java.awt.Point;

import javax.swing.text.Position;

public class AvlTree {
	private AvlNode root = null;
	private static final Point DEFAULT_POSITION = new Point(400, 10);

	public AvlTree() {
		root = null;
	}
	public void clear() {
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public AvlNode getRootNode() {
		return root;
	}

	/** Retorna a altura da árvore */
	private static int height(AvlNode t) {
		return t == null ? -1 : t.height;
	}

	/**
	 * Retorna o maior valor ente lhs e rhs.
	 */
	private static int max(int lhs, int rhs) {
		return lhs > rhs ? lhs : rhs;
	}

	/** Retorna o fator de balanceamento da árvore com raiz t */
	private int getFactor(AvlNode t) {
		return height(t.left) - height(t.right);
	}

	public boolean insert(int x, Tela ob) {
		root = insert(x, root, ob);
		return true;
	}

	public boolean insertP(int x, Tela ob) {
		root = insertP(x, root, ob);
		return true;
	}

	private AvlNode insertP(int x, AvlNode t, Tela ob) {
		if (t == null){
			t = new AvlNode(x, null, null);
			t.nova=true;
		}else if (x < t.key)
			t.left = insertP(x, t.left, ob);
		else if (x > t.key)
			t.right = insertP(x, t.right, ob);

		return t;
	}

	private AvlNode insert(int x, AvlNode t, Tela ob) {
		if (t == null){
			t = new AvlNode(x, null, null);
		}else if (x < t.key)
			t.left = insert(x, t.left, ob);
		else if (x > t.key)
			t.right = insert(x, t.right, ob);
		else
			t.nova=false;
		t = balance(t, ob);
		return t;
	}
	public void altura(){
		this.root = altura(root);
	}
	
	private AvlNode altura(AvlNode t) {
		int hl=-1,hr=-1;
		if(t.left!=null){
			t.left = altura(t.left);
			hl = height(t.left);
		}if(t.right!=null){
			t.right = altura(t.right);
			hr = height(t.right);
		}
		t.height = max(hl,hr )+1;
		return t;
	}

	public AvlNode balance(AvlNode t, Tela ob) {
		if (getFactor(t) == 2) {
			if (getFactor(t.left) > 0)
				t = doRightRotation(t, ob);
			else
				t = doDoubleRightRotation(t, ob);
		} else if (getFactor(t) == -2) {
			if (getFactor(t.right) < 0) {
				t = doLeftRotation(t, ob);
			} else
				t = doDoubleLeftRotation(t, ob);
		}
		t.height = max(height(t.left), height(t.right)) + 1;
		return t;
	}

	/** Faz Rotação simples a direita */
	private static AvlNode doRightRotation(AvlNode k2, Tela ob) {
		System.out.println("RR: " + k2.key);
		AvlNode k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = max(height(k2.left), height(k2.right)) + 1;
		k1.height = max(height(k1.left), k2.height) + 1;
		if(ob!=null)
			ob.addRotation(k1);
		return k1;
	}

	/** Rotação simples à esquerda */
	private static AvlNode doLeftRotation(AvlNode k1, Tela ob) {

		AvlNode k2 = k1.right;
		k1.right = k2.left;
		k2.left = k1;
		k1.height = max(height(k1.left), height(k1.right)) + 1;
		k2.height = max(height(k2.right), k1.height) + 1;
		if(ob!=null)ob.addRotation(k2);
		return k2;
	}

	/** Rotação dupla à direita */
	private static AvlNode doDoubleRightRotation(AvlNode k3, Tela ob) {
		k3.left = doLeftRotation(k3.left, ob);
		return doRightRotation(k3, ob);
	}

	/** Rotação dupla à esquerda */
	private static AvlNode doDoubleLeftRotation(AvlNode k1, Tela ob) {
		k1.right = doRightRotation(k1.right, ob);
		return doLeftRotation(k1, ob);
	}

	public AvlNode search(int el) {
		return search(root, el);
	}
	public boolean remove(int delNum, Tela ob) {
		if(root==null)return false;
		root = remove(delNum, root, null);
		//root = insert(root.key,root, ob);
		return true;
	}

	private AvlNode remove(int delNum, AvlNode node, Tela ob) {

		if (node == null) {
			System.out.println(delNum + " Not found in AVL Tree\n");
			return null;
		} else {
			if (node.key < delNum)
				node.right = remove(delNum, node.right, ob);
			else if (node.key > delNum)
				node.left = remove(delNum, node.left, ob);
			else if (node.left == null)
				node = node.right;
			else if (node.right == null)
				node = node.left;			
			else if (height(node.left) > height(node.right)) {
				node = doRightRotation(node, ob);
				node.right = remove(delNum, node.right, ob);
			} else {
				node = doLeftRotation(node, ob);
				node.left = remove(delNum, node.left, ob);
			}

			if(node!=null)
				node = balance(node, ob);
		}
		return node;
	}

	protected AvlNode search(AvlNode p, int el) {
		while (p != null) {
			/* se valor procuradp == chave do nó retorna referência ao nó */
			if (el == p.key)
				return p;
			/*
			 * se valor procurado < chave do nó, procurar na sub-árvore esquerda
			 * deste nó
			 */
			else if (el < p.key)
				p = p.left;
			/*
			 * se valor procurado > chave do nó, procurar na sub-árvore direita
			 * deste nó
			 */
			else
				p = p.right;
		}
		// caso chave não foi achada, retorna null
		return null;
	}

	public void inorder() {
		inorder(root);
	}

	protected void inorder(AvlNode p) {
		if (p != null) {
			inorder(p.left);
			System.out.print(p.key + " - ");
			inorder(p.right);
		}
	}

	public void preorder() {
		preorder(root);
	}

	protected void preorder(AvlNode p) {
		if (p != null) {
			System.out.print(p.key + " ");
			preorder(p.left);
			preorder(p.right);
		}
	}

	public void postorder() {
		postorder(root);
	}

	protected void postorder(AvlNode p) {
		if (p != null) {
			postorder(p.left);
			postorder(p.right);
			System.out.print(p.key + " ");
		}
	}

	protected AvlNode searchFather(int el) {
		AvlNode p = root;
		AvlNode prev = null;
		while (p != null && !(p.key == el)) { // acha o nó p com a chave el
			prev = p;
			if (p.key < el)
				p = p.right;
			else
				p = p.left;
		}
		if (p != null && p.key == el)
			return prev;
		return null;
	}

	public void calculaPosicao() {
		if(root==null)return;
		//System.out.println("ROOT: " + this.root.key);
		this.root.setPosicao(DEFAULT_POSITION.x, DEFAULT_POSITION.y);
		this.root.calculaPosicao();
	}

	public void calculaPosicaoDestino() {
		if(root==null)return;
		//System.out.println("ROOT D: " + this.root.key);
		this.root.setDestino(DEFAULT_POSITION.x, DEFAULT_POSITION.y);
		this.root.calculaPosicaoDestino();
	}

	public void criaMove(Tela t) {
		if(root==null)return;
		if (this.root.isDiferente()) {
			t.addProcesso(new Thread(new Move(t, this.root)));
		}else
			this.root.setPosicao(this.root.getDestino().x, this.root.getDestino().y);
		this.root.criaMove(t);
	}

	public void displayTree(Graphics g) {		
		if (isEmpty()) {
			System.out.println("Árvore vazia!");
			return;
		}
		if(root==null){
			g.clearRect(0, 0, 800, 400);
			return;
		}
		this.root.desenha(g);
	}



} // class