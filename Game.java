import java.awt.event.*;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.io.*;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.Timer;


/*

	DIFFERENT WAYS TO PLAY GAME:
	
	Capture mode with:
	1) two human players with play until all							
	2) two human players with play until most							
	3) computer player one, human player two play until all				
	4) computer player one, human player two play until most				
	5) computer player two, human player one play until all				
	6) computer player two, human player one play until most				
	

	Avalanche mode with:
	1) two human players with play until all
	2) two human players with play until most
	3) computer player one, human player two play until all
	4) computer player one, human player two play until most
	5) computer player two, human player one play until all
	6) computer player two, human player one play until most


*/


public class Game extends JPanel implements ActionListener
{	
	private int[] arr;						// array of integers to represent the number of stones in each slot
	private int[] saveArr;					// array of integers that allows user to undo move
	private int size = 14;					// number of holes (2 for storing points, 6 on either side)
	private int seeds = 4;
	private int totalSeeds;
	private String winner = "";
	private int pl1_store;						// number indicating the array element for player one "store"
	private int pl2_store;						// number indicating the array element for player two "store"
	private int Player = 1;						// used to display whose turn it is
	
	private boolean endTurn;					// set to false if the game should NOT change players
	private boolean CAPTURE = true;				// set to false if the game does not use capture mode
	private boolean AVALANCHE = false;			// set to true if the game uses avalanche mode
	private boolean playUntilAll = true;		// true if game plays until one player's side of the board is empty
	
	
	private boolean save = true;				/* particularly used for avalanche mode
													set to true if the array should be saved
													sometimes, when the actionPerformed function is called the function 
													should should not be saved because each button triggers another call 
													to the function*/
													
													
	private boolean GameOver = false;			// set to true once the game is over
	private boolean committed = true;			/* buttons should do nothing if committed == false
													should be set to true at first so that buttons will do something
													then set to false as soon as a button has been clicked
													and has performed the operation
													do not set to true until the commit button is clicked
													undoing a move should set committed back to true so that user can
													click the buttons */
									
									
	/* if both variables below are false, we have two human players */										
	private boolean ComputerPlayer1 = false;		// set to true if the computer is player 1
	private boolean ComputerPlayer2 = false;		// set to true if the computer is player 2
																								

	
	private JLabel turn;					// text label for displaying whose turn it is
	private JButton undoMove;				// button for undoing a move
	private JButton commitMove;				// button for committing the player's chosen move
	private JButton [] slot;				// array of buttons representing each hollow (parallel to int [] arr
	private JButton PlayAgain;				// button re-initializes values to restart game (set visible when GameOver == true)
	
	private Player playerOne, playerTwo;	// Player variables used to let a user take their turn / shift the array values
	
	private String playerOneName, playerTwoName;
	private JButton startGame;
	private String bg;
	
	
	
	private JRadioButton cptr;
	private JRadioButton avlnch;
	private ButtonGroup mode;
	private JRadioButton mostseed;
	private JRadioButton allseed;
	private ButtonGroup playuntil;
	private JRadioButton cp1;
	private JRadioButton cp2;
	private JRadioButton cpoff;
	private ButtonGroup comp;
	private JRadioButton matbutt;
	private JRadioButton candybutt;
	private JRadioButton beachbutt;
	private JRadioButton horrorbutt;
	
	private Image img;
	private JLabel pl1Side;
	private JLabel pl2Side;
	
	private String theme;
	
