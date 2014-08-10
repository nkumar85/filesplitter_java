import java.util.*;
import javax.swing.*;
import java.text.*;
import java.awt.Color;

class Clock extends JLabel implements Runnable
{
	Date date;
	Thread tr;
	SimpleDateFormat format;
	public Clock()
	{
		super();
		format=new SimpleDateFormat();
		format.applyPattern("'Today is 'EEEEEEEEEEE ',' dd 'th' MMMMMMMMMMMMMM  yyyy  'Time:' hh.mm.ss a");
		setForeground(Color.blue.darker());
		tr=new Thread(this);
		tr.start();
	}
	public void run()
	{

		while(tr!=null)
		{
			date=new Date();
			setText(format.format(date));
			try
			{
				Thread.currentThread().sleep(980);
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			}
		}
	}
	public void stop()
	{
		tr=null;
	}

}
