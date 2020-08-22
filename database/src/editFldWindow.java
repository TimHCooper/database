import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class editFldWindow implements ActionListener
{
	private JFrame myFrame = new JFrame();
	private JPanel mainPanel;
	
	ArrayList<JRadioButton> albg = new ArrayList<JRadioButton>();
	ButtonGroup bg = new ButtonGroup();
	JButton saveButton = new JButton("Save");
	
	private JTextField editTextField = new JTextField(15);
	
	public editFldWindow()
	{
		myFrame.setTitle("Edit field");
		myFrame.setLocation(600, 300);
		myFrame.setResizable(false);
		
		mainPanel = (JPanel) myFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Edit field"));
		
		JRadioButton tButton;
		for(String field : database.fieldDisplay)
		{
			tButton = new JRadioButton(field);
			tButton.addActionListener(this);
			albg.add(tButton);
			bg.add(tButton);
			mainPanel.add(tButton);
		}
		
		mainPanel.add(new JLabel("\n"));
		mainPanel.add(editTextField);
		mainPanel.add(saveButton);
		saveButton.addActionListener(this);
		
		myFrame.pack();
		myFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) 
	{
		Object ob = e.getSource();
		
		if(ob == saveButton)
		{
			String name = null;
			for(JRadioButton button : albg)
			{
				if(button.isSelected())
				{
					name = button.getText();
					break;
				}
			}
			
			if(name == null)
			{
				JOptionPane.showMessageDialog(null, "No field selected", 
						"Edit field", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
					int index = database.fieldDisplay.indexOf(name);
					String oldName = database.fields.get(index);
					String newName = editTextField.getText();
					
					database.fieldDisplay.set(index, newName);
					String fld;
					if(newName.contains(" "))
					{
						fld = new String();
						for(char c : newName.toCharArray())
						{
							fld += (c == ' ') ? '_' : c;
						}
					}
					else
						fld = newName;
					database.fields.set(index, fld);
					
					for(entry Entry : database.entries)
					{
						Entry.fldVals.put(fld, Entry.fldVals.get(oldName));
						Entry.fldVals.put(oldName, "");
					}
					
					databaseWindow.updateData();
					databaseWindow.updateBoxes();
					databaseWindow.setWidths();
					databaseWindow.sortUpdate();
					
					database.save();
					
					myFrame.setVisible(false);
					myFrame.dispose();
			}
		}
		else if(ob instanceof JRadioButton)
		{
			editTextField.setText(((JRadioButton) ob).getText());
		}
	}
}