import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DnDPanel extends SplitterResource implements ActionListener
{

	//subPanel1
	JPanel dropPanel;

	DropButton dropButton;

	//subPanel2
	JPanel sizePanel;

	JPanel mainPanel;

	JPanel sub1,sub2;

	JRadioButton predefined,manual;

	JComboBox predefinedSizes;

	JTextField manualField;

	JComboBox scaling;

	/*Default Settings*/

	private ImageIcon imageFiles[]={
		getIcon("properties.FS","COMBO_ICON_1"),
		getIcon("properties.FS","COMBO_ICON_2"),
		getIcon("properties.FS","COMBO_ICON_3"),
		getIcon("properties.FS","COMBO_ICON_4"),
		getIcon("properties.FS","COMBO_ICON_5"),
		getIcon("properties.FS","COMBO_ICON_6")
		};

	ImageIcon images[];

	public DnDPanel()
	{
		mainPanel=new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.X_AXIS));
		dropPanel=new JPanel();
		dropButton=new DropButton();
		dropPanel.add(dropButton);

		sizePanel=new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel,BoxLayout.PAGE_AXIS));

		sub1=new JPanel();
		sub1.setPreferredSize(new Dimension(270,10));
		sub1.setLayout(new FlowLayout(FlowLayout.LEFT));
		sub2=new JPanel();
		sub2.setLayout(new FlowLayout(FlowLayout.LEFT));

		predefined=new JRadioButton("Predefined");
		predefined.setSelected(true);
		setImages(predefined);
		predefined.setMnemonic('p');
		predefined.setFocusPainted(false);

		Object obj[]=getPredefinedKeys();
		for(int i=0;i<obj.length;i++)
		{
			imageFiles[i].setDescription(obj[i].toString());
		}
		ComboBoxRenderer render=new ComboBoxRenderer();
		predefined.addActionListener(this);
		predefinedSizes=new JComboBox(imageFiles);
		predefinedSizes.setMaximumRowCount(4);
		predefinedSizes.setRenderer(render);
		predefinedSizes.setToolTipText("Choose Predefined Size");

		manual=new JRadioButton("Manual");
		setImages(manual);
		manual.setFocusPainted(false);
		manual.addActionListener(this);
		manual.setMnemonic('m');
		manual.setToolTipText("Click for Custom Size");

		manualField=new JTextField(5);
		manualField.setHorizontalAlignment(JTextField.RIGHT);
		manualField.setEditable(false);
		manualField.setToolTipText("Type Custom Number");
		manualField.setForeground(Color.blue);
		scaling=new JComboBox(getScalingFactors());
		scaling.setMaximumRowCount(1);
		scaling.setEnabled(false);
		scaling.setToolTipText("Choose Scaling Factor");

		ButtonGroup group=new ButtonGroup();
		group.add(predefined);
		group.add(manual);

		sub1.add(predefined);
		sub1.add(predefinedSizes);
		sub2.add(manual);
		sub2.add(manualField);
		sub2.add(scaling);

		sizePanel.add(sub1);
		sizePanel.add(sub2);

		mainPanel.add(dropPanel);
		mainPanel.add(sizePanel);

	}

	public void actionPerformed(ActionEvent evt)
	{
		Object obj=evt.getSource();
		if(obj instanceof JRadioButton)
		{
			predefined.setToolTipText("Click Choose a Predefined Size");
			predefinedSizes.setEnabled(predefined.isSelected());
			manualField.setEditable(manual.isSelected());
			scaling.setEnabled(manual.isSelected());
			if(!predefined.isSelected())
			predefined.setToolTipText("Click Here to Choose Predefined Size");
			else
			predefined.setToolTipText(null);
			if(!manual.isSelected())
			manual.setToolTipText("Click for Custom Size");
			else
			manual.setToolTipText(null);
		}
		if(obj==manual)
		{
			manual.setRequestFocusEnabled(true);
			manualField.requestFocus();
		}
	}

	/* Returns the batch size depending on RadioButton selected*/
	public long getBatchSize()
	{
		if(predefined.isSelected())
		{
			Object obj=predefinedSizes.getSelectedItem();
			Long size=(Long)getObject(obj.toString());
			return size.longValue();
		}
		if(manual.isSelected())
		{
			long scaler=0,scaled=0;
			Object obj=scaling.getSelectedItem();
			Long size=(Long)(getObject(obj.toString()));
			scaled=size.longValue();
			try
			{
				scaler=Long.parseLong(manualField.getText());
			}
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(mainPanel.getRootPane(),"Invalid Number","ERROR",JOptionPane.ERROR_MESSAGE);
			}
			return (scaled*scaler);
		}
		return 0;
	}

	/* Rollover Icons for JRadioButton*/
	private void setImages(JRadioButton button)
	{
		button.setRolloverEnabled(true);
		button.setIcon(getIcon("properties.FS","_ICON"));
		button.setRolloverIcon(getIcon("properties.FS","ROLL_OVER_ICON"));
		button.setPressedIcon(getIcon("properties.FS","PRESSED_ICON"));
		button.setRolloverSelectedIcon(getIcon("properties.FS","ROLL_OVER_SELECTED_ICON"));
		button.setSelectedIcon(getIcon("properties.FS","SELECTED_ICON"));
	}

	/* Used By the super class */
	public JComponent getMainComponent()
	{
		return mainPanel;
	}

	/* This method I found in on of Java Demos by Sun MicroSystems*/
	class ComboBoxRenderer extends JLabel implements ListCellRenderer
	{
		public ComboBoxRenderer()
		{
			setOpaque(true);
		}
		public Component getListCellRendererComponent(
		                                           JList list,
		                                           Object value,
		                                           int index,
		                                           boolean isSelected,
		                                           boolean cellHasFocus)
	 	{
		            if (isSelected)
		            {
		                setBackground(list.getSelectionBackground());
		                setForeground(list.getSelectionForeground());
		            } else
		            {
		                setBackground(list.getBackground());
		                setForeground(list.getForeground());
		            }

		            ImageIcon icon = (ImageIcon)value;
		            setText(icon.getDescription());
		            setIcon(icon);
		            return this;
        }
	}

}