package Code;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

//Class for ball instances, handles painting and updating location
public class Ball {

	private int x;
	private int y;
	private int verticalVelocity;
	private int horizontalVelocity;
	private int size;
	private int rnd;
	private Random random;

	public Ball() {

		//Initializing variables
		y = 450;
		verticalVelocity = -2;
		horizontalVelocity = 0;
		size = 10;

		// Initializing random number generator
		random = new Random();

		// Randomly choosing if ball is going left or right
		rnd = random.nextInt(2);

		if (rnd == 0) {
			horizontalVelocity = 2;
		} else {
			horizontalVelocity = -2;
		}

		// Randomly choosing where ball starts on the x axis
		x = random.nextInt(395) + 185;
	}

	// Painting ball as a white circle
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x, y, size, size);
	}

	// Method for updating balls speed and location
	public void update() {

		// Moving ball horizontally
		x += horizontalVelocity;

		// Checking if ball hit left wall
		if (x <= 110) {
			x = 110;
			horizontalVelocity = 2;
		}

		// Checking if ball hit right wall
		if (x >= 480) {
			x = 480;
			horizontalVelocity = -2;
		}

		// Moving ball vertically
		y += verticalVelocity;

		// Checking if ball hit top wall
		if (y <= 90) {
			y = 90;
			verticalVelocity = 2;
		}
	}

	public void invertVerticalVelocity() {
		verticalVelocity *= -1;
	}

	public void invertHorizontalVelocity() {
		horizontalVelocity *= -1;
	}

	public void multiplyHorizontalVelocity(int horVel) {
		horizontalVelocity *= horVel;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public int getVerticalVelocity() {
		return verticalVelocity;
	}

	public int getHorizontalVelocity() {
		return horizontalVelocity;
	}

	public void setY(int y) {
		this.y = y;
	}
}