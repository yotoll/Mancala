import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;


public class RulesPopup extends JFrame
{
	JButton rules;
	
	public RulesPopup()
	{
		super("Rules of Mancala");
		
		rules = new JButton("Rules");
		
		rules.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog
				(null, "                                   How to Play Mancala\r\n" + 
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
					
		});
		
		rules.setBounds(300, 80, 400, 100);
		add(rules);
		rules.setVisible(true);
		
	}
	
}
