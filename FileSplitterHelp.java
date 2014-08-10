import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.io.*;

public class FileSplitterHelp extends JFrame implements ActionListener
{
	private JPanel helpPanel;

	private JPanel buttonPanel;

	private JPanel mainPanel;

	private JEditorPane helpPane;

	private JButton close;

	private JButton print;

	private HTMLPane pane;

	private boolean flag=true;

	public FileSplitterHelp()
	{
		super("File Splitter Help");
		setIconImage(SplitterResource.getIcon("properties.FS","HELP_ICON").getImage());

		mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

		helpPanel=new JPanel();
		buttonPanel=new JPanel();

		try
		{
			pane=new HTMLPane(SplitterResource.getFile("properties.FS","HELP_FILE"));
			helpPanel.add(pane);
		}
		catch(Exception exc)
		{
			JOptionPane.showMessageDialog(this,"Help file is missing."+
			"\nPlease restore it in the help directory and then run Help Window"+
			"\nIf you have renamed the file HELP.html please restore the"+
			" name of file as HELP.html",
			"Error finding Help File",JOptionPane.ERROR_MESSAGE);
			flag=false;
		}

		print=new JButton("Print");
		buttonPanel.add(print);
		print.addActionListener(this);
		close=new JButton("Close");
		buttonPanel.add(close);
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				dispose();
			}
		});

		mainPanel.add(helpPanel);
		mainPanel.add(buttonPanel);

		setSize(600,500);
		getContentPane().add(mainPanel);
		pack();
		setVisible(flag);
		setResizable(false);
	}
	public static void main(String args[])
	{
		new FileSplitterHelp();
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();
		if(obj==print)
		{
			JOptionPane.showMessageDialog(this,"This feature hasn't been accomplished.Sorry for the inconvenience\nIf you have the solution please mail me.","Sorry",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}