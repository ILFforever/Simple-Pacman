import java.awt.*;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.random.*;
import javax.swing.*;

public class Pacman extends JPanel implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int rowC = 21;
	private int colC = 19;
	private int tileSize = 32;
	private int boardWidth = colC * tileSize;
	private int boardHeight = rowC * tileSize;
	// Sprite for ghost
	private Image wallImage;
	private Image blueGhostImage;
	private Image orageGhostImage;
	private Image pinkGhostImage;
	private Image redGhostImage;

	// Spite for Pacman
	private Image PacmanUpImage;
	private Image PacmanLeftImage;
	private Image PacmanRightImage;
	private Image PacmanDownImage;

	// Set of Objects
	HashSet<Block> walls;
	HashSet<Block> foods;
	HashSet<Block> ghosts;
	Block Pacman;
	Random random = new Random();
	char[] direction = { 'U', 'D', 'L', 'R' };

	int score = 0;
	int life = 3;
	boolean GameOver = false;
	Timer gameLoop;

	// X = wall, O = skip, P = Pacman, ' ' = food
	// Ghosts: b = blue, o = orange, p = pink, r = red
	private String[] tileMap = { "XXXXXXXXXXXXXXXXXXX", "X        X        X", "X XX XXX X XXX XX X",
			"X                 X", "X XX X XXXXX X XX X", "X    X       X    X", "XXXX XXXX XXXX XXXX",
			"OOOX X       X XOOO", "XXXX X XXrXX X XXXX", "O       pob       O", "XXXX X XXXXX X XXXX",
			"OOOX X       X XOOO", "XXXX X XXXXX X XXXX", "X        X        X", "X XX XXX X XXX XX X",
			"X  X     P     X  X", "XX X X XXXXX X X XX", "X    X   X   X    X", "X XXXXXX X XXXXXX X",
			"X                 X", "XXXXXXXXXXXXXXXXXXX" };

	// Constructor
	Pacman() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setBackground(Color.BLACK);

		// load images
		wallImage = new ImageIcon(getClass().getResource("/wall.png")).getImage();
		blueGhostImage = new ImageIcon(getClass().getResource("/blueGhost.png")).getImage();
		orageGhostImage = new ImageIcon(getClass().getResource("/orangeGhost.png")).getImage();
		pinkGhostImage = new ImageIcon(getClass().getResource("/pinkGhost.png")).getImage();
		redGhostImage = new ImageIcon(getClass().getResource("/redGhost.png")).getImage();

		PacmanUpImage = new ImageIcon(getClass().getResource("/pacmanUp.png")).getImage();
		PacmanDownImage = new ImageIcon(getClass().getResource("/pacmanDown.png")).getImage();
		PacmanLeftImage = new ImageIcon(getClass().getResource("/pacmanLeft.png")).getImage();
		PacmanRightImage = new ImageIcon(getClass().getResource("/pacmanRight.png")).getImage();

		addKeyListener(this);
		setFocusable(true); // what does this do
		loadmap();
		for (Block ghost : ghosts) {
			char newDirection = direction[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
		gameLoop = new Timer(50, this); // somehow calls actionPerformed every 50ms
		gameLoop.start(); // start timer
	}

	class Block {

		String name;
		int x;
		int y;
		int width;
		int height;
		Image image;

		int startX;
		int startY;

		int direction = 'U';
		int velocityX = 0;
		int velocityY = 0;

		Block(String name, Image image, int x, int y, int width, int height) {
			this.name = name;
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
		}

		// functions in class block
		void updateDirection(char direction) {
			char prevDirection = (char) this.direction;
			this.direction = direction;
			updateVelocity();
			this.x += this.velocityX;
			this.y += this.velocityY;
			// check for collision when turning
			for (Block wall : walls) {
				if (collision(wall, this)) {
					this.x -= this.velocityX;
					this.y -= this.velocityY;
					this.direction = prevDirection;
					updateVelocity();
				}
			}
		}

		void updateVelocity() {
			if (this.direction == 'U') {
				this.velocityX = 0;
				this.velocityY = -tileSize / 4;
			} else if (this.direction == 'D') {
				this.velocityX = 0;
				this.velocityY = +tileSize / 4;
			} else if (this.direction == 'L') {
				this.velocityX = -tileSize / 4;
				this.velocityY = 0;
			} else if (this.direction == 'R') {
				this.velocityX = +tileSize / 4;
				this.velocityY = 0;
			}
		}

		void reset() {
			this.x = this.startX;
			this.y = this.startY;
		}
	}

	public void loadmap() {
		walls = new HashSet<Block>();
		foods = new HashSet<Block>();
		ghosts = new HashSet<Block>();

		for (int r = 0; r < rowC; r++) {
			for (int c = 0; c < colC; c++) {
				String row = tileMap[r];
				char tileMapchar = row.charAt(c);

				int x = c * tileSize;
				int y = r * tileSize;

				if (tileMapchar == 'X') { // wall
					Block wall = new Block("", wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
				} else if (tileMapchar == 'b') { // blue ghost
					Block ghost = new Block("BlueGhost", blueGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'o') { // orange ghost
					Block ghost = new Block("OrangeGhost", orageGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'r') { // red ghost
					Block ghost = new Block("RedGhost", redGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'p') { // pink ghost
					Block ghost = new Block("Pinkghost", pinkGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'P') { // Pacman
					Pacman = new Block("Pacman", PacmanRightImage, x, y, tileSize, tileSize);
				} else if (tileMapchar == ' ') {// food tile
					Block food = new Block("food", null, x + (tileSize / 2) - ((tileSize / 8) - 2),
							y + (tileSize / 2) - ((tileSize / 8) - 2), tileSize / 8, tileSize / 8);
					foods.add(food);
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		g.drawImage(Pacman.image, Pacman.x, Pacman.y, Pacman.width, Pacman.height, null);

		for (Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
		}

		for (Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);

		}
		g.setColor(Color.WHITE);
		for (Block food : foods) {
			g.fillRect(food.x, food.y, food.width, food.height);
		}
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		if (GameOver) {
			g.drawString("GAME OVER " + String.valueOf(score), tileSize / 2, tileSize / 2);
		} else {
			g.drawString("x" + String.valueOf(life) + " Score: " + String.valueOf(score), tileSize / 2, tileSize / 2);
		}

	}

	public void move() {
		// move Pacman
		Pacman.x += Pacman.velocityX;
		Pacman.y += Pacman.velocityY;
		for (Block wall : walls) { // check all walls
			if (collision(Pacman, wall)) {
				// move back a frame so he isn't stuck forever
				Pacman.x -= Pacman.velocityX;
				Pacman.y -= Pacman.velocityY;
				break;
			}
		}
		if (Pacman.x + Pacman.width < 0) // entire pacman disappear off screen
			Pacman.x = boardWidth;
		if (Pacman.x > boardWidth) // entire pacman disappear off screen
			Pacman.x = 0 - Pacman.width;

		for (Block ghost : ghosts) {
			if (collision(ghost, Pacman)) {
				life -= 1;
				if (life == 0) {
					GameOver = true;
				}
				resetPosition();
			}
			ghost.x += ghost.velocityX;
			ghost.y += ghost.velocityY;
			for (Block wall : walls) {
				if (collision(wall, ghost)) {
					ghost.x -= ghost.velocityX;
					ghost.y -= ghost.velocityY;
					char newdirection = direction[random.nextInt(4)];
					ghost.updateDirection(newdirection);
				}
			}
			// check collision on all positions of the ghost
			char curDirection = (char) ghost.direction;
			// System.out.println(curDirection);
			Map<String, Boolean> DirectionCheck = new HashMap<>(Map.of("U", true, "D", true, "L", true, "R", true));

			for (Block wall : walls) {
				// Check upward collision
				if (ghost.y - tileSize < wall.y + wall.height && ghost.y >= wall.y && ghost.x + ghost.width > wall.x
						&& ghost.x < wall.x + wall.width) {
					DirectionCheck.put("U", false); // Block upward direction

				}
				// Check downward collision
				if (ghost.y + ghost.height + tileSize > wall.y && ghost.y + ghost.height <= wall.y + wall.height
						&& ghost.x + ghost.width > wall.x && ghost.x < wall.x + wall.width) {
					DirectionCheck.put("D", false); // Block downward direction
				}
				// Check leftward collision
				if (ghost.x - tileSize < wall.x + wall.width && ghost.x >= wall.x && ghost.y + ghost.height > wall.y
						&& ghost.y < wall.y + wall.height) {
					DirectionCheck.put("L", false); // Block leftward direction
				}
				// Check rightward collision
				if (ghost.x + ghost.width + tileSize > wall.x && ghost.x + ghost.width <= wall.x + wall.width
						&& ghost.y + ghost.height > wall.y && ghost.y < wall.y + wall.height) {
					DirectionCheck.put("R", false); // Block rightward direction
				}
			}

			// Count valid directions
			int count = 0;
			for (Boolean c : DirectionCheck.values()) {
				if (c)
					count++;
			}
			String newDirection = null;
			if (count == 3) { // fork in the road
				// System.out.println(ghost.name+" has found a fork!");
				for (Map.Entry<String, Boolean> entry : DirectionCheck.entrySet()) {
					if (entry.getValue() && !entry.getKey().equals(String.valueOf(curDirection))
							&& !entry.getKey().equals(String.valueOf(getOppositeDirection(curDirection)))) {
						int chance = random.nextInt(3); // 33% chance
						if (newDirection == null || chance == 2) {
							newDirection = entry.getKey();
						}
					}
				}
			} else if (count == 4) {
				System.out.println(ghost.name + " has found an intersection!");
				if (random.nextBoolean()) {
					ArrayList<Character> directions = new ArrayList<>(Arrays.asList('U', 'D', 'L', 'R'));
					directions.removeIf(c -> c == curDirection); // remove specific direction
					directions.removeIf(c -> c == getOppositeDirection(curDirection));
					newDirection = String.valueOf(directions.get(random.nextInt(directions.size())));
				}
			}
			// Update the ghost's direction if a new direction is found
			if (newDirection != null && ghost.x >= 0 && ghost.x < boardWidth) {
				ghost.updateDirection(newDirection.charAt(0));
				// System.out.println(ghost.name + " changed direction to " + newDirection);
			}

			if (ghost.x + ghost.width < 0) // entire pacman disappear off screen
				ghost.x = boardWidth;
			if (ghost.x > boardWidth) // entire pacman disappear off screen
				ghost.x = 0 - ghost.width;

			// check for food collision
			Block foodate = null;
			for (Block food : foods) {
				if (collision(food, Pacman)) {
					score += 10;
					foodate = food;
				}
			}
			foods.remove(foodate);
			//Level Complete!
			if (foods.isEmpty()){
				loadmap();
				resetPosition();
			}
		}
	}

	void resetPosition() {
		Pacman.reset();
		Pacman.velocityX = 0;
		Pacman.velocityY = 0;
		for (Block ghost : ghosts) {
			ghost.reset();
			char newDirection = direction[random.nextInt(4)];
			ghost.updateDirection(newDirection);
		}
	}
	
	char getOppositeDirection(char direction) {
		switch (direction) {
		case 'U':
			return 'D';
		case 'D':
			return 'U';
		case 'L':
			return 'R';
		case 'R':
			return 'L';
		default:
			return ' ';
		}
	}

	public boolean collision(Block a, Block b) {
		return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		move(); // move objects
		repaint(); // draw game
		if (GameOver) {
			gameLoop.stop();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (GameOver) {
			loadmap();
			resetPosition();
			life = 3;
			score = 0;
			GameOver = false;
			gameLoop.start();
		}
		// System.out.println("press : "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			Pacman.updateDirection('U');
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			Pacman.updateDirection('D');
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			Pacman.updateDirection('L');
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Pacman.updateDirection('R');
		}

		if (Pacman.direction == 'U') {
			Pacman.image = PacmanUpImage;
		} else if (Pacman.direction == 'D') {
			Pacman.image = PacmanDownImage;
		} else if (Pacman.direction == 'L') {
			Pacman.image = PacmanLeftImage;
		} else if (Pacman.direction == 'R') {
			Pacman.image = PacmanRightImage;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
