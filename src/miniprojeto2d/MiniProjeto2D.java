package miniprojeto2d;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import cg2d.utils.Utils;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.geom.*;
import java.awt.event.*;

public class MiniProjeto2D extends JFrame implements ActionListener {
	MyPanel panel;
	public static void main(String[] args) {
		JFrame frame = new MiniProjeto2D();
		frame.setTitle("Flight Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.pack();
		frame.setVisible(true);
	}
	
		public MiniProjeto2D() {
			//Menu			
			JMenuBar mb = new JMenuBar();
			setJMenuBar(mb);    //insere o MenuBar no frame
			
			JMenu menu = new JMenu("File");
			JMenuItem me = new JMenuItem("Exit");
			JMenuItem mr = new JMenuItem("Reset");
			me.addActionListener(this);
			mr.addActionListener(this);
			menu.add(mr);
			menu.add(me);
			mb.add(menu);	
			
			panel = new MyPanel();
			this.getContentPane().add(panel); //adiciona ao frame o panel
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.equals("Exit")) {
				System.exit(0);
			}else if(cmd.equals("Reset")) {
				panel.reset();
			}
			
		}
}


class MyPanel extends JPanel implements Runnable, KeyListener {
	
	
	Stroke hW = new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	Stroke houseWall = new BasicStroke(12f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL);
	
	AlphaComposite ac =  AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
	AlphaComposite reset =  AlphaComposite.getInstance(AlphaComposite.SRC, 1.0f);
	
	boolean completo = false;
	
	float ang = 0;
	
	float tLua = 0; 
	float speed = 5;
	
	Shape nuvem = null;
	Shape estrela = null;
	
	//Inicializar o jogador(aviao)
	Shape player = null;	
	int playerX = 10;
	int playerY = 450;
	int keyboardVX = 0;
	int keyboardVY = 0;
	BufferedImage playerImage = null;
	//
	
	//imagem da arvore
	Shape arvore = null;
	BufferedImage tree = null;
	
	//imagem da segunda arvore
	Shape arvore2 = null;
	BufferedImage tree2 = null;
	//
	

	public MyPanel() {
		setPreferredSize(new Dimension(900, 650));
		setBackground(Color.cyan);
		
		
		tree = Utils.readImage(this, "images/tree.png");
		tree2 = Utils.readImage(this, "images/tree.png");
		playerImage = Utils.readImage(this, "images/plane.png");
		
		
		//Desenhar a segunda arvore
		ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp op = new ColorConvertOp(colorSpace, null);
		tree2 = op.filter(tree2, null);
		//
		
			
		addKeyListener(this);
		setFocusable(true);
		
		
		Thread thread = new Thread(this);
		thread.start();	
	}

		
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		AffineTransform at = new AffineTransform();   //criar um objeto AffineTransform
		
		//Desenhar a arvore
		Shape arvore = new Rectangle2D.Double(470, 400, 120, 250);
		TexturePaint treePaint = new TexturePaint(tree, arvore.getBounds());
		g2.setPaint(treePaint);
		g2.fill(arvore);
		//
		
		
		//Evocar a segunda arvore com o algoritmo aplicado
		Shape arvore2 = new Rectangle2D.Double(600, 400, 120, 250);
		TexturePaint treePaint2 = new TexturePaint(tree2, arvore2.getBounds());
		g2.setPaint(treePaint2);
		g2.fill(arvore2);
		//
		
		
		player = new Rectangle2D.Double(30, 100, 60, 60);
		
		//
		if(keyboardVX != 0 || keyboardVY != 0) {
		playerX = playerX + keyboardVX;
		playerY = playerY + keyboardVY;
		}
		
		//translacao para mover o player
		at.setToIdentity(); 
		at.translate(playerX, playerY);
		player = at.createTransformedShape(player);
		//
		
		checkPanelLimits();
		
