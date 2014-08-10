import javax.swing.*;
import javax.swing.border.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Arrays;
import java.io.File;
import java.io.IOException;
import java.io.FileFilter;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

/**
* A simple file tree.
* @author - Nandakumar
* <a href=mailto:nandakumar_nie@yahoo.com>mail</a>
*/

public class Folder_Tree extends SplitterResource implements TreeWillExpandListener,FileFilter,TreeSelectionListener,ActionListener
{
	private JTree tree;

	private DefaultMutableTreeNode []object;

	private DefaultMutableTreeNode rootNode;

	private DefaultMutableTreeNode desktop;

	private JPanel selectionPanel;

	private JLabel selectedPath;

	private JPanel buttonPanel;

	private JScrollPane treePane;

	private JScrollPane pathPane;

	private JPanel mainPanel;

	private JButton makeNewFolder;

	private JButton deleteFolder;

	private JButton copyClipboard;

	private JButton refresh;

	private JButton level_up;

	private JButton history;

	private JToolBar buttonBar;

	private Vector<DefaultMutableTreeNode> rootVector;

	private Vector<File> fileVector;

	private TreePath expandPath;

	private static final String tfn="Type Folder Name";

	private static final String folderBlank="Folder name can't be blank";

	private static final String invalidName="The name entered by you has"+
	" character(s) reserved by OS.\n"+
	" Please rectify folder name.\n"+
	" Invalid characters are \\/*?<>|\":";

	private static final String exists="The folder name that you have entered"+
	" is already existing.\n Please give a different name.";

	private static final String fail="Could not create folder."+
	" Make sure the disk is writable and properly configured.\n"+
	" If the disk is removable medium,"+
	" please insert the medium and then retry.";

	private static final String confirmDelete="Warning: Deleting folder is"+
	" destructive process in the sense \n"+
	" it is not dumped to Recycle Bin but deleted permanently from your system.\n"+
	" Are you sure you wan't to proceed ?";

	private static final ImageIcon FOLDER_ICON=getIcon("properties.FT","FOLDER_CLOSED");

	private static final ImageIcon DELETE_ICON=getIcon("properties.FT","FOLDER_DELETE");

	private static final ImageIcon REFRESH_ICON=getIcon("properties.FT","REFRESH");

	private static final ImageIcon CLIPBOARD_ICON=getIcon("properties.FT","COPY_CLIPBOARD");

	private static final ImageIcon UP_ICON=getIcon("properties.FT","UP_ONE_LEVEL");

	private static final ImageIcon HISTORY_ICON=getIcon("properties.FT","HISTORY_ICON");

