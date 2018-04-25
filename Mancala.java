
import javax.swing.JFrame;
import java.io.*;

/*

	CLEANED UP FILES
	Mancala.java
	Game.java


*/



// Mancala class for declaring the JFrame and displaying the game menu
public class Mancala
{
	public static void main(String [] args) throws IOException
	{
		
		GameOptions Mancala = new GameOptions();
		Mancala.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Mancala.setSize(700,700);
		Mancala.setVisible(true);	
		
		
	}
}




