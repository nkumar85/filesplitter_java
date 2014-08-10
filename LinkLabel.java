import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.*;

class LinkLabel extends JPanel implements MouseListener
{
	Color rollOver=new Color(100,100,100);
	Color standing=new Color(200,200,200);
	private String suffix="";
	private String url="";
	JLabel linker;
	private Cursor defaultCursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private Cursor handCursor=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

	LinkLabel(String suffix,String url,Color standing,Color rollOver)
	{
		setPreferredSize(new Dimension(190,25));
		linker=new JLabel(url);
		linker.setForeground(standing);
		add(linker);
		this.suffix=suffix;
		this.url=url;
		this.standing=standing;
		this.rollOver=rollOver;
		addMouseListener(this);
	}
	public void mouseClicked(MouseEvent evt)
	{
		//System.out.println("MOUSE CLICKED");
		//System.out.println(evt.getComponent());
		//System.out.println(evt.getPoint());
		if(evt.getX()>=208&&evt.getY()>=9&&evt.getX()<363&&evt.getY()<=17)
		{
			Process p=null;
			try
			{
				linker.setCursor(handCursor);
				//System.out.println("LABEL FOUND");
				Runtime run=Runtime.getRuntime();
				p=run.exec("rundll32 url.dll,FileProtocolHandler "+suffix+url);
				this.setForeground(standing);
			}
			catch(java.io.IOException exc)
			{
				System.out.println(exc);
			}
		}
	}
	public void mouseEntered(MouseEvent evt)
	{
		if(evt.getX()>=208&&evt.getY()>=9&&evt.getX()<363&&evt.getY()<=25)
		{
			linker.setForeground(rollOver);
		}
	}
	public void mouseExited(MouseEvent evt)
	{
		linker.setForeground(standing);
	}
	public void mousePressed(MouseEvent evt)
	{

	}
	public void mouseReleased(MouseEvent evt)
	{
		linker.setForeground(standing);
	}
}
