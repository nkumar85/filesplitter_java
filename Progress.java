import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Progress extends JProgressBar
{
	public Progress(int orien)
	{
		super(orien);
		setStringPainted(true);

	}
}