import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.*;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JLabel;
import java.awt.Font;


public class Background extends JPanel
{
	private Image img;
	private JLabel pl1Side;
	private JLabel pl2Side;

	public Background(String x) throws IOException
	{
		setLayout(null);
		
		if(x == "mat")
		{
			img = ImageIO.read(getClass().getResource("mat.jpg"));
		}
		else if(x == "candy")
		{
			img = ImageIO.read(getClass().getResource("candy.jpg"));
		}
		else if(x == "beach")
		{
			img = ImageIO.read(getClass().getResource("beach.jpg"));
		}
		else if(x == "horror")
		{
			img = ImageIO.read(getClass().getResource("horror.jpg"));
		}
		else
		{
			img = ImageIO.read(getClass().getResource("mat.jpg"));
		}

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
			
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(4));
		
		// game board
		g.setColor(Color.BLACK);
		g.drawRoundRect(200,100,250,460,20,20);
			
	}	
	
}