		//Jogador		
		TexturePaint playerPaint = new TexturePaint(playerImage, player.getBounds());  
		g2.setPaint(playerPaint);
		g2.fill(player);
		//g2.setColor(Color.BLACK);
		//g2.draw(player.getBounds());
		//
		
		
		//Chamar e desenhar a estrela Versao1
		//estrela = new Star(400, 200, 50, 50);
		//AffineTransform atStar = new AffineTransform();
		//atStar.rotate(Math.toRadians(ang), 499, 199);
		//estrela = atStar.createTransformedShape(estrela);
		//g2.setColor(Color.YELLOW);
		//g2.fill(estrela);
		//
		
		//Chamar e desenhar a estrela Versao definitiva
		Shape point = new Ellipse2D.Double(400, 200, 1, 1);
		AffineTransform atP = new AffineTransform();
		atP.rotate(Math.toRadians(ang),499,199);
		point = atP.createTransformedShape(point);
		int x = point.getBounds().x;
		int y = point.getBounds().y;
		estrela = new Star(0, 0, 50, 50);
		AffineTransform atStar = new AffineTransform();
		atStar.translate(x,y);
		estrela = atStar.createTransformedShape(estrela);
		g2.setColor(Color.YELLOW);
		g2.fill(estrela);
		//
		
		//Chamar e desenhar a nuvem
		nuvem = new Cloud(150, 150, 100, 65);
		AffineTransform atCloud = new AffineTransform();
		atCloud.rotate(Math.toRadians(ang), 149, 149);
		nuvem = atCloud.createTransformedShape(nuvem);
		g2.setColor(Color.WHITE);
		g2.fill(nuvem);
		//
		
		
		//Desenhar a lua
		Shape lua1 = new Ellipse2D.Double(450, 20, 50, 50);
		Shape lua2 = new Ellipse2D.Double(467, 20, 50, 50);
		Area a1 = new Area(lua1);
		Area a2 = new Area(lua2);
		a1.subtract(a2);
		AffineTransform atMoon = new AffineTransform();
		atMoon.scale(0.9f,0.9f);
		a1 = a1.createTransformedArea(atMoon);
		atMoon.setToTranslation(tLua,0);
		a1 = a1.createTransformedArea(atMoon);
		g2.setColor(Color.GRAY); 
		g2.fill(a1);
		//
		
		
		if(estrela.intersects(player.getBounds())) {
			g2.setColor(Color.BLUE);
			g2.fill(estrela);
		}else if(nuvem.intersects(player.getBounds())) {
			g2.setColor(Color.GRAY);
			g2.fill(nuvem);
		}else if(a1.intersects(player.getBounds())) {
			g2.setColor(Color.BLACK);
			g2.fill(a1);
		}
		
		
		
		//Cor da casa
		g2.setColor(Color.pink);
		g2.fillRect(306, 500, 140, 140);
		//		
		
		
		//Parede da frente da casa
		g2.setPaint(Color.BLACK);
		g2.setStroke(houseWall);
		g2.drawLine(300, 500, 450, 500);
		g2.drawLine(300, 500, 300, 640);
		g2.drawLine(450, 500, 450, 640);
		g2.drawLine(300, 640, 450, 640);
		//
		
				
		g2.setStroke(hW);
		
		
		//Desenhar a janela1
		Shape window1 = new Rectangle2D.Double(320, 525, 25, 30);
		GradientPaint WindowColor1 = new GradientPaint(332.5f, 530f, Color.BLUE, 332.5f, 560f, Color.WHITE);
		g2.setPaint(WindowColor1);
		g2.fill(window1);
		
		//Desenhar a janela2
		Shape window2 = new Rectangle2D.Double(400, 525, 25, 30);
		GradientPaint WindowColor2 = new GradientPaint(412.5f, 530f, Color.BLUE, 412.5f, 560f, Color.WHITE);
		g2.setPaint(WindowColor2);
		g2.fill(window2);
		
