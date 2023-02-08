package Code;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

//Class used to paint galaxy background and playable zone outline
public class Background {

	private Image background;
	private Image outline;

	public Background() {

		//Loading background image
		URL url1 = Main.class.getResource("/Resources/background.png");
		ImageIcon imageicon1 = new ImageIcon(url1);
		Image img1 = imageicon1.getImage();
		background = img1.getScaledInstance(600, 600, Image.SCALE_SMOOTH);

		//Loading outline image
		URL url2 = Main.class.getResource("/Resources/outline.png");
		ImageIcon imageicon2 = new ImageIcon(url2);
		Image img2 = imageicon2.getImage();
		outline = img2.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
	}

	//Paint method draws both images
	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);
		g.drawImage(outline, 100, 75, null);
	}
}