

// import statements

import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.lang.Thread;




/*

	ADDED:
	
	Undo and Commit features







*/



/*

	CHANGES:
	
	******For easier testing each hollow contains 2 seeds. Change the seeds per hollow by 
		  changing variable "seeds" in class Game
		  
		  
	
	Choosing a winner:
		- change the private member boolean variables "playUntilAll" and "playUntilMost" (in class Game)
		  to change the rules of the game
		- if playUntilAll == true and playUntilMost == false
			- the game will continue until one player's side of the board is empty
			- if one player's side becomes empty, the opponent collects any remaining seeds on his/her side of the board
			- the two player's "store" hollows are compared to determine a winner
		- if playUntilMost == true and playUntilAll == false
			- the game will continue until one player's store contains more than half of the total number of seeds
			- the player with more than half of the seeds is the winner
			- if one player's side of the board becomes empty before this occurs, the rules for playUntilAll are used
			

			
	Rules for capture mode added:
		- if the last seed for a player lands in his/her store, that player gets to go again
		- if the last seed for a player lands in an empty hollow and there are seeds inside the hollow opposite
		  this hollow, those seeds are captured and added to that player's store
				- NOTE that this does not count towards the player getting another turn
		- NOTE that the seeds are only captured if the last seed lands in an empty hollow on THAT PLAYER'S side
		  of the board. In other words, a player cannot capture his/her own seeds by landing in an empty hollow on
		  the opponent's side of the board.
	
	
	
	Once the game is over, a "Play Again" button appears. Click this button to reset the game.
	
*/



/*
	SETUP

	- Game begins with player 1
	- If player one chooses a hole from player 2's side, no action occurs and the turn doesn't change
	- If player chooses a hole with 0 stones inside, no action occurs and the turn doesn't change
	- If the player chooses a valid hole (one on their side of the board and one that contains more
	  than 0 seeds inside), then the numbers will update as if the stones were dropped one-by-one
	  counterclockwise
	- After a player makes a valid move, the screen will update to show that it is the other
	  player's turn
	

	Start up menu
	1) Load saved game / new game
		- if new game, prompt user for game options
		- two player or one player vs. computer
	2) options
	
		Default settings - check box
		* normal, random off
		* pick theme
		* user HAS to chose whether or not they are playing computer
		
		
		If default is not checked:
				- which game (normal or custom)
				
				One menu with everything in common
				Radio button for custom or normal
				click "create": bring up new menu with specifics for whether its custom or normal
				
				
				options for both normal and custom:
				- play until (radio button) most are captured, or all are captured
				- let user chose number of seeds per hollow
					two radio buttons for custom or normal (entry box for custom)
					two radio buttons for random (on or off)
				- theme
				- play with computer or two player
				- 	if two player: enter player 1 / player 2 names

		
	3) rules set
	4) history of Mancala
	5) 
	
	
	Menu: 
	1) Rules set
	2) Save game
	3) Change theme
	4) key mnemonic to change the screen size

	
	  
	CLASSES: Mancala, Game, Background
	- current setup should make it easy for changing themes with 
	
			setContentPane(new Background());
	
	- make a new class for each theme, then add a listener to change the content pane
	  based on the user's selection	  
	  
	  
	  
	  
	 Games: 
	 - Capture mode
	 - Avalanche mode
	 - Oware: seeds do not get placed into the original hole they were picked up from
	 - Custom: let the user choose the number of holes and seeds to start with
	 - Children's Bao?
	 
	 
	Themes:
	 1) African
	 2) Beach: holes in the sand, shells for seeds
	 3) horror
	 4) coins instead of seeds
	 5) 
	  
	  
	  
	Game starts:  
		1) player one enters name
		2) player two enters name
		3) one of the players chose 
	  
	  
	  
	Plan for this file
	* add menu bar
	* add seed animation
	* add a menu screen that can show options and start game based on options
	* put REDRAW in a different location
		- seeds should be redrawn every time a seed is placed in a new location
		- delay this action so that you can see them move
	  
	
*/



// AVALANCHE MODE DOESN'T PROPERLY LET USER GET ANOTHER TURN



/*

	clicking on the other opponent's side keeps player from selecting button
	(keeps actionListener from performing)


	
	SOMETIMES selecting a player's store hollow allows that user to get another turn
	

	
	update: clicking on any other button that isn't a store and THEN clicking a store gives that player
	another turn


*/








public class Mancala
{
	
	public static void main(String [] args) throws IOException
	{
		Game mancala = new Game();
		mancala.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mancala.setSize(700,700);
		mancala.setVisible(true);	
	}
}