	//CAPTURE,AVALANCHE,playUntilAll,ComputerPlayer1,ComputerPlayer2
	public Game(String bg, boolean cp, boolean aval, boolean plUntAll, boolean cmpPl1, boolean cmpPl2, 
				String playerOneName, String playerTwoName) throws IOException
	{
		
		// set member variables given specification from user
		theme = bg;
		CAPTURE = cp;
		AVALANCHE = aval;
		playUntilAll = plUntAll;
		ComputerPlayer1 = cmpPl1;
		ComputerPlayer2 = cmpPl2;
		Color txtcolor = new Color(Color.WHITE.getRGB());
		
		
		// decide which image needs to be drawn
		if(theme == "mat")
		{
			img = ImageIO.read(getClass().getResource("mat.jpg"));
		}
		else if(theme == "candy")
		{
			img = ImageIO.read(getClass().getResource("candy.jpg"));
			txtcolor = new Color(Color.BLACK.getRGB());
		}
		else if(theme == "beach")
		{
			img = ImageIO.read(getClass().getResource("beach.jpg"));
			txtcolor = new Color(Color.BLACK.getRGB());
		}
		else if(theme == "horror")
		{
			img = ImageIO.read(getClass().getResource("horror.jpg"));
			txtcolor = new Color(Color.WHITE.getRGB());
		}
		else
		{
			theme = "mat";
			img = ImageIO.read(getClass().getResource("mat.jpg"));
			txtcolor = new Color(Color.WHITE.getRGB());
			
		}

		pl1Side = new JLabel();
		pl1Side.setBounds(230,-15,300,150);
		pl1Side.setForeground(txtcolor);
		pl1Side.setFont(new Font("Serif", Font.BOLD, 35));
		pl1Side.setText("P1");
		add(pl1Side);
		
		pl2Side = new JLabel();
		pl2Side.setBounds(365,-15,300,150);
		pl2Side.setForeground(txtcolor);
		pl2Side.setFont(new Font("Serif", Font.BOLD, 35));
		pl2Side.setText("P2");
		add(pl2Side);
		
		
		
		//super("MANCALA");
		setLayout(null);				
		setForeground(txtcolor);		
		
		// initialize the array for holding the number of seeds per hollow
		arr = new int[size];
		
		// set size for save array (to be initialized later)
		saveArr = new int[size];
		
		// set the store for player 1 to the bottom hollow
		pl1_store = (size - 2) / 2;
		
		// set the store for player 2 to the top hollow
		pl2_store = size - 1;
		
		// initialize the array (set stores to 0)
		for (int i = 0; i < size; i++)
		{
			if (i == pl1_store || i == pl2_store)
			{
				arr[i] = 0;
				saveArr[i] = 0;
			}
			else
			{
				arr[i] = seeds;	
				saveArr[i] = seeds;
			}				
		}
		
		
		// do not count the two store hollows when determining the total number of seeds
		totalSeeds = (size - 2) * seeds;
		
		// declare the Player member variables which are used to let players take a turn (select a hollow)
		
		// if we have two human players
		if (ComputerPlayer1 == false && ComputerPlayer2 == false)
		{
			playerOne = new Player(1, pl1_store, pl2_store);
			playerTwo = new Player(2, pl2_store, pl1_store);
		}
		
		// if first player is computer player and second is human
		else if (ComputerPlayer1 == true && ComputerPlayer2 == false)
		{
			playerOne = new Computer(1, pl1_store, pl2_store);
			playerTwo = new Player(2, pl2_store, pl1_store);
		}
		
		// if first player is human and second is computer player
		else if (ComputerPlayer1 == false && ComputerPlayer2 == true)
		{
			playerOne = new Player(1, pl1_store, pl2_store);
			playerTwo = new Computer(2, pl2_store, pl1_store);
		}
			
			
		
		// set the player one and player two names from the parameters in constructor
		playerOne.name = playerOneName;
		playerTwo.name = playerTwoName;
			
			
		// initialize array of buttons
		slot = new JButton[size];
		

		// declare JLabel for displaying whose turn it is
		turn = new JLabel();
		
		// set label properties for displaying whose turn it is
		turn.setBounds(20,540,500,150);
		turn.setForeground(txtcolor);
		turn.setFont(new Font("Serif", Font.BOLD, 40));
		turn.setText(playerOne.name + "'s turn");
		
		
		// used to determine the positions of the buttons (hollows) on the screen
		int yslot = 155;
		int xslot = 215;
		
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
				
			// no action listener for slots at the ends of the board
			if (i != pl1_store && i != pl2_store)
			{
				slot[i] = new JButton(arr[i]+"");
				slot[i].setBounds(xslot,yslot,60,60);
				slot[i].addActionListener( this );
			}
			else
			{
				slot[i] = new JButton(arr[i]+"");
				slot[i].setHorizontalTextPosition(AbstractButton.CENTER);
				slot[i].setVerticalTextPosition(AbstractButton.BOTTOM);
				
				if (i == pl2_store)
					slot[i].setBounds(245,105,140,45);
				else if (i == pl1_store)
					slot[i].setBounds(245,525,140,45);
				
			}
			
			
			slot[i].setBackground(Color.WHITE);
			
			
			if (i != pl1_store && i != pl2_store)
			{
				if (i < (pl1_store - 1))
					yslot += 60;
				else
					yslot -= 60;
				
				if (i == (pl1_store - 1))
				{
					yslot = 455;
					xslot = 360;
				}
			}
					
		}
		
		
		//redrawTextonButtons();
		try
		{
			redrawTextonButtons();
		}
		catch(IOException drawExc)
		{
			drawExc.printStackTrace();
		}
		

