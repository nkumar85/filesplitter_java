import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

public class ChecksumWindow extends SplitterResource implements ActionListener,Runnable
{
	private JRadioButton crc;

	private JRadioButton md2;

	private JRadioButton md5;

	private JRadioButton sha;

	private JLabel path;

	private JLabel size;

	private JLabel value;

	private JButton browse;

	private JButton compute;

	private JButton copy;

	private JPanel mainPanel;

	private JPanel buttonPanel;

	private JPanel rbPanel;

	private JPanel labelPanel;

	private File file;

	private ButtonGroup  group;

	private OutputDir dir;

	public ChecksumWindow()
	{
		mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		//mainPanel.setPreferredSize(new Dimension(350,200));

		buttonPanel=new JPanel();
		rbPanel=new JPanel();
		labelPanel=new JPanel();
		labelPanel.setLayout(new GridLayout(0,1));

		group=new ButtonGroup();

		crc=createRadioButton("CRC32",'c',"CRC-32",null);
		md2=createRadioButton("MD2",'m',"MD2",null);
		md5=createRadioButton("MD5",'d',"MD5",null);
		sha=createRadioButton("SHA",'s',"SHA",null);
		path=new JLabel("File : ");
		value=new JLabel("Value : ");
		size=new JLabel("Size : ");

		crc.setSelected(true);

		crc.setActionCommand("CRC32");
		md2.setActionCommand("MD2");
		md5.setActionCommand("MD5");
		sha.setActionCommand("SHA");

		browse=createButton("BROWSE FOR FILE..",null,"Choose Source File",'b',this);
		compute=createButton("COMPUTE",null,"Compute File Vale",'p',this);
		copy=createButton("COPY VALUE",null,"Copy Value to Clipboard",'v',this);

		dir=new OutputDir("Choose File",JFileChooser.FILES_ONLY);

		group.add(crc);
		group.add(md2);
		group.add(md5);
		group.add(sha);

		rbPanel.add(crc);
		rbPanel.add(md2);
		rbPanel.add(md5);
		rbPanel.add(sha);

		labelPanel.add(path);
		labelPanel.add(size);
		labelPanel.add(value);

		buttonPanel.add(browse);
		buttonPanel.add(compute);
		buttonPanel.add(copy);

		mainPanel.add(rbPanel);
		mainPanel.add(labelPanel);
		mainPanel.add(buttonPanel);
	}

	public void showWindow(Component parent)
	{
		Object ob[]=new Object[]{"Close"};

		int j=JOptionPane.showOptionDialog(parent, //component to block
		mainPanel,// add this to OptionDialog
		"Check File Value",//Title of Dialog
		JOptionPane.DEFAULT_OPTION,//Option type
		JOptionPane.DEFAULT_OPTION,//Message type
		null,//Optional Icon
		ob,// The array of String which will be transformed to buttons
		ob[0]);// The default button to be focused.

	}
	public static void main(String []args)
	{
		new ChecksumWindow().showWindow(null);
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();

		if(obj==browse)
		{
			int i=dir.showOpenDialog(mainPanel);
			if(i==JFileChooser.APPROVE_OPTION)
			{
				this.file=dir.getSelectedFile().getAbsoluteFile();
				path.setText("File : "+file.toString());
				NumberFormat format=NumberFormat.getInstance();
				String val=format.format(file.length());
			    size.setText("Size : "+val+" bytes");
			    value.setText("Value : ");
			}
		}

		if(obj==compute)
		{
			value.setText("Value : Calculating Please Wait..........");
			Thread tr=new Thread(this);
			tr.start();
		}

		if(obj==copy)
		{
			if(!value.getText().equalsIgnoreCase("Value : "))
			copyToClipboard(value.getText()+" ("+group.getSelection().getActionCommand()+")");
		}
	}
	public void run()
	{
		ButtonModel model=group.getSelection();
		String alg=model.getActionCommand();

		try
		{
			if(alg.equals("CRC32"))
			{
				value.setText("Value : "+CheckFile.getCRC(file.toString()));
			}
			else if(alg.equals("MD5")||alg.equals("MD2"))
			{
				value.setText("Value : "+CheckFile.getMD(file.toString(),alg));
			}
			else if(alg.equals("SHA"))
			{
				value.setText("Value : "+CheckFile.getSHA(file.toString()));
			}

		}
		catch(IOException exc)
		{
			JOptionPane.showMessageDialog(mainPanel,exc,"Error",JOptionPane.ERROR_MESSAGE);
			value.setText("Value : ");
		}

		catch(NullPointerException exc)
		{
			Object ob=exc+"\nNo File Specified..";
			JOptionPane.showMessageDialog(mainPanel,ob,"Error",JOptionPane.ERROR_MESSAGE);
			value.setText("Value : ");
		}
	}
}