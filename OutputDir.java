import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.filechooser.*;

public class OutputDir extends JFileChooser
{
	public OutputDir(String title,int mode)
	{
		super(FileSystemView.getFileSystemView());
		setDialogTitle(title);
		setFileSelectionMode(mode);
		setDialogType(JFileChooser.OPEN_DIALOG);
		setMultiSelectionEnabled(false);
		setCurrentDirectory(new File(System.getProperty("user.dir")));
		setFileView(new CustomFileView());
	}
}