import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class FileSplitterGUI extends SplitterResource implements ActionListener
{


	private static JFrame frame;

	private JPanel mainPanel;

	private JMenuBar menubar;

	private JMenu fileMenu;
	private JMenu helpMenu;

	private JMenuItem exit;
	private JMenuItem chcrc;
	private JMenuItem merge;
	private JMenuItem help;
	private JMenuItem about;
	private JMenuItem splashi;

	/*first Panel*/
	static DnDPanel dndPanel;

	/*second Panel*/
	static ManualPanel manualPanel;

	/*Miscellaneous Panel*/
	static OptionPanel optionPanel;

	/*Progress Monitor as well the back-end*/
	static FileSplitter splitter;

	private Clock clock;

	private Splash splash;
	/*Default Settings*/

	private Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();

	private int height=screenSize.height;

	private int width=screenSize.width;

	private static long size=0;

	private static String redirection=null;

	private static boolean commandMode=false;//flag set to true in command mode

	private static boolean isSoundBoxOn=true;

	private static boolean openFolder=false;

    public FileSplitterGUI()
	{
		try{
		long y=System.currentTimeMillis();
		frame=new JFrame("File Splitter(BETA)");
		frame.setLocation(width/2-215,10);
		frame.setSize(446,530);
		frame.setVisible(!commandMode);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		splash=new Splash();
		splash.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if(splash.isShowing())
				{
					splash.setVisible(false);
				}
			}
		});
		splash.setVisible(!commandMode);
		mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
		frame.getContentPane().add(mainPanel);

		menubar=new JMenuBar();
		menubar.setLayout(new FlowLayout(FlowLayout.LEFT));

		fileMenu=createMenu("File",'i',menubar);
		helpMenu=createMenu("Help",'h',menubar);

		clock=new Clock();
		menubar.add(clock);

		merge=createMenuItem("Merge",KeyEvent.VK_M,fileMenu,this);
		chcrc=createMenuItem("Compute File",KeyEvent.VK_C,fileMenu,this);

		exit=createMenuItem("Exit",KeyEvent.VK_E,fileMenu,this);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.ALT_MASK));
		exit.setIcon(getIcon("properties.FS","EXIT_ICON"));

		help=createMenuItem("Help",KeyEvent.VK_H,helpMenu,this);
		help.setIcon(getIcon("properties.FS","HELP_ICON"));

		about=createMenuItem("About",KeyEvent.VK_A,helpMenu,this);
		about.setIcon(getIcon("properties.FS","ABOUT_ICON"));

		splashi=createMenuItem("Splash",KeyEvent.VK_S,helpMenu,this);

		mainPanel.add(menubar);
		frame.repaint();

		dndPanel=new DnDPanel();
		mainPanel.add(dndPanel.getMainComponent());
		frame.repaint();

		manualPanel=new ManualPanel();
		mainPanel.add(manualPanel.getMainComponent());
		frame.repaint();

		optionPanel=new OptionPanel(frame);
		mainPanel.add(optionPanel.getMainComponent());
		frame.repaint();

		splitter=new FileSplitter();
		mainPanel.add(splitter);
		frame.pack();
		//System.out.println(frame.getPreferredSize());
		splash.setVisible(false);
		}
		catch(Exception exc)
		{
			JOptionPane.showMessageDialog(frame,exc);
		}
	}

	public JMenu createMenu(String name,JComponent c)
	{
		JMenu menu=new JMenu(name);
		c.add(menu);
		return menu;
	}
	public static void main(String args[])
	{
		//If no arguments start the window
		if(args.length==0)
		{
			FileSplitterGUI fsgui=new FileSplitterGUI();
		}
		else
		{
			commandMode=true;
			new FileSplitterGUI();
			if(args[0].equals("-help"))
			{
				printUsage();
				System.exit(0);
			}
			try
			{
				String source=args[0];
				//check for the existence of source file.
				if(!new File(source).exists())
				{
					System.out.println("No Such file exists\n");
					printUsage();
					System.exit(1);
				}
				size=Long.parseLong(args[1]);
				System.out.println(size);
				if(args.length==3)
				{
					redirection=args[2];
					if(!new File(args[2]).exists())
					{
						System.out.println("Output Path doesn't exists.Redirecting to default directory...");
						redirection=new File(source).getParentFile().toString();
					}
				}
				else
				{
					redirection=new File(source).getParentFile().toString();
				}
				System.out.println(redirection);
				System.out.println(source);
				splitFile(new File(source));
			}
			catch(Exception exc)
			{
				printUsage();
				System.exit(1);
			}
		}
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();
		if(obj==help)
		{
			FileSplitterHelp fsh=new FileSplitterHelp();
		}
		if(obj==about)
		{
			AboutMessage message=new AboutMessage(frame);
		}
		if(obj==exit)
		{
			System.exit(0);
		}
		if(obj==splashi)
		{
			splash.setVisible(true);
		}
		if(obj==chcrc)
		{
			ChecksumWindow window=new ChecksumWindow();
			window.showWindow(frame);
		}
	}
	public static void splitFile(File file)throws IOException,InterruptedException
	{
		size=dndPanel.getBatchSize();
		redirection=optionPanel.getRedirectionPath();
		if(redirection==null)
		redirection=file.getParentFile().toString();
		//System.out.println(size);
		//System.out.println(file.length());
		//System.out.println(redirection);
		if(commandMode)
		{
			BEEP=false;
			OPEN_FOLDER=false;
		}

		if(size>=file.length())
		{
			if(!commandMode)
			{
				JOptionPane.showMessageDialog(dndPanel.getMainComponent(),"Split Size is>=Original File Size","Message",JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				System.out.println("Split Size is>=Original File Size");
			}
			if(commandMode)
			System.exit(0);
		}

		else if(redirection!=null&&size>0)
		{
			splitter.splitFile(file.getAbsoluteFile(),new File(redirection),size);
		}

		if(OPEN_FOLDER)
		{
			Runtime.getRuntime().exec("explorer "+redirection);
		}
	}
	public static void printUsage()
	{
		String commandlineOptions="\n\t\t*************USAGE************\n";
		String usage="java -jar FileSplitter.jar <source_file> <Split Size> <Destination_Path>\n";
		String comment="The last argument is optional.\nIf Nothing specified in last argument the default directory in which the source file resides is chosen as Destination.";
		String note="\n Note: you need to type full file paths for Source as well as Destination.";
		String examples="\n example 1: java -jar FileSplitter.jar <drive>\\My_Folder\\Myfile 12345\n example 2: java -jar FileSplitter.jar <drive>:\\My_Folder\\Myfile 12345 <Drive>:\\MyOutputPath";
		System.out.println(commandlineOptions+usage+comment+note+examples);
	}
}