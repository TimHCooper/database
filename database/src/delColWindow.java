import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

public class delColWindow implements ActionListener
{
	private JFrame myFrame = new JFrame();
	private JPanel mainPanel;
	
	ArrayList<JRadioButton> albg = new ArrayList<JRadioButton>();
	ButtonGroup bg = new ButtonGroup();
	JButton saveButton = new JButton("Delete");
	
	public delColWindow()
	{
		myFrame.setTitle("Delete Column");
		myFrame.setLocation(600, 300);
		myFrame.setResizable(false);
		
		mainPanel = (JPanel) myFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Delete Column"));
		
		JRadioButton tButton;
		for(String column : database.colsDisplay)
		{
			tButton = new JRadioButton(column);
			albg.add(tButton);
			bg.add(tButton);
			mainPanel.add(tButton);
		}
		
		mainPanel.add(saveButton);
		saveButton.addActionListener(this);
		
		myFrame.pack();
		myFrame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) 
	{
		Object ob = e.getSource();
		
		if(ob == saveButton);
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
				JOptionPane.showMessageDialog(null, "No column selected", 
						"Delete Column", JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + name + "?", 
						"Delete Column", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirm == 0)
				{
					int index = database.colsDisplay.indexOf(name);
					database.colsDisplay.remove(index);
					database.columns.remove(index);
					databaseWindow.updateData();
					databaseWindow.updateBoxes();
					databaseWindow.setWidths();
					databaseWindow.sortUpdate();
					
					database.save();
				}
			}
		}
	}
}
