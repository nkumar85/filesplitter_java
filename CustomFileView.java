/*
Custom File View for OutputDir.
Author- Nandakumar
e-mail: nandakumar_nie@yahoo.com
*/

import javax.swing.filechooser.FileView;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.io.File;
import java.util.Hashtable;

public class CustomFileView extends FileView
{
	private Hashtable<String,ImageIcon> iconTable;

	public CustomFileView()
	{
		iconTable=new Hashtable<String,ImageIcon>();
		iconTable.put("exe",SplitterResource.getIcon("properties.FV","EXE_ICON"));
		iconTable.put("bat",SplitterResource.getIcon("properties.FV","BAT_ICON"));
		iconTable.put("html",SplitterResource.getIcon("properties.FV","WEB_ICON"));
		iconTable.put("htm",SplitterResource.getIcon("properties.FV","WEB_ICON"));
		iconTable.put("asp",SplitterResource.getIcon("properties.FV","WEB_ICON"));
		iconTable.put("jsp",SplitterResource.getIcon("properties.FV","WEB_ICON"));
		iconTable.put("jar",SplitterResource.getIcon("properties.FV","JAR_ICON"));
	}
	public Icon getIcon(File file)
	{
		if(isRoot(file))
		return null;

		if(file.isDirectory())
		return new ImageIcon("images/folder_closed.gif");

		String extension=getExtension(file);
		//System.out.println(extension);
		if(extension!=null)
		{
			if(iconTable.get(extension)!=null)
			{
				ImageIcon icon=(ImageIcon)iconTable.get(extension);
				return icon;
			}
		}
		return null;
	}
	public String getExtension(File f)
	{
		String name=f.getName();
		if(name!=null)
		{
			int index=name.lastIndexOf('.');

			if(index<0)
			return null;

			String ext=name.substring(index+1);
			return ext.toLowerCase();
		}
		return null;
	}

	public boolean isRoot(File file)
	{
		File roots[]=File.listRoots();
		for(int i=0;i<roots.length;i++)
		{
			if(file.equals(roots[i]))
			return true;
		}
		return false;
	}

}