	public Folder_Tree()
	{
		selectionPanel=new JPanel();
		buttonPanel=new JPanel();
		mainPanel=new JPanel();

		selectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonPanel.setBorder(new LineBorder(Color.blue,2,true));
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

		/* This Panel keeps track of the tree path selected*/

		selectedPath=new JLabel();
		selectionPanel.add(selectedPath);
		pathPane=new JScrollPane(selectionPanel);
		pathPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		pathPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//pathPane.setPreferredSize(new Dimension(100,30));

		/* The following is the folder selector tree*/

		//Root-Node for the disks

		rootNode=new DefaultMutableTreeNode("My Computer");
		desktop=new DefaultMutableTreeNode("Desktop");
		rootNode.add(desktop);
		//get all roots as reported by JVM
		File []roots=File.listRoots();
		//construct an array of the roots
		object=new DefaultMutableTreeNode[roots.length];
		rootVector=new Vector<DefaultMutableTreeNode>(roots.length);
		for(int i=0;i<roots.length;i++)
		{
			/**
			*Add root to itself as children so as to show Root Handles.
			*This is required to activate tree Listeners.
			*Instead of adding first set of children to roots,
			*I have simply filled the roots with itself
			*so that the roots are expanded only at runtime and not in constructor.
			*This reduces the time to load constructor because of no disk access
			*as well as frees disturbance of Floppy Drive.
			*You will also feel this disturbance after you restart
			*system from Hibernation and you had previously accessed Floppy Drive.
			*If you have wonderful suggestions regarding the topic or if you
			*have appreciation to my Idea please mail me to :
			*nandakumar.nie@gmail.com
			*/

			object[i]=new DefaultMutableTreeNode(roots[i]);
			object[i].add(new DefaultMutableTreeNode(roots[i].toString()));
			rootNode.add(object[i]);
			rootVector.add(object[i]);
		}
		fileVector=new Vector<File>();
		tree=new JTree(rootNode);
		DefaultTreeSelectionModel model=new DefaultTreeSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setSelectionModel(model);
		tree.setSelectionRow(2);
		tree.setToggleClickCount(1);
		selectedPath.setText(constructPath(tree.getSelectionPath().getPath()));
		expandPath=tree.getSelectionPath();
		tree.addTreeWillExpandListener(this);
		tree.addTreeSelectionListener(this);
		tree.setCellRenderer(new Renderer());
		tree.setShowsRootHandles(true);
		treePane=new JScrollPane(tree);
		treePane.setBorder(createBorder("File Tree"));
		treePane.setPreferredSize(new Dimension(350,220));

		/* Button to create new folder,deletion,refreshing tree*/

		makeNewFolder=createButton("",FOLDER_ICON,"Create New Folder in the Selected Path",'n',this);
		deleteFolder=createButton("",DELETE_ICON,"Delete the Selected Folder",'x',this);
		deleteFolder.setEnabled(false);
		copyClipboard=createButton("",CLIPBOARD_ICON,"Copy Selected Path to Clipboard",'c',this);
		refresh=createButton("",REFRESH_ICON,"Reload",'r',this);
		history=createButton("",HISTORY_ICON,"History",'h',this);
		level_up=createButton("",UP_ICON,"Up One Level",'u',this);

		buttonBar=new JToolBar("Folder Tasks");
		buttonBar.setRollover(true);
		buttonBar.setFloatable(false);

		buttonBar.add(makeNewFolder);
		buttonBar.add(deleteFolder);
		buttonBar.add(copyClipboard);
		buttonBar.add(refresh);
		buttonBar.add(history);
		buttonBar.add(level_up);

		buttonPanel.add(buttonBar);

		mainPanel.add(buttonPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(100,10)));
		mainPanel.add(treePane);
		mainPanel.add(Box.createRigidArea(new Dimension(100,10)));
		mainPanel.add(pathPane);
	}
	public static void main(String []args)
	{
		Folder_Tree test=new Folder_Tree();
		String str=test.showDialog(null);;
		System.out.println(str);
	}

	/*Whenever the parent is expanded the corresponding children are added
	by this function*/
	public void addChildren(DefaultMutableTreeNode node,String alter)throws IOException
	{
		Object path[]=node.getUserObjectPath();
		String str;
		if(alter==null)
		str=constructPath(path);
		else
		str=alter;
		File f=new File(str);
		File files[]=f.listFiles(this);
		DefaultMutableTreeNode nodes[];
		if(files!=null)
		{
			nodes=new DefaultMutableTreeNode[files.length];
			Arrays.sort(files);
			for(int k=0;k<files.length;k++)
			{
				nodes[k]=new DefaultMutableTreeNode(files[k].getName());
				node.add(nodes[k]);
			}
		}
	}

	/*Wrap the folder tree in a JOptionPane. This dialog returns the
	selected folder path when user clicks OK*/
	public String showDialog(Component parent)
	{
		Object ob[]=new Object[]{"OK","Cancel"};

		int j=JOptionPane.showOptionDialog(parent, //component to block
		mainPanel,// add this to OptionDialog
		"Folder Chooser",//Title of Dialog
		JOptionPane.DEFAULT_OPTION,//Option type
		JOptionPane.DEFAULT_OPTION,//Message type
		null,//Optional Icon
		ob,// The array of String which will be transformed to buttons
		ob[0]);// The default button to be focused.

		//Evaluate the condition depending on button clicked.

		switch(j)
		{
			case 0:
			String path=selectedPath.getText();
			String str=(path!=null&&path!="")?path:null;
			str=(path.equals("Desktop"))?System.getProperty("user.home")+"\\desktop":path;
			return str;

			case 1:
			return null;

			default:
			return null;
		}
	}

	/* ActionListeners for Buttons*/
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();

		if(obj==makeNewFolder)
		{
			//ask for a folder name
			String str=JOptionPane.showInputDialog(mainPanel,tfn,"Folder Name",JOptionPane.INFORMATION_MESSAGE);
			//If user clicked OK then check whether the name is blank.
			if(str!=null)
			{
				boolean flag=true;
				//If yes throw an error
				if(str.equals(""))
				{
					JOptionPane.showMessageDialog(mainPanel,folderBlank,"Error",JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					//Check whether the name contains characters reserved by OS
					boolean b=parse(str);
					//If Yes throw an error.
					if(!b)
					{
						JOptionPane.showMessageDialog(mainPanel,invalidName,"Error",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						try
						{
							//get path from label
							String path=selectedPath.getText();
							//create a file instance of path
							File f=new File(path+str);
							//throw an error if the directory exists.
							if(f.exists())
							{
								JOptionPane.showMessageDialog(mainPanel,exists,"Info",JOptionPane.INFORMATION_MESSAGE);
							}
							else
							{
								//else create a directory
								flag=f.mkdir();
								if(flag)
								{
									//If success update the corresponding parent with new Child.
									Object trees=tree.getSelectionPath().getLastPathComponent();
									DefaultMutableTreeNode node=(DefaultMutableTreeNode)trees;
									node.add(new DefaultMutableTreeNode(str));
									DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
									//reload the parent
									model.reload(node);
									tree.expandPath(tree.getSelectionPath());
								}
							}
							if(!flag)
							{
								//if error throw it
								JOptionPane.showMessageDialog(mainPanel,fail,"Error",JOptionPane.ERROR_MESSAGE);
							}
						}
						catch(Exception exc)
						{
							System.out.println(exc);
						}
					}
				}
			}
		}
		if(obj==deleteFolder)
		{
			//show confirmation dialog before deleting
			int option=JOptionPane.showConfirmDialog(mainPanel,confirmDelete,"Warning",JOptionPane.INFORMATION_MESSAGE);
			//If clicked yes
			if(option==JOptionPane.YES_OPTION)
			{
			  Object trees=tree.getSelectionPath().getLastPathComponent();
			  DefaultMutableTreeNode node=(DefaultMutableTreeNode)trees;
			  String path=selectedPath.getText();
			  //create file instance of selectedpath
			  File f=new File(path);
			  /*Java returns false if an attempt is made to delete folder
			  containing files. Hence I have included my subroutine to delete
			  the parent*/
			  boolean flag=removeFileTree(f);
			  if(flag)
			  {
					boolean int1=true;
					/* FileVector contains parent directories.Delete them all
					from last. This is because the last directories are children
					while the preceding are parents*/
					int size=fileVector.size();
					for(int i=size-1;i>=0;i--)
					{
						File file=(File)fileVector.get(i);
						boolean inter=file.delete();
						if(!inter)
						{
							JOptionPane.showMessageDialog(mainPanel,"Couldn't delete "+file,"Error",JOptionPane.ERROR_MESSAGE);
							int1=false;
						}
					}
					fileVector.removeAllElements();
					//Finally clear the vector
					DefaultMutableTreeNode parent=(DefaultMutableTreeNode)node.getParent();
					selectedPath.setText(constructPath(parent.getUserObjectPath()));
					if(!int1)
					parent.remove(node);
					DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
					model.reload(parent);
					model.reload(desktop);
					tree.expandPath(tree.getSelectionPath());
			  }
			  else
			  {
				  JOptionPane.showMessageDialog(mainPanel,"Coudn't delete "+f,"Error",JOptionPane.ERROR_MESSAGE);
			  }
		    }
		}
		if(obj==refresh)
		{
			//This is very simple
			DefaultTreeModel model=(DefaultTreeModel)tree.getModel();
			model.reload();
			if(!makeNewFolder.isEnabled())
			makeNewFolder.setEnabled(true);
			tree.setSelectionRow(2);
			selectedPath.setText(constructPath(tree.getSelectionPath().getPath()));
			this.expandPath=tree.getSelectionPath();
		}
		if(obj==copyClipboard)
		{
			//This is also very simple
			copyToClipboard(selectedPath.getText());
		}
		if(obj==level_up)
		{
			//Very simple!
			if(expandPath!=null)
			tree.collapsePath(expandPath.getParentPath());
		}
	}

	/* This was actually wasn't required. But because while collapsing
	path if the path is home Path or drive, then some of the buttons are
	to be disabled especially DeleteFolder Button*/
	public void treeWillCollapse(TreeExpansionEvent evt)
	{
		TreePath path=evt.getPath();
		this.expandPath=path;
		DefaultMutableTreeNode lastPath=(DefaultMutableTreeNode)path.getLastPathComponent();
		if(lastPath.equals(rootNode))
		{
			makeNewFolder.setEnabled(false);
			deleteFolder.setEnabled(false);
			level_up.setEnabled(false);
		}
		if(isRootFileNode(lastPath))
		deleteFolder.setEnabled(false);
	}

	/* Forget about look of the Folder_Tree. Without this routine
	everything is bald! This is the routine that does all expanding work*/
	public void treeWillExpand(TreeExpansionEvent evt)
	{
		TreePath path=evt.getPath();
		this.expandPath=path;
		DefaultMutableTreeNode lastPath=(DefaultMutableTreeNode)path.getLastPathComponent();
		Object[] obj=path.getPath();
		//If root then add drives from the Vector
		if(lastPath.equals(rootNode))
		{
			try
			{
				for(int j=0;j<object.length;j++)
				rootNode.add(object[j]);
				/* If you wan't to really know the implication of below
				three lines remove them and re-Run the Program*/
				makeNewFolder.setEnabled(false);
				deleteFolder.setEnabled(false);
				level_up.setEnabled(false);
			}
			catch(Exception exc)
			{
				System.out.println(exc);
			}
		}
		else
		{
			try
			{
				//If the path is root remove dummy node added earlier and add
				//true children
				if(isRootFileNode(lastPath))
				{
					lastPath.removeAllChildren();
					addChildren(lastPath,null);
				}

				//As well update the children with their children
				Enumeration next=lastPath.children();
				while(next.hasMoreElements())
				{
					DefaultMutableTreeNode dmt=(DefaultMutableTreeNode)next.nextElement();
					addChildren(dmt,null);
				}

			}
			catch(Exception exc)
			{
				System.out.println(exc);
			}
			makeNewFolder.setEnabled(true);
			level_up.setEnabled(true);
		}
	}

	/*TreeSelectionListener notifies the label from which the user path is
	referenced.*/
	public void valueChanged(TreeSelectionEvent evt)
	{
		TreePath path=tree.getSelectionPath();
		if(tree.getSelectionPath()==null)
		deleteFolder.setEnabled(false);
		if(path!=null)
		{
			Object obj[]=path.getPath();

			if(obj!=null)
			{

				selectedPath.setText(constructPath(path.getPath()));
				DefaultMutableTreeNode lastPath=(DefaultMutableTreeNode)path.getLastPathComponent();

				if(!(lastPath.equals(rootNode)||isRootFileNode(lastPath)))
				deleteFolder.setEnabled(true);
			}
			int i=obj.length-1;
			if(i==1&&obj[i].toString().equals("Desktop"))
			selectedPath.setText(getHomePath()+"\\desktop");
		}
	}

	/*Filter those files which are directories and not hidden*/
	public boolean accept(File f)
	{
		if(f.isDirectory()&&(!f.isHidden()))
		return true;

		return false;
	}

	/* This subroutine is required to supply the full path as String
	from the DefaultMutableTreeNode which returns an array of Object*/
	private String constructPath(Object path[])
	{
		String str="";
		if(path!=null)
		{
			for(int i=1;i<path.length;i++)
			{
				if(path[i]!=null)
				{
					if(i==1)
					str+=path[i].toString();
					else
					str+=path[i].toString()+"\\";
				}
			}
		}
		return str;
	}

	/*Check whether the node is root Node*/
	private boolean isRootFileNode(DefaultMutableTreeNode node)
	{
		return rootVector.contains(node);
	}

	/*The parsing of Folder Name is required to warn the user not to enter
	name which has characters reserved by the OS[\/:*?"<>|]*/
	protected boolean parse(String str)
	{
		if(str==null)
		return false;

		if(str.equals(""))
		return false;

		char ch[]=str.toCharArray();
		for(int i=0;i<ch.length;i++)
		{
			if(isReservedChar(ch[i]))
			return false;
		}
		return true;
	}

	/*Checks whether the character is reserved by OS*/
	protected boolean isReservedChar(char c)
	{
		if(c=='\\'||c=='/'||c=='*'||c=='?'||c=='"'||c=='<'||c=='>'||c=='|'||c==':')
		return true;

		return false;
	}

	/* This subroutine is simple. This actually the children if they are
	files and if they are directories they are updated in the fileVector.
	This is a recursive subroutine. First the file is checked for its children
	If no children, the task is simple. If it has children then call the function
	again but before calling, the fileVector has to be updated with parent
	directory. Finally the directories in fileVector are to be removed
	by the calling entity by casting elemnts as File.*/
	public boolean removeFileTree(File f)
	{
		boolean flag=false;
		try
		{
			File files[]=f.listFiles();
			if(files==null)
			return f.delete();
			else if(files.length==0)
			return f.delete();
			else
			{
				if(!fileVector.contains(f))
				fileVector.add(f);
				for(int i=0;i<files.length;i++)
				{
					flag=removeFileTree(files[i]);
				}

			}
		}
		catch(Exception exc)
		{
			System.out.println(exc);
		}
		return flag;
	}

	private Border createBorder(String name)
	{
		TitledBorder tb=new TitledBorder(name);
		tb.setTitleColor(new Color(110,207,61));
		return tb;
	}

	public String getHomePath()
	{
		return System.getProperty("user.home");
	}

	/* Renderer is component which is mapped to tree
	for custom appearances. This I found in one of Java Demos*/
	private class Renderer extends JLabel implements TreeCellRenderer
	{
		private final ImageIcon iconClose=SplitterResource.getIcon("properties.FT","FOLDER_CLOSED");

		private final ImageIcon iconOpen=SplitterResource.getIcon("properties.FT","FOLDER_OPEN");

		private final ImageIcon root=SplitterResource.getIcon("properties.FT","HOME");

		private final ImageIcon disk=SplitterResource.getIcon("properties.FT","DRIVE_ICON");

		private final ImageIcon desk=SplitterResource.getIcon("properties.FT","DESKTOP_ICON");

		private final Color jfcBlue=new Color(204,204,255);

		boolean selected;

		public Renderer()
		{
			setOpaque(false);
			setFont(new Font("Bookman Old Style",Font.BOLD,15));
			setForeground(Color.BLUE.darker());
		}

		public JComponent getTreeCellRendererComponent(JTree tree,Object value,boolean selected,boolean expanded,boolean leaf,int row,boolean hasFocus)
		{
			String stringValue = tree.convertValueToText(value, selected,
								   expanded, leaf, row, hasFocus);


				DefaultMutableTreeNode node=(DefaultMutableTreeNode)value;
				/* Set the text. */
				setText(stringValue);

				/* Set the image. */
				if(stringValue==null)
				setIcon(null);
				else if(node.equals(desktop))
				setIcon(desk);
				else if(isRootFileNode(node))
				setIcon(disk);
				else if(node.equals(rootNode))
				setIcon(root);
				else if(selected)
				setIcon(iconOpen);
				else if(leaf)
				setIcon(iconClose);
				else
	  		  	setIcon(iconClose);

	  		  	this.selected=selected;
	  		 return this;
		}

		/* Paint the Renderer when Selected with suitable background within
		the Bounds*/
		public void paint(Graphics g)
		{
			Color            bColor;
			Icon             currentI = getIcon();

			if(selected)
			    bColor = jfcBlue;
			else if(getParent() != null)
			    bColor = getParent().getBackground();
			else
			    bColor = getBackground();
			g.setColor(bColor);
			if(currentI != null && getText() != null) {
			    int          offset = (currentI.getIconWidth() + getIconTextGap());

		            if (getComponentOrientation().isLeftToRight()) {
		                g.fillRect(offset, 0, getWidth() - 1 - offset,
		                           getHeight() - 1);
		            }
		            else {
		                g.fillRect(0, 0, getWidth() - 1 - offset, getHeight() - 1);
		            }
			}
			else
			    g.fillRect(0, 0, getWidth()-1, getHeight()-1);
			super.paint(g);
   	 	}
	}
}