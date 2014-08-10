/*
HTMLPane embeds html files into a scrollpane
and the listener works as per the protocol of URL

Author-NandaKumar
*/


import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import java.awt.Dimension;
import java.awt.Cursor;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.net.URL;

public class HTMLPane extends JScrollPane implements HyperlinkListener
{
	private JEditorPane textPane;

	public HTMLPane(String htmlFile)throws java.io.IOException
	{
		super();
		if(htmlFile==null)
		throw new NullPointerException("Must provide HTML File Name Path");
		textPane=new JEditorPane(htmlFile);
		textPane.setEditable(false);
		getViewport().add(textPane);
		setPreferredSize(new Dimension(700,400));
		textPane.addHyperlinkListener(this);

	}
	public static void main(String args[])throws java.io.IOException
	{
		HTMLPane help=new HTMLPane("file:HELP/gpl.html");
	}
	public void hyperlinkUpdate(HyperlinkEvent evt)
	{
		URL url=evt.getURL();
		String protocol=url.getProtocol().toLowerCase();
		if(evt.getEventType()==HyperlinkEvent.EventType.ACTIVATED)
		{
			Process p=null;
			try
			{
				if(protocol.equals("file")||protocol.equals("jar"))
				{
					textPane.setPage(url);
				}
				else
				{
					Runtime run=Runtime.getRuntime();
					p=run.exec("rundll32 url.dll,FileProtocolHandler "+url.toString());
				}
			}
			catch(Exception exc)
			{
				System.out.println(exc);
			}
		}
	}

}