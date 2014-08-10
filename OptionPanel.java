import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;

public class OptionPanel extends SplitterResource implements ActionListener
{
	JCheckBox onTop,sound,redirectSplitFile,openFolder,deleteFiles,noBatch;

	JPanel sub;

	JTextField filePath;

	JButton browseReDirect;

	Folder_Tree tree;

	public static String redirectionOutput=null;

	private Window window;

	private JScrollPane scrollPane;

	private JPanel parentPanel;

	private JPanel mainPanel;

	GridBagLayout g1;

	GridBagConstraints gbc;

	public OptionPanel(Window window)
	{
		g1=new GridBagLayout();
		gbc=new GridBagConstraints();

		mainPanel=new JPanel();
		mainPanel.setBorder(new TitledBorder("Options"));

		this.window=window;
		parentPanel=new JPanel();
		parentPanel.setLayout(g1);

		sound=createCheckBox("Beep On Completion",'p',"Check This Box To Beep After Completion",this);
		sound.setSelected(BEEP);


		sub=new JPanel();
		redirectSplitFile=createCheckBox("ReDirect Output",'r',"Check This Box to Redirect Split Files",this);
		redirectSplitFile.setSelected(REDIRECTION);

		filePath=new JTextField(15);
		filePath.setAlignmentX(Component.LEFT_ALIGNMENT);
		filePath.setEditable(false);

		browseReDirect=new JButton("Choose");
		browseReDirect.setIcon(SplitterResource.getIcon("properties.FS","BROWSE_REDIRECT_ICON"));
		browseReDirect.setPreferredSize(new Dimension(105,28));
		browseReDirect.setEnabled(false);
		browseReDirect.setHorizontalTextPosition(AbstractButton.LEFT);
		browseReDirect.addActionListener(this);
		browseReDirect.setMnemonic('c');
		browseReDirect.setToolTipText("Click This Button To Choose Folder");

		sub.add(filePath);
		sub.add(browseReDirect);

		onTop=createCheckBox("Stay On Top",'t',"Make Window on TOP",this);
		openFolder=createCheckBox("Open Destination Folder",'f',"Check Here to open Destination Folder while Processing",this);
		deleteFiles=createCheckBox("Delete Source on Split",'d',"Check here to delete files after Split",this);
		noBatch=createCheckBox("Don't Create Batch File",'b',"Check Not To Create batch file",this);
		parentPanel.add(onTop);
		parentPanel.add(sound);
		parentPanel.add(openFolder);
		parentPanel.add(redirectSplitFile);
		parentPanel.add(deleteFiles);
		parentPanel.add(noBatch);
		parentPanel.add(sub);

		tree=new Folder_Tree();

		organise(onTop,g1,gbc,1,1);
		organise(sound,g1,gbc,4,1);
		organise(openFolder,g1,gbc,1,4);
		organise(deleteFiles,g1,gbc,4,4);
		organise(noBatch,g1,gbc,1,8);
		organise(redirectSplitFile,g1,gbc,1,16);
		organise(sub,g1,gbc,4,16);

		scrollPane=new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(420,100));
		scrollPane.getViewport().add(parentPanel);
		mainPanel.add(scrollPane);
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();

		if(obj==onTop)
		{
			window.setAlwaysOnTop(onTop.isSelected());
		}
		if(obj==sound)
		{
			BEEP=sound.isSelected();
		}
		if(obj==redirectSplitFile)
		{
			browseReDirect.setEnabled(redirectSplitFile.isSelected());
			REDIRECTION=redirectSplitFile.isSelected();
		}
		if(browseReDirect.getText().equals("Choose")&&obj==browseReDirect)
		{
			String path=tree.showDialog(mainPanel);

			if(path!=null)
			{
				filePath.setText(path);
				redirectionOutput=path;
			}
		}
		if(obj==deleteFiles)
		{
			if(deleteFiles.isSelected())
			{
				int i=JOptionPane.showConfirmDialog(mainPanel,"Warning: The deleted files are not put to the Recycle Bin.\nAre you sure to select this option?","Warning",JOptionPane.YES_NO_OPTION);
				deleteFiles.setSelected(false);
				if(i==JOptionPane.YES_OPTION)
				{
					deleteFiles.setSelected(true);
				}
				DELETE_SOURCE=deleteFiles.isSelected();
			}
		}
		if(obj==openFolder)
		{
			OPEN_FOLDER=openFolder.isSelected();
		}

	}

	public String getRedirectionPath()
	{
		if(REDIRECTION)
		{
			if(redirectionOutput!=null&&!redirectionOutput.equals(""))
			{
				return redirectionOutput;
			}
		}
		return null;
	}

	private void organise(JComponent c,GridBagLayout g,GridBagConstraints gb,int x,int y)
	{
			gb.anchor=GridBagConstraints.EAST;
			gb.fill=GridBagConstraints.BOTH;
			gb.gridx=x;
			gb.gridy=y;
			g.setConstraints(c,gb);
	}

	public JComponent getMainComponent()
	{
		return mainPanel;
	}
}