class Game extends JFrame implements ActionListener
{
	private Background background;
	
	private int[] arr;					// array of integers to represent the number of stones in each slot
	private int[] saveArr;				// array of integers that allows user to undo move
	
	
	
	private int size = 14;				// number of holes (2 for storing points, 6 on either side)
	private int seeds = 2;
	
	private boolean CAPTURE = true;			// set to false if the game does not use capture mode
	private boolean AVALANCHE = false;		// set to true if the game uses avalanche mode
	
	private boolean playUntilAll = true;	// true if game plays until one player's side of the board is empty
	private boolean playUntilMost = false;	// true if game plays until one player's store contains more than half
											//		of the board's total number of seeds

	private boolean GameOver = false;	// set to true once the game is over
	private int winner = 0;				// set winner to 1 for pl1, set to 2 for pl2
										//		set winner to 3 if tie
	
	private int pl1;					// number indicating the array element for player one "store"
	private int pl2;					// number indicating the array element for player two "store"
	
	private int Player = 1;				// used to display whose turn it is
	private JLabel turn;				// text label for displaying whose turn it is
	
	private JButton [] slot;			// array of buttons representing each hollow
	private JButton PlayAgain;			// button re-initializes values to restart game
										//		only set to visible when GameOver == true
	private JButton undoMove;
	private JButton commitMove;	
	
	private boolean committed = true;			// buttons should do nothing if committed == false
												// should be set to true at first so that buttons will do something
												// then set to false as soon as a button has been clicked
												//		and has performed the operation
												// do not set to true until the commit button is clicked
												// undoing a move should set committed back to true so that user can
												//		click the buttons
										
										
	private boolean freeTurn = false;	// true if the turn should not change
	private boolean flag;
	
