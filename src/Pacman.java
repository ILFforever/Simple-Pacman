import java.awt.*;
import java.awt.Event;
import java.util.HashSet;
import java.util.random.*;
import javax.swing.*;

public class Pacman extends JPanel {
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
	// X = wall, O = skip, P = pac man, ' ' = food
	// Ghosts: b = blue, o = orange, p = pink, r = red
	private String[] tileMap = { "XXXXXXXXXXXXXXXXXXX", "X        X        X", "X XX XXX X XXX XX X",
			"X                 X", "X XX X XXXXX X XX X", "X    X       X    X", "XXXX XXXX XXXX XXXX",
			"OOOX X       X XOOO", "XXXX X XXrXX X XXXX", "O       bpo       O", "XXXX X XXXXX X XXXX",
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

		loadmap();
	}

	class Block {
		int x;
		int y;
		int width;
		int height;
		Image image;

		int startX;
		int startY;

		Block(Image image, int x, int y, int width, int height) {
			this.image = image;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.startX = x;
			this.startY = y;
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
					Block wall = new Block(wallImage, x, y, tileSize, tileSize);
					walls.add(wall);
				} else if (tileMapchar == 'b') { // blue ghost
					Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'o') { // orange ghost
					Block ghost = new Block(orageGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'r') { // red ghost
					Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'p') { // pink ghost
					Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
					ghosts.add(ghost);
				} else if (tileMapchar == 'P') { // Pacman
					Pacman = new Block(PacmanRightImage, x, y, tileSize, tileSize);
				} else if (tileMapchar == ' ') {// food tile
					Block food = new Block(null, x + (tileSize / 2) - ((tileSize / 8) - 2),
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
		g.drawImage(Pacman.image, Pacman.x, Pacman.y , Pacman.width, Pacman.height, null);
		
		for (Block ghost : ghosts) {
			g.drawImage(ghost.image, ghost.x, ghost.y , ghost.width, ghost.height, null);
		}
		
		for (Block wall : walls) {
			g.drawImage(wall.image, wall.x, wall.y , wall.width, wall.height, null);			

		}
		g.setColor(Color.WHITE);
		for (Block food : foods) {
			g.fillRect(food.x, food.y , food.width, food.height);			
		}
	
	}
}
