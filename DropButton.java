import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;

public class DropButton extends JButton implements DropTargetListener,ActionListener
{

		private DropTarget dt;

		public DropButton()
		{
			super("Drop Files Here");
			setIcon(SplitterResource.getIcon("properties.FS","DROP_BUTTON_ICON"));
			setPreferredSize(new Dimension(120,120));
			setFocusPainted(false);
			setVerticalTextPosition(AbstractButton.TOP);
        	setHorizontalTextPosition(AbstractButton.CENTER);
        	setToolTipText("Drop File on this BUTTON to Split");
			addActionListener(this);

			dt=new DropTarget(this,this);
		}

		public void dragEnter(DropTargetDragEvent evt)
		{
			evt.acceptDrag(DnDConstants.ACTION_COPY);
		}

		public void dragExit(DropTargetEvent evt){}

		public void dragOver(DropTargetDragEvent evt)
		{
			evt.acceptDrag(DnDConstants.ACTION_COPY);
		}

		public void drop(DropTargetDropEvent evt)
		{

			DataFlavor available[]=evt.getCurrentDataFlavors();
			DataFlavor fileFlavor=null;
			Transferable transfer;

			for(int i=0;i<available.length;i++)
			{
				if(available[i].equals(DataFlavor.javaFileListFlavor))
				{
					fileFlavor=available[i];
					break;
				}
			}

			if(fileFlavor!=null)
			{
				if((evt.getSourceActions()&DnDConstants.ACTION_COPY)!=0)
				{
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					try
					{
						transfer=evt.getTransferable();
						Object obj=transfer.getTransferData(fileFlavor);
						String dummyPath=obj.toString();
						String fileName=obj.toString().substring(1,dummyPath.length()-1);
						File file=new File(fileName);
						System.out.println(file);
						if(file.isDirectory())
						{
							JOptionPane.showMessageDialog(this.getRootPane(),"Can't Split Directories","Message",JOptionPane.INFORMATION_MESSAGE);
						}
						else
						{
							try
							{
								FileSplitterGUI.splitFile(file);
							}
							catch(IOException ex)
							{
								JOptionPane.showMessageDialog(this.getRootPane(),ex,"ERROR",JOptionPane.ERROR_MESSAGE);
							}
							catch(InterruptedException ex)
							{
								JOptionPane.showMessageDialog(this.getRootPane(),ex,"ERROR",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					catch(UnsupportedFlavorException exc)
					{
						JOptionPane.showMessageDialog(this.getRootPane(),exc,"ERROR",JOptionPane.ERROR_MESSAGE);
					}
					catch(IOException ioe)
					{
						JOptionPane.showMessageDialog(this.getRootPane(),ioe,"ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
				else
				{
					evt.rejectDrop();
				}
			}
		}

		public void dropActionChanged(DropTargetDragEvent evt){}

		public void actionPerformed(ActionEvent evt)
		{
			if(evt.getSource()==this)
			{
				JOptionPane.showMessageDialog(this.getRootPane(),"Drag & Drop Files on this Button to split");
			}
		}
}