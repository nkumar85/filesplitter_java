import java.awt.*;
import javax.swing.*;

public class AboutMessage extends JDialog
{
	AboutTabbedPane pane;
	JPanel simple;
	public AboutMessage(Frame frame)
	{
		super(frame,"ABOUT",true);
		simple=new JPanel();
		pane=new AboutTabbedPane();
		simple.add(pane);
		getContentPane().add(simple);
		setSize(600,200);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String args[])
	{
		new AboutMessage(new JFrame());
	}

}