import javax.swing.JWindow;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Frame;

public class Splash extends JWindow
{
	JLabel imageLabel;
	public Splash()
	{
		super();
		imageLabel=new JLabel();
		imageLabel.setIcon(SplitterResource.getIcon("properties.FS","SPLASH_ICON"));
		getContentPane().add(imageLabel);
		pack();
		setLocationRelativeTo(null);
	}
	public static void main(String args[])
	{
		new Splash();
	}
}