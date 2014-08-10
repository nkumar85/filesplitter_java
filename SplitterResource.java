import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import javax.swing.*;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import java.net.URL;
import java.awt.datatransfer.*;

public class SplitterResource extends ListResourceBundle
{
	private final Object[][]resources={
		{"(250KB) E-Mail",new Long(250*1024)},
		{"(750KB) Web",new Long(750*1024)},
		{"720KB (Floppy Disk)",new Long(720*1024)},
		{"1.44MB (Floppy Disk)",new Long(1457000)},
		{"2.88MB (Floppy Disk)",new Long(2*1457000)},
		{"650MB (CD)",new Long(650*1024*1024)},
		{"bytes",new Long(1)},
		{"KB",new Long(1024)},
		{"MB",new Long(1024*1024)},
		{"GB",new Long(1024*1024*1024)}
							   };

	protected static boolean ON_TOP=false;

	public static boolean BEEP=true;

	protected static boolean OPEN_FOLDER=false;

	protected static boolean DELETE_SOURCE=false;

	protected static boolean NO_BATCH=false;

	protected static boolean REDIRECTION=false;


	public Object[][] getContents()
	{
		return resources;
	}

	public final Object[] getPredefinedKeys()
	{
		int length=resources.length-4;
		//neglect last four scaling factors
		Object[] obj=new Object[length];
		for(int i=0;i<length;i++)
		{
			obj[i]=resources[i][0];
		}
		return obj;
	}

	public final Object[] getScalingFactors()
	{
		int length=resources.length-4;
		Object obj[]=new Object[4];

		for(int i=0;length<10;length++,i++)
		obj[i]=resources[length][0];

		return obj;
	}

	public static ImageIcon getIcon(String name,String key)
	{
		ResourceBundle bundle=ResourceBundle.getBundle(name);
		URL url=ClassLoader.getSystemResource(bundle.getString(key));
		return new ImageIcon(url);
	}

	public static String getFile(String name,String key)
	{
		ResourceBundle bundle=ResourceBundle.getBundle(name);
	//	System.out.println(ClassLoader.getSystemResource(bundle.getString(key)).toString());
		return ClassLoader.getSystemResource(bundle.getString(key)).toString();
	}

	public JCheckBox createCheckBox(String name,
									char m,
									String tip,
									ActionListener listener
									)
	{
		JCheckBox cb=new JCheckBox(name);
		cb.setFocusPainted(false);
		cb.setMnemonic(m);
		cb.setToolTipText(tip);
		cb.addActionListener(listener);
		return cb;
	}

	public JMenu createMenu(String name,char mnemonic,JComponent c)
	{
		JMenu menu=new JMenu(name);
		menu.setMnemonic(mnemonic);
		c.add(menu);
		return menu;
	}

	public JMenuItem createMenuItem(String name,int acc,JComponent c,ActionListener listener)
	{
		JMenuItem item=new JMenuItem(name,acc);
		c.add(item);
		item.addActionListener(listener);
		return item;
	}

	public JRadioButton createRadioButton(String name,
									char m,
									String tip,
									ActionListener listener
									)
	{
		JRadioButton rb=new JRadioButton(name);
		rb.setFocusPainted(false);
		rb.setMnemonic(m);
		rb.setToolTipText(tip);
		rb.addActionListener(listener);
		return rb;
	}

	public JButton createButton(String name,
								ImageIcon ico,
								String tooltip,
								char mnemonic,
								ActionListener listener
								)
	{
		JButton but=new JButton(name,ico);
		but.setFocusPainted(false);
		but.setToolTipText(tooltip);
		but.setMnemonic(mnemonic);
		but.addActionListener(listener);
		return but;
	}

	public void copyToClipboard(String string)
	{
		if(string!=null)
		{
			Transferable stringTransfer=new StringSelection(string);
			Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
			clip.setContents(stringTransfer,null);
		}
	}
}
