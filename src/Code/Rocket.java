package Code;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

//Class for rocket instances
public class Rocket {

	private boolean exploded;
	private int x;
	private int y;
	private int width;
	private int heigth;
	private int velocity;
	private Image rocket;

	public Rocket(int x, int y) {

		//Initalizing variables
		this.x = x;
		this.y = y;
		width = 15;
		heigth = 30;
		velocity = 2;
		exploded = false;

		//Loading rocket texture (bad design to have each rocket load the texture)
		URL url = Main.class.getResource("/Resources/Rocket.png");
		ImageIcon imageicon = new ImageIcon(url);
		Image img = imageicon.getImage();
		rocket = img.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);
	}

	//Painting rocket image
	public void paint(Graphics g) {
		g.drawImage(rocket, x, y, null);
	}

	//Updating rocket's location
	public void update() {
		y -= velocity;

		//Checking if rocket hit top wall
		if (y <= 90) {
			exploded = true;
		}
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

	public boolean isExploded() {
		return exploded;
	}

	public void setExploded(boolean exploded) {
		this.exploded = exploded;
	}
}