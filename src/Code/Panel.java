package Code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

//Class for handling game logic and overall painting of game entities
public class Panel extends JPanel implements KeyListener {

	// Timer running at 60 ticks per second, acts as game loop
	private Timer timer = new Timer(15, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			update();
		}
	});

	private GUI GUI = new GUI();
	private Background background = new Background();
	private Player player = new Player();
	private Rocket r = new Rocket(525, 200);
	private Font font1 = new Font("Comic Sans MS", Font.BOLD, 30);
	private Font font2 = new Font("Comic Sans MS", Font.PLAIN, 25);
	private Random random = new Random();

	// Different list containing game entities
	private ArrayList<Ball> balls = new ArrayList<Ball>();
	private ArrayList<Brick> bricks = new ArrayList<Brick>();
	private ArrayList<Rocket> rockets = new ArrayList<Rocket>();
	private ArrayList<PowerUp> powerups = new ArrayList<PowerUp>();

	private int level = 1;
	private int ammo;

	// Booleans used to track game state
	private boolean inGame = false;
	private boolean inMidGameFailed = false;
	private boolean inMidGamePassed = false;
	private boolean inOutro = false;
	private boolean inIntro = true;

	// Good coding practice would be to initialize variables in the constructor,
	// something I didn't know at the time
	public Panel() {
		this.addKeyListener(this);
		this.setFocusable(true);
		this.setPreferredSize(new Dimension(600, 600));

		// Setting intputMap for ENTER keystroke
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "enter");

		// Action to be called when user presses the ENTER key
		ActionMap action = this.getActionMap();
		action.put("enter", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				// Checking if user is in one of three states where he can press ENTER
				if (inIntro || inMidGamePassed || inMidGameFailed) {

					// User pressed ENTER in inIntro state
					if (inIntro) {
						loadLevel(level);

					// User pressed ENTER in inMidGamePassed state
					} else if (inMidGamePassed) {
						level += 1;
						loadLevel(level);
					}

					inIntro = false;
					inMidGamePassed = false;
					inMidGameFailed = false;
					ammo = 0;
					powerups.clear();
					rockets.clear();
					player.setForm(1);
					balls.add(new Ball());
					inGame = true;
				}
			}
		});

		// Setting intputMap for Y keystroke
		InputMap i = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		i.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, 0, false), "y");

		// Action to be called when user presses the Y key
		ActionMap a = this.getActionMap();
		a.put("y", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				// Clearing all bricks -> automatically win current level
				bricks.clear();
			}
		});

		// Setting intputMap for SPACE keystroke
		InputMap in = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		in.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "space");

		// Action to be called when user presses the SPACE key
		ActionMap ac = this.getActionMap();
		ac.put("space", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {

				// Check if user has ammo
				if (ammo > 0) {

					// Launching rocket from middle of paddle (based on paddle form)
					if (player.getForm() == 1) {
						rockets.add(new Rocket(player.getX() + 22, 450));
					} else {
						rockets.add(new Rocket(player.getX() + 42, 450));
					}

					ammo -= 1;
				}
			}
		});

		// Starting game loop
		timer.start();
	}

	// Method responsible for drawing game, calls game entities' own paint method
	public void paint(Graphics g) {

		// Painting based on current game state
		if (inGame) {
			background.paint(g);
			player.paint(g);

			g.setColor(Color.WHITE);
			g.setFont(font1);
			g.drawString("Level " + level, 255, 50);

			// Drawing ammo count if player has rockets
			if (ammo > 0) {
				r.paint(g);
				g.setFont(font2);
				g.drawString("" + ammo, 550, 225);
			}

			for (Ball b : balls) {
				b.paint(g);
			}

			for (Brick b : bricks) {
				b.paint(g);
			}

			for (PowerUp p : powerups) {
				p.paint(g);
			}

			for (Rocket r : rockets) {
				r.paint(g);
			}

		} else if (inMidGamePassed) {
			GUI.paintMidGamePassed(g, level);

		} else if (inMidGameFailed) {
			GUI.paintMidGameFailed(g, level);

		} else if (inOutro) {
			GUI.paintOutro(g);

		} else if (inIntro) {
			GUI.paintIntro(g);
		}
	}

	// Method responsable for updating game entities and redrawing screen
	public void update() {

		if (inGame) {

			// Checking for collissions between game entities
			checkCollission();

			// Checking state of game entities
			checkState();

			player.update();

			for (PowerUp p : powerups) {
				p.update();
			}

			for (Ball b : balls) {
				b.update();
			}

			for (Rocket r : rockets) {
				r.update();
			}
		}

		// Updating screen
		repaint();
	}

	// Checks for game entities that need to be removed and for changes to the game
	// state (player won/lost)
	public void checkState() {

		// Deleting entities that are no longer needed
		balls.removeIf(ball -> (ball.getY() >= 500));
		bricks.removeIf(brick -> (brick.getState() >= 4));
		rockets.removeIf(rocket -> (rocket.isExploded()));
		powerups.removeIf(powerup -> (powerup.isUsed()));

		if (inGame) {

			// Checking if player lost
			if (balls.size() == 0) {
				inGame = false;
				player.setX(275);
				rockets.clear();
				inMidGameFailed = true;

				// Checking if player won
			} else if (bricks.size() == 0) {
				rockets.clear();
				inGame = false;

				// Checking if player cleared whole game
				if (level == 5) {
					balls.clear();
					rockets.clear();
					inOutro = true;

				} else {
					balls.clear();
					player.setX(275);
					inMidGamePassed = true;
				}
			}
		}
	}

	// Checks for collisions between game entities, this function is messy and has
	// alot of duplicate calculations. Breaking it down into smaller functions would
	// be ideal
	public void checkCollission() {

		// Checking if paddle collided with a powerup
		for (PowerUp powerup : powerups) {

			if (new Rectangle(player.getX(), player.getY() + 12, 60, 1)
					.intersects(new Rectangle(powerup.getX(), powerup.getY(), powerup.getWidth(), powerup.getHeigth()))
					|| new Rectangle(player.getX(), player.getY() + 12, 100, 1).intersects(
							new Rectangle(powerup.getX(), powerup.getY(), powerup.getWidth(), powerup.getHeigth()))) {

				// Updating powerup's state
				powerup.setUsed(true);

				// Applying powerup effect based on powerup's form
				if (powerup.getForm() == 1) {
					player.setForm(2);

				} else if (powerup.getForm() == 2) {
					ammo += 5;

				} else if (powerup.getForm() == 3) {
					balls.add(new Ball());
					balls.add(new Ball());
					balls.add(new Ball());
				}
			}
		}

		for (Ball ball : balls) {

			// Checking if ball collided with player paddle
			if (player.getForm() == 1) {

				if (new Rectangle(player.getX(), player.getY() + 12, 60, 1)
						.intersects(new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize()))) {
					ball.setY(ball.getY() - 1);

					if ((ball.getX() >= player.getX() && ball.getX() <= player.getX() + 15)
							|| (ball.getX() >= player.getX() + 45 && ball.getX() <= player.getX() + 60)) {
						ball.invertHorizontalVelocity();
						ball.invertVerticalVelocity();
						ball.multiplyHorizontalVelocity(2);

					} else {
						ball.invertVerticalVelocity();
					}

				}

			} else {

				if (new Rectangle(player.getX() + 10, player.getY() + 12, 80, 1)
						.intersects(new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize()))) {
					ball.setY(ball.getY() - 1);

					if ((ball.getX() >= player.getX() && ball.getX() <= player.getX() + 25)
							|| (ball.getX() >= player.getX() + 75 && ball.getX() <= player.getX() + 100)) {
						ball.invertHorizontalVelocity();
						ball.invertVerticalVelocity();
						ball.multiplyHorizontalVelocity(2);

					} else {
						ball.invertVerticalVelocity();
					}
				}

			}

			// Checking if ball collided with bricks
			for (Brick brick : bricks) {

				if (new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeigth())
						.intersects(new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize()))) {

					brick.changeState();

					if (random.nextInt(10) + 1 == 5) {
						powerups.add(new PowerUp(brick.getX() + 5, brick.getY()));
					}

					if ((ball.getX() >= brick.getX() - (ball.getSize() + 1))
							&& (ball.getX() <= brick.getX() + brick.getWidth() - 1)
							&& ((ball.getY() == (brick.getY() - ball.getSize() + 1))
									|| (ball.getY() == brick.getY() + brick.getHeigth() - 1))) {

						ball.invertVerticalVelocity();

					} else {

						ball.invertHorizontalVelocity();
					}

				}

				// Checking if rocket collided with brick
				for (Rocket rocket : rockets) {

					if (new Rectangle(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeigth()).intersects(
							new Rectangle(rocket.getX(), rocket.getY(), rocket.getWidth(), rocket.getHeigth()))) {

						rocket.setExploded(true);
						brick.setState(4);

						if (random.nextInt(10) + 1 == 5) {
							powerups.add(new PowerUp(brick.getX() + 5, brick.getY()));
						}
					}
				}
			}
		}
	}

	// Method to load a level's bricks into the bricks arraylist
	public void loadLevel(int level) {

		if (level == 1) {
			bricks.add(new Brick(2, 173, 175));
			bricks.add(new Brick(2, 209, 175));
			bricks.add(new Brick(2, 245, 175));
			bricks.add(new Brick(2, 281, 175));
			bricks.add(new Brick(2, 317, 175));
			bricks.add(new Brick(2, 353, 175));
			bricks.add(new Brick(2, 389, 175));
			bricks.add(new Brick(2, 173, 191));
			bricks.add(new Brick(2, 209, 191));
			bricks.add(new Brick(2, 245, 191));
			bricks.add(new Brick(2, 281, 191));
			bricks.add(new Brick(2, 317, 191));
			bricks.add(new Brick(2, 353, 191));
			bricks.add(new Brick(2, 389, 191));
			bricks.add(new Brick(2, 173, 207));
			bricks.add(new Brick(2, 209, 207));
			bricks.add(new Brick(2, 245, 207));
			bricks.add(new Brick(2, 281, 207));
			bricks.add(new Brick(2, 317, 207));
			bricks.add(new Brick(2, 353, 207));
			bricks.add(new Brick(2, 389, 207));
			bricks.add(new Brick(2, 173, 223));
			bricks.add(new Brick(2, 209, 223));
			bricks.add(new Brick(2, 245, 223));
			bricks.add(new Brick(2, 281, 223));
			bricks.add(new Brick(2, 317, 223));
			bricks.add(new Brick(2, 353, 223));
			bricks.add(new Brick(2, 389, 223));
			bricks.add(new Brick(2, 173, 239));
			bricks.add(new Brick(2, 209, 239));
			bricks.add(new Brick(2, 245, 239));
			bricks.add(new Brick(2, 281, 239));
			bricks.add(new Brick(2, 317, 239));
			bricks.add(new Brick(2, 353, 239));
			bricks.add(new Brick(2, 389, 239));

		} else if (level == 2) {
			bricks.add(new Brick(2, 281, 159));
			bricks.add(new Brick(2, 245, 175));
			bricks.add(new Brick(2, 281, 175));
			bricks.add(new Brick(2, 317, 175));
			bricks.add(new Brick(2, 209, 191));
			bricks.add(new Brick(2, 245, 191));
			bricks.add(new Brick(2, 281, 191));
			bricks.add(new Brick(2, 317, 191));
			bricks.add(new Brick(2, 353, 191));
			bricks.add(new Brick(2, 173, 207));
			bricks.add(new Brick(2, 209, 207));
			bricks.add(new Brick(2, 245, 207));
			bricks.add(new Brick(1, 281, 207));
			bricks.add(new Brick(2, 317, 207));
			bricks.add(new Brick(2, 353, 207));
			bricks.add(new Brick(2, 389, 207));
			bricks.add(new Brick(2, 209, 223));
			bricks.add(new Brick(2, 245, 223));
			bricks.add(new Brick(2, 281, 223));
			bricks.add(new Brick(2, 317, 223));
			bricks.add(new Brick(2, 353, 223));
			bricks.add(new Brick(2, 245, 239));
			bricks.add(new Brick(2, 281, 239));
			bricks.add(new Brick(2, 317, 239));
			bricks.add(new Brick(2, 281, 255));

		} else if (level == 3) {
			bricks.add(new Brick(2, 209, 175));
			bricks.add(new Brick(2, 353, 175));
			bricks.add(new Brick(2, 173, 191));
			bricks.add(new Brick(1, 209, 191));
			bricks.add(new Brick(2, 245, 191));
			bricks.add(new Brick(2, 317, 191));
			bricks.add(new Brick(1, 353, 191));
			bricks.add(new Brick(2, 389, 191));
			bricks.add(new Brick(2, 137, 207));
			bricks.add(new Brick(1, 173, 207));
			bricks.add(new Brick(1, 209, 207));
			bricks.add(new Brick(1, 245, 207));
			bricks.add(new Brick(2, 281, 207));
			bricks.add(new Brick(1, 317, 207));
			bricks.add(new Brick(1, 353, 207));
			bricks.add(new Brick(1, 389, 207));
			bricks.add(new Brick(2, 425, 207));
			bricks.add(new Brick(2, 173, 223));
			bricks.add(new Brick(1, 209, 223));
			bricks.add(new Brick(2, 245, 223));
			bricks.add(new Brick(2, 317, 223));
			bricks.add(new Brick(1, 353, 223));
			bricks.add(new Brick(2, 389, 223));
			bricks.add(new Brick(2, 209, 239));
			bricks.add(new Brick(2, 353, 239));

		} else if (level == 4) {
			bricks.add(new Brick(1, 173, 175));
			bricks.add(new Brick(1, 389, 175));
			bricks.add(new Brick(1, 209, 191));
			bricks.add(new Brick(2, 281, 191));
			bricks.add(new Brick(1, 353, 191));
			bricks.add(new Brick(2, 245, 207));
			bricks.add(new Brick(2, 281, 207));
			bricks.add(new Brick(2, 317, 207));
			bricks.add(new Brick(1, 209, 223));
			bricks.add(new Brick(2, 281, 223));
			bricks.add(new Brick(1, 353, 223));
			bricks.add(new Brick(1, 173, 239));
			bricks.add(new Brick(1, 389, 239));

		} else if (level == 5) {
			bricks.add(new Brick(1, 209, 175));
			bricks.add(new Brick(1, 281, 175));
			bricks.add(new Brick(1, 353, 175));
			bricks.add(new Brick(1, 173, 191));
			bricks.add(new Brick(1, 245, 191));
			bricks.add(new Brick(1, 317, 191));
			bricks.add(new Brick(1, 389, 191));
			bricks.add(new Brick(1, 209, 207));
			bricks.add(new Brick(1, 281, 207));
			bricks.add(new Brick(1, 353, 207));
			bricks.add(new Brick(1, 173, 223));
			bricks.add(new Brick(1, 245, 223));
			bricks.add(new Brick(1, 317, 223));
			bricks.add(new Brick(1, 389, 223));
			bricks.add(new Brick(1, 209, 239));
			bricks.add(new Brick(1, 281, 239));
			bricks.add(new Brick(1, 353, 239));
		}
	}

	// Method to check for A or L key presses
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A) {
			player.setVelocity(-5);
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			player.setVelocity(5);
		}
	}

	// Method to check for A or L key releases
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_L) {
			player.setVelocity(0);
		}
	}

	public void keyTyped(KeyEvent e) {
	}
}