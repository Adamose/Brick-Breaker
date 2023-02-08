package Code;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

//Class for player paddle
public class Player {

	private int x;
	private int y;
	private int velocity;
	private int form;
	private Image platform;
	private Image platformBig;

	public Player() {

		//Initializing variables
		x = 275;
		y = 460;
		velocity = 0;
		form = 1;

		//Loading paddle texture
		URL url = Main.class.getResource("/Resources/Platform.png");
		ImageIcon imageicon = new ImageIcon(url);
		Image img = imageicon.getImage();
		platform = img.getScaledInstance(60, 40, Image.SCALE_SMOOTH);

		//Creating second instance of paddle but stretch for paddle upgrade
		platformBig = img.getScaledInstance(100, 40, Image.SCALE_SMOOTH);
	}

	//Painting paddle based on form variable
	public void paint(Graphics g) {
		if (form == 2) {
			g.drawImage(platformBig, x, y, null);
		} else {
			g.drawImage(platform, x, y, null);
		}
	}

	//Updating paddle's location
	public void update() {

		//Moving paddle on x axis
		x += velocity;

		if (form == 1) {

			//Checking if small paddle hit left wall
			if (x <= 105) {
				x = 105;
			}

			//Checking if small paddle hit right wall
			if (x >= 435) {
				x = 435;
			}

		} else {

			//Checking if big paddle hit left wall
			if (x <= 100) {
				x = 100;
			}

			//Checking if big paddle hit right wall
			if (x >= 402) {
				x = 402;
			}
		}
	}

	public void setVelocity(int v) {
		velocity = v;
	}

	public void setForm(int f) {
		form = f;
	}

	public void setX(int X) {
		x = X;
	}

	public void setY(int Y) {
		x = Y;
	}

	public int getForm() {
		return form;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}