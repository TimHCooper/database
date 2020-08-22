import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class database 
{
	
	public static ArrayList<String> colsDisplay;
	public static ArrayList<String> columns;
	public static ArrayList<entry> entries;
	
	public static int maxid = 0;
	
	public static void main(String[] args) 
	{
		colsDisplay = new ArrayList<String>();
		columns = new ArrayList<String>();
		entries = new ArrayList<entry>();
		try 
		{
			File xmlDatabase = new File("database.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlDatabase);
			doc.getDocumentElement().normalize();
			
			NodeList NLcolumns = doc.getElementsByTagName("column");
			
			for(int i = 0; i < NLcolumns.getLength(); i++)
			{
				Node nNode = NLcolumns.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					String column = nNode.getTextContent();
					columns.add(column);
					String col;
					if(column.contains("_"))
					{
						col = new String();
						for(char c : column.toCharArray())
						{
							col += (c == '_') ? ' ' : c;
						}
					}
					else
						col = column;
					
					colsDisplay.add(col);
				}
			}
			
			NodeList NLentries = doc.getElementsByTagName("entry");
			
			for(int i = 0; i < NLentries.getLength(); i++)
			{
				Node nNode = NLentries.item(i);
				
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;
					
					int id = Integer.parseInt(eElement.getAttribute("id"));
					String name = eElement.getElementsByTagName("name").item(0).getTextContent();
					
					if(id > maxid)
						maxid = id;
					
					entries.add(new entry(id, name));
					
					for(String column : columns)
					{
						String val = eElement.getElementsByTagName(column).item(0).getTextContent();
						entries.get(i).colVals.put(column, val);
					}
				}
			}
		} 
		catch (Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
		new databaseWindow();
	}
	
	public static ArrayList<entry> sort(ArrayList<entry> in, int compareType)
	{
		//compare types: 0 = id, 1 = name (alphabetical), 2 = name (reverse alphabetical)
		//bubble sort implementation: O(n) best case, O(n^2) average, O(n^2) worst case
		boolean sorted = true;
		for(int i = 0; i < in.size() - 1; i++)
		{
			if(in.get(i).compare(in.get(i + 1), compareType) == -1)
			{
				entry temp = in.get(i + 1);
				in.set(i + 1, in.get(i));
				in.set(i, temp);
				sorted = false;
			}
		}
		if(!sorted)
		{
			in = sort(in, compareType);
		}
		
		return in;
	}
	
	public static ArrayList<entry> sort(ArrayList<entry> in, String compareType)
	{
		//compare type is String column from database to compare alphabetically
		//bubble sort implementation: O(n) best case, O(n^2) average, O(n^2) worst case
		
		boolean sorted = true;
		for(int i = 0; i < in.size() - 1; i++)
		{
			
			if(in.get(i).compare(in.get(i + 1), compareType) == -1)
			{
				entry temp = in.get(i + 1);
				in.set(i + 1, in.get(i));
				in.set(i, temp);
				sorted = false;
			}
		}
		if(!sorted)
		{
			in = sort(in, compareType);
		}
		
		return in;
	}
	
	public static ArrayList<entry> search(ArrayList<entry> in, String search, byte compareType)
	{
		ArrayList<entry> searchedList = new ArrayList<entry>();
		
		switch(compareType)
		{
		case 0:
			for(entry Entry : in)
			{
				if((Integer.toString(Entry.id)).contains(search))
				{
					searchedList.add(Entry);
				}
			}
			break;
		case 1:
			for(entry Entry : in)
			{
				if(Entry.fullname.toLowerCase().contains(search.toLowerCase()) 
						|| Entry.name.toLowerCase().contains(search.toLowerCase()))
				{
					searchedList.add(Entry);
				}
			}
			break;
		default:
			for(entry Entry : in)
			{
				if(Entry.colVals.get(columns.get(compareType - 2)).toLowerCase().contains(search.toLowerCase()))
				{
					searchedList.add(Entry);
				}
			}
			break;
		}
		return searchedList;
	}
	
	public static void save()
	{
		try
		{
			entries = sort(entries, 0);
			
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			
			Element root = document.createElement("database");
			document.appendChild(root);
			
			Element Ecolumns = document.createElement("columns");
			root.appendChild(Ecolumns);
			
			Element element;
			for(String column : columns)
			{
				element = document.createElement("column");
				element.appendChild(document.createTextNode(column));
				Ecolumns.appendChild(element);
			}
			
			Element Eentries = document.createElement("entries");
			root.appendChild(Eentries);
			
			Element entry;
			Attr attr;
			Element name;
			
			for(int i = 0; i <= maxid; i++)
			{
				entry = document.createElement("entry");
				Eentries.appendChild(entry);
				
				attr = document.createAttribute("id");
				attr.setValue("" + i);
				entry.setAttributeNode(attr);
				
				name = document.createElement("name");
				name.appendChild(document.createTextNode(entries.get(i).fullname));
				entry.appendChild(name);
				
				for(String column : columns)
				{
					element = document.createElement(column);
					element.appendChild(document.createTextNode(entries.get(i).colVals.get(column)));
					entry.appendChild(element);
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File("database.xml"));
			
			transformer.transform(domSource, streamResult);
			
			System.out.println("File Successfully saved");
		}
		catch(ParserConfigurationException pce)
		{
			pce.printStackTrace();
		}
		catch(TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}
}