	// PLAY AGAIN ACTION LISTENER
	// button for restarting game (do not set to visible unless GameOver == true)
		PlayAgain = new JButton("Play Again");
		PlayAgain.addActionListener(new PlayAgainButton());	
		PlayAgain.setForeground(Color.BLACK);
		PlayAgain.setBackground(Color.WHITE);
		PlayAgain.setFont(new Font("Serif", Font.BOLD, 20));
		PlayAgain.setBounds(20,300,140,75);
		
		
		
	// UNDO MOVE ACTION LISTENER
	// button for undoing a player's move (do not set to visible unless a player has taken a turn)
		undoMove = new JButton("Undo");
		undoMove.setBackground(Color.WHITE);
		undoMove.addActionListener(new UndoMoveButton());
		undoMove.setBounds(20,200,150,75);
		

	// COMMIT MOVE ACTION LISTENER
	// button for making a player's move official (do not set to visible unless the player has taken a turn)
		commitMove = new JButton("Commit Move");
		commitMove.setBackground(Color.WHITE);
		commitMove.addActionListener(new CommitMoveButton());		
		commitMove.setBounds(20,100,150,75);
	
		
		// set turn and buttons to be visible
		add(turn);
		turn.setVisible(true);
		
		// add undoMove, playAgain, and commitMove buttons
		add(undoMove);
		undoMove.setVisible(false);
		
		add(PlayAgain);
		PlayAgain.setVisible(false);
		
		add(commitMove);
		commitMove.setVisible(false);
		
