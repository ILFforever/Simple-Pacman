import javax.swing.JFrame;

public class App {

	public static void main(String[] args) {
		int rowC = 21;
		int colC = 19;
		int tileSize = 32;
		int boardWidth = colC * tileSize;
		int boardHeight = rowC * tileSize;
		
		//Draw app window
		JFrame frame = new JFrame("PacMan");
		frame.setVisible(false);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(boardWidth,boardHeight);
		
		Pacman pacmanGame = new Pacman();
		frame.add(pacmanGame); //Add JFrame from Pacman
		frame.pack(); //Stretched to fit window
		pacmanGame.requestFocus();
		frame.setVisible(true);

	}

}