	private BufferedImage rug;
	private BufferedImage wood;
	

	
	
	
	public Game() throws IOException
	{
		
		super("MANCALA");
		
		setLayout(null);
		
		rug = ImageIO.read(getClass().getResource("rug.jpg"));
				
		setContentPane(new Background());
		
		
		
		// add button for restarting game (do not set to visible unless GameOver == true)
		PlayAgain = new JButton("Play Again");
		PlayAgain.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// reset the game if clicked
					JButton b = (JButton)(e.getSource());
					initialize();

					// set visibility to false if clicked
					b.setVisible(false);
				}
			});
		
		PlayAgain.setBounds(20,300,150,75);
		
		add(PlayAgain);
		PlayAgain.setVisible(false);
		
		
		undoMove = new JButton("Undo");
		undoMove.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton b = (JButton)(e.getSource());
					
					for (int i = 0; i < arr.length; i++)
					{
						arr[i] = saveArr[i];
					}
					
					redrawTextonButtons();
					
					b.setVisible(false);
					
					freeTurn = true;
					
					commitMove.setVisible(false);
					
					committed = true;
					
					//flag = true;
				}
			});
			
		
		
		
		undoMove.setBounds(20,200,150,75);
		add(undoMove);
		undoMove.setVisible(false);
		
		
		
		
		
		commitMove = new JButton("Commit Move");
		commitMove.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton b = (JButton)(e.getSource());
					
					if (Player == 1 && flag == true)
					{
						Player = 2;
						
						// display whose turn it is
						turn.setText("Player " + Player + "'s turn");
					}
					else if (Player == 2 && flag == true)
					{
						Player = 1;
						
						// display whose turn it is
						turn.setText("Player " + Player + "'s turn");
					}

					
					
					// determine if someone wins
					if (Winner())
					{
						// if the game was not a tie
						if (winner != 3)
							turn.setText("Player " + winner + " wins!");
						// game is a tie if winner == 3
						else
							turn.setText("Draw!");
						
						
						// set play again button visibility to true so that user can chose to play again
						PlayAgain.setVisible(true);
						
					}
					
					b.setVisible(false);
					undoMove.setVisible(false);
					committed = true;
					//flag = true;
					
				}
				
				
			}
		
		
		
		);
		
		commitMove.setBounds(20,100,150,75);
		add(commitMove);
		commitMove.setVisible(false);
		
		
		
		
		
		
		arr = new int[size];
		saveArr = new int[size];
		pl1 = (size - 2) / 2;
		pl2 = size - 1;
		
		for (int i = 0; i < size; i++)
		{
			if (i == pl1 || i == pl2)
				arr[i] = 0;
			else
				arr[i] = seeds;		
		}
		
		// initialize array of buttons and array of rectangles (array of rectangles are used
		//	to help create the buttons
		slot = new JButton[size];
		

		// declare JLabel for displaying whose turn it is
		turn = new JLabel();
		
		
		// set label properties for displaying whose turn it is
		turn.setBounds(20,540,300,150);
		turn.setForeground(Color.WHITE);
		turn.setFont(new Font("Serif", Font.BOLD, 40));
		turn.setText("Player " + Player + "'s turn");
		add(turn);
		
		
		
		int yslot = 165;
		int xslot = 225;
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
				
			// no action listener for slots at the ends of the board
			if (i != pl1 && i != pl2)
			{
				slot[i] = new JButton(arr[i]+"");
				slot[i].setBounds(xslot,yslot,50,50);
				slot[i].addActionListener( this );
			}
			else
			{
				slot[i] = new JButton(arr[i]+"");
				
				if (i == pl2)
					slot[i].setBounds(255,115,140,40);
				else if (i == pl1)
					slot[i].setBounds(255,510,140,40);
				
			}
			
			
			add(slot[i]);
			
			
			if (i != pl1 && i != pl2)
			{
				if (i < (pl1 - 1))
					yslot += 55;
				else
					yslot -= 55;
				
				if (i == (pl1 - 1))
				{
					yslot = 440;
					xslot = 380;
				}
			}
					
		}
		
		
		
		

	}
	
	
	
	
	
	
	
	public void actionPerformed(ActionEvent e)
	{
		/*
			this function moves values in an array depending on which button the user has chosen
			this function also modifies the values displayed on each hollow/button
			no action occurs if the user choses a hollow that is not on their side of the board
			no action occurs if there are no seeds in the hollow that the user has chosen
			every time a button is clicked, the Winner() function is called to determine if the
			the game is over
		*/
		
		/*
		for (int i = 0;  i < arr.length; i++)
		{
			saveArr[i] = arr[i];
		}
		*/
		

		flag = true;		
		int pos = 0;
		int totalPtsPos;
		
		
		// button should do nothing if there are no stones inside
		
		
		// get the position of the opposing user's "store" slot in array
		if (Player == 1)
			totalPtsPos = pl2;	// 13
		else
			totalPtsPos = pl1;	// 6
		
		
		
		// check if the user has selected one of their own holes
		//		do nothing if the user has selected the wrong hole
		for (JButton button : slot)
		{
			if (e.getSource() == button)
			{
				if (Player == 1 && (pos >= (pl1 + 1) && pos <= (arr.length - 2)))
					flag = false;
				else if (Player == 2 && (pos >= 0 && pos <= (pl1 - 1)))
					flag = false;
				else
					flag = true;
			}
			
			pos++;
			
			if (flag == false)
				break;
						
		}
		
		
		
		// set flag to false if the button contains 0 seeds
		if (Integer.parseInt(e.getActionCommand()) == 0)
			flag = false;
		
		
		
		if (committed == false)
			flag = false;
		
		
		
		// if the user has selected one of their own holes
		if (flag == true)
		{
			
			for (int i = 0;  i < arr.length; i++)
			{
				saveArr[i] = arr[i];
			}
	
			
			//hand
			int k = Integer.parseInt(e.getActionCommand());
			
			//index
			int i = 0;
			for (JButton button : slot)
			{
				if (e.getSource() == button)
					break;
				else
					i++;
				
			}
		
			// index to access
			int l = i;
			
			// number of stones currently holding
			int hand = k;
			
			
			l = shiftArray(hand,l,totalPtsPos,k);
			

			undoMove.setVisible(true);
			commitMove.setVisible(true);
			
			
			
			
			if ((l == pl1 && Player == 1) || (l == pl2 && Player == 2))
				flag = false;
			
		
			
			

			redrawTextonButtons();			
			
			committed = false;
			
		}
		

		
		
		
	}
	
	public int shiftArray(int hand, int l, int totalPtsPos, int k)
	{
		
		// for the number of stones in that hole
		for (int j = 0; j <= hand; j++)
		{
			// at first, pick up all the stone
			if (j == 0)
			{
				arr[l] = 0;
				
				
			}
			
			// otherwise, set that element to what it was before plus one
			else
			{
				if (l != totalPtsPos)
				{
					arr[l] = arr[l] + 1;
					
				
					// lost one stone
					k--;
					
				}
				else
					j--;
				
				
			
				slot[l].setText(arr[l] + "" );
			
			
			
			}
			
			
						
			slot[l].setText(arr[l] + "" );
			

			
			
			// go to the next element in the array
			l++;
			
			
			// if j is arr[i] - 1 and the hand (k) is not empty
			if (l == (arr.length) && k != 0)
			{
				l = 0;
			}
			
			
		
			
			
			
	
			
		}
		
		l--;
		if (CAPTURE)
		{
			Capture(l);				
		}
		
		if (AVALANCHE)
		{
			if (l == pl1 && Player == 1)
				freeTurn = true;
			else if (l == pl2 && Player == 2)
				freeTurn = true;
			else
				freeTurn = false;
		
			
			
			if (arr[l] != 1)
				slot[l].doClick();
		}
	

			
		redrawTextonButtons();	
			
	
		return l;
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public boolean Winner()
	{
		
		/*
			This function returns true if there is a winner and false otherwise
			If winner, this function sets private member variables winner and GameOver
				winner = 1,2, or 3 (1 if player one wins, 2 if player two wins, and 3 if it's a tie)
				GameOver = true if there is a winner
		
			If one of the players has run out of seeds, the other player collects any remaining seeds left
			In his/her hollows
			
			Then the two player's "stores" are compared to determine the winner
		
		*/
		
		
		// if one side of the board is empty, the other user collects the rest of his/her seeds
		if (playUntilAll == true)
		{
			return playUntilAll();
		}
		
		
		// if one player's store collects more than half of the total number of seeds, that player wins
		//		if one side becomes empty before that happens, then let the other player collect the rest
		//		of his/her seeds
		if (playUntilMost == true)
		{
			if (arr[pl1] > (((arr.length-2)*seeds))/2)
			{
				winner = 1;
				GameOver = true;
				return true;
			}
			else if (arr[pl2] > (((arr.length-2)*seeds))/2)
			{
				winner = 2;
				GameOver = true;
				return true;
			}
			
			return playUntilAll();

		}
		
		
		return false;

	}
	

	public boolean playUntilAll()
	{
		int flag = 0;			// set to 1 if player one has run out of seeds
		int flag2 = 0;			// set to 1 if player two has run out of seeds
		
		
		// check if player one has run out of seeds
		for (int i = 0; i < pl1; i++)
		{
			if (arr[i] != 0)
			{
				flag = 1;
				break;
			}

		}
		
		
		// if player one has run out of seeds
		if (flag != 1)
		{
			
			// player two collects remaining seeds in her hollows
			//	ONLY IF PLAYING IN CAPTURE MODE
			if (CAPTURE == true)
			{
				player2CollectSeeds(arr);
			}
			
			
			// reset the text for each button
			redrawTextonButtons();
			
			
			if (arr[pl1] > arr[pl2])
			{
				winner = 1;
				GameOver = true;
			}
			else if (arr[pl1] < arr[pl2])
			{
				winner = 2;
				GameOver = true;
			}
			else
			{
				winner = 3;
				GameOver = true;
			}
			
			
			// return true if the game is over
			return true;
		}
		

		
		// check if player two has run out of seeds
		for (int i = pl1 + 1; i < pl2; i++)
		{
			if (arr[i] != 0)
				flag2 = 1;
			
			if (flag2 == 1)
				break;		
			
		}
		
		
		// if player two has run out of seeds
		if (flag2 != 1)
		{
			
			// player one collects remaining seeds in her hollows
			// ONLY IF PLAYING IN CAPTURE MODE
			if (CAPTURE == true)
			{
				player1CollectSeeds(arr);
			}
			
			
			// reset the text for each button
			redrawTextonButtons();
			
			if (arr[pl1] > arr[pl2])
			{
				winner = 1;
				GameOver = true;
			}
			else if (arr[pl1] < arr[pl2])
			{
				winner = 2;
				GameOver = true;
			}
			else
			{
				winner = 3;
				GameOver = true;
			}
			// return true if the game is over
			return true;
		}
		
		
		return false;
				
	}
	
	
	
	// re-initializes variables so that user can restart game
	public void initialize()
	{
		
		for (int i = 0; i < size; i++)
		{
			if (i == pl1 || i == pl2)
				arr[i] = 0;
			else
				arr[i] = seeds;		
		}
		
		
		Player = 1;
		
		turn.setText("Player " + Player + "'s turn");
		
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
			slot[i].setText(arr[i]+"");				
		}
		
		
		GameOver = false;		// set to true once the game is over
		winner = 0;				// set winner to 1 for pl1, set to 2 for pl2
	
	}
	
	
	public void redrawTextonButtons()
	{
		
		// reset the text for each button
		int p = 0;
		for (JButton button : slot)
		{
			button.setText(arr[p]+"");
			p++;
			
		}
	}
	
	
	public void player2CollectSeeds(int [] arr)
	{
		// player two collects remaining seeds on his/her side of the board
		for (int i = pl1 + 1; i < pl2; i++)
		{
			arr[pl2] = arr[pl2] + arr[i];
			arr[i] = 0;
		}

	}
	
	
	public void player1CollectSeeds(int [] arr)
	{
		// player one collects the remaining seeds on his/her side of the board
		for (int i = 0; i < pl1; i++)
		{
			arr[pl1] = arr[pl1] + arr[i];
			arr[i] = 0;
		}
	}
	
	
	public int getOpponentIndex(int [] arr, int i)
	{
		/*
			this function returns the index opposite the given index in the array
			(the opponents corresponding index)
			
			this function uses an array to determine what should be added to the given index
			to get the opponent's corresponding index
			
			ex. for an array with size 14, index opposite to position 4 is 4 + 4 = 8,
				and the index opposite to position 5 is 5 + 2 = 7
				
				Array of size 14 (where 6 and 13 are the player "stores")
				0 1 2 3 4 5 	6 		7 8 9 10 11 12 			13

				i			   i    c[i]
				5 --> return = 5  +  2  =  7
				4 --> return = 4  +  4  =  8
				3 --> return = 3  +  6  =  9
				2 --> return = 2  +  8  =  10
				1 --> return = 1  +  10  =  11
				0 --> return = 0  +  12  =  12
		
		*/
		
		
		
		// contains values for what needs to be added to each corresponding 
		//	element in array to get opponent spot

		int [] c = new int[arr.length];;
		int add = arr.length - 2;
		
		// first half
		for (int k = 0; k < pl1; k++)
		{
			c[k] = add;
			add -= 2;
		}
		
		add -= 2;
		c[pl1] = pl1 + 1;
		c[pl2] = -1 * (pl1 + 1);
		
		// second half of array
		for (int k = pl1 + 1; k < pl2; k++)
		{
			c[k] = add;
			add -= 2;
		}
		
		
		return (i + c[i]);
		
	}
	
	
	public void Capture(int l)
	{
		int oppIndex;

		// this is not correct
		// only capture if filling a hole on correct side
			
		// check opponent index only if we've landed in an empty hollow on CORRECT SIDE
		//		and not landing in a player's "hole"
		
		if ((Player == 1 && l < pl1) || (Player == 2 && l > pl1))
		{
			if (arr[l] == 1 && l != pl1 && l != pl2)
			{
				oppIndex = getOpponentIndex(arr,l);
				
				
				if (arr[oppIndex] != 0)
				{
					arr[l] = arr[l] + arr[oppIndex];
					arr[oppIndex] = 0;
					
					// determine which player's store these seeds go to
					if (oppIndex > pl1)
					{
						arr[pl1] = arr[pl1] + arr[l];
						arr[l] = 0;
					}
					else
					{
						arr[pl2] = arr[pl2] + arr[l];
						arr[l] = 0;
						
					}
				}
			}
		}

		
		
	}


	
}