		//Desenhar a porta
		Shape door = new Rectangle2D.Double(300, 550, 40, 60);
		g2.translate(50, 24);
		g2.setColor(Color.ORANGE);
		g2.fill(door);

		
		//Dsenhar o vitral da porta
		Shape dWin = new Ellipse2D.Double(290, 300, 15, 25);
		at.setToIdentity();
		at.translate(22, 260);
		dWin = at.createTransformedShape(dWin);
		g2.setComposite(ac);
		g2.setColor(Color.RED);
		g2.fill(dWin);
		//
	
		
		g2.setComposite(reset);
		
		//Desenhar Start
		g2.setColor(Color.BLACK);
		Font start = new Font("Castellar", Font.BOLD, 20);
		g2.setFont(start.deriveFont(at));
		g2.drawString("Start", 50, 180);
		Shape meta1 = new Ellipse2D.Double(60, 410, 90, 40);
		AffineTransform at1 = new AffineTransform();
		//at1.rotate(Math.PI/6);
		Shape oeste = at1.createTransformedShape(meta1);
		g2.draw(oeste);
		g2.drawLine(60, 425, 60, 620);
		g2.drawLine(150, 425, 150, 620);
		//
		
		//Desenhar Finish
		g2.setColor(Color.BLACK);
		Font finish = new Font("Algerian", Font.BOLD, 20);
		at.shear(-0.5,0.8);
		g2.setFont(finish.deriveFont(at));
		g2.drawString("FINISH", 700, 120);
		Shape meta = new Ellipse2D.Double(805, -60, 100, 50);
		AffineTransform at2 = new AffineTransform();
		at2.rotate(Math.PI/6);
		Shape este = at2.createTransformedShape(meta);
		g2.draw(este);
		g2.drawLine(715, 370, 715, 620);
		g2.drawLine(800, 400, 800, 620);
		//
		
		if(este.intersects(player.getBounds())) {
			g2.drawString("Level Completed!", 300, -100);
			completo = true;
		}
		
		//Desenhar o telhado da casa
		g2.setColor(Color.RED);
		GeneralPath triangulo = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		triangulo.moveTo(325,365);
		triangulo.lineTo(400,470);
		triangulo.lineTo(250,470);
		triangulo.closePath();
		
		g2.clip(triangulo);
		g2.fill(triangulo);
		g2.setColor(Color.BLACK);
		g2.drawLine(250, 390, 400, 390);
		g2.drawLine(250, 400, 400, 400);
		g2.drawLine(250, 410, 400, 410);
		g2.drawLine(250, 420, 400, 420);
		g2.drawLine(250, 430, 400, 430);
		g2.drawLine(250, 440, 400, 440);
		g2.drawLine(250, 450, 400, 450);
		g2.drawLine(250, 460, 400, 460);
		g2.drawLine(250, 470, 400, 470);
		//
		
	}

	public void reset() {
		playerX = 10;
		playerY = 450;
		completo = false;
	}

	private void checkPanelLimits() {
		if(playerX < 0) {
			playerX = 1;
		}else if(playerX  > 810) {
			playerX = 810;
		}
		
		if(playerY <= -80) {
			playerY = -80;
		}else if(playerY > 489) {
			playerY = 489;
		}
		
	}



	@Override
	public void run() {
		while(true) {
			if((tLua >= 450) || (tLua <= -400)) {
				speed = speed * -1;
			}
			tLua = tLua + speed;
			ang = (ang + 2) % 360;
			repaint();
			try {
				Thread.sleep(50);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		int KeyCode = e.getKeyCode();
		
	if(!completo) {
		switch(KeyCode) {
		case KeyEvent.VK_A:
			keyboardVX = -10;  //negativo para ser para a esquerda
			keyboardVY = 0;
			break;
		case KeyEvent.VK_D:
			keyboardVX = 10; 
			keyboardVY = 0;
			break;
		case KeyEvent.VK_W:
			keyboardVX = 0;
			keyboardVY = -10;
			break;
		case KeyEvent.VK_S:
			keyboardVX = 0;
			keyboardVY = 10;
			break;
		}
	}
	
	}



	@Override
	public void keyReleased(KeyEvent e) {
		keyboardVX = 0;  //para anular o efeito das teclas quando nao sao pressionadas
		keyboardVY = 0;
		
	}
	
}