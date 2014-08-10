import javax.swing.border.BevelBorder;
import javax.swing.*;

public class DetailsPane extends JTextArea
{

	public DetailsPane()
	{
		super();
		setEnabled(false);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
	}
}