package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	private final int WIDTH = 160;
	private final int  HEIGHT = 120;
	private final int  SCALE = 4;
	
	private BufferedImage image;
	private Spritesheet sheet;
	private BufferedImage[] player;
	private int frames = 0;
	private int maxFrame = 5;
	private int curAnimation = 0, maxAnimation = 3;
	
	private int x = 0;
	
	public Game() {
		sheet = new Spritesheet("/spritesheet.png");
		player = new BufferedImage[4];
		
		
		player[0] = sheet.getSprite(0, 0, 16, 16); 
		player[1] = sheet.getSprite(16, 0, 16, 16); 
		player[2] = sheet.getSprite(32, 0, 16, 16); 
		player[3] = sheet.getSprite(48, 0, 16, 16); 
		
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Game game = new Game();
		game.start();
		
	}
	
	public void tick() {
		
		frames ++;
		
		if(frames > maxFrame) {
			
			frames = 0;
			curAnimation++;
			
			if(curAnimation > maxAnimation) {
				
				curAnimation = 0;
				
			}
		}
		
		if(x > WIDTH) {
			x = 0;
		}x++;
		
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(55, 20, 20));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.RED);
//		g.fillOval(20, 10, 20, 20);
		g.fillOval(x, 10, 20, 20);
		
		g.setFont(new Font("Arial", Font.BOLD, 20));		
		g.drawString("Olá Mundo", 10, 50);
		
		/*
		 * Renderixação do jogo 
		 */
		
//		g.drawImage(player[curAnimation], 90, 90, null);
		g.drawImage(player[curAnimation], x, 90, null);
		
		Graphics2D g2 = (Graphics2D) g;
		/*
		 * Rotação de imagem
		 */
		/*
		g2.rotate(Math.toRadians(45), 90+8, 90+8);
		g2.drawImage(player, 90, 90, null);
		*/
		
		
		/***/
		g.dispose();		
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
		
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 240.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
			
		}
		
		stop();
		
	}
	
	
	
}