		// add buttons (slots) to the board and set their visiblity
		for (int i = 0; i < arr.length; i++)
		{
			add(slot[i]);
			slot[i].setVisible(true);
		}
	
	
		// if the computer is player one, envoke the computer player's move
		if (Player == 1 && ComputerPlayer1 == true)
		{
			// let the computer take a turn at the beginning of the game
			ComputerPlayer1TakeTurn();
			
			// if playing with capture mode, commit the move
			if (CAPTURE)
				commitMove.doClick();
			
		}
		

	}

	
	
	// draw the background image
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
					
		g.drawImage(img,0,0,684,661,null);
			
			
		// do not draw the rectangle boarder for certain themes
		if (theme != "candy" && theme != "beach")
		{
			Graphics2D g2d = (Graphics2D) g;
			g2d.setStroke(new BasicStroke(4));
			
			// game board
			g.setColor(Color.BLACK);
			
			g.drawRect(175,80,275,510);

			
		}
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		/*
			this function moves values in an array depending on which button the user has chosen
			no action occurs if the user choses a hollow that is not on their side of the board
			no action occurs if there are no seeds in the hollow that the user has chosen
			every time a button is clicked, the Winner() function is called to determine if the
			the game is over
		*/
		
		
		if (GameOver == false)
		{	
			// only perform an action for a button if a turn has been committed by clicking the "Commit Move" button
			if (committed == true)
			{	
				int pos = 0;				// used to determine the position of the button clicked in the array of buttons
				int opponentStoreIndex;
				int l = 0;		
				
				
				
				// get the position of the opposing user's "store" slot in array
				if (Player == 1)
					opponentStoreIndex = playerTwo.store;	// 13
				else
					opponentStoreIndex = playerOne.store;	// 6
				
				
					
				endTurn= true;
				
			
				/*
					Loop through to find the position of the button we've clicked
					Check if the user has selected one of their own holes
					Do nothing if the user has selected a hole on the wrong side of the board
					Perform this block ONLY if one of the two possibilities
						1) using capture mode
						2) using avalanche mode and it is the button clicked by the USER
							Any clicks performed after that are invoked by the program to simulate the avalanche feature
							So any clicks done on the opponents side of the board SHOULD be allowed 
				*/
				if (CAPTURE || (AVALANCHE && save))
				{
					for (JButton button : slot)
					{
							if (e.getSource() == button)
							{
								if (Player == 1 && (pos >= (playerOne.store + 1) && pos <= (arr.length - 2)))
								{
									endTurn= false;
								}
								else if (Player == 2 && (pos >= 0 && pos <= (playerOne.store - 1)))
								{
									endTurn= false;
								}

							}
							
							pos++;
							
							if (endTurn== false)
								break;
									
					}
				}		
					
					
				// set endTurnto false if the button contains 0 seeds
				if (Integer.parseInt(e.getActionCommand()) == 0)
				{
					endTurn= false;
				}
				
			
				// if the user has selected one of their own holes
				if (endTurn == true)
				{
					// save the array so that the user can "undo" move
					// the array is saved differently depending on which mode the game is being played in
					if (CAPTURE == true)
					{
						for (int i = 0;  i < arr.length; i++)
						{
							saveArr[i] = arr[i];
						}
					}
					
					
					// if playing in avalanche mode and the button has been clicked by the user
					// do not save the array if the doClick() function was invoked by the program
					// otherwise saveArr would not save the original array
					if (AVALANCHE == true && save == true)
					{
						for (int i = 0;  i < arr.length; i++)
						{
							saveArr[i] = arr[i];
						}	
					}
					save = false;
					
					
					
					//hand / number of seeds currently holding
					int hand = Integer.parseInt(e.getActionCommand());
					
					
					// set l to the index of the button clicked in the array of buttons (named "slot")
					for (JButton button : slot)
					{
						if (e.getSource() == button)
							break;
						else
							l++;
						
					}
				
				
					
					// if it is Player 1's turn, let playerOne take a turn, otherwise let playerTwo take a turn
					if (Player == 1)
					{
						l = playerOne.takeTurn(hand, l, arr, slot, CAPTURE, AVALANCHE);
					}
					else
					{
						l = playerTwo.takeTurn(hand, l, arr, slot, CAPTURE, AVALANCHE);
					}
					
					try
					{
						redrawTextonButtons();
					}
					catch(IOException drawExc)
					{
						drawExc.printStackTrace();
					}
					
					
					undoMove.setVisible(true);
					commitMove.setVisible(true);
					
					
					
					// maybe use something like 
					// if (playerOne.turn) where turn is true if it is this player's turn
					if ((l == playerOne.store && Player == 1) || (l == playerTwo.store && Player == 2))
					{
						endTurn= false;
					}
		
					try
					{
						redrawTextonButtons();
					}
					catch(IOException drawExc)
					{
						drawExc.printStackTrace();
					}
					committed = false;
					
				}
				
				if ((l == playerOne.store && Player == 1) || (l == playerTwo.store && Player == 2))
				{
					endTurn= false;
				}
			
				
		
			}
		}
		
		if (ComputerPlayer1 == true && Player == 1 && AVALANCHE)
				commitMove.doClick();
		
	}
	
	
	// envoked so that the computer player can "take a turn" if the computer is player one
	public void ComputerPlayer1TakeTurn()
	{
		int index;
		
		if (AVALANCHE)
		{
			do
			{
				index = playerOne.takeTurn(-1, -1, arr, slot, CAPTURE, AVALANCHE);
			} while (playerOne.freeTurn == true);
		}
		
		if (CAPTURE)
		{
			
			do{
				index = playerOne.takeTurn(-1, -1, arr, slot, CAPTURE, AVALANCHE);
			} while (index == playerOne.store);
	
		}
	
	
		Player = 2;
		
		
		// display a temporary label to simulate that the computer is making a move
		showComputerLabel();

		
	}
	
	
	public void showComputerLabel()
	{
		// used to delay when computer is choosing a hollow
		// and also display that the computer is making a turn
		
		Timer timer = new Timer(1000, 
		new ActionListener()
		{
            public void actionPerformed(ActionEvent e) {
			
				if (Player == 2 && ComputerPlayer1 == true)
				{
					Player = 2;
					// display whose turn it is
					turn.setText(playerTwo.name + "'s turn");
				}
				else if (Player == 1 && ComputerPlayer2 == true)
				{
					Player = 1;
					// display whose turn it is
					turn.setText(playerOne.name + "'s turn");
				}
				
				try
				{
					redrawTextonButtons();
				}
				catch(IOException drawExc)
				{
					drawExc.printStackTrace();
				}
				
                revalidate();
				
				CheckForWinner();
				
            }
        });
       
		timer.start();
        timer.setRepeats(false);
		
		
		revalidate();
		if (ComputerPlayer1 == true)
			turn.setText(playerOne.name + " making a move");
		else if (ComputerPlayer2 == true)
			turn.setText(playerTwo.name + " making a move");
		
		
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
		else if (playUntilAll == false)
		{
			return playUntilMost();
		}
		

		return false;

	}
	
	
	public boolean playUntilMost()
	{
		if (arr[playerOne.store] > (totalSeeds/2))
		{
			winner = playerOne.name;
			GameOver = true;
			return true;
		}
		else if (arr[playerTwo.store] > (totalSeeds/2))
		{
			winner = playerTwo.name;
			GameOver = true;
			return true;
		}
		
		return playUntilAll();
		
	}

	public boolean playUntilAll()
	{
		int flag1 = 0;			// set to 1 if player one has run out of seeds
		int flag2 = 0;			// set to 1 if player two has run out of seeds
		
		
		// check if player one has run out of seeds
		for (int i = 0; i < playerOne.store; i++)
		{
			if (arr[i] != 0)
			{
				flag1 = 1;
				break;
			}

		}
		
		
		// if player one has run out of seeds
		if (flag1 != 1)
		{
			
			// player two collects remaining seeds in her hollows
			//	ONLY IF PLAYING IN CAPTURE MODE
			if (CAPTURE == true)
			{
				player2CollectSeeds();
			}
			
			
			// reset the text for each button
			try
			{
				redrawTextonButtons();
			}
			catch(IOException drawExc)
			{
				drawExc.printStackTrace();
			}
			
			
			if (arr[playerOne.store] > arr[playerTwo.store])
			{
				winner = playerOne.name;
				GameOver = true;
			}
			else if (arr[playerOne.store] < arr[playerTwo.store])
			{
				winner = playerTwo.name;
				GameOver = true;
			}
			else
			{
				winner = "draw";
				GameOver = true;
			}
			
			
			// return true if the game is over
			return true;
		}
		

		
		// check if player two has run out of seeds
		for (int i = playerOne.store + 1; i < playerTwo.store; i++)
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
				player1CollectSeeds();
			}
			
			
			// reset the text for each button
			try
			{
				redrawTextonButtons();
			}
			catch(IOException drawExc)
			{
				drawExc.printStackTrace();
			}
			
			
			// set winner
			if (arr[playerOne.store] > arr[playerTwo.store])
			{
				winner = playerOne.name;
				GameOver = true;
			}
			else if (arr[playerOne.store] < arr[playerTwo.store])
			{
				winner = playerTwo.name;
				GameOver = true;
			}
			else
			{
				winner = "draw";
				GameOver = true;
			}
			
			
			// return true if the game is over
			return true;
		}
		
		
		return false;
				
	}
	
	
	public void player2CollectSeeds()
	{
		// player two collects remaining seeds on his/her side of the board
		for (int i = pl1_store + 1; i < pl2_store; i++)
		{
			arr[pl2_store] = arr[pl2_store] + arr[i];
			arr[i] = 0;
		}
		
		playerTwo.points = arr[pl2_store];

	}
	
	
	
	// playeone collects the remaining seeds on his/her side of the board
	public void player1CollectSeeds()
	{
		for (int i = 0; i < pl1_store; i++)
		{
			arr[pl1_store] = arr[pl1_store] + arr[i];
			arr[i] = 0;
		}
		
		playerOne.points = arr[pl1_store];
		
	}
	
	
	// re-initializes variables so that user can restart game
	public void initialize()
	{
		
		for (int i = 0; i < size; i++)
		{
			if (i == pl1_store || i == pl2_store)
				arr[i] = 0;
			else
				arr[i] = seeds;		
		}
		
		
		Player = 1;
		
		turn.setText(playerOne.name + "'s turn");
		
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
			slot[i].setText(arr[i]+"");				
		}
		
		
		GameOver = false;		// set to true once the game is over
		winner = "";
	
	}
	
	
	
	public void redrawTextonButtons() throws IOException
	{
		
		// reset the text for each button
		int p = 0;
		for (JButton button : slot)
		{
			
			// display no icon for a slot that contains no tokens
			if (arr[p] == 0)
				button.setIcon(null);
			
			// set the token for each icon
			else
			{
				if (theme == "beach")
					button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("beachtoken.jpg"))));
				else if (theme == "mat")
					button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("mattoken.jpg"))));
				else if (theme == "candy")
					button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("candytoken.jpg"))));
				else if (theme == "horror")
					button.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("horrortoken.jpg"))));


			}
			
			
			button.setText(arr[p]+"");
			
			if (p != playerOne.store && p != playerTwo.store)
			{
				button.setHorizontalTextPosition(AbstractButton.CENTER);
				button.setVerticalTextPosition(AbstractButton.BOTTOM);
			}
			else
			{
				button.setHorizontalTextPosition(AbstractButton.LEFT);
				button.setVerticalTextPosition(AbstractButton.CENTER);	
			}
			
			
			p++;
		}
	}
	
	
	
	
	// play again function for restarting game with currenly set options
	private class PlayAgainButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// reset the game if clicked
			JButton b = (JButton)(e.getSource());
			initialize();
			
			
			if (Player == 1 && ComputerPlayer1 == true)
			{
				// let the computer take a turn at the beginning of the game
				ComputerPlayer1TakeTurn();			
			}
			
			try
			{
				redrawTextonButtons();
			}
			catch(IOException drawExc)
			{
				drawExc.printStackTrace();
			}

			
			// set visibility to false if clicked
			b.setVisible(false);
			GameOver = false;
		}
		
	}
	
	
	// undo move button to undo the chosen option selected by the user
	private class UndoMoveButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton b = (JButton)(e.getSource());
			
			for (int i = 0; i < arr.length; i++)
			{
				arr[i] = saveArr[i];
			}
			

			// redraw the text buttons
			try
			{
				redrawTextonButtons();
			}
			catch(IOException drawExc)
			{
				drawExc.printStackTrace();
			}
			
			b.setVisible(false);
			
			if (Player == 1)
				playerOne.freeTurn = true;
			else
				playerTwo.freeTurn = true;
			
			commitMove.setVisible(false);
			committed = true;
			save = true;

		}
		
	}
	
	
	// commitmovebutton for commiting the move made by a user
	private class CommitMoveButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JButton b = (JButton)(e.getSource());
			boolean checkWin;
					
					
			// change label that displays whose turn it is
			ChangeTurns();

			
			// determine if someone wins
			checkWin = CheckForWinner();
			
			int index;
			int s;
		
			if (!checkWin)
			{
				if ((Player == 2 && ComputerPlayer2 == true) || (Player == 1 && ComputerPlayer1 == true))
				{
					
					// let the computer keep taking a turn while the last seed lands inside it's store
					do{
						
						// DISPLAY A LABEL THAT SAYS PLAYER TWO OR ONE IS GOING
						
						
						// first two parameters are not needed for the takeTurn method in class Computer
						if (Player == 1)
						{
							index = playerOne.takeTurn(-1, -1, arr, slot, CAPTURE, AVALANCHE);
							s = playerOne.store;
							
						}
						else
						{
							index = playerTwo.takeTurn(-1, -1, arr, slot, CAPTURE, AVALANCHE);
							s = playerTwo.store;
						}

						//redrawTextonButtons();
						try
						{
							redrawTextonButtons();
						}
						catch(IOException drawExc)
						{
							drawExc.printStackTrace();
						}
						
						
						checkWin = CheckForWinner();
						
					
					} while (index == s && checkWin == false);
					
									
					if (!checkWin)
					{
						commitMove.doClick();
					}
					
					showComputerLabel();	
					
				}
			}
			
		}
				
	}
	
	
	public void ChangeTurns()
	{
		if (Player == 1 && endTurn== true)
		{				
			Player = 2;
			// display whose turn it is
			turn.setText(playerTwo.name + "'s turn");
		}
		else if (Player == 2 && endTurn== true)
		{
			Player = 1;
			// display whose turn it is
			turn.setText(playerOne.name + "'s turn");
		}
				
	}
	
	
	
	// for checking/reseting the visibility of certain buttons once/if a player has won a game and displaying who won
	public boolean CheckForWinner()
	{
		boolean c = Winner();
		if (c)
		{
			// if the game was not a tie
			if (winner != "draw")
				turn.setText(winner + " wins!");
			// game is a tie if winner == 3
			else
				turn.setText("Draw!");
			
			
			// set play again button visibility to true so that user can chose to play again
			PlayAgain.setVisible(true);
			
			
			
		}
		
		commitMove.setVisible(false);
		undoMove.setVisible(false);
		committed = true;
		save = true;	

		return c;
		
	}

	

}




