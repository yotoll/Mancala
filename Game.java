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
import java.lang.Thread;
import javax.swing.JRadioButton;


/*

	seeds
	CAPTURE
	AVALANCHE
	playUntilAll (t or false) - if false, playUntilMost
	ComputerPlayer1 and ComputerPlayer2
	playerOneName, playerTwoName;
	themes
	
	Monday
	- menu option so user can set above
	- avalanche mode for computer
	
	Extra
	- tokens on buttons or setting background
	- save game
	- music

*/



public class Game extends JFrame implements ActionListener
{	
	private int[] arr;						// array of integers to represent the number of stones in each slot
	private int[] saveArr;					// array of integers that allows user to undo move
	private int size = 14;					// number of holes (2 for storing points, 6 on either side)
	private int seeds = 2;
	private int totalSeeds;
	private int winner = 0;						// set winner to 1 for pl1_store, set to 2 for pl2_store, set to 3 if tie
	private int pl1_store;						// number indicating the array element for player one "store"
	private int pl2_store;						// number indicating the array element for player two "store"
	private int Player = 1;						// used to display whose turn it is
	
	
	
	//private boolean freeTurn = false;			// true if the turn should not change
	private boolean endTurn;					// set to false if the game should NOT change players
	private boolean CAPTURE = true;				// set to false if the game does not use capture mode
	private boolean AVALANCHE = false;			// set to true if the game uses avalanche mode
	private boolean playUntilAll = true;		// true if game plays until one player's side of the board is empty
	
	
	private boolean save = true;				/* particularly used for avalanche mode
													set to true if the array should be saved
													sometimes, when the actionPerformed function is called the function should
													should not be saved because each button triggers another call to the function*/
	private boolean GameOver = false;			// set to true once the game is over
	private boolean committed = true;			/* buttons should do nothing if committed == false
													should be set to true at first so that buttons will do something
													then set to false as soon as a button has been clicked
													and has performed the operation
													do not set to true until the commit button is clicked
													undoing a move should set committed back to true so that user can
													click the buttons */
									
									
	/* if both variables below are false, we have two human players */										
	private boolean ComputerPlayer1 = false;	// set to true if the computer is player 1
	private boolean ComputerPlayer2 = true;	// set to true if the computer is player 2
	
	private JLabel turn;					// text label for displaying whose turn it is
	private JButton undoMove;				// button for undoing a move
	private JButton commitMove;				// button for committing the player's chosen move
	private JButton [] slot;				// array of buttons representing each hollow (parallel to int [] arr
	private JButton PlayAgain;				// button re-initializes values to restart game (set visible when GameOver == true)
	
	private Player playerOne, playerTwo;	// Player variables used to let a user take their turn / shift the array values
	
