import java.util.TreeMap;

public class entry 
{
	public int id;
	public String name;
	public String fullname;
	public TreeMap<String, String> fldVals;
	
	public entry(int i, String n)
	{
		fldVals = new TreeMap<String, String>();
		id = i;
		name = n.substring(n.lastIndexOf(" ") + 1) + ", " + n.substring(0, n.lastIndexOf(" "));
		fullname = n;
	}
	
	public byte compare(entry in, int compareType)
	{
		switch(compareType)
		{
		case 0:
			return (byte) ((id > in.id) ? -1 : 1);
		case 1:
			int compare = (name.toLowerCase().compareTo(in.name.toLowerCase()));
			int compareAbs = Math.abs(compare);
			if(compare != 0)
				return (byte) -(compare/compareAbs);
			else
				return 0;
		case 2:
			compare = (name.toLowerCase().compareTo(in.name.toLowerCase()));
			compareAbs = Math.abs(compare);
			if(compare != 0)
				return (byte) (compare/compareAbs);
			else
				return 0;
		default:
			return 0;
		}
	}
	
	public byte compare(entry in, String compareType)
	{
		int compare = (fldVals.get(compareType).toLowerCase().compareTo(in.fldVals.get(compareType).toLowerCase()));
		int compareAbs = Math.abs(compare);
		if(compare != 0)
			return (byte) -(compare/compareAbs);
		else
			return 0;
	}
}
