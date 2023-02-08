package Code;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.Random;
import javax.swing.ImageIcon;

//Class for instances of powerups
public class PowerUp {

	private int x;
	private int y;
	private int width;
	private int heigth;
	private int velocity;
	private int form;
	private boolean used;
	private Image powerUp;
	private Random random;

	//A big improvement would be to load these textures statically to avoid
	//every instance having it's own copy and wasting memory
	public PowerUp(int x, int y) {

		//Initalizing variables
		random = new Random();
		this.x = x;
		this.y = y;
		width = 30;
		heigth = 20;
		velocity = 1;
		used = false;

		//Randomly choosing instance's powerup type
		form = random.nextInt(3) + 1;

		//Loading texture based on type (bad design to have each instance load the texture)
		if (form == 1) {
			URL url1 = Main.class.getResource("/Resources/Powerup1.png");
			ImageIcon imageicon1 = new ImageIcon(url1);
			Image img1 = imageicon1.getImage();
			powerUp = img1.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		} else if (form == 2) {
			URL url2 = Main.class.getResource("/Resources/Powerup2.png");
			ImageIcon imageicon2 = new ImageIcon(url2);
			Image img2 = imageicon2.getImage();
			powerUp = img2.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		} else if (form == 3) {
			URL url3 = Main.class.getResource("/Resources/Powerup3.png");
			ImageIcon imageicon3 = new ImageIcon(url3);
			Image img3 = imageicon3.getImage();
			powerUp = img3.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
		}
	}

	//Painting powerup image
	public void paint(Graphics g) {
		g.drawImage(powerUp, x, y, null);
	}

	//Updating powerup's location
	public void update() {
		y += velocity;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeigth() {
		return heigth;
	}

	public int getForm() {
		return form;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}