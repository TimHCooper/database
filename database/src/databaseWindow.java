import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumn;

public class databaseWindow implements ActionListener
{
	public static boolean searched = false;
	
	private static ArrayList<String> colsDisplay;
	private static ArrayList<String> columns;
	private static ArrayList<entry> entries;
	private static ArrayList<entry> searchedEntries;
	
	public static String[] searchOptions;
	public static String[] sortOptions;
	public static int[] maxWidths;
	private static int size;
	
	private static JFrame myFrame = new JFrame();
	private static JPanel managePanel = new JPanel();
	private static JPanel searchPanel = new JPanel();
	public static JPanel infoPanel = new JPanel();
	private static JPanel mainPanel;
	
	private static JTextField searchTextField = null;
	private static JTable dataTable = null;
	private static JScrollPane dataScrollPane = null;
	
	private JButton addColButton = null;
	private JButton delColButton = null;
	private JButton editColButton = null;
	private JButton searchButton = null;
	private JButton newButton = null;
	private JButton delButton = null;
	private JButton editButton = null;
	
	private static JPanel sortByPanel = new JPanel();
	private static JPanel searchTextBoxPanel = new JPanel();
	private static JComboBox<String> sortBox = null;
	private static JComboBox<String> searchBox = null;
	public static Graphics g;
	
