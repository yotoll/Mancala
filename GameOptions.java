

import java.awt.event.*;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.lang.Thread;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Graphics;
import java.awt.Image;



class GameOptions extends JFrame
{
	
	// radio buttons for options layout
	private JRadioButton cptr;
	private JRadioButton avlnch;
	private JRadioButton mostseed;
	private JRadioButton allseed;
	private JRadioButton cp1;
	private JRadioButton cp2;
	private JRadioButton cpoff;
	private JRadioButton matbutt;
	private JRadioButton candybutt;
	private JRadioButton beachbutt;
	private JRadioButton horrorbutt;
	
	// button group for grouping the radio buttons
	private ButtonGroup mode;							// for choosing between capture mode and avalanche mode
	private ButtonGroup playuntil;						// for choosing between play until all or play until most
	private ButtonGroup comp;							// for choosing whether or not the user wants to play with computer
	
	
	// buttons for starting the game, opening the game options layout, viewing the rules of the game and ending the game
	//		(respectively)
	private JButton startGame;
	private JButton GameOptions;
	private JButton rules;
	private JButton endGame;
	
	
	// JlLables for labeling each set of radio buttons
	private JLabel Mode, PlayUntil, ComputerPlayer, Theme;
	private JLabel Options;
	private JLabel EnterName1;
	private JLabel EnterName2;
	
	
	// variables to be passed into the Game constructor
	private boolean CAPTURE = true;
	private boolean AVALANCHE = false;
	private boolean playUntilAll;
	private boolean ComputerPlayer1;
	private boolean ComputerPlayer2;
	private String bg;
	private JTextField player1;
	private JTextField player2;
	
	
	private Clip clip;			// for playing music
	private MainMenu menu;		// JPanel to display main menu background images
	private Game mancala;		// JPanel for displaying the game / game logic
	
