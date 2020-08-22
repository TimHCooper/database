import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class newEntryWindow implements ActionListener
{
	private JFrame myFrame = new JFrame();
	private JPanel mainPanel;
	private JPanel fnPanel;
	private JPanel lnPanel;
	private JPanel mnPanel;
	private ArrayList<JPanel> colPanels = new ArrayList<JPanel>();
	
	private JTextField fnTextField = new JTextField(15);
	private JTextField lnTextField = new JTextField(15);
	private JTextField mnTextField = new JTextField(15);
	private ArrayList<JTextField> colFields = new ArrayList<JTextField>();
	
	private int id;
	private JLabel idLabel;
	private JLabel fnLabel = new JLabel("First Name*:");
	private JLabel lnLabel = new JLabel("Last Name*:");
	private JLabel mnLabel = new JLabel("Middle Name:");
	private ArrayList<JLabel> colLabels = new ArrayList<JLabel>();
	
	private JButton saveButton = new JButton("Save");
	
	public newEntryWindow()
	{
		myFrame.setTitle("New Entry");
		myFrame.setLocation(600, 300);
		myFrame.setResizable(false);
		
		id = database.maxid + 1;
		idLabel = new JLabel("ID: " + id);
		
		fnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		fnPanel.add(fnLabel);
		fnPanel.add(fnTextField);
		
		mnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		mnPanel.add(mnLabel);
		mnPanel.add(mnTextField);
		
		lnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		lnPanel.add(lnLabel);
		lnPanel.add(lnTextField);
		
		for(int i = 0; i < database.colsDisplay.size(); i++)
		{
			colPanels.add(new JPanel(new FlowLayout(FlowLayout.RIGHT)));
			colLabels.add(new JLabel(database.colsDisplay.get(i) + ":"));
			colPanels.get(i).add(colLabels.get(i));
			colFields.add(new JTextField(15));
			colPanels.get(i).add(colFields.get(i));
		}
		
		mainPanel = (JPanel) myFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"New Entry"));
		
		mainPanel.add(idLabel);
		mainPanel.add(fnPanel);
		mainPanel.add(mnPanel);
		mainPanel.add(lnPanel);
		
		for(JPanel panel : colPanels)
		{
			mainPanel.add(panel);
		}
		
		saveButton.addActionListener(this);
		mainPanel.add(saveButton);
		
		myFrame.pack();
		myFrame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		Object ob = e.getSource();
		if(ob == saveButton)
		{
			String fn = fnTextField.getText();
			String ln = lnTextField.getText();
			boolean filled = true;
			if(fn.equals(""))
			{
				fnLabel.setForeground(Color.red);
				filled = false;
			}
			else
				fnLabel.setForeground(Color.black);
			if(ln.equals(""))
			{
				lnLabel.setForeground(Color.red);
				filled = false;
			}
			else
				lnLabel.setForeground(Color.black);
			if(filled)
			{
				entry newEntry = new entry(id, fn + " " + ((!mnTextField.getText().isEmpty()) ?  
						(mnTextField.getText() + " " + ln) : ln));
				for(int i = 0; i < database.columns.size(); i++)
				{
					newEntry.colVals.put(database.columns.get(i), colFields.get(i).getText());
				}
				database.entries.add(newEntry);
				databaseWindow.setWidths();
				databaseWindow.sortUpdate();
				
				database.maxid++;
				
				database.save();
				
				myFrame.setVisible(false);
				myFrame.dispose();
			}
		}
	}
	
}
