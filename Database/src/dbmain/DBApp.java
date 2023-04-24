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



public class DBApp implements Serializable{
	private static final long serialVersionUID = 1L;
	private Vector<Page> loadedPages;
	private Table loadedTable;
	
	
	
	
	
	
	
	public DBApp() {
		
	}

	// this does whatever initialization you would like
	// or leave it empty if there is no code you want to
	// execute at application startup
	public void init( )
	{
		
	}
		
	
	// following method creates one table only
	// strClusteringKeyColumn is the name of the column that will be the primary
	// key and the clustering column as well. The data type of that column will
	// be passed in htblColNameType
	// htblColNameValue will have the column name as key and the data
	// type as value
	// htblColNameMin and htblColNameMax for passing minimum and maximum values
	// for data in the column. Key is the name of the column
	public void createTable(String strTableName,
	String strClusteringKeyColumn,
	Hashtable<String,String> htblColNameType,                              
	Hashtable<String,String> htblColNameMin,
	Hashtable<String,String> htblColNameMax )throws DBAppException
	{
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
		        Enumeration<String> columnNames = htblColNameType.keys();
		        while (columnNames.hasMoreElements()) {
		            String columnName = columnNames.nextElement();
		            String columnType = htblColNameType.get(columnName);
		            String min = htblColNameMin.get(columnName);
		            String max = htblColNameMax.get(columnName);
		            boolean clusteringKey = columnName.equals(strClusteringKeyColumn);
		            String indexed = "Null"; 

		            writer.write(strTableName + "," + columnName + "," + columnType + "," + clusteringKey + "," + indexed + "," + indexed + "," + min + "," + max + "\n");
		        }

		        // Close metadata file
		        writer.close();
		    
		        System.out.println("Table " + strTableName + " created successfully.");
		 } catch (IOException e) {
			 e.printStackTrace();
		    }
		
	}
	
	public static void main(String[] args) throws DBAppException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		Hashtable<String,String> htblColNameType = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMin = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMax = new Hashtable<String, String>( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		htblColNameMin.put("id", "0");
		htblColNameMin.put("name", "0");
		htblColNameMin.put("gpa", "0");
		htblColNameMax.put("id", "1000");
		htblColNameMax.put("name", "99");
		htblColNameMax.put("gpa", "4");
		
		dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
		//manga awy
		String manga = "manga";
		System.out.println(manga);
	}
	

	

	// following method inserts one row only.
	// htblColNameValue must include a value for the primary key
	public void insertIntoTable(String strTableName,
	Hashtable<String,Object> htblColNameValue)
	throws DBAppException
	{
		    try {
		        // Check if table exists
		        File tableFile = new File(strTableName + ".class");
		        if (!tableFile.exists()) {
		            System.out.println("Table " + strTableName + " does not exist.");
		            throw new DBAppException();
		        }

		        // Load table from disk
		        ObjectInputStream in = new ObjectInputStream(new FileInputStream(tableFile));
		        Table table = (Table) in.readObject();
		        in.close();
		        
		        
		        
		        boolean primaryexists = false;
				
				BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
				String line = br.readLine();
				line = br.readLine();
				Enumeration<String> columnNames = htblColNameValue.keys();
				Object insertedPkValue = null;
				String tableName;
				String pk = null;
				int insertRowIndex=0;
				int insertPageIndex=0;
				while (line != null) 
				{
					boolean flag = false;
					String[] content = line.split(",");
					
					if(columnNames.hasMoreElements()== false)
						break;
					
					if(content[0] == strTableName)
					{
						tableName = strTableName;
						
						String insertedColName = columnNames.nextElement();
						Object insertedvalue = htblColNameValue.get(insertedColName);

						if (content[1] == insertedColName)
						{
							if(content[2] == insertedvalue.getClass().toString())
							{
								if(insertedvalue.getClass().toString() == "java.lang.Double" ||
										insertedvalue.getClass().toString() == "java.lang.Integer")
								{
									int min = Integer.parseInt(content[6]);
									int max = Integer.parseInt(content[7]);

									if ( (int)insertedvalue >= min && (int)insertedvalue <= max)
									{
										flag=true;
										if (content [3] == "TRUE")
										{
											//new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if(!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}

								else 
								{
									String min = content[6];
									String max = content[7];
									String insertedvalstring = (String) insertedvalue;
									int comparemin = insertedvalstring.compareTo(min);
									int comparemax = insertedvalstring.compareTo(max);
									if (comparemin >= 0 && comparemax<=0)
									{
										flag=true;
										if (content [3] == "TRUE")
										{
											//new
											pk = content[1];
											insertedPkValue = htblColNameValue.get(pk);
											if(!insertedPkValue.equals(null)) {
												primaryexists = true;
											}
										}
									}
								}


							}


						}

						if(flag == false || !primaryexists)
							throw new DBAppException();


					}
					
					
					line = br.readLine();
				}
				
				br.close();
				
				if(primaryexists)
				{
					
					loadPages(loadedTable);
//					Binary Search
					boolean found = false;
					int i =0;
					while(!found){
					 int low = 0;
					 int high = loadedPages.get(i).getNumUsedRows() - 1;

					    while (low <= high) {
				            int mid = (low + high) / 2;
				            if(insertedPkValue.getClass().toString() == "java.lang.Double" ||
									insertedPkValue.getClass().toString() == "java.lang.Integer")
							{
					        Double compare = (Double)loadedPages.get(i).getRow(mid).getValue(pk) - (Double)insertedPkValue;

					        if (compare > 0) {
					        	 high = mid - 1;
					            
					        } else if (compare < 0) {
					        	low = mid + 1;
					        } else 
					        	if(compare ==0) {
					        		found = true;
					        		break;
					        	}
					        	
					        
					        if(low >=loadedPages.get(i).getNumUsedRows() - 1) 
					        {
					        	if(loadedPages.get(i).getMaxRows()==loadedPages.get(i).getNumUsedRows()) 
					        	{
					        		break;
					        		
					        	}
					        	else 
					        	{
					        		insertPageIndex = i;
					        		insertRowIndex = loadedPages.get(i).getNumUsedRows();
					        	}
					
					        }
							}
				            else {
								String x = (String)loadedPages.get(i).getRow(mid).getValue(pk);
						        if (x.compareTo((String)insertedPkValue)<0) 
						        {
						        	
						        	 low = mid + 1;
						            
						        } 
						        else if (x.compareTo((String)insertedPkValue)>0) 
						        {
						        	 high = mid - 1;
						        } 
						        else 
						        	if(x.compareTo((String)insertedPkValue)==0) 
						        	{
						        		found = true;
						        		break;
						        	}
						        	
						
						        if(low >=loadedPages.get(i).getNumUsedRows() - 1) {
						        	if(loadedPages.get(i).getMaxRows()==loadedPages.get(i).getNumUsedRows()) {
						        		break;
						        		
						        	}
						        	else {
						        		insertPageIndex = i;
						        		insertRowIndex = loadedPages.get(i).getNumUsedRows();
						        	}
						
						        }
							}
				            if(low ==high) {
				            	insertPageIndex = i;
				            	insertRowIndex = low;
				            }
					        }
					    
					    	i++;}
					//Suggested change but I will still revise it!!!
//					int insertRowIndex = -1;
//					int insertPageIndex = -1;
//					boolean found = false;
//					int i = 0;
//
//					while (!found && i < loadedPages.size() && insertPageIndex == -1) {
//					    int low = 0;
//					    int high = loadedPages.get(i).getNumUsedRows() - 1;
//
//					    while (low <= high) {
//					        int mid = (low + high) / 2;
//					        Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(i).getRow(mid).getValue(loadedPages.get(i).getRow(mid).getPkIndex());
//					        int compare = pkValue.compareTo(insertedPkValue);
//
//					        if (compare > 0) {
//					            high = mid - 1;
//					        } else if (compare < 0) {
//					            low = mid + 1;
//					        } else if(compare = 0) {
//					            found = true;
//					            insertPageIndex = i;
//					            insertRowIndex = mid;
//					            break;
//					        }
					//      if(low == high && (!pkValue.compareTo(insertedPkValue) == 0) && (high != (loadedPages.get(i).getNumUsedRows()-1))){
					//          insertPageIndex = i;
					//			insertRowIndex = low;
					//			break;
//					    }
//
//					    if (!found && loadedPages.get(i).getMaxRows() > loadedPages.get(i).getNumUsedRows()) {
//					        insertPageIndex = i;
//					        insertRowIndex = low;
//					        break;
//					    }
//
//					    i++;
//					}
//
//					}
//					if (!found && insertRowIndex == -1 && insertPageIndex == -1) {
//					    insertPageIndex = loadedPages.size();
//					    insertRowIndex = 0;


				
				
				//
					//End of suggested change!!!
				}
				
       

				// Insert row into table
				Row row = new Row(htblColNameValue);
				loadedPages.get(insertPageIndex).addRow(row, insertRowIndex);
				//write pages back to disk
				savePages();
			
				// Write table back to disk
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tableFile));
				out.writeObject(table);
				out.close();
		    } catch (Exception e) {
		    	System.out.println("An error occurred while inserting row into table " + strTableName + ": " + e.getMessage());
		    	throw new DBAppException();
		    }


		
	}
	
	public void loadPages (Table table) throws ClassNotFoundException, IOException
	{
		loadedPages = null;
		Vector<String> pages = table.getPages();
		for(String s : pages)
		{
			//load the page file from disk
			File pageFile = new File(s + ".class");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(pageFile));
			Page page = (Page) in.readObject();
			loadedPages.add(page);
			in.close();

		}
	}
	
	public void savePages() throws IOException
	{
		for(Page p : loadedPages)
		{
		File pageFile = new File(p.getPageName() + ".class");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pageFile));
		out.writeObject(p);
		out.close();
		}
		loadedPages=null;
	}
	
	
	public void loadTable(String tableName) throws ClassNotFoundException, IOException
	{
		loadedTable= null;

		File tableFile = new File(tableName + ".class");
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(tableFile));
		Table out= (Table) in.readObject();
		loadedTable=out;
		in.close();


	}
	
	public void saveTable() throws IOException
	{
		File tableFile = new File(loadedTable.getTableName() + ".class");
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(tableFile));
		out.writeObject(loadedTable);
		out.close();
		
		loadedTable=null;
	}
	
	public boolean checkForPrimaryKey(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException
	{
		Enumeration<String> columnNames = htblColNameValue.keys();
		  while (columnNames.hasMoreElements()) 
		  {
	            String columnName = columnNames.nextElement();
	            Object columnValue = htblColNameValue.get(columnName);
	            
	            BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
	    		String line = br.readLine();
	    		line = br.readLine();
	    		while(line!=null)
	    		{
	    			String[] content = line.split(",");
					if(content[0]==strTableName && content[1] == columnName && content[3]== "TRUE")
						{
							return true;
						}
					line = br.readLine();
	    		}
	    		br.close();
	    		
		  }
		  return false;
		  
		  
	}

	
	
	
	// following method updates one row only
	// htblColNameValue holds the key and new value
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName,
	String strClusteringKeyValue,
	Hashtable<String,Object> htblColNameValue )
	throws DBAppException
	{
		
		
	}
	// following method could be used to delete one or more rows.
	// htblColNameValue holds the key and value. This will be used in search
	// to identify which rows/tuples to delete.
	// htblColNameValue enteries are ANDED together
	public void deleteFromTable(String strTableName,
			Hashtable<String,Object> htblColNameValue)
					throws DBAppException, IOException, ClassNotFoundException
	{

		//find el pages ele feha el 7aga
		loadTable(strTableName);
		loadPages(loadedTable);
		boolean hasPk = checkForPrimaryKey(strTableName, htblColNameValue );
		boolean firstloop = true;
		Vector<Row> tmpRows = new Vector<Row>();

		Enumeration<String> columnNames = htblColNameValue.keys();
		while (columnNames.hasMoreElements()) 
		{
			String columnName = columnNames.nextElement();
			Object columnValue = htblColNameValue.get(columnName);


			if (hasPk) //idk nour me7tag yeshof el kalam dah
			{
				//binary search
				int insertRowIndex;
				int insertPageIndex;
				boolean found = false;
				int i =0;
				while(!found)
				{
					int low = 0;
					int high = loadedPages.get(i).getNumUsedRows() - 1;

					while (low <= high) 
					{
						int mid = (low + high) / 2;
						if(columnValue.getClass().toString() == "java.lang.Double" ||
								columnValue.getClass().toString() == "java.lang.Integer")
						{
							Double compare = (Double)loadedPages.get(i).getRow(mid).getValue(columnName) - (Double)columnValue;

							if (compare > 0) {
								high = mid - 1;

							} else if (compare < 0) {
								low = mid + 1;
							} else 
								if(compare ==0) {
									found = true;
									break;
								}


							if(low >=loadedPages.get(i).getNumUsedRows() - 1) 
							{
								if(loadedPages.get(i).getMaxRows()==loadedPages.get(i).getNumUsedRows()) 
								{
									break;

								}
								else 
								{
									insertPageIndex = i;
									insertRowIndex = loadedPages.get(i).getNumUsedRows();
								}

							}
						}
						else {
							String x = (String)loadedPages.get(i).getRow(mid).getValue(columnName);
							if (x.compareTo((String)columnValue)<0) 
							{

								low = mid + 1;

							} 
							else if (x.compareTo((String)columnValue)>0) 
							{
								high = mid - 1;
							} 
							else 
								if(x.compareTo((String)columnValue)==0) 
								{
									found = true;
									break;
								}


							if(low >=loadedPages.get(i).getNumUsedRows() - 1) {
								if(loadedPages.get(i).getMaxRows()==loadedPages.get(i).getNumUsedRows()) 
								{
									break;

								}
								else {
									insertPageIndex = i;
									insertRowIndex = loadedPages.get(i).getNumUsedRows();
								}

							}
						}
						if(low ==high) {
							insertPageIndex = i;
							insertRowIndex = low;
						}
					}

					i++;       //la2et el value sh5syn delete it 

				}

			}
			else
			{
				if(firstloop) //enta fel first loop
				{
					
					for(Page p: loadedPages)
					{
						for(Row r : p.getRows())
						{
							if (r.getValue(columnName).equals(columnValue))
								tmpRows.add(r);
						}
					}
					firstloop=false;


				}
				else //enta msh fel first loop
				{
					for(Row r : tmpRows)
					{
						if ( !(r.getValue(columnName).equals(columnValue)) ) //law el second column doesnt satisfy the condition, remove the row from tmp rows
							tmpRows.remove(r);

					}
				}

			}



		}
		//tle3t bara el while loop, no more conditions
		//start deleting the rows
		for (Page p : loadedPages)
		{
			for(Row r : tmpRows)
			{
				if(p.getRows().contains(r))
				{
					p.deleteRow(r);
					//implement delete the page if empty here
					if (p.isEmpty())
					{
						 File pageFile = new File(p.getPageName()+ ".class");
						 loadedTable.getPages().remove(p.getPageName()+ ".class");
						 loadedPages.remove(p);
						 pageFile.delete();
					}
					tmpRows.remove(r);
				}
			}
		}
		
		savePages();
		saveTable();
		
	}










}
	
//	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
//	String[] strarrOperators)
//	throws DBAppException
//	{
//		
//	}
	
//	// following method creates an octree
//	// depending on the count of column names passed.
//	// If three column names are passed, create an octree.
//	// If only one or two column names is passed, throw an Exception.
//	public void createIndex(String strTableName,
//	String[] strarrColName) throws DBAppException
//	{
//		
//	}




