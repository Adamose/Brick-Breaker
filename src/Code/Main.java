package Code;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

//Class for setting up JFrame
public class Main extends JFrame {

	public Main() {

		// Loading texture for program's icon
		URL url1 = Main.class.getResource("/Resources/Brick2.jpg");
		ImageIcon imageicon1 = new ImageIcon(url1);
		Image img1 = imageicon1.getImage();
		Image img = img1.getScaledInstance(90, 90, Image.SCALE_SMOOTH);

		// Setting JFrame's parameters
		this.setResizable(false);
		this.setTitle("Brick Breaker");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(img);
		this.add(new Panel());
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}