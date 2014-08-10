import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AboutTabbedPane extends JTabbedPane implements ChangeListener
{
	private HTMLPane []panes;

	final String license=SplitterResource.getFile("properties.FS","ABOUT_FILE");

	final String gpl=SplitterResource.getFile("properties.FS","GPL_FILE");

	public AboutTabbedPane()
	{
		super();
		panes=new HTMLPane[2];
		try
		{
			panes[0]=new HTMLPane(license);
			panes[1]=new HTMLPane(gpl);
		}
		catch(Exception exc)
		{
			System.out.println(exc);
		}
		addTab("About",panes[0]);
		addTab("GPL",panes[1]);
	}
	public void stateChanged(ChangeEvent evt)
	{

	}
}