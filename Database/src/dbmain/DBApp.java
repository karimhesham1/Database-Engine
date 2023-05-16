package dbmain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class DBApp implements Serializable {
	private static final long serialVersionUID = 1L;
	private Vector<Page> loadedPages;
	private Table loadedTable;
	private OctTree loadedOctree;
	private String loadedIndexName;
	private boolean firstDeletion = true;
	private String currIndexName = "";

	// Test
	// This is a part of a pull request

	public DBApp() {

	}

	// this does whatever initialization you would like
	// or leave it empty if there is no code you want to
	// execute at application startup
	public void init() {

	}


	// following method creates one table only
	// strClusteringKeyColumn is the name of the column that will be the primary
	// key and the clustering column as well. The data type of that column will
	// be passed in htblColNameType
	// htblColNameValue will have the column name as key and the data
	// type as value
	// htblColNameMin and htblColNameMax for passing minimum and maximum values
	// for data in the column. Key is the name of the column
	public void createTable(String strTableName, String strClusteringKeyColumn,
			Hashtable<String, String> htblColNameType, Hashtable<String, String> htblColNameMin,
			Hashtable<String, String> htblColNameMax) throws DBAppException {
		try {
			// Create a new table file with the given name
			File tableFile = new File(strTableName + ".class");
			if (tableFile.exists()) {
				System.out.println("Table " + strTableName + " already exists.");
				throw new DBAppException();

			}
			tableFile.createNewFile();

			// Create a new Table object and serialize it to disk
			Table table = new Table(strTableName);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tableFile));
			out.writeObject(table);
			out.close();

			// Check if metadata file exists

			File metadataFile = new File("metadata.csv");
			boolean metadataExists = metadataFile.exists();

			// Open metadata file for appending
			FileWriter writer = new FileWriter(metadataFile, true);

			// If metadata file doesn't exist, write headers
			if (!metadataExists) {
				writer.write("Table Name,Column Name,Column Type,Clustering Key,IndexName,IndexType,Min,Max\n");
			}

			// Loop through columns in hashtable and write to metadata file

			Enumeration<String> columnNames1 = htblColNameType.keys();
			if ((checkTypeMinMax(columnNames1, htblColNameType, htblColNameMin, htblColNameMax)) == false) {
				throw new DBAppException("Error with data consistency");
			}
			Enumeration<String> columnNames = htblColNameType.keys();
			while (columnNames.hasMoreElements()) {
				String columnName = columnNames.nextElement();
				String columnType = htblColNameType.get(columnName);
				String min = htblColNameMin.get(columnName);
				String max = htblColNameMax.get(columnName);
				boolean clusteringKey = columnName.equals(strClusteringKeyColumn);
				String indexed = "Null";

				writer.write(strTableName + "," + columnName + "," + columnType + "," + clusteringKey + "," + indexed
						+ "," + indexed + "," + min + "," + max + "\n");
			}

			// Close metadata file
			writer.close();

			System.out.println("Table " + strTableName + " created successfully.");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBAppException(e.getMessage());
		}

	}

	// following method inserts one row only.
	// htblColNameValue must include a value for the primary key
	public void insertIntoTable(String strTableName, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		try {
			// Check if table exists
			File tableFile = new File(strTableName + ".class");
			if (!tableFile.exists()) {
				System.out.println("Table " + strTableName + " does not exist.");
				throw new DBAppException();
			}

			loadTable(strTableName);

			boolean primaryexists = false;

			Enumeration<String> columnNames = htblColNameValue.keys();
			Object insertedPkValue = null;
			String tableName;
			String pk = null;

			while (columnNames.hasMoreElements()) {
				boolean flag = false;
				String insertedColName = columnNames.nextElement();
				Object insertedvalue = htblColNameValue.get(insertedColName);
				BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
				String line = br.readLine();
				boolean firstloop = true;
				while (line != null) {

					if (firstloop)
						line = br.readLine();

					firstloop = false;

					String[] content = line.split(",");

					// if(!(columnNames.hasMoreElements()) )
					// break;

					if (content[0].equals(strTableName)) {
						tableName = strTableName;

						if (content[1].equals(insertedColName)) {

							if (content[2].equals(insertedvalue.getClass().getName())) {
								if ((insertedvalue.getClass().getName()).equals("java.lang.Double")) {
									int min = Integer.parseInt(content[6]);
									int max = Integer.parseInt(content[7]);

									if ((double) insertedvalue >= min && (double) insertedvalue <= max) {
										flag = true;
										if (content[3].equals("true")) {
											// new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if (!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}

								else if ((insertedvalue.getClass().getName()).equals("java.lang.Integer")) {
									int min = Integer.parseInt(content[6]);
									int max = Integer.parseInt(content[7]);

									if ((int) insertedvalue >= min && (int) insertedvalue <= max) {
										flag = true;
										if (content[3].equals("true")) {
											// new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if (!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}

								else if ((insertedvalue.getClass().getName()).equals("java.lang.String")) {
									String min = content[6];
									String max = content[7];
									String insertedvalstring = (String) insertedvalue;
									int comparemin = insertedvalstring.compareToIgnoreCase(min);
									int comparemax = insertedvalstring.compareToIgnoreCase(max);
									if (comparemin >= 0 && comparemax <= 0) {
										flag = true;
										if (content[3].equals("true")) {
											// new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if (!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}

								else if ((insertedvalue.getClass().getName()).equals("java.util.Date")) {
									String min = content[6];
									String max = content[7];
									Date insertedvaldate = (Date) insertedvalue;

									if (isValidDate(insertedvaldate, parseStringToDate(min), parseStringToDate(max))) {
										flag = true;
										if (content[3].equals("true")) {
											// new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if (!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}
							}

						}

					}

					line = br.readLine();
				}
				br.close();
				if (flag == false)
					throw new DBAppException("the values u entered are out of range");
			}
			if (!primaryexists)
				throw new DBAppException();

			int insertRowIndex1 = -1;
			int insertPageIndex1 = -1;
			boolean found = false;
			if (primaryexists) {
				insertNullValues(htblColNameValue, strTableName);
				loadPages(loadedTable);
				//					Binary Search			

				// if the record we are inserting is the first record, table has no pages aslan
				// (new) error fixing
				if (loadedPages.isEmpty()) {
					Row newRow = new Row(htblColNameValue);
					Page newPage = new Page(loadedTable);
					newPage.addRow(newRow, 0);
					loadedPages.add(newPage);
					savePages();
					saveTable();
					return;
				}

				int i = 0;

				while (!found && i < loadedPages.size() && insertPageIndex1 == -1) {
					int low = 0;
					int high = loadedPages.get(i).getNumUsedRows() - 1;

					while (low <= high) {
						int mid = ((low + high) / 2); // new error fix
						Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(i).getRow(mid).getValue(pk);
						Comparable<Object> pkValueLow = (Comparable<Object>) loadedPages.get(i).getRow(low)
								.getValue(pk);
						Comparable<Object> pkValueHigh = (Comparable<Object>) loadedPages.get(i).getRow(high)
								.getValue(pk);

						int compare = pkValue.compareTo(insertedPkValue);

						if (compare > 0) { // Check condition
							high = mid - 1;

						} else if (compare < 0) {
							low = mid + 1;
							if (loadedPages.get(i).getNumUsedRows() == 1) {
								insertPageIndex1 = i;
								insertRowIndex1 = low;
								break;
							}
							pkValueLow = (Comparable<Object>) loadedPages.get(i).getRow(low).getValue(pk);

						} else if (compare == 0) {
							found = true;
							insertPageIndex1 = i;
							insertRowIndex1 = mid;
							break;
						}
						if (low >= high && pkValueLow.compareTo(insertedPkValue) > 0) // law el inserted as8ar mn ely
							// ana wa2ef 3aleh
						{
							insertPageIndex1 = i;
							insertRowIndex1 = low;
							break;
						}
						// de law la2ena el location ely el mafrod ne7ot feh el 7aga
						pkValueHigh = (Comparable<Object>) loadedPages.get(i).getRow(high).getValue(pk);
						if (low >= high && (pkValueHigh.compareTo(insertedPkValue) < 0)
								&& (high == (loadedPages.get(i).getNumUsedRows() - 1))
								&& loadedPages.get(i).getNumUsedRows() < loadedPages.get(i).getNumUsedRows()) {
							insertPageIndex1 = i;
							insertRowIndex1 = high + 1;
							break;
						}
						if (low >= high && pkValueLow.compareTo(insertedPkValue) < 0
								&& high != loadedPages.get(i).getMaxRows() - 1) // el3ak
						{
							insertPageIndex1 = i;
							insertRowIndex1 = low + 1;
							break;
						}
						if (low >= high && pkValueLow.compareTo(insertedPkValue) < 0
								&& high == loadedPages.get(i).getMaxRows() - 1) // el3ak
						{

							break;
						}

						// m7tag shift
					} // de law el page msh full, fa insert 3ady fe a5er el page
					if (!found && loadedPages.get(i).getMaxRows() > loadedPages.get(i).getNumUsedRows()
							&& insertPageIndex1 == -1) {
						insertPageIndex1 = i;
						insertRowIndex1 = low;
						break;
					}

					i++;
				}

				// el page full, fa 3ayzeen page gdeda
				// check en law el insertpage index = size bta3 el loaded pages yeb2a create new
				// page,
				// wel insert row index heya awel row fel page el gdeda
				if (!found && insertRowIndex1 == -1 && insertPageIndex1 == -1) {
					insertPageIndex1 = loadedPages.size();
					insertRowIndex1 = 0;
				}

				// End of suggested change!!!
			}

			if (!primaryexists || found)
				throw new DBAppException("primary doesnt exist or duplicate");

			// Insert row into table

			Row newRow = new Row(htblColNameValue);
			int s1 = insertPageIndex1;

			// create new page, assign it to the table
			// ely da5ely max, w mafesh page lel max dah, fa create new page
			if (insertPageIndex1 == loadedPages.size()) {
				Page newPage = new Page(loadedTable);
				newPage.addRow(newRow, insertRowIndex1);
				loadedPages.add(newPage); // new error fixing kimo
			}
			// insert fel nos aw a5er page feha makan
			else {
				while (s1 < loadedPages.size() - 1) // loop 3ala kol el pages ely ba3d ely hamel feha insert, shifting
				{
					if (loadedPages.get(s1).getNumUsedRows() >= loadedPages.get(s1).getMaxRows()) // >: nozbot el
						// shifting =: awel
						// mara bned5ol
						// nshof law feha
						// makan wla la
					{
						Row shiftedRow = loadedPages.get(s1).getRow(loadedPages.get(s1).getMaxRows() - 1);
						loadedPages.get(s1).deleteRow(shiftedRow);
						loadedPages.get(s1 + 1).addRow(shiftedRow, 0);
						if (loadedPages.get(s1 + 1).getNumUsedRows() <= loadedPages.get(s1 + 1).getMaxRows())
							break; // law el page ely ba3deha msh me7taga shifting break;

					} else
						break; // law el page ely ana feha msh me7taga shifting
					s1++;
				}
				// ba3d kol el shifting betshof a5er page me3adeya el max wla la, 3shan te
				// create new page
				if (loadedPages.get(loadedPages.size() - 1).getNumUsedRows() > loadedPages.get(loadedPages.size() - 1)
						.getMaxRows()) {
					Page newPage = new Page(loadedTable);
					Row shiftedRow = loadedPages.get(loadedPages.size() - 1)
							.getRow(loadedPages.get(loadedPages.size() - 1).getMaxRows());
					loadedPages.get(loadedPages.size() - 1).deleteRow(shiftedRow);
					newPage.addRow(shiftedRow, 0);
				}
				// insert ba2a.
				loadedPages.get(s1).addRow(newRow, insertRowIndex1);
			}

			
			String[] res = hasIndex(strTableName);
			
			if(res.length == 3)
			{
				loadIndex(strTableName, currIndexName);
				loadedOctree = new OctTree(strTableName, res);
				for (Page p : loadedPages) {
					for (Row r : p.getRows()) {
						Object x = r.getValue(res[0]);
						Object y = r.getValue(res[1]);
						Object z = r.getValue(res[2]);
						Object pkv = r.getValue(pk);
						Object ref = p.getPageName();
						loadedOctree.insert(x, y, z, ref, pk);
					}
				}
				loadedOctree.printOctTree(loadedOctree.getRoot(), "");
				
				saveIndex();
			}


			
			
			//ArrayList<String> indexCols = indexCols(strTableName, htblColNameValue);
			//reCreateIndex(strTableName);
//			if(indexCols != null) {
//				loadIndex(strTableName, indexCols.get(3));
//				Object x = indexCols.get(0);
//				Object y = indexCols.get(1);
//				Object z = indexCols.get(2);
//				//Object pk = r.getValue(pkname);
//				Object ref = loadedPages.get(s1);
//				loadedOctree.insert(x, y, z, ref, pk);
//				saveIndex();
//			}
			
//			String[] indexColNames = new String[3];
//			indexColNames[0] = indexCols.get(0);
//			indexColNames[1] = indexCols.get(1);
//			indexColNames[2] = indexCols.get(2);
//			
//			if(hasIndex(strTableName, htblColNameValue)) {
//				loadIndex(strTableName, indexCols.get(3));
//				loadedOctree = new OctTree(strTableName, indexColNames);
//				loadTable(strTableName);
//				loadPages(loadedTable);
//				String indexName = "";
//				indexName = indexColNames[0] + indexColNames[1] + indexColNames[2] + strTableName + ".class";
//

				savePages();
				saveTable();
				
//				// serialize to disk
//				File indexFile = new File(indexName + strTableName + ".class");
//				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(indexFile));
//				out.writeObject(loadedOctree);
//				out.close();
				
				//loadedOctree.printOctTree(loadedOctree.getRoot(), "");
			//}
			
			

		} catch (Exception e) {
			System.out.println(
					"An error occurred while inserting row into table " + strTableName + ": " + e.getMessage());
			throw new DBAppException();
		}

	}


public String[] hasIndex(String strTableName) throws IOException
{
	

	String[] res = new String[3];
	int i=0;
	BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
		String line = br.readLine();
		boolean firstloop =true;
		while (line != null) 
		{

			if (firstloop)
				line = br.readLine();

			firstloop = false;

			String[] content = line.split(",");

			if(content[0].equals(strTableName))
			{

				if (!content[4].equals("Null"))
				{
					res[i]=(content[1]);
					currIndexName = content[4];
					i++;
					
				}

			}
			line = br.readLine();

		}
		br.close();
	
	
		//loadIndex(strTableName, indexName);
		return res;
	


}

	// following method updates one row only
	// htblColNameValue holds the key and new value
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName, String strClusteringKeyValue,
			Hashtable<String, Object> htblColNameValue) throws DBAppException {
		try {

			loadTable(strTableName);
			loadPages(loadedTable);

			if (loadedPages.size() == 0) {
				throw new DBAppException("The table is empty");
			}

			boolean valid = validateHashtable(strTableName, htblColNameValue);
			if (!valid)
				throw new DBAppException("htbl not valid");

			Enumeration<String> columnNames = htblColNameValue.keys();

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			line = br.readLine();
			String pkName = "";
			String pkType = "";
			while (line != null) {
				String[] content = line.split(",");
				if (content[0].equals(strTableName) && content[3].equals("true")) {
					pkName = content[1];
					pkType = content[2];

				}

				line = br.readLine();
			}

			if (htblColNameValue.containsKey(pkName))
				throw new DBAppException("Cannot update primary key!");

			boolean found = false;

			int lowPage = 0;
			int highPage = loadedPages.size() - 1;
			int midPage = (lowPage + highPage) / 2;
			int lowRow = 0;
			int highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
			int midRow = (lowRow + highRow) / 2;
			while (!found && lowPage <= highPage && lowRow <= highRow) {
				Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(midPage).getRow(midRow).getValue(pkName);
				int compare = compare(pkValue, strClusteringKeyValue);

				if (compare > 0) {
					Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(lowRow).getValue(pkName);
					int compare2 = compare(pkValue2, strClusteringKeyValue);
					if (compare2 > 0) {
						highPage = midPage - 1;
						midPage = (lowPage + highPage) / 2;
						highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
						midRow = (lowRow + highRow) / 2;
					} else {
						highRow = midRow - 1;
						midRow = (lowRow + highRow) / 2;
					}
				} else if (compare < 0) {
					Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(highRow).getValue(pkName);
					int compare2 = compare(pkValue2, strClusteringKeyValue);
					if (compare2 < 0) {
						lowPage = midPage + 1;
						midPage = (lowPage + highPage) / 2;
						highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
						midRow = (lowRow + highRow) / 2;
					} else {
						lowRow = midRow + 1;
						midRow = (lowRow + highRow) / 2;
					}
				} else {
					while (columnNames.hasMoreElements()) {
						found = true;
						String columnName = columnNames.nextElement();
						Object columnValue = htblColNameValue.get(columnName);

						// check for the the data type

						// update el 7aga

						loadedPages.get(midPage).getRow(midRow).addValue(columnName, columnValue);
					}

				}

			}
			if (!found)
				throw new DBAppException("Pk is not found");

			//reCreateIndex(strTableName);
			savePages();
			saveTable();

		} catch (Exception e) {
			e.printStackTrace();
			throw new DBAppException(e.getMessage());
		}
	}

	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search
	// to identify which rows/tuples to delete.
	// htblColNameValue enteries are ANDED together

public Hashtable<String,String[]> tableHasIndex(String table) throws IOException {
	
	String[] names = null;
	
	
	
	Hashtable<String,String[]> ret = new Hashtable<String, String[]>( );
	
	
	for(int k=0;k<5;k++) {
		int j=0;
		int i=0;
		boolean first=true;
		boolean firstloop =true;
		BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
		String[] indices = null;
		String line = br.readLine();
	while (line != null) 
	{

		if (firstloop)
			line = br.readLine();

		firstloop = false;

		String[] content = line.split(",");

		//					if(!(columnNames.hasMoreElements()) )
		//						break;

		if(content[0].equals(table))
		{



			if (!content[4].equals("Null") && (unique(names,content[1])||(i<3 && i>0)))
			{
				if(first) {
				names[j]=content[4];
				indices[i] = content[1];
				i++;
				j++;
				
				first=false;
				}
				else {
					if(content[4].equals(names[j])) {
						indices[i] = content[1];
						i++;
					}
				}
				
			}

		}
		line = br.readLine();
		if(i==3) {
			first=true;
			int r = j-1;
			ret.put(names[r], indices);
			indices = null;
			i=0;
		}
	}
	br.close();


	}
	return ret;
}


public boolean unique(String[] a,String b) {
	for(int i=0;i<a.length;i++) {
		if(a[i].equals(b))
			return false;
	}
	return true;
}
public void reCreateIndex(String strTableName) throws FileNotFoundException, ClassNotFoundException, IOException, DBAppException {
	if(!tableHasIndex(strTableName).isEmpty()) {
		Hashtable<String,String[]> use = new Hashtable<String,String[]>();
		use = tableHasIndex(strTableName);
		Enumeration<String> IndexName = use.keys();
		
		
		while(IndexName.hasMoreElements()){
			String Index = IndexName.nextElement();
			String[] cols = use.get(Index);
			String inName = getIndexName(Index,cols[0]);
			loadIndex(strTableName,inName);
			createIndex(strTableName,cols);
		}
		
	}
}
public String getIndexName(String table,String col1) throws IOException {
	boolean firstloop =true;
	BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
	String[] indices = null;
	String line = br.readLine();
while (line != null) 
{

	if (firstloop)
		line = br.readLine();

	firstloop = false;

	String[] content = line.split(",");

	//					if(!(columnNames.hasMoreElements()) )
	//						break;

	if(content[0].equals(table))
	{



		if (content[1].equals(col1))
		{
			return content[4];
			
		}

	}
	line = br.readLine();
	
}
br.close();
return null;


}
public boolean hasIndex(String strTableName,Hashtable<String,Object> htblColNameValue) throws IOException, ClassNotFoundException {
		Enumeration<String> columnNames = htblColNameValue.keys();

		String indexName = "";
		int i=0;

		while(columnNames.hasMoreElements()) {


			String insertedColName = columnNames.nextElement();
			//Object insertedvalue = htblColNameValue.get(insertedColName);
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			boolean firstloop =true;
			while (line != null) 
			{

				if (firstloop)
					line = br.readLine();

				firstloop = false;

				String[] content = line.split(",");

				//					if(!(columnNames.hasMoreElements()) )
				//						break;

				if(content[0].equals(strTableName))
				{



					if (content[1].equals(insertedColName)&& !content[4].equals("Null"))
					{
						i++;
						indexName = content[4];
					}

				}
				line = br.readLine();

			}
			br.close();
		}
		if(i==3) {
			loadIndex(strTableName, indexName);
			return true;
		}



		return false;
	}
	public void deleteUsingIndex(String strTableName,
			Hashtable<String,Object> htblColNameValue) throws ClassNotFoundException, IOException, DBAppException {
		/////////////////////////HERRRREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
		Enumeration<String> columnNames = htblColNameValue.keys();
		String columnName;
		Object columnValue;
		Object[] columnsSorted = new Object[3];
		columnNames = htblColNameValue.keys();
		Node UseNode;
		Vector<Point> deletePoints = null;
		
		while (columnNames.hasMoreElements()) {
			columnName = columnNames.nextElement();
			columnValue = htblColNameValue.get(columnName);

			if(columnName.equals(loadedOctree.getxName())) {
				columnsSorted[0]=columnValue;
			}
			if(columnName.equals(loadedOctree.getyName())) {
				columnsSorted[1]=columnValue;
			}
			if(columnName.equals(loadedOctree.getzName())) {
				columnsSorted[2]=columnValue;
			}
		}
		if(loadedOctree.getRoot().search(columnsSorted)) {
			UseNode= loadedOctree.getRoot().get(columnsSorted, false);
			for(int j=0;j<UseNode.getRows().size();j++) {
				if(UseNode.getRows().get(j).get(0).getX().equals(columnsSorted[0])
						&&UseNode.getRows().get(j).get(0).getY().equals(columnsSorted[1])
						&&UseNode.getRows().get(j).get(0).getZ().equals(columnsSorted[2])) {
					deletePoints= UseNode.getRows().get(j);
					

				}
			}
			String table = loadedOctree.getName();
			
			///hena khod el page number mn el refrence beta3 kol point fel vector w rooh shelhom mn el pages dol
			loadedOctree.getRoot().del(deletePoints);
			
			//////////////////////////////////////////////////////////////
			//Now You have the points you want to delete in the array deletePoints, 
			//now you need to remove them from the node and use their references to delete them from the table itself
			
			Vector<Object> pksToDelete = new Vector<Object>();
			for(Point p : deletePoints)
				pksToDelete.add(p.getPk()); //adds all the pks to this vector
			
			
			loadTable(table);
			loadPagesNeeded(deletePoints); //new loadPages method which loads only the pages we need
			String pkColName = getPrimaryKeyColName(strTableName); //new method, gets col name of pk
			
			for(int i=0 ; i<loadedPages.size() ; i++)
			{
				Page currPage = loadedPages.get(i);
				for(int j=0 ; j<currPage.getRows().size() ; j++)
				{
					Row currRow = currPage.getRow(j);
					Object pk = currRow.getValue(pkColName);
					if(pksToDelete.contains(pk))
					{
						currPage.deleteRow(currRow); //deletes the row if this row's pk value is found in the pkstodelete vector
						j--;
					}
				}
			}
		
			
			loadedOctree.printOctTree(loadedOctree.getRoot(), "");
			
			//reCreateIndex(strTableName);
			saveIndex();
			savePages();
			saveTable();
			
			
			
			
		}

	}

	public void deleteFromTable(String strTableName,

			Hashtable<String,Object> htblColNameValue)
					throws DBAppException, IOException, ClassNotFoundException
	{

		// find el pages ele feha el 7aga
		

		boolean valid = validateHashtable(strTableName, htblColNameValue);
		if (!valid)
			throw new DBAppException("htbl not valid");
		if(hasIndex(strTableName,htblColNameValue)) {
			deleteUsingIndex(strTableName,htblColNameValue);
			return;
		}
		loadTable(strTableName);
		loadPages(loadedTable);
		boolean hasPk = checkForPrimaryKey(strTableName, htblColNameValue);
		boolean firstloop = true;
		Vector<Row> tmpRows = new Vector<Row>();

		//		Enumeration<String> columnNames = htblColNameValue.keys();
		//			String columnName = columnNames.nextElement();
		//			Object columnValue = htblColNameValue.get(columnName);

		if (loadedPages.size() == 0) {
			throw new DBAppException("The table is empty");
		}

		if (hasPk) {
			// binary search

			boolean found = false;

			int lowPage = 0;
			int highPage = loadedPages.size() - 1;
			int midPage = (lowPage + highPage) / 2;

			int lowRow = 0;
			int highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
			int midRow = (lowRow + highRow) / 2;
			// 5ale el pk fel colName
			String columnName = getPrimaryKey(strTableName, htblColNameValue);
			Object columnValue = htblColNameValue.get(columnName);

			//				if(!loadedTable.getPages().contains(columnValue))
			//					throw new DBAppException();
			//				
			//				
			//				for(int i=0;i<loadedTable.getPages().size(); i++)
			//				{
			//					Page p= loadedTable.getPages().get(i);
			//					
			//				}

			// law el value ely badawar 3aleha aslan msh 3andy?? daniela
			while (!found && lowPage <= highPage && lowRow <= highRow) {

				Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(midPage).getRow(midRow)
						.getValue(columnName);
				int compare = pkValue.compareTo(columnValue);
				if (compare > 0) {
					Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(lowRow)
							.getValue(columnName);
					int compare2 = pkValue2.compareTo(columnValue);
					if (compare2 > 0) {
						highPage = midPage - 1;
						midPage = (lowPage + highPage) / 2;
						highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
						midRow = (lowRow + highRow) / 2;
					} else {
						highRow = midRow - 1;
						midRow = (lowRow + highRow) / 2;
					}
				} else if (compare < 0) {
					Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(highRow)
							.getValue(columnName);
					int compare2 = pkValue2.compareTo(columnValue);
					if (compare2 < 0) {
						lowPage = midPage + 1;
						midPage = (lowPage + highPage) / 2;
						highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
						midRow = (lowRow + highRow) / 2;
					} else {
						lowRow = midRow + 1;
						midRow = (lowRow + highRow) / 2;
					}
				} else // b2o equal le b3d 5las

				{
					// found immediately check b2et el cases

					found = true;
					boolean hnmsa7 = true;
					Enumeration<String> columnNames = htblColNameValue.keys();
					while (columnNames.hasMoreElements()) {

						columnName = columnNames.nextElement();
						columnValue = htblColNameValue.get(columnName);

						if (!(loadedPages.get(midPage).getRow(midRow).getValue(columnName).equals(columnValue))) {
							hnmsa7 = false;
							break;
						}

					}
					if (hnmsa7) // lw kol el conditions kanet 7lwa fa mfesh 7aga 3'yret el flag hmsa7 el row da
					{
						loadedPages.get(midPage).deleteRow(loadedPages.get(midPage).getRow(midRow));

						// if the page is empty ba2a :)

						if (loadedPages.get(midPage).isEmpty()) {

							// wa5deno mn ta7t m3rfsh sa7 wla eh el nezam
							File pageFile = new File((loadedPages.get(midPage)).getPageName() + ".class");
							loadedTable.getPages().remove((loadedPages.get(midPage)).getPageName());
							loadedPages.remove((loadedPages.get(midPage)));
							pageFile.delete();

						}

						// upshift mn page le ele ablaha

						if (loadedPages.size() > midPage + 1) {
							if (!(loadedPages.get(midPage + 1).isEmpty()))

							{
								Row shift = loadedPages.get(midPage + 1).getRow(0);

								//									this.insertIntoTable(loadedTable.getTableName(), shift);
								loadedPages.get(midPage).addRow(shift, (loadedPages.get(midPage).getNumUsedRows()));

								loadedPages.get(midPage + 1).deleteRowAtIndex(0);

								if (loadedPages.get(midPage + 1).isEmpty()) {

									// wa5deno mn ta7t m3rfsh sa7 wla eh el nezam
									File pageFile = new File((loadedPages.get(midPage + 1)).getPageName() + ".class");
									loadedTable.getPages().remove((loadedPages.get(midPage + 1)).getPageName());
									loadedPages.remove((loadedPages.get(midPage + 1)));
									pageFile.delete();

								}

							}
						}

					}

				}
			}
			//reCreateIndex(strTableName);

			savePages();
			saveTable();

		}

		else {
			Enumeration<String> columnNames = htblColNameValue.keys();
			String columnName;
			Object columnValue;

			columnNames = htblColNameValue.keys();
			while (columnNames.hasMoreElements()) {
				columnName = columnNames.nextElement();
				columnValue = htblColNameValue.get(columnName);

				if (firstloop) // enta fel first loop
				{

					for (Page p : loadedPages) {
						for (int i = 0; i < p.getNumUsedRows(); i++) {
							Row r = p.getRow(i);
							if (r.getValue(columnName).equals(columnValue))
								tmpRows.add(r);
						}
					}
					firstloop = false;

				} else // enta msh fel first loop
				{
					for (int i = 0; i < tmpRows.size(); i++) {
						Row r = tmpRows.get(i);
						if (!(r.getValue(columnName).equals(columnValue)))// law el second column doesnt satisfy the
							// condition, remove the row from tmp rows
						{
							tmpRows.remove(r);
							i--;
						}
					}
				}

			}

			// tle3t bara el while loop, no more conditions
			// start deleting the rows
			for (int i = 0; i < loadedPages.size(); i++) {
				Page p = loadedPages.get(i);
				for (int j = 0; j < tmpRows.size(); j++) {
					Row r = tmpRows.get(j);
					if (p.getRows().contains(r)) {
						p.deleteRow(r);
						// implement delete the page if empty here
						if (p.isEmpty()) {
							File pageFile = new File(p.getPageName() + ".class");
							loadedTable.getPages().remove(p.getPageName());
							loadedPages.remove(p);
							i--;
							pageFile.delete();
						}

					}

				}

				// upshift men el page ely ba3daha

				if (loadedPages.size() > i + 1) {
					if (!(loadedPages.get(i + 1).isEmpty()))

					{
						Row shift = loadedPages.get(i + 1).getRow(0);

						//						this.insertIntoTable(loadedTable.getTableName(), shift);
						loadedPages.get(i).addRow(shift, (loadedPages.get(i).getNumUsedRows()));

						loadedPages.get(i + 1).deleteRowAtIndex(0);

						if (loadedPages.get(i + 1).isEmpty()) {

							// wa5deno mn ta7t m3rfsh sa7 wla eh el nezam
							File pageFile = new File((loadedPages.get(i + 1)).getPageName() + ".class");
							loadedTable.getPages().remove((loadedPages.get(i + 1)).getPageName());
							loadedPages.remove((loadedPages.get(i + 1)));
							pageFile.delete();

						}

					}
				}

			}

			//reCreateIndex(strTableName);
			savePages();
			saveTable();

			if (firstDeletion) {
				firstDeletion = false;
				this.deleteFromTable(strTableName, htblColNameValue);
				//reCreateIndex(strTableName);

			}

		}

	}

	public boolean hasIndexSelect(String strTableName, SQLTerm[] arrSQLTerms, String[] strarrOperators) throws IOException, ClassNotFoundException
	{
		for(int i=0 ; i<strarrOperators.length ; i++)
		{
			if(!strarrOperators[i].equals("AND"))
				return false;
		}
		
		if(arrSQLTerms.length!=3) //assume en mafesh 2 conditions on same column
			return false;

		
		
		String indexName = "";
		int i=0;

		for(int j=0;j<arrSQLTerms.length; j++) 
		{


			String insertedColName = arrSQLTerms[j]._strColumnName;
			//Object insertedvalue = htblColNameValue.get(insertedColName);
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			boolean firstloop =true;
			while (line != null) 
			{

				if (firstloop)
					line = br.readLine();

				firstloop = false;

				String[] content = line.split(",");

				if(content[0].equals(strTableName))
				{

					if (content[1].equals(insertedColName)&& !content[4].equals("Null"))
					{
						i++;
						indexName = content[4];
					}

				}
				 line = br.readLine();

			}
			br.close();
		}
		if(i==3) {
			loadIndex(strTableName, indexName);
			return true;
		}



		return false;
	}
	
	public Iterator selectUsingIndex(SQLTerm[] arrSQLTerms,
			String[] strarrOperators) throws ClassNotFoundException, IOException
	{
		
		
	
		Object[] columnsSorted = new Object[3];
		String [] opSorted = new String[3];
		
		
		Vector<Vector<Point>> selectedPoints = new Vector<Vector<Point>>() ;
		
		
		for(int i=0; i<3; i++)
		{
			String columnName =arrSQLTerms[i]._strColumnName;
			Object columnValue= arrSQLTerms[i]._objValue;
			String op =arrSQLTerms[i]._strOperator;
			
			if(columnName.equals(loadedOctree.getxName())) 
			{
				columnsSorted[0]=columnValue;
				opSorted[0]=op;
			}
			if(columnName.equals(loadedOctree.getyName())) 
			{
				columnsSorted[1]=columnValue;
				opSorted[1]=op;
			}
			if(columnName.equals(loadedOctree.getzName())) 
			{
				columnsSorted[2]=columnValue;
				opSorted[2]=op;
			}
			
			
		}
		
		

			
		
			
					
selectedPoints.addAll( loadedOctree.getRoot().getX(columnsSorted[0], opSorted[0]) );
selectedPoints.addAll( loadedOctree.getRoot().getY(columnsSorted[1], opSorted[1]) );
selectedPoints.addAll( loadedOctree.getRoot().getZ(columnsSorted[2], opSorted[2]) );


String table = loadedOctree.getName();
Vector<Point> goodSelectedPoints= new Vector<Point> ();
Vector<Row> result= new Vector<Row>(); 


for(Vector<Point> v: selectedPoints)
{
	for(Point p: v)
	{
		goodSelectedPoints.add(p);
	}
}
	

Vector<Object> pksToDelete = new Vector<Object>();
for(Point p : goodSelectedPoints)
	pksToDelete.add(p.getPk()); //adds all the pks to this vector


loadTable(table);
loadPagesNeeded(goodSelectedPoints); //new loadPages method which loads only the pages we need
String pkColName = getPrimaryKeyColName(table); //new method, gets col name of pk

for(int i=0 ; i<loadedPages.size() ; i++)
{
	Page currPage = loadedPages.get(i);
	for(int j=0 ; j<currPage.getRows().size() ; j++)
	{
		Row currRow = currPage.getRow(j);
		Object pk = currRow.getValue(pkColName);
		if(pksToDelete.contains(pk))
		{
			result.add(currRow);
			
		}
	}
}


savePages();
saveTable();



Iterator<Row> iterator =result.iterator();

return iterator;
		
		
		
	
			









		
		

	}
		
		
	
	
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
			String[] strarrOperators)
			throws DBAppException, ClassNotFoundException, IOException
	{
		
		if(hasIndexSelect(arrSQLTerms[0]._strTableName, arrSQLTerms, strarrOperators))
		{
			return selectUsingIndex(arrSQLTerms, strarrOperators);
			//return;
		}
		
		
		
		ArrayList<Row> result = new ArrayList<Row>();
		boolean primary = false;
		String pkColumn ="";
		if (arrSQLTerms.length==0)
		{
			throw new DBAppException("arrSQLTerms must not empty");
		}
		//check eno el 4 fehom values
		
		
		String strTableName = arrSQLTerms[0]._strTableName;
		loadTable(strTableName);
		loadPages(loadedTable);
		
		for ( int i=0 ; i<arrSQLTerms.length ;i++ )
		{
			if (arrSQLTerms[i]._strTableName==null || arrSQLTerms[i]._strColumnName==null || arrSQLTerms[i]._strOperator==null || arrSQLTerms[i]._objValue==null )
			{
				throw new DBAppException("missing conditon");
			}
			
			if (arrSQLTerms[i]._strTableName != strTableName)
			{
				throw new DBAppException("Different table names");
			}
			
			
			/////////////////////Validate Me/////////////////////
			String colName = arrSQLTerms[i]._strColumnName;
			boolean valid = false;
			
			

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			while (line != null) 
			{
				String[] content = line.split(",");
			
				if (content[0].equals(strTableName) && content[1].equals(colName)) 
				{
					valid = true;
					if (content[3].equals("true"))
					{
						primary = true;
						pkColumn = colName;
					}

				line = br.readLine();
			}

			if (!valid) {
				br.close();
				throw new DBAppException(
						"inserted column names dont match the table's column names or already has an index");
			}
			///////////////Validated/////////////////////////////
			
//			if (colName == pkColumn )
//			{
//				
//			}
			for (Page p : loadedPages)
			{
				for (Row r : p.getRows())
				{
					
					int operationRes = compareOP(arrSQLTerms[i]._objValue, r.getValue(colName));
					
					String operator = arrSQLTerms[i]._strOperator;
					
					if(i==0)
					{
					switch (operator) {
				    case ">":
				        if (operationRes == 1)
				        {
				        	result.add(r);
				        }
				        break;
				    case ">=":
				    	if (operationRes==0 || operationRes == 1)
				    	{
				        	result.add(r);
				        }
				        break;
				    case "<":
				        if (operationRes == 1)
				        {
				        	result.add(r);
				        }
				        break;
				    case "<=":
				    	if (operationRes==0 || operationRes == -1)
				    	{
				        	result.add(r);
				        }
				        break;
				    case "!=":
				    	if (operationRes==1 || operationRes == -1)
				    	{
				        	result.add(r);
				        }
				        break;
				    case "=":
				    	if (operationRes==0)
				    	{
				        	result.add(r);
				        }
				        break;
				    default:
				    	throw new DBAppException("Unknown operator");
				}

			}
					else
					{
					 String bigOp = strarrOperators[i];
					 
					 switch(bigOp)
					 {
					 case "AND":
						 
					boolean flag=false; 
					for(int d=0; d<result.size(); d++)
					 {
						operationRes = compareOP(arrSQLTerms[i]._objValue,result.get(d));
						switch (operator) {
					    case ">":
					        if (operationRes == 1)
					        {
					        	flag=true;
					        }
					        break;
					    case ">=":
					    	if (operationRes==0 || operationRes == 1)
					    	{
					    		flag=true;
					        }
					        break;
					    case "<":
					        if (operationRes == 1)
					        {
					        	flag=true;
					        }
					        break;
					    case "<=":
					    	if (operationRes==0 || operationRes == -1)
					    	{
					    		flag=true;
					        }
					        break;
					    case "!=":
					    	if (operationRes==1 || operationRes == -1)
					    	{
					    		flag=true;
					        }
					        break;
					    case "=":
					    	if (operationRes==0)
					    	{
					    		flag=true;
					        }
					        break;
					    default:
					    	throw new DBAppException("Unknown operator");
					}
						if(!flag)
						{
							result.remove(r);
						}
					
						
					 }
					break;
					
					 case "OR" :
						  operationRes = compareOP(arrSQLTerms[i]._objValue, r.getValue(colName));
							switch (operator) {
						    case ">":
						        if (operationRes == 1)
						        {
						        	if(!(result.contains(r)))
						        		result.add(r);
						        }
						        break;
						    case ">=":
						    	if (operationRes==0 || operationRes == 1)
						    	{
						    		if(!(result.contains(r)))
						    			result.add(r);
						        }
						        break;
						    case "<":
						        if (operationRes == 1)
						        {
						        	if(!(result.contains(r)))
						    			result.add(r);
						        }
						        break;
						    case "<=":
						    	if (operationRes==0 || operationRes == -1)
						    	{
						    		if(!(result.contains(r)))
						    			result.add(r);
						        }
						        break;
						    case "!=":
						    	if (operationRes==1 || operationRes == -1)
						    	{
						    		if(!(result.contains(r)))
						    			result.add(r);
						        }
						        break;
						    case "=":
						    	if (operationRes==0)
						    	{
						    		if(!(result.contains(r)))
						    			result.add(r);
						        }
						        break;
						    default:
						    	throw new DBAppException("Unknown operator");
						}
							break;
							
							
							
					 case "XOR" :
						 boolean xorFlag = false;
						 
							switch (operator) {
						    case ">":
						        if (operationRes == 1)
						        {
						        	xorFlag=true;
						        }
						        break;
						    case ">=":
						    	if (operationRes==0 || operationRes == 1)
						    	{
						    		xorFlag=true;
						        }
						        break;
						    case "<":
						        if (operationRes == 1)
						        {
						        	xorFlag=true;
						        }
						        break;
						    case "<=":
						    	if (operationRes==0 || operationRes == -1)
						    	{
						    		xorFlag=true;
						        }
						        break;
						    case "!=":
						    	if (operationRes==1 || operationRes == -1)
						    	{
						    		xorFlag=true;
						        }
						        break;
						    case "=":
						    	if (operationRes==0)
						    	{
						    		xorFlag=true;
						        }
						        break;
						    default:
						    	throw new DBAppException("Unknown operator");
						}
							
							if (xorFlag)
							{
								if((result.contains(r)))
									result.remove(r);
								else
									result.add(r);		
							}
							
							break;
							
							
							default: 
								throw new DBAppException("Unknown operator");

					 
					 }
					}
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
						
					}
				}
			}
			
			
			
			
			
			
		}
		
		Iterator<Row> iterator =result.iterator();
		
		return iterator;
		
		
	}



	
	public String getPkColName(String strTableName) throws IOException
	{
		String pkname = "";
		BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			if (content[0].equals(strTableName) && content[3].equals("true"))
			{
				pkname = content[1];
			}

			line = br.readLine();
		}

		

		br.close();
		return pkname;
	}
	

	//	 following method creates an octree
	//	 depending on the count of column names passed.
	//	 If three column names are passed, create an octree.
	//	 If only one or two column names is passed, throw an Exception.
	public void createIndex(String strTableName, String[] strarrColName)
			throws DBAppException, IOException, ClassNotFoundException {
		String pkname = getPkColName(strTableName);

		if (strarrColName.length != 3)
			throw new DBAppException();
		//===============================================================================

		// validate en el 3 column names dol mawgoden lel table dah

		for (int j = 0; j < 3; j++) {
			String insertedColName = strarrColName[j];
			boolean valid = false;

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			while (line != null) {
				String[] content = line.split(",");
				if (content[0].equals(strTableName) && content[1].equals(insertedColName) && content[4].equals("Null")
						&& content[5].equals("Null")) {

					valid = true;
					break;
				}

				line = br.readLine();
			}

			if (!valid) {
				br.close();
				throw new DBAppException(
						"inserted column names dont match the table's column names or already has an index");
			}

			br.close();
		}

		//==================================================================

		// edit the csv content

		
		String indexName = "";
		for (int j = 0; j < 3; j++) {
			String insertedColName = strarrColName[j];
			StringBuilder newMetadata = new StringBuilder();
			indexName = "";
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			while (line != null) {
				String[] content = line.split(",");

				if (content[0].equals(strTableName) && content[1].equals(insertedColName)) {

					for (int i = 0; i < strarrColName.length; i++) {
						indexName += strarrColName[i];
					}

					// edit specific columns in the line
					content[4] = indexName ; //shelna el "Index"
					content[5] = "Octree";
					
					
					
				}

				// append the edited line to the new metadata string
				for (int i = 0; i < content.length; i++) {
					newMetadata.append(content[i]);
					if (i < content.length - 1) {
						newMetadata.append(",");
					}
				}
				newMetadata.append(System.lineSeparator());

				line = br.readLine();

			}

			// write the new metadata string to the metadata file
			try (FileWriter fw = new FileWriter("metadata.csv")) {
				fw.write(newMetadata.toString());
				fw.close();
				
			} catch (IOException e) {
				br.close();
				throw new DBAppException(e.getMessage());
			}

			br.close();
		}
		
		
		

		// create the octree
		//mango
		OctTree octTree = new OctTree(strTableName, strarrColName);
		loadTable(strTableName);
		loadPages(loadedTable);

		for (Page p : loadedPages) {
			for (Row r : p.getRows()) {

				Object x = r.getValue(strarrColName[0]);
				Object y = r.getValue(strarrColName[1]);
				Object z = r.getValue(strarrColName[2]);
				Object pk = r.getValue(pkname);
				Object ref = p.getPageName();
				octTree.insert(x, y, z, ref, pk);

			}
		}

		savePages();
		saveTable();

		// serialize to disk
		File indexFile = new File(indexName + strTableName + ".class");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(indexFile));
		out.writeObject(octTree);
		out.close();
		
		octTree.printOctTree(octTree.getRoot(), "");

	}

	//	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
	//	String[] strarrOperators)
	//	throws DBAppException
	//	{
	//		
	//	}



	//----------------LOADS AND SAVES/HELPER METHODS--------------------------------------

	public String getPrimaryKeyColName(String strTableName) throws IOException {
	
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				String[] content = line.split(",");
				if (content[0].equals(strTableName) && content[3].equals("true")) {
					br.close();
					return content[1];
				}
				line = br.readLine();
			}
			br.close();

		
		return "";
	}
	public static int compareOP(Object o1, Object o2) {
        if (o1 instanceof Integer && o2 instanceof Integer) {
            return Integer.compare((int) o1, (int) o2);
        } else if (o1 instanceof Double && o2 instanceof Double) {
            return Double.compare((double) o1, (double) o2);
        } else if (o1 instanceof String && o2 instanceof String) {
            return ((String) o1).compareTo((String) o2);
        } else if (o1 instanceof Date && o2 instanceof Date) {
            return ((Date) o1).compareTo((Date) o2);
        } else {
            throw new IllegalArgumentException("Objects must be of the same type");
        }
    }
	
	
	
	public void loadPages(Table table) throws ClassNotFoundException, IOException {
		loadedPages = new Vector<Page>();
		Vector<String> pages = table.getPages();
		for (String s : pages) {
			// load the page file from disk
			File pageFile = new File(s + ".class");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(pageFile));
			Page page = (Page) in.readObject();
			loadedPages.add(page);
			in.close();

		}
	}
	
	public void loadPagesNeeded(Vector<Point> points) throws IOException, ClassNotFoundException
	{
		loadedPages = new Vector<Page>();
		Vector<String> names = new Vector<String>();
		names.add((String) points.get(0).getRef()); //adds awel page name
		
		for(int i=1; i<points.size() ; i++)
		{
			if( !names.contains(points.get(i).getRef())  ) //law el page name dah msh already added
				names.add((String) points.get(i).getRef());
		}
		
		for(String s : names)
		{
			// load the page file from disk
			File pageFile = new File(s + ".class");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(pageFile));
			Page page = (Page) in.readObject();
			loadedPages.add(page);
			in.close();
		}
	}

	public void savePages() throws IOException {
		for (Page p : loadedPages) {
			File pageFile = new File(p.getPageName() + ".class");
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pageFile));
			out.writeObject(p);
			out.close();
		}
		loadedPages = null;
	}

	public void loadTable(String tableName) throws ClassNotFoundException, IOException {
		loadedTable = new Table(tableName);

		File tableFile = new File(tableName + ".class");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(tableFile));
		Table out = (Table) in.readObject();
		loadedTable = out;
		in.close();

	}

	public void saveTable() throws IOException {
		File tableFile = new File(loadedTable.getTableName() + ".class");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tableFile));
		out.writeObject(loadedTable);
		out.close();

		loadedTable = null;
	}

	public void loadIndex(String tableName, String indexName) throws FileNotFoundException, IOException, ClassNotFoundException
	{


		File indexFile = new File(indexName + tableName + ".class");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(indexFile));
		OctTree out = (OctTree) in.readObject();
		loadedOctree = out;
		loadedIndexName = indexName;
		in.close();
	}

	public void saveIndex() throws IOException
	{
		File indexFile = new File(loadedIndexName + loadedTable.getTableName() + ".class");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(indexFile));
		out.writeObject(loadedOctree);
		out.close();
	}

	public static Date parseStringToDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return format.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isValidDate(Date date, Date min, Date max) {
		return date.compareTo(min) >= 0 && max.compareTo(date) >= 0;
	}

	public boolean checkTypeMinMax(Enumeration<String> columnNames, Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameMin, Hashtable<String, String> htblColNameMax) {
		boolean flag = true;
		while (columnNames.hasMoreElements()) {
			String columnName = columnNames.nextElement();
			String columnType = htblColNameType.get(columnName);
			String min = htblColNameMin.get(columnName);
			String max = htblColNameMax.get(columnName);
			if (columnType == "java.lang.Integer") {
				try {
					Integer.parseInt(min);
					Integer.parseInt(max);
				} catch (NumberFormatException e) {
					return false;
				}
				if (Integer.parseInt(min) > Integer.parseInt(max))
					return false;
			}
			if (columnType == "java.lang.Double") {
				try {
					Double.parseDouble(min);
					Double.parseDouble(max);
				} catch (NumberFormatException e) {
					return false;
				}
				if (Double.parseDouble(min) > Double.parseDouble(max))
					return false;
			}
			if (columnType == "java.lang.String") {

				if (min.compareTo(max) < 0) {
					return false;
				}
			}
			if (columnType == "java.util.Date" || columnType == "java.text.SimpleDateFormat") {
				String format = "YYYY-MM-DD";
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				dateFormat.setLenient(true);
				Date date1;
				Date date2;
				try {
					dateFormat.parse(min.trim());
					dateFormat.parse(max.trim());
					date1 = dateFormat.parse(min);
					date2 = dateFormat.parse(max);
				} catch (ParseException e) {
					return false;
				}

				if (date1.compareTo(date2) > 0) {
					return false;

				}
			}
		}
		return true;
	}

	private void insertNullValues(Hashtable<String, Object> htblColNameValue, String strTableName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			ArrayList<String> missingAtt = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if (values[0].equals(strTableName)) {
					missingAtt.add(values[1]);
				}
			}
			br.close();

			Enumeration<String> e = htblColNameValue.keys();
			while (e.hasMoreElements()) {
				String s = (String) e.nextElement();
				if (missingAtt.contains(s)) {
					missingAtt.remove(s);
				}
			}

			for (String s : missingAtt) {
				htblColNameValue.put(s, new nullWrapper());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean checkForPrimaryKey(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws IOException {
		Enumeration<String> columnNames = htblColNameValue.keys();
		while (columnNames.hasMoreElements()) {
			String columnName = columnNames.nextElement();
			Object columnValue = htblColNameValue.get(columnName);

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				String[] content = line.split(",");
				if (content[0].equals(strTableName) && content[1].equals(columnName) && content[3].equals("true")) {

					br.close();
					return true;
				}
				line = br.readLine();
			}
			br.close();

		}
		return false;
	}

	public String getPrimaryKey(String strTableName, Hashtable<String, Object> htblColNameValue) throws IOException {
		Enumeration<String> columnNames = htblColNameValue.keys();
		while (columnNames.hasMoreElements()) {
			String columnName = columnNames.nextElement();
			Object columnValue = htblColNameValue.get(columnName);

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				String[] content = line.split(",");
				if (content[0].equals(strTableName) && content[1].equals(columnName) && content[3].equals("true")) {
					br.close();
					return content[1];
				}
				line = br.readLine();
			}
			br.close();

		}
		return "";
	}

	public boolean validateHashtable(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws IOException {

		Hashtable<String, Object> tmphtbl = new Hashtable<String, Object>();
		Enumeration<String> columnNamestmp = htblColNameValue.keys();
		//		String columnName = columnNamestmp.nextElement();
		//		Object columnValue = htblColNameValue.get(columnName);
		while (columnNamestmp.hasMoreElements()) {
			String columnName = columnNamestmp.nextElement();
			Object columnValue = htblColNameValue.get(columnName);
			tmphtbl.put(columnName, columnValue);
			//			columnNamestmp.nextElement();
		}

		Enumeration<String> columnNames = tmphtbl.keys();
		boolean firstloop = true;
		while (columnNames.hasMoreElements()) // ben loop 3ala el hashtable
		{
			String insertedColName = columnNames.nextElement();
			Object insertedvalue = tmphtbl.get(insertedColName);

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();

			if (firstloop)
				line = br.readLine();

			firstloop = false;
			while (line != null) {
				String[] content = line.split(",");

				if (content[0].equals(strTableName) && insertedColName.equals(content[1]))// law el line ely ana masko
					// mawgod fel htbl
				{

					if (content[2].equals(insertedvalue.getClass().getName())) {
						if ((insertedvalue.getClass().getName()).equals("java.lang.Integer")) {
							int min = Integer.parseInt(content[6]);
							int max = Integer.parseInt(content[7]);

							if ((int) insertedvalue >= min && (int) insertedvalue <= max) {
								tmphtbl.remove(insertedColName);// remove el entry mn htbl law valid
								break;
								//									if (content [3] == "TRUE")
								//									{
								//										//new
								//										pk = content[1];
								//										insertedPkValue = htblColNameValue.get(pk);
								//										if(!insertedPkValue.equals(null)) {
								//											primaryexists = true;
								//										}
								//									}
							}
						}

						else if ((insertedvalue.getClass().getName()).equals("java.lang.Double")) {

							int min = Integer.parseInt(content[6]);
							int max = Integer.parseInt(content[7]);

							if ((double) insertedvalue >= min && (double) insertedvalue <= max) {
								tmphtbl.remove(insertedColName);// remove el entry mn htbl law valid
								break;
								//									if (content [3] == "TRUE")
								//									{
								//										//new
								//										pk = content[1];
								//										insertedPkValue = htblColNameValue.get(pk);
								//										if(!insertedPkValue.equals(null)) {
								//											primaryexists = true;
								//										}
								//									}
							}

						}

						else if ((insertedvalue.getClass().getName()).equals("java.lang.String")) {
							String min = content[6];
							String max = content[7];
							String insertedvalstring = (String) insertedvalue;
							int comparemin = insertedvalstring.compareToIgnoreCase(min);
							int comparemax = insertedvalstring.compareToIgnoreCase(max);
							if (comparemin >= 0 && comparemax <= 0) {
								tmphtbl.remove(insertedColName);// remove el entry mn htbl law valid
								break;
								//									if (content [3] == "TRUE")
								//									{
								//										//new
								//										pk = content[1];
								//										insertedPkValue = htblColNameValue.get(pk);
								//										if(!insertedPkValue.equals(null)) {
								//											primaryexists = true;
								//										}
								//									}
							}
						} else {
							tmphtbl.remove(insertedColName);// remove el entry mn htbl law valid
						}

					}

				}

				line = br.readLine();
			}

			br.close();

		} // a5er el while bta3et el htbl

		if (tmphtbl.isEmpty())
			return true;
		else
			return false;

	}

	public int compare(Object x, String y) {

		if (x instanceof String)
			return ((String) x).compareTo(y);
		if (x instanceof Double) {
			Double yy = Double.parseDouble(y);
			if ((Double) x > yy)
				return 1;
			else if ((Double) x < yy)
				return -1;
			else
				return 0;
		}

		Integer yy = Integer.parseInt(y);
		if ((Integer) x > yy)
			return 1;
		else if ((Integer) x < yy)
			return -1;

		return 0;

	}

	public void printTable(String tablename) throws ClassNotFoundException, IOException {
        loadTable(tablename);
        loadPages(loadedTable);
        for (int j = 0; j < loadedPages.size(); j++) {
            Page p = loadedPages.get(j);
            System.out.println("Start of page");
            for (int i = 0; i < p.getNumUsedRows(); i++) {
                p.getRow(i).printRow();
                System.out.println(" ");
            }
        }
        savePages();
        saveTable();


    }



	public ArrayList<String> indexCols(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws IOException, ClassNotFoundException {
		Enumeration<String> columnNames = htblColNameValue.keys();

		String indexName = "";
		ArrayList<String> indexCols = new ArrayList<String>();

		while (columnNames.hasMoreElements()) {

			String insertedColName = columnNames.nextElement();
			// Object insertedvalue = htblColNameValue.get(insertedColName);
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				String[] content = line.split(",");
				// if(!(columnNames.hasMoreElements()) )
				// break;
				if (content[0].equals(strTableName)) {
					if (content[1].equals(insertedColName) && !content[4].equals("Null")) {
						indexCols.add(content[1]);
						indexName = content[4];
					}
				}
				line = br.readLine();
			}
			br.close();
		}
		if (indexCols.size() == 3) {
			loadIndex(strTableName, indexName);
			indexCols.add(indexName);
			return indexCols;
		}
		return null;
	}





}