class Background extends JPanel
{
	private Image img;
	private Image img2;
	private JLabel pl1Side;
	private JLabel pl2Side;

	public Background() throws IOException
	{
		setLayout(null);
		img = ImageIO.read(getClass().getResource("rug.jpg"));
		img2 = ImageIO.read(getClass().getResource("wood.jpg"));
		
		
		
		pl1Side = new JLabel();
		pl1Side.setBounds(230,-15,300,150);
		pl1Side.setForeground(Color.WHITE);
		pl1Side.setFont(new Font("Serif", Font.BOLD, 35));
		pl1Side.setText("P1");
		add(pl1Side);
		
		pl2Side = new JLabel();
		pl2Side.setBounds(385,-15,300,150);
		pl2Side.setForeground(Color.WHITE);
		pl2Side.setFont(new Font("Serif", Font.BOLD, 35));
		pl2Side.setText("P2");
		add(pl2Side);
	}	
	
	// two dimensional vector
	public void paintComponent(Graphics g)
	{
		
		super.paintComponent(g);
					
		g.drawImage(img,-45,-50,760,760,null);
		g.drawImage(img2,200,100,250,460,null);
			
			
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(4));
		
		// game board
		g.setColor(Color.BLACK);
		g.drawRoundRect(200,100,250,460,20,20);
			
	}	
	
}



