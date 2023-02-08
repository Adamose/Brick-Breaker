package Code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

//Class for painting GUI frames and drawing text
public class GUI {

	private Image Intro;
	private Image Outro;
	private Image MidGame;
	private Font font;

	public GUI() {

		// Initalizing font
		font = new Font("ArialBlack", Font.PLAIN, 40);

		// Loading GUI frames
		URL url1 = Main.class.getResource("/Resources/Intro.png");
		ImageIcon imageicon1 = new ImageIcon(url1);
		Image img1 = imageicon1.getImage();
		Intro = img1.getScaledInstance(600, 600, Image.SCALE_SMOOTH);

		URL url2 = Main.class.getResource("/Resources/Outro.png");
		ImageIcon imageicon2 = new ImageIcon(url2);
		Image img2 = imageicon2.getImage();
		Outro = img2.getScaledInstance(600, 600, Image.SCALE_SMOOTH);

		URL url3 = Main.class.getResource("/Resources/MidGame.png");
		ImageIcon imageicon3 = new ImageIcon(url3);
		Image img3 = imageicon3.getImage();
		MidGame = img3.getScaledInstance(600, 600, Image.SCALE_SMOOTH);
	}

	public void paintIntro(Graphics g) {
		g.drawImage(Intro, 0, 0, null);
	}

	public void paintOutro(Graphics g) {
		g.drawImage(Outro, 0, 0, null);
	}

	public void paintMidGameFailed(Graphics g, int level) {
		g.drawImage(MidGame, 0, 0, null);

		g.setColor(Color.MAGENTA);
		g.setFont(font);
		g.drawString("Level " + level + " Failed", 190, 290);
		g.drawString("Try Again", 230, 340);
	}

	public void paintMidGamePassed(Graphics g, int level) {
		g.drawImage(MidGame, 0, 0, null);

		g.setColor(Color.MAGENTA);
		g.setFont(font);
		g.drawString("Level " + level + " Passed", 180, 290);
		g.drawString("On to Level " + (level + 1), 200, 340);
	}
}