	public GameOptions() throws IOException
	{
		
		super("MANCALA");
		setLayout(null);
		
		
		// set content pane to main menu
		try
		{
			menu = new MainMenu(true);
			setContentPane(menu);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		// initialize start game button located on options layout
		startGame = new JButton("Start Game");
		startGame.addActionListener(new StartGame());
		startGame.setBounds(500,540,150,75);
		add(startGame);
		startGame.setBackground(Color.RED);
		startGame.setForeground(Color.WHITE);
		startGame.setFont(new Font("Serif", Font.BOLD, 20));
		
		
		
		// button for ending the game and returning to the main menu
		endGame = new JButton("End Game");
		endGame.addActionListener(new EndGame());				
		endGame.setBounds(500,20,150,50);
		add(endGame);
		endGame.setBackground(Color.WHITE);
		endGame.setForeground(Color.BLACK);
		endGame.setFont(new Font("Serif", Font.BOLD, 20));
		
		
		// mode radio buttons
		cptr = new JRadioButton("Capture", true);
		avlnch = new JRadioButton("Avalanche", false);		
		add(cptr);
		add(avlnch);
		
		// mode button group
		mode = new ButtonGroup();
		mode.add(cptr);
		mode.add(avlnch);
		
		// add action listeners to mode radio buttons
		cptr.addItemListener(new ModeChange('c'));
		avlnch.addItemListener(new ModeChange('a'));
		
	
		// play until radio buttons
		mostseed = new JRadioButton("Most tokens are captured", true);
		allseed = new JRadioButton("All tokens are captured", false);
		add(mostseed);
		add(allseed);
		
		// play until button group
		playuntil = new ButtonGroup();
		playuntil.add(mostseed);
		playuntil.add(allseed);

		// add item listeners to play until radio buttons
		mostseed.addItemListener(new PlayUntilChange('m'));
		allseed.addItemListener(new PlayUntilChange('a'));
		
		
		// computer player radio buttons
		cp1 = new JRadioButton("Player 1", false);
		cp2 = new JRadioButton("Player 2", false);
		cpoff = new JRadioButton("Off (two human players)", true);
		add(cp1);
		add(cp2);
		add(cpoff);
		
		// computer player button group
		comp = new ButtonGroup();
		comp.add(cp1);
		comp.add(cp2);
		comp.add(cpoff);
		
		
		// add action listeners to computer player radio buttons
		cp1.addItemListener(new ComputerChange(1));
		cp2.addItemListener(new ComputerChange(2));
		cpoff.addItemListener(new ComputerChange(0));

		
		
		// Theme radio buttons
		matbutt = new JRadioButton("African Mat", true);
		candybutt = new JRadioButton("Candy", false);
		beachbutt = new JRadioButton("Beach", false);
		horrorbutt = new JRadioButton("Horror", false);
		add(matbutt);
		add(candybutt);
		add(beachbutt);
		add(horrorbutt);
		
		// theme button group
		ButtonGroup theme = new ButtonGroup();
		theme.add(matbutt);
		theme.add(candybutt);
		theme.add(beachbutt);
		theme.add(horrorbutt);
		
		// add item listeners to theme radio buttons
		matbutt.addItemListener(new ThemeChange('m'));
		candybutt.addItemListener(new ThemeChange('c'));
		beachbutt.addItemListener(new ThemeChange('b'));
		horrorbutt.addItemListener(new ThemeChange('h'));
		
	

		// ADD LABELS, BOUNDARIES, FONTS
		int yPos = 40;
		
		Options = new JLabel();
		Options.setBounds(250,-20,300,150);
		Options.setForeground(Color.BLACK);
		Options.setFont(new Font("Serif", Font.BOLD, 35));
		Options.setText("OPTIONS");
		add(Options);
		
		Theme = new JLabel();
		Theme.setBounds(20,yPos,300,150);
		Theme.setForeground(Color.BLACK);
		Theme.setFont(new Font("Serif", Font.BOLD, 28));
		Theme.setText("Themes:");
		add(Theme);
			
			
		ComputerPlayer = new JLabel();
		ComputerPlayer.setBounds(20,yPos + 90,300,150);
		ComputerPlayer.setForeground(Color.BLACK);
		ComputerPlayer.setFont(new Font("Serif", Font.BOLD, 28));
		ComputerPlayer.setText("Computer:");
		add(ComputerPlayer);
		
		PlayUntil = new JLabel();
		PlayUntil.setBounds(20,yPos + 190,300,150);
		PlayUntil.setForeground(Color.BLACK);
		PlayUntil.setFont(new Font("Serif", Font.BOLD, 28));
		PlayUntil.setText("Play Until:");
		add(PlayUntil);
		PlayUntil.setVisible(false);
		
		
		// set bounds, colors and fonts for theme buttons
		matbutt.setBounds(50,yPos + 90,165,50);
		beachbutt.setBounds(220,yPos + 90,110,50);
		candybutt.setBounds(340,yPos + 90,110,50);
		horrorbutt.setBounds(460,yPos + 90,110,50);
		matbutt.setForeground(Color.BLACK);
		beachbutt.setForeground(Color.BLACK);
		candybutt.setForeground(Color.BLACK);
		horrorbutt.setForeground(Color.BLACK);
		matbutt.setFont(new Font("Serif", Font.BOLD, 24));
		beachbutt.setFont(new Font("Serif", Font.BOLD, 24));
		candybutt.setFont(new Font("Serif", Font.BOLD, 24));
		horrorbutt.setFont(new Font("Serif", Font.BOLD, 24));
		matbutt.setOpaque(false);
		horrorbutt.setOpaque(false);
		candybutt.setOpaque(false);
		beachbutt.setOpaque(false);
		
		
		// set bounds, colors and fonts for cp1 cp2 and cpoff radio buttons
		cp1.setBounds(50,yPos + 180,150,50);
		cp2.setBounds(220,yPos + 180,150,50);
		cpoff.setBounds(380,yPos + 180,300,50);
		cp1.setForeground(Color.BLACK);
		cp2.setForeground(Color.BLACK);
		cpoff.setForeground(Color.BLACK);
		cp1.setFont(new Font("Serif", Font.BOLD, 24));
		cp2.setFont(new Font("Serif", Font.BOLD, 24));
		cpoff.setFont(new Font("Serif", Font.BOLD, 24));
		cp1.setOpaque(false);
		cp2.setOpaque(false);
		cpoff.setOpaque(false);
		
		
		
		// set bounds, colors and fonts for mostseed and allseed radio buttons
		mostseed.setBounds(50,yPos + 280,290,50);
		allseed.setBounds(380,yPos + 280,290,50);
		mostseed.setForeground(Color.BLACK);
		allseed.setForeground(Color.BLACK);
		mostseed.setFont(new Font("Serif", Font.BOLD, 24));
		allseed.setFont(new Font("Serif", Font.BOLD, 24));
		mostseed.setOpaque(false);
		allseed.setOpaque(false);
	
		
		
		// add labels for each set of radio buttons
		//private JLabel Mode, PlayUntil, ComputerPlayer, Theme;
		Mode = new JLabel();
		Mode.setBounds(20,yPos + 285,300,150);
		Mode.setForeground(Color.BLACK);
		Mode.setFont(new Font("Serif", Font.BOLD, 28));
		Mode.setText("Game Mode:");
		add(Mode);
		Mode.setVisible(false);
		
		// set bounds, colors and fonts for mostseed and allseed radio buttons
		cptr.setBounds(50,yPos + 375,150,40);
		avlnch.setBounds(220,yPos + 375,150,40);
		cptr.setForeground(Color.BLACK);
		avlnch.setForeground(Color.BLACK);
		cptr.setFont(new Font("Serif", Font.BOLD, 24));
		avlnch.setFont(new Font("Serif", Font.BOLD, 24));
		cptr.setOpaque(false);
		avlnch.setOpaque(false);
		
		
		EnterName1 = new JLabel("");
		EnterName1.setBounds(90,yPos + 380,300,150);
		EnterName1.setForeground(Color.BLACK);
		EnterName1.setFont(new Font("Serif", Font.BOLD, 28));
		EnterName1.setText("Player one name:");
		add (EnterName1);
		EnterName1.setVisible(false);
		
		// add the player 1 and player 2 name fields
		player1 = new JTextField(2);
		player1.setText("Player 1");
		player1.setBounds(110,yPos + 480,340,30);
		player1.setFont(new Font("Serif", Font.PLAIN, 18));
		player1.setVisible(false);
		add(player1);
		
		
		yPos += 10;
		
		EnterName2 = new JLabel("");
		EnterName2.setBounds(90,yPos + 450,300,150);
		EnterName2.setForeground(Color.BLACK);
		EnterName2.setFont(new Font("Serif", Font.BOLD, 28));
		EnterName2.setText("Player two name:");
		add(EnterName2);
		EnterName2.setVisible(false);
		
		player2 = new JTextField(2);
		player2.setText("Player 2");
		player2.setBounds(110,yPos + 550,340,30);
		player2.setFont(new Font("Serif", Font.PLAIN, 18));
		player2.setVisible(false);
		add(player2);
		
		
		GameOptions = new JButton("START");
		GameOptions.setBounds(230,300,180,75);
		GameOptions.addActionListener(new Options());
		add(GameOptions);
		GameOptions.setBackground(Color.RED);
		GameOptions.setForeground(Color.WHITE);
		GameOptions.setFont(new Font("Serif", Font.BOLD, 20));
		
		
		
		rules = new JButton("HOW TO PLAY");
		rules.setBounds(230,400,180,75);
		rules.addActionListener(new Rules());
		add(rules);
		rules.setBackground(Color.RED);
		rules.setForeground(Color.WHITE);
		rules.setFont(new Font("Serif", Font.BOLD, 20));

		
		
		
		GameOptions.setVisible(true);
		matbutt.setVisible(false);
		candybutt.setVisible(false);
		beachbutt.setVisible(false);
		horrorbutt.setVisible(false);
		cptr.setVisible(false);
		avlnch.setVisible(false);
		mostseed.setVisible(false);
		allseed.setVisible(false);
		cp1.setVisible(false);
		cp2.setVisible(false);
		cpoff.setVisible(false);
		Options.setVisible(false);
		Theme.setVisible(false);
		ComputerPlayer.setVisible(false);
		PlayUntil.setVisible(false);
		Mode.setVisible(false);
		startGame.setVisible(false);
		endGame.setVisible(false);
		
		
		// play start up music
		playMusic("AdventureLand.wav");
		
		

	}
	
	
	public void playMusic(String filename)
	{
		try
		{
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(getClass().getResource(filename)));
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);	
		}
		catch(LineUnavailableException ex)
		{
			ex.printStackTrace();			
		}
		catch(UnsupportedAudioFileException ex)
		{
			ex.printStackTrace();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	
	// listener for ending game and returning to main menu
	private class EndGame implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// warn user that data will be lost
			int reply = JOptionPane.showConfirmDialog(GameOptions.this, "Are you sure you want to end game? All data will be lost.",  
				"Warning", JOptionPane.YES_NO_OPTION);
			
			
			if (reply == JOptionPane.YES_OPTION)
			{	
				mancala.setVisible(false);
				getContentPane().remove(mancala);
				
				menu.showImage2 = true;
				setContentPane(menu);
				
				GameOptions.setVisible(true);
				
			}
			
			// change music to original start up if the player was using horror theme
			if (bg == "horror")
			{
				clip.stop();
				playMusic("AdventureLand.wav");
			}

			
		}
		
	}
	
	
	// action listener for viewing the options layout
	private class Options implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{	
		
			// keep content pain but repaint so that the title image does not display
			menu.showImage2 = false;
			
		
			// set radio buttons to be visible
			matbutt.setVisible(true);
			candybutt.setVisible(true);
			beachbutt.setVisible(true);
			horrorbutt.setVisible(true);
			cptr.setVisible(true);
			avlnch.setVisible(true);
			mostseed.setVisible(true);
			allseed.setVisible(true);
			cp1.setVisible(true);
			cp2.setVisible(true);
			cpoff.setVisible(true);
			Options.setVisible(true);
			Theme.setVisible(true);
			ComputerPlayer.setVisible(true);
			PlayUntil.setVisible(true);
			Mode.setVisible(true);
			startGame.setVisible(true);
			player1.setVisible(true);
			player2.setVisible(true);
			EnterName1.setVisible(true);
			EnterName2.setVisible(true);
			
			
			// set radio button default selections
			cptr.setSelected(true);
			avlnch.setSelected(false);
			mostseed.setSelected(false);
			allseed.setSelected(true);
			matbutt.setSelected(true);
			candybutt.setSelected(false);
			beachbutt.setSelected(false);
			horrorbutt.setSelected(false);
			cp1.setSelected(false);
			cp2.setSelected(false);
			cpoff.setSelected(true);
			
				
			player1.setText("Player 1");
			player2.setText("Player 2");
			
			
			GameOptions.setVisible(false);
			rules.setVisible(false);
			
			
			
		}
		
	}

	
	
	
	//Options menu
	private class ModeChange implements ItemListener
	{
		char mode;

		public ModeChange(char c){ mode = c; }

		public void itemStateChanged(ItemEvent event)
			{
				if(mode =='a')
				{
					CAPTURE = false;
					AVALANCHE = true;
				}
				else if(mode =='c')
				{
					CAPTURE = true;
					AVALANCHE = false;
				}
			}
	}

	
	// listener for initializing a new game
	
	private class StartGame implements ActionListener
	{
		
		public void actionPerformed(ActionEvent ev)
		{
			
			// do nothing if user has not entered a name
			String n1 = player1.getText();
			String n2 = player2.getText();
			
			
			if (n1.length() == 0)
			{
				JOptionPane.showConfirmDialog(GameOptions.this, "Please enter a name for player 1",  
				"Enter player name", JOptionPane.PLAIN_MESSAGE);
			}
			else if (n2.length() == 0)
			{
				JOptionPane.showConfirmDialog(GameOptions.this, "Please enter a name for player 2",  
				"Enter player name", JOptionPane.PLAIN_MESSAGE);
			}
				
			else
			{
				startGame.setVisible(false);
						
				try
				{
					mancala = new Game(bg, CAPTURE,AVALANCHE,playUntilAll,ComputerPlayer1,ComputerPlayer2,
										player1.getText(), player2.getText());
				}
				catch(IOException event)
				{
					event.printStackTrace();
				}
				
				
				GameOptions.setVisible(true);
				rules.setVisible(true);
				matbutt.setVisible(false);
				candybutt.setVisible(false);
				beachbutt.setVisible(false);
				horrorbutt.setVisible(false);
				cptr.setVisible(false);
				avlnch.setVisible(false);
				mostseed.setVisible(false);
				allseed.setVisible(false);
				cp1.setVisible(false);
				cp2.setVisible(false);
				cpoff.setVisible(false);
				Options.setVisible(false);
				Theme.setVisible(false);
				ComputerPlayer.setVisible(false);
				PlayUntil.setVisible(false);
				Mode.setVisible(false);
				startGame.setVisible(false);
				player1.setVisible(false);
				player2.setVisible(false);
				EnterName1.setVisible(false);
				EnterName2.setVisible(false);
				
				
				// if user selected horror theme change the music
				if (bg == "horror")
				{
					clip.stop();
					playMusic("Bones.wav");
				}
				
				
				mancala.add(endGame);
				mancala.validate();
				setContentPane(mancala);
				endGame.setVisible(true);
						
			}			
	
		}
	}
	
	
	
	private class PlayUntilChange implements ItemListener
	{
		char mode;

		public PlayUntilChange(char c){ mode = c; }

		public void itemStateChanged(ItemEvent event)
			{
				if(mode =='a')
				{
					playUntilAll = true;
				}
				else
					playUntilAll = false;
			}
	}

	private class ComputerChange implements ItemListener
	{
		int mode;

		public ComputerChange(int x){ mode = x; }

		public void itemStateChanged(ItemEvent event)
			{
				if(mode ==0)
				{
					ComputerPlayer1 = false;
					ComputerPlayer2 = false;
				}
				else if(mode ==1)
				{
					ComputerPlayer1 = true;
					ComputerPlayer2 = false;
				}
				else
				{
					ComputerPlayer1 = false;
					ComputerPlayer2 = true;
				}
			}
	}

	private class ThemeChange implements ItemListener
	{
		char mode;

		public ThemeChange(char c){ mode = c; }

		public void itemStateChanged(ItemEvent event)
			{
				if(mode =='b')
				{
					bg = "beach";
				}
				else if(mode =='c')
				{
					bg = "candy";
				}
				else if(mode == 'h')
				{
					bg = "horror";
				}
				else
				{
					bg = "mat";
				}
				
				
			}
	}
	
	
	
	private class Rules implements ActionListener
	{
		public void actionPerformed(ActionEvent ev)
		{
			JOptionPane.showMessageDialog
				(GameOptions.this, "                                   How to Play Mancala\r\n" + 
						"\r\n" + 
						"Basics:\r\n" + 
						"     There are two players, one on each side of the\r\n" + 
						"board. There are four tokens in each of the six slots, \r\n" + 
						"and two stores on either side. \r\n" + 
						"     Players take turns picking up tokens and playing \r\n" + 
						"them on the board in a counterclockwise direction. A token \r\n" + 
						"is dropped in each slot as the player moves the tokens. When \r\n" + 
						"the player reaches their store, they place a token into it. \r\n" + 
						"If the last token they're holding is placed in the store, the\r\n" + 
						"player gains an additional turn. \r\n" + 
						"     If there are more seeds left after the player puts it\r\n" + 
						"into their store, they continue onto the opponent's side of \r\n" + 
						"the board counterclockwise until they run out of tokens. The\r\n" + 
						"player does not put a token into the opponent's store if they\r\n" + 
						"reach it. They continue onto their side of the board until \r\n" + 
						"they are out of tokens.\r\n" +
						"     The game is over when either most of the tokens on\r\n" + 
						"the board are captured or all tokens are captured.\r\n" + 
						"\r\n" + 
						"Capture Mode:\r\n" + 
						"     When a token lands in an empty slot and there are\r\n" + 
						"tokens in the slot on the opposite side, the player collects\r\n" + 
						"the tokens in both slots and puts them into their store.\r\n" + 
						"This does not grant the player another turn.\r\n" + 
						"\r\n" + 
						"Avalanche Mode:\r\n" + 
						"     When a token lands in a slot with one or more\r\n" + 
						"tokens, the player picks up all of the tokens and continues\r\n" + 
						"around the board until their last seed lands in their store\r\n" + 
						"or in an empty slot.",
						"Rules of Mancala", 
						JOptionPane.PLAIN_MESSAGE);
		}
			
	}
			
	
	
	
	
	
	
}
	
	
// for setting content pane with background images for main menu and options layouts
class MainMenu extends JPanel
{
	
	private Image img;
	private Image img2;
	//private JButton GameOptions;
	public boolean showImage2 = true;
	
	public MainMenu(boolean image2) throws IOException
	{
		showImage2 = image2;
		setLayout(null);
		img = ImageIO.read(getClass().getResource("options.jpg"));
		img2 = ImageIO.read(getClass().getResource("start.jpg"));
	}
	
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);			
		g.drawImage(img, 0, 0,700,700,null);
		
		if (showImage2)
			g.drawImage(img2, 100, 80, 460,160,null);
	}

	
}


















