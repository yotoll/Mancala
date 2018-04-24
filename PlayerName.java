import javax.swing.JTextField;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

public class PlayerName extends JFrame
{
	private String name;
	private JTextField input;
	
	public PlayerName()
	{
		input = new JTextField("Player 1's name");	
		
		input.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				name = input.getText();
				if(name.length() == 0)
				JOptionPane.showMessageDialog(null, "ERROR: Empty name slot",
						"Name input", JOptionPane.PLAIN_MESSAGE);
			}
			
		});
		add(input);
	}
	
}