	public databaseWindow()
	{
		colsDisplay = database.colsDisplay;
		columns = database.columns;
		entries = database.entries;
		searchedEntries = entries;
		
		searchBox = new JComboBox<String>();
		sortBox = new JComboBox<String>();
		updateBoxes();
		
		maxWidths = new int[colsDisplay.size() + 2];
		for(int i = 0; i < maxWidths.length; i++)
		{
			maxWidths[i] = 0;
		}
		
		//create JFrame for Window
		myFrame.setTitle("Database");
		myFrame.setLocation(200, 100);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//myFrame.setResizable(false);
				
		//create main JPanel for overall Frame
		mainPanel = (JPanel)myFrame.getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
		
		//create database management panel + buttons
		managePanel.setLayout(new BoxLayout(managePanel, BoxLayout.PAGE_AXIS));
		managePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Database Management"));
		
		addColButton = new JButton("Add Column");
		delColButton = new JButton("Delete Column");
		editColButton = new JButton("Edit Column");
		addColButton.addActionListener(this);
		delColButton.addActionListener(this);
		editColButton.addActionListener(this);
		
		JPanel manageButtonPanel = new JPanel();
		
		manageButtonPanel.add(addColButton);
		manageButtonPanel.add(delColButton);
		manageButtonPanel.add(editColButton);
		managePanel.add(manageButtonPanel);
		
		mainPanel.add(managePanel);
				
		//create JPanel for search & search settings
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
		searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
			"Search Options"));
				
		//create search label + text box + button for search function
		searchTextBoxPanel.add(new JLabel("Search:"));
				
		searchTextField = new JTextField(15);
		searchTextBoxPanel.add(searchTextField);
		
		searchBox.addActionListener(this);
		searchTextBoxPanel.add(searchBox);
				
		searchButton = new JButton("Search");
		searchButton.addActionListener(this);
		searchTextBoxPanel.add(searchButton);
				
		searchPanel.add(searchTextBoxPanel);
				
		//create JPanel for database info display
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
		infoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),
				"Database Info"));
				
		//create order display label + JComboBox
		sortByPanel.add(new JLabel("Sort By:"));
		sortBox.addActionListener(this);
		sortByPanel.add(sortBox);
				
		newButton = new JButton("New Entry");
		newButton.addActionListener(this);
		JPanel entryPanel = new JPanel();
		entryPanel.add(newButton);
				
		delButton = new JButton("Delete Entry");
		delButton.addActionListener(this);
		JPanel delPanel = new JPanel();
		delPanel.add(delButton);
		
		editButton = new JButton("Edit Entry");
		editButton.addActionListener(this);
		JPanel editPanel = new JPanel();
		editPanel.add(editButton);
				
		JPanel sortEntryPanel = new JPanel();
		sortEntryPanel.add(sortByPanel);
		sortEntryPanel.add(entryPanel);
		sortEntryPanel.add(delPanel);
		sortEntryPanel.add(editPanel);
				
		infoPanel.add(sortEntryPanel);

		//add search + info panels to main
		mainPanel.add(searchPanel);
		mainPanel.add(infoPanel);
		
		//pack frame and set visible to screen
	    myFrame.pack();
	    myFrame.setVisible(true);
	    
	    //create Scroll panel & table for database information
        setWidths();
        
        dataTable = buildTable(entries);
        dataScrollPane = new JScrollPane(dataTable);
        infoPanel.add(dataScrollPane);
        
        //pack frame and set visible to screen
	    myFrame.pack();
	    myFrame.setSize(size, 600);
	    myFrame.setVisible(true);
	}
	
	private static JTable buildTable(ArrayList<entry> in) 
	{
		String[][] entriesA = new String[in.size()][colsDisplay.size() + 2];
		
		//build required vars for JTable
		for(int i = 0; i < in.size(); i++)
		{
			
			entry tEntry = in.get(i);
        	int id = tEntry.id;
        	String name = tEntry.name;
        	
			entriesA[i][0] = "" + id;
        	entriesA[i][1] = name;
        	
        	
        	for(int n = 0; n < colsDisplay.size(); n++)
        	{
        		entriesA[i][n+2] = tEntry.colVals.get(columns.get(n));
        	}
		}
		
		String[] COLUMNS = new String[colsDisplay.size() + 2];
		COLUMNS[0] = "ID";
		COLUMNS[1] = "Name";
		for(int i = 0; i < colsDisplay.size(); i++)
		{
			COLUMNS[i + 2] = colsDisplay.get(i);
		}
		
		JTable tempDataTable = new JTable(entriesA, COLUMNS);
		
		//set width of table to be used
		int i = 0;
        for(int width : maxWidths)
        {
        	TableColumn column = tempDataTable.getColumnModel().getColumn(i++);
        	column.setMinWidth(20);
        	column.setMaxWidth(500);
        	column.setPreferredWidth(width);
        }
		
		return tempDataTable;
	}

	public void actionPerformed(ActionEvent e) 
	{
		Object ob = e.getSource();
		if(ob == sortBox)
		{
			sortUpdate();
		}
		
		else if(ob == searchButton)
		{
			String search = searchTextField.getText();
			infoPanel.remove(dataScrollPane);
			if(!search.equals(""))
			{
				searched = true;
				searchedEntries = database.search(entries, search, (byte) searchBox.getSelectedIndex());
			}
			else
			{
				searched = false;
			}
			
			if(searched == true && searchedEntries.size() == 0)
			{
				searched = false;
				dataTable = buildTable(entries);
				JOptionPane.showMessageDialog(null, "No entries found matching search criteria, please try again", 
						"Search Results", JOptionPane.WARNING_MESSAGE);
			}
			else if(searched == false)
			{
				dataTable = buildTable(entries);
			}
			else
			{
				dataTable = buildTable(searchedEntries);
			}
			dataScrollPane = new JScrollPane(dataTable);
			infoPanel.add(dataScrollPane);
			infoPanel.revalidate();
			infoPanel.repaint();
			mainPanel.revalidate();
			mainPanel.repaint();
			
			//pack frame and set visible to screen
		    myFrame.pack();
		    myFrame.setSize(size, 500);
		    myFrame.setVisible(true);
		}
		else if(ob == newButton)
		{
			new newEntryWindow();
		}
		else if(ob == delButton)
		{
			try
			{	
				int row = dataTable.getSelectedRow();
				int id = Integer.parseInt(dataTable.getModel().getValueAt(row,0).toString());
				entry selected = database.search(entries, Integer.toString(id), (byte) 0).get(0);
				int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete " + selected.fullname + "?", 
					"Delete Entry", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirm == 0)
				{
					entries = database.sort(entries, 0);
					for(int i = id; i < entries.size(); i++)
					{
						entries.get(i).id--;
					}
					database.maxid--;
					database.save();
					
					if(searchedEntries.contains(entries.get(id)) && searched)
						searchedEntries.remove(entries.remove(id));
					else
						entries.remove(id);
					
					sortUpdate();
					
					infoPanel.remove(dataScrollPane);
					if(searched)
						dataTable = buildTable(searchedEntries);
					else
						dataTable = buildTable(entries);
					
					dataScrollPane = new JScrollPane(dataTable);
					infoPanel.add(dataScrollPane);
					infoPanel.revalidate();
					infoPanel.repaint();
					mainPanel.revalidate();
					mainPanel.repaint();
					
					//pack frame and set visible to screen
				    myFrame.pack();
				    myFrame.setSize(size, 500);
				    myFrame.setVisible(true);
				}
			
			}
			catch (Exception ex)
			{
				System.out.println(ex.getStackTrace());
			}
		}
		else if(ob == addColButton)
		{
			String colName = JOptionPane.showInputDialog("What column do you want to add?");
			if(colName != null && !colName.isEmpty())
				{
				database.colsDisplay.add(colName);
				String col;
				if(colName.contains(" "))
				{
					col = new String();
					for(char c : colName.toCharArray())
					{
						col += (c == ' ') ? '_' : c;
					}
				}
				else
					col = colName;
				database.columns.add(col);
			
				infoPanel.remove(dataScrollPane);
				if(!searched)
					dataTable = buildTable(entries);
				else
				{
					dataTable = buildTable(searchedEntries);
				}
				dataScrollPane = new JScrollPane(dataTable);
				infoPanel.add(dataScrollPane);
				infoPanel.revalidate();
				infoPanel.repaint();
				mainPanel.revalidate();
				mainPanel.repaint();
			
				//pack frame and set visible to screen
				myFrame.pack();
		    	myFrame.setSize(size, 500);
		    	myFrame.setVisible(true);
		    	
		    	database.save();
		    	updateBoxes();
		    	updateData();
			}
		}
		else if(ob == delColButton)
		{
			new delColWindow();
		}
		else if(ob == editColButton)
		{
			new editColWindow();
		}
		else if(ob == editButton)
		{
			try {
			int row = dataTable.getSelectedRow();
			int id = Integer.parseInt(dataTable.getModel().getValueAt(row,0).toString());
			entry selected = database.search(entries, Integer.toString(id), (byte) 0).get(0);
			new editEntryWindow(selected);
			}
			catch (Exception ex)
			{
				System.out.println(ex);
			}
		}
	}
	
	public static void sortUpdate()
	{
		entries = database.entries;
		if(searched)
		{
			database.search(entries, searchTextField.getText(), (byte) searchBox.getSelectedIndex());
		}
		
		//sort database to new selected sort type
		int type = sortBox.getSelectedIndex();
		if(type < 3)
		{
			entries = database.sort(entries, type);
			searchedEntries = database.sort(searchedEntries, type);
		}
		else
		{
			String compareType = columns.get(colsDisplay.indexOf((String) sortBox.getSelectedItem()));
			//System.out.println(compareType);
			ArrayList<entry> tempSortList = new ArrayList<entry>();
			ArrayList<entry> tempSortEList = new ArrayList<entry>();
			ArrayList<entry> tempSearchList = new ArrayList<entry>();
			ArrayList<entry> tempSearchEList = new ArrayList<entry>();
			
			for(entry Entry : entries)
			{
				if(!Entry.colVals.get(compareType).isEmpty())
				{
					tempSortList.add(Entry);
				}
				else
					tempSortEList.add(Entry);
			}
			
			for(entry EntryS : searchedEntries)
			{
				if(!EntryS.colVals.get(compareType).isEmpty())
				{
					tempSearchList.add(EntryS);
				}
				else
					tempSearchEList.add(EntryS);
			}
			tempSortList = database.sort(tempSortList, compareType);
			tempSearchList = database.sort(tempSearchList, compareType);
			
			tempSortList.addAll(tempSortEList);
			tempSearchList.addAll(tempSearchEList);
			
			entries = tempSortList;
			searchedEntries = tempSearchList;
		}
		
		//put the newly sorted database into the table
		infoPanel.remove(dataScrollPane);
		if(searched)
			dataTable = buildTable(searchedEntries);
		else
			dataTable = buildTable(entries);
		dataScrollPane = new JScrollPane(dataTable);
		infoPanel.add(dataScrollPane);
		//updateBoxes();
		infoPanel.revalidate();
		infoPanel.repaint();
		mainPanel.revalidate();
		mainPanel.repaint();
        
        //pack frame and set visible to screen
	    myFrame.pack();
	    myFrame.setSize(size, 500);
	    myFrame.setVisible(true);
	}
	
	public static void setWidths()
	{
		g = infoPanel.getGraphics();
		
		maxWidths = new int[colsDisplay.size() + 2];
		
        for(entry Entry : entries)
        {
        	int idlen = g.getFontMetrics().stringWidth("" + Entry.id);
        	int namelen = g.getFontMetrics().stringWidth(Entry.name);
        	
        	if(idlen > maxWidths[0])
        		maxWidths[0] = idlen;
        	if(namelen > maxWidths[1])
        		maxWidths[1] = namelen;
        }
        
        for(int i = 0; i < colsDisplay.size(); i++)
        {
        	String targetCol = columns.get(i);
        	for(entry Entry : entries)
        	{
        		if(Entry.colVals.get(targetCol) != null)
        		{
        			int targetlen = g.getFontMetrics().stringWidth(Entry.colVals.get(targetCol));
        			if(targetlen > maxWidths[i + 2])
        				maxWidths[i + 2] = targetlen;
        		}
        	}
        }
        
        int sum = 0;
        for(int n = 0; n < maxWidths.length; n++)
        {
        	sum += maxWidths[n];
        }
        
        size = sum + 100;
	}
	public static void updateBoxes()
	{
		sortOptions = new String[colsDisplay.size() + 3];
		sortOptions[0] = "ID";
		sortOptions[1] = "Name (A-Z)";
		sortOptions[2] = "Name (Z-A)";
		
		for(int i = 3; i < sortOptions.length; i++)
		{
			sortOptions[i] = colsDisplay.get(i - 3);
		}
		
		searchOptions = new String[colsDisplay.size() + 2];
		searchOptions[0] = "ID";
		searchOptions[1] = "Name";
		
		for(int i = 0; i < colsDisplay.size(); i++)
		{
			searchOptions[i+2] = colsDisplay.get(i);
		}
		
		DefaultComboBoxModel<String> sortModel = new DefaultComboBoxModel<String>(sortOptions);
		DefaultComboBoxModel<String> searchModel = new DefaultComboBoxModel<String>(searchOptions);
		
		sortBox.setModel(sortModel);
		searchBox.setModel(searchModel);
		
		//System.out.println("Boxes updated");
	}
	
	public static void updateData()
	{
		columns = database.columns;
		colsDisplay = database.colsDisplay;
		entries = database.entries;
	}
}
