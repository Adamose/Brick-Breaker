package Code;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

//Class for brick instances, loads textures and paints brick
public class Brick {

	private Image brick;
	private Image regular_brick;
	private Image damaged_brick;
	private Image broken_brick;
	private int x;
	private int y;
	private int state;
	private int width;
	private int heigth;

	// A big improvement would be to load these three textures statically to avoid
	// every instance having it's own copy and wasting memory
	public Brick(int s, int x, int y) {

		// Initalizing variables
		state = s;
		this.x = x;
		this.y = y;
		width = 35;
		heigth = 15;

		// Loading textures
		URL url1 = Main.class.getResource("/Resources/Brick1.png");
		ImageIcon imageicon1 = new ImageIcon(url1);
		Image img1 = imageicon1.getImage();
		regular_brick = img1.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);

		URL url2 = Main.class.getResource("/Resources/Brick2.jpg");
		ImageIcon imageicon2 = new ImageIcon(url2);
		Image img2 = imageicon2.getImage();
		damaged_brick = img2.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);

		URL url3 = Main.class.getResource("/Resources/brick3.jpg");
		ImageIcon imageicon3 = new ImageIcon(url3);
		Image img3 = imageicon3.getImage();
		broken_brick = img3.getScaledInstance(width, heigth, Image.SCALE_SMOOTH);

		setState();
	}

	// Paints current brick texture
	public void paint(Graphics g) {
		g.drawImage(brick, x, y, null);
	}

	// Changes current brick texture based on state variable
	public void setState() {
		if (state == 1) {
			brick = regular_brick;
		} else if (state == 2) {
			brick = damaged_brick;
		} else if (state == 3) {
			brick = broken_brick;
		}
	}

	public void changeState() {
		state += 1;
		setState();
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}