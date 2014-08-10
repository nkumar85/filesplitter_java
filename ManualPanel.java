import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class ManualPanel extends SplitterResource implements ActionListener
{
	JCheckBox manualSelection;
	JTextField fileName;
	JButton browseToSplit;
	OutputDir targetFile;
	JPanel mainPanel;

	private boolean manualOption=false;

	public ManualPanel()
	{
		mainPanel=new JPanel();
		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		mainPanel.setBorder(new TitledBorder("Split Manually"));

		manualSelection=createCheckBox("Manual Split   ",'s',"Click Here For Manual Split",this);
		manualSelection.setSelected(manualOption);

		fileName=new JTextField(15);
		fileName.setEditable(false);

		browseToSplit=new JButton("Browse");
		browseToSplit.setEnabled(false);
		browseToSplit.addActionListener(this);
		browseToSplit.setMnemonic('b');
		browseToSplit.setToolTipText("Click Here To Locate The Source File");

		targetFile=new OutputDir("Open Files to Split",JFileChooser.FILES_ONLY);

		mainPanel.add(manualSelection);
		mainPanel.add(fileName);
		mainPanel.add(browseToSplit);
	}
	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();

		if(obj instanceof JCheckBox)
		{
			browseToSplit.setEnabled(((JCheckBox)obj).isSelected());
		}
		if(!manualSelection.isSelected())
		{
			manualSelection.setText("Manual Split   ");
			browseToSplit.setText("Browse");
			fileName.setText("");
			manualSelection.setToolTipText("Click Here To Select Manual Split");
		}
		else
		{
			manualSelection.setText("Clear Selection");
			manualSelection.setToolTipText("Clear Manual Selection");
		}
		if(obj==browseToSplit){
			if(browseToSplit.getText().equals("Browse"))
			{
				int reaction=targetFile.showOpenDialog(mainPanel);
				if(reaction==JFileChooser.APPROVE_OPTION)
				{
					String name=targetFile.getSelectedFile().getAbsolutePath();
					fileName.setText(name);
					browseToSplit.setText("Split");
				}
				if(reaction==JFileChooser.CANCEL_OPTION)
				{
					targetFile.cancelSelection();
				}
			}
			else if(browseToSplit.getText().equals("Split"))
			{
				try
				{
					FileSplitterGUI.splitFile(new File(fileName.getText()));
				}
				catch(Exception exc)
				{
					System.out.println(exc);
				}
			}
		}
	}
	public JComponent getMainComponent()
	{
		return mainPanel;
	}
}