	private String playerOneName, playerTwoName;
	private String bg;						//Background theme
	private JButton startGame;
	
	
	public Game() throws IOException
	{
		
		super("MANCALA");
		setLayout(null);				
		setForeground(Color.WHITE);
	
		/*Options go here*/
		
		/*seeds
		CAPTURE
		AVALANCHE
		playUntilAll (t or false) - if false, playUntilMost
		ComputerPlayer1 and ComputerPlayer2
		playerOneName, playerTwoName;
		themes*/
		
		//Game mode
		
		JRadioButton cptr = new JRadioButton("Capture", true);
		JRadioButton avlnch = new JRadioButton("Avalanche", false);
		
		add(cptr);
		add(avlnch);
		
		ButtonGroup mode = new ButtonGroup();
		
		mode.add(cptr);
		mode.add(avlnch);
		
		cptr.addItemListener(new ItemStateChanged()
			public void itemStateChanged(ItemEvent event)
			{
				CAPTURE = true;
				AVALANCHE = false;
			}
		);

		avlnch.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				CAPTURE = false;
				AVALANCHE = true;
			}
		);
		
		//Play until 
			//-all seeds are captured
			//-most seeds are captured
		JRadioButton mostseed = 
				new JRadioButton("Most seeds are captured", true);
		JRadioButton allseed = 
				new JRadioButton("All seeds are captured", false);
		
		add(mostseed);
		add(allseed);
		
		ButtonGroup playuntil = new ButtonGroup();
		
		playuntil.add(mostseed);
		playuntil.add(allseed);

		mostseed.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				playUntilAll = false;
			}
		);
		allseed.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				playUntilAll = true;
			}
		);
		
		//Player names
		//text fields
		
		
		//Computer is:
			//-Player 1
			//-Player 2 (default?)
			//-Off
		JRadioButton cp1 = new JRadioButton("Player 1", false);
		JRadioButton cp2 = new JRadioButton("Player 2", true);
		JRadioButton cpoff = new JRadioButton("Off", false);
		
		add(cp1);
		add(cp2);
		add(cpoff);
		
		ButtonGroup comp = new ButtonGroup();
		
		comp.add(cp1);
		comp.add(cp2);
		comp.add(cpoff);
		
		cp1.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				ComputerPlayer1 = true;
				ComputerPlayer2 = false;
			}
		);
		cp2.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				ComputerPlayer1 = false;
				ComputerPlayer2 = true;
			}
		);
		cpoff.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				ComputerPlayer1 = false;
				ComputerPlayer2 = false;
			}
		);

		//Theme
		
		JRadioButton matbutt = new JRadioButton("Mat", true);
		JRadioButton candybutt = new JRadioButton("Candy", false);
		JRadioButton beachbutt = new JRadioButton("Beach", false);
		JRadioButton horrorbutt = new JRadioButton("Horror", false);
		
		add(matbutt);
		add(candybutt);
		add(beachbutt);
		add(horrorbutt);
		
		ButtonGroup theme = new ButtonGroup();
		
		theme.add(matbutt);
		theme.add(candybutt);
		theme.add(beachbutt);
		theme.add(horrorbutt);
		
		//button bounds
		matbutt.setBounds(50,50,75,75);
		
		
		matbutt.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				bg = "mat";
			}
		);

		candybutt.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				bg = "candy";
			}
		);

		beachbutt.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				bg = "beach";
			}
		);

		horrorbutt.addItemListener(new ItemState Changed()
			public void itemStateChanged(ItemEvent event)
			{
				bg = "horro";
			}
		);

		matbutt.setVisible(true);
		candybutt.setVisible(true);
		beachbutt.setVisible(true);
		horrorbutt.setVisible(true);
		
		/*End options*/
		
		startGame = new JButton("Start Game");
		startGame.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
		
					startGame.setVisible(false);
					
					try
					{
						setContentPane(new Background(/*string*/));
					}
					catch(IOException event)
					{
						event.printStackTrace();						
					}
					
					
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
					
					
					for (int i = 0; i < arr.length; i++)
					{
						add(slot[i]);
						slot[i].setVisible(true);
					}
					
					
				}
			});

		startGame.setBounds(250,300,150,75);
		add(startGame);
		startGame.setVisible(true);
		
		// used to determine the positions of the buttons (hollows) on the screen
		int yslot = 165;
		int xslot = 225;
		
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
				arr[i] = 0;
			else
				arr[i] = seeds;		
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
			
			
		
		// initialize array of buttons
		slot = new JButton[size];
		

		// declare JLabel for displaying whose turn it is
		turn = new JLabel();
		
		// set label properties for displaying whose turn it is
		turn.setBounds(20,540,300,150);
		turn.setForeground(Color.WHITE);
		turn.setFont(new Font("Serif", Font.BOLD, 40));
		turn.setText("Player " + Player + "'s turn");
		//add(turn);
		//turn.setVisible(false);
		
		
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
				
			// no action listener for slots at the ends of the board
			if (i != pl1_store && i != pl2_store)
			{
				slot[i] = new JButton(arr[i]+"");
				slot[i].setBounds(xslot,yslot,50,50);
				slot[i].addActionListener( this );
			}
			else
			{
				slot[i] = new JButton(arr[i]+"");
				
				if (i == pl2_store)
					slot[i].setBounds(255,115,140,40);
				else if (i == pl1_store)
					slot[i].setBounds(255,510,140,40);
				
			}
			
			// add each button to the JFrame
			//add(slot[i]);
			//slot[i].setVisible(false);
			
			
			if (i != pl1_store && i != pl2_store)
			{
				if (i < (pl1_store - 1))
					yslot += 55;
				else
					yslot -= 55;
				
				if (i == (pl1_store - 1))
				{
					yslot = 440;
					xslot = 380;
				}
			}
					
		}
		

	// PLAY AGAIN ACTION LISTENER
	// button for restarting game (do not set to visible unless GameOver == true)
		PlayAgain = new JButton("Play Again");
		PlayAgain.addActionListener(
			new ActionListener()
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

					
					
					// set visibility to false if clicked
					b.setVisible(false);
				}
			});
		
		PlayAgain.setBounds(20,300,150,75);
		//add(PlayAgain);
		//PlayAgain.setVisible(false);
		
		
	// UNDO MOVE ACTION LISTENER
	// button for undoing a player's move (do not set to visible unless a player has taken a turn)
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
					
					if (Player == 1)
						playerOne.freeTurn = true;
					else
						playerTwo.freeTurn = true;
					
					commitMove.setVisible(false);
					committed = true;
					save = true;

				}
			});

		undoMove.setBounds(20,200,150,75);
		//add(undoMove);
		//undoMove.setVisible(false);
		

	// COMMIT MOVE ACTION LISTENER
	// button for making a player's move official (do not set to visible unless the player has taken a turn)
		commitMove = new JButton("Commit Move");
		commitMove.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JButton b = (JButton)(e.getSource());
					boolean checkWin;
					
					if (Player == 1 && endTurn== true)
					{
						Player = 2;
						
						// display whose turn it is
						turn.setText("Player " + Player + "'s turn");
					}
					else if (Player == 2 && endTurn== true)
					{
						Player = 1;
						
						// display whose turn it is
						turn.setText("Player " + Player + "'s turn");
					}

					
					// determine if someone wins
					checkWin = CheckForWinner(b);


					/*	IN PROGRESS
					
						THIS IS WHERE WE INVOKE A CLICK FOR COMPUTER PLAYER IF COMPUTER PLAYER IS PLAYER 2
					*/
					
					int index;
					int s;
				
					if (!(Winner()))
					{
						if ((Player == 2 && ComputerPlayer2 == true) || (Player == 1 && ComputerPlayer1 == true))
						{
							// let the computer keep taking a turn while the last seed lands inside it's store
							do{
								//delay();
								
								
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

								redrawTextonButtons();

								System.out.println("Computer lands on index: " + index);
							
								if (index == s)
									System.out.println("\t\t GO AGAIN");
								
								
								checkWin = CheckForWinner(b);
								
							
							} while (index == s && checkWin == false);
							
							//endTurn = true;
							if (!checkWin)
								commitMove.doClick();
							
						}
				}
					
				}
			
		
				public boolean CheckForWinner(JButton b)
				{
					boolean c = Winner();
					if (c)
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
					save = true;	

					return c;
					
				}
				
	
			
			});
		
		commitMove.setBounds(20,100,150,75);
		//add(commitMove);
		//commitMove.setVisible(false);
		
		
		/*	IN PROGRESS
					
			THIS IS WHERE WE INVOKE A CLICK FOR COMPUTER PLAYER IF COMPUTER PLAYER IS PLAYER 1
		*/
	
		if (Player == 1 && ComputerPlayer1 == true)
		{
			// let the computer take a turn at the beginning of the game
			ComputerPlayer1TakeTurn();			
		}
		

			
			
			

	}

	
	public void ComputerPlayer1TakeTurn()
	{
		int index;
		
		do{
				//delay();
			
				index = playerOne.takeTurn(-1, -1, arr, slot, CAPTURE, AVALANCHE);
			
				slot[index].doClick();
				redrawTextonButtons();
			
			
			} while (index == playerOne.store);
			
			Player = 2;
						
			// display whose turn it is
			turn.setText("Player " + Player + "'s turn");
		
		
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
				if (CAPTURE)
				{
					for (int i = 0;  i < arr.length; i++)
					{
						saveArr[i] = arr[i];
					}
				}
				
				// if playing in avalanche mode and the button has been clicked by the user
				// do not save the array if the doClick() function was invoked by the program
				// otherwise saveArr would not save the original array
				if (AVALANCHE && save)
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
					redrawTextonButtons();
				}
				else
				{
					l = playerTwo.takeTurn(hand, l, arr, slot, CAPTURE, AVALANCHE);
					redrawTextonButtons();
				}

				undoMove.setVisible(true);
				commitMove.setVisible(true);
				
				
				
				// maybe use something like 
				// if (playerOne.turn) where turn is true if it is this player's turn
				if ((l == playerOne.store && Player == 1) || (l == playerTwo.store && Player == 2))
				{
					endTurn= false;
				}
	
				redrawTextonButtons();			
				
				committed = false;
				
			}
			
			if ((l == playerOne.store && Player == 1) || (l == playerTwo.store && Player == 2))
			{
				endTurn= false;
			}
		

		
		}
		
	}
	

	public void delay()
	{
		// used to delay when computer is choosing a hollow
		
		// delay for 1 second
		try
		{
			Thread.sleep(1000);
		}
		catch(InterruptedException p)
		{
			p.printStackTrace();
		}
		
	}
	
	/*
	public void CheckForWinner()
	{
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
		save = true;	

		
		
	}
	
	*/
	
	
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
		else
		{
			return playUntilMost();
		}
		


	}
	
	
	public boolean playUntilMost()
	{
		if (playerOne.points > (totalSeeds/2))
		{
			winner = 1;
			GameOver = true;
			return true;
		}
		else if (playerTwo.points > (totalSeeds/2))
		{
			winner = 2;
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
			redrawTextonButtons();
			
			
			if (arr[playerOne.store] > arr[playerTwo.store])
			{
				winner = 1;
				GameOver = true;
			}
			else if (arr[playerOne.store] < arr[playerTwo.store])
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
			redrawTextonButtons();
			
			if (arr[playerOne.store] > arr[playerTwo.store])
			{
				winner = 1;
				GameOver = true;
			}
			else if (arr[playerOne.store] < arr[playerTwo.store])
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
	
	
	public void player1CollectSeeds()
	{
		// player one collects the remaining seeds on his/her side of the board
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
		
		turn.setText("Player " + Player + "'s turn");
		
		// draw the slots on the board
		for (int i = 0; i < size; i++)
		{
			slot[i].setText(arr[i]+"");				
		}
		
		
		GameOver = false;		// set to true once the game is over
		winner = 0;				// set winner to 1 for pl1_store, set to 2 for pl2_store
	
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
	

}



