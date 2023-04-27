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
	
	public static void main(String[] args) throws DBAppException, IOException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		
		//table htbl names and type and max/min
		Hashtable<String,String> htblColNameType = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMin = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMax = new Hashtable<String, String>( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.Double");
		htblColNameMin.put("id", "0");
		htblColNameMax.put("id", "999999999");
		htblColNameMin.put("name", "a");
		htblColNameMax.put("name", "ZZZZZZZZ");
		htblColNameMin.put("gpa", "0");
		htblColNameMax.put("gpa", "4");
		
		//create the table, done, working
		//dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
		
		//inserting some records in the database
		Hashtable<String, Object> htblColNameValue = new Hashtable( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		

		//System.out.println(htblColNameType);
		
		String manga = "manga";
		
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

		       loadTable(strTableName);
		        
		        
		        
		        boolean primaryexists = false;
				
				BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
				String line = br.readLine();
				line = br.readLine();
				Enumeration<String> columnNames = htblColNameValue.keys();
				Object insertedPkValue = null;
				String tableName;
				String pk = null;
				while (line != null) 
				{
					boolean flag = false;
					String[] content = line.split(",");
					
					if(columnNames.hasMoreElements()== false)
						break;
					
					if(content[0].equals(strTableName))
					{
						tableName = strTableName;
						
						String insertedColName = columnNames.nextElement();
						Object insertedvalue = htblColNameValue.get(insertedColName);

						if (content[1].equals(insertedColName))
						{
							
							if(content[2].equals(insertedvalue.getClass().getName()))
							{
								if(insertedvalue.getClass().getName() == "java.lang.Double" ||
										insertedvalue.getClass().getName() == "java.lang.Integer")
								{
									int min = Integer.parseInt(content[6]);
									int max = Integer.parseInt(content[7]);

									if ( (double)insertedvalue >= min && (double)insertedvalue <= max)
									{
										flag=true;
										if (content [3].equals( "TRUE"))
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
									int comparemin = insertedvalstring.compareToIgnoreCase(min);
									int comparemax = insertedvalstring.compareToIgnoreCase(max);
									if (comparemin >= 0 && comparemax<=0)
									{
										flag=true;
										if (content [3].equals( "TRUE"))
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

						if(flag == false)
							throw new DBAppException();


					}
					
					
					line = br.readLine();
				}
				if(!primaryexists)
					throw new DBAppException();
				
				br.close();
				
				int insertRowIndex1 = -1;
				int insertPageIndex1 = -1;
				boolean found = false;
				if(primaryexists)
				{
					
					loadPages(loadedTable);
//					Binary Search			
//					Suggested change but I will still revise it!!!

					int i = 0;

					while (!found && i < loadedPages.size() && insertPageIndex1 == -1) {
					    int low = 0;
					    int high = loadedPages.get(i).getNumUsedRows() - 1;

					    while (low <= high) {
					    	int mid = (low + high) / 2;
					    	Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(i).getRow(mid).getValue(pk);
					    	int compare = pkValue.compareTo(insertedPkValue);

					    	if (compare > 0) { //Check condition
					    		high = mid - 1;
					    	} else if (compare < 0) {
					    		low = mid + 1;
					    	} else if(compare == 0) {
					    		found = true;
					    		insertPageIndex1 = i;
					    		insertRowIndex1 = mid;
					    		break;
					    	}
					    	//de law la2ena el location ely el mafrod ne7ot feh el 7aga 
					    	if(low == high && (pkValue.compareTo(insertedPkValue) != 0) && (high != (loadedPages.get(i).getNumUsedRows()-1))){
					    		insertPageIndex1 = i;
					    		insertRowIndex1 = low;
					    		break;
					    	}

					    	//m7tag shift
					    }	//de law el page msh full, fa insert 3ady fe a5er el page
					    if (!found && loadedPages.get(i).getMaxRows() > loadedPages.get(i).getNumUsedRows() && insertPageIndex1 == -1) {
					    	insertPageIndex1 = i;
					    	insertRowIndex1 = low;
					    	break;
					    }

					    i++;
					}

					//el page full, fa 3ayzeen page gdeda 
					//check en law el insertpage index = size bta3 el loaded pages yeb2a create new page,
					//wel insert row index heya awel row fel page el gdeda
					if (!found && insertRowIndex1 == -1 && insertPageIndex1 == -1) {
					    insertPageIndex1 = loadedPages.size();
					    insertRowIndex1 = 0;
					}

					
					//End of suggested change!!!
				}
				
				if(!primaryexists || found)
					throw new DBAppException("primary doesnt exist or duplicate");
       
				
				// Insert row into table
				Row newRow = new Row(htblColNameValue);
				int s1= insertPageIndex1;
				
				//create new page, assign it to the table
				//ely da5ely max, w mafesh page lel max dah, fa create new page
				if(insertPageIndex1 == loadedPages.size())
				{
					Page newPage = new Page(loadedTable);
					newPage.addRow(newRow, 0);
				}
				//insert fel nos aw a5er page feha makan
				else
				{
					while(s1 < loadedPages.size()-1) //loop 3ala kol el pages ely ba3d ely hamel feha insert, shifting
					{
						if(loadedPages.get(s1).getNumUsedRows() >= loadedPages.get(s1).getMaxRows()) //>: nozbot el shifting =: awel mara bned5ol nshof law feha makan wla la 
						{
							Row shiftedRow = loadedPages.get(s1).getRow(loadedPages.get(s1).getMaxRows()-1);
							loadedPages.get(s1).deleteRow(shiftedRow);
							loadedPages.get(s1 + 1).addRow(shiftedRow, 0);
							if(loadedPages.get(s1 + 1).getNumUsedRows() <= loadedPages.get(s1 + 1).getMaxRows())
								break;  // law el page ely ba3deha msh me7taga shifting break;
							
						}
						else
							break; //law el page ely ana feha msh me7taga shifting
						s1++;
					}
					//ba3d kol el shifting betshof a5er page me3adeya el max wla la, 3shan te create new page
					if (loadedPages.get(loadedPages.size()-1).getNumUsedRows() > loadedPages.get(loadedPages.size()-1).getMaxRows())
					{
						Page newPage = new Page(loadedTable);
						Row shiftedRow = loadedPages.get(loadedPages.size()-1).getRow(loadedPages.get(loadedPages.size()-1).getMaxRows());
						loadedPages.get(loadedPages.size()-1).deleteRow(shiftedRow);
						newPage.addRow(shiftedRow, 0);
					}
					//insert ba2a
					loadedPages.get(s1).addRow(newRow, insertRowIndex1);
				}
				

				savePages();
				saveTable();
			
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
					if(content[0].equals(strTableName) && content[1].equals(columnName) && content[3].equals("TRUE"))
						{
						
							br.close();
							return true;
						}
					line = br.readLine();
	    		}
	    		br.close();
	    		
		  }
		  return false;
	}
	
	public String getPrimaryKey(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException
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
	    			if(content[0].equals(strTableName) && content[1].equals(columnName) && content[3].equals("TRUE"))
						{
							br.close();
							return content[1];
						}
					line = br.readLine();
	    		}
	    		br.close();
	    		
		  }
		  return "";
	}
	
	
	public boolean validateHashtable (String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException
	{
	
		Enumeration<String> columnNames = htblColNameValue.keys();
		boolean firstloop = true;
		while(columnNames.hasMoreElements()== true) //ben loop 3ala el hashtable
		{
			String insertedColName = columnNames.nextElement();
			Object insertedvalue = htblColNameValue.get(insertedColName);

			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			
			if (firstloop)
				line = br.readLine();
			
			firstloop = false;
			while (line != null) 
			{
				String[] content = line.split(",");
				
				
				if(content[0].equals(strTableName) && insertedColName.equals(content[1]) )//law el line ely ana masko mawgod fel htbl
				{				
					
						if(content[2].equals(insertedvalue.getClass().getName()) )
						{
							if(insertedvalue.getClass().getName() == "java.lang.Double" ||
									insertedvalue.getClass().getName() == "java.lang.Integer")
							{
								int min = Integer.parseInt(content[6]);
								int max = Integer.parseInt(content[7]);

								if ( (int)insertedvalue >= min && (int)insertedvalue <= max)
								{
									htblColNameValue.remove(insertedColName);//remove el entry mn htbl law valid
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

							else 
							{
								String min = content[6];
								String max = content[7];
								String insertedvalstring = (String) insertedvalue;
								int comparemin = insertedvalstring.compareTo(min);
								int comparemax = insertedvalstring.compareTo(max);
								if (comparemin >= 0 && comparemax<=0)
								{
									htblColNameValue.remove(insertedColName);//remove el entry mn htbl law valid
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


						}


				}
				
				
				line = br.readLine();
			}
			
			br.close();
			
		}//a5er el while bta3et el htbl
	
		
		if (htblColNameValue.isEmpty())
			return true;
		else
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
		try {
//			loadTable(strTableName);
//			loadPages(loadedTable);
//			
//			int updateRowIndex1 = -1;
//			int updatePageIndex1 = -1;
//			boolean found = false;
//			int page = 0;
//			
//			while (!found && page < loadedPages.size() && updatePageIndex1 == -1) {
//				
//			}
			loadTable(strTableName);
			loadPages(loadedTable);
			
			boolean valid=validateHashtable(strTableName, htblColNameValue);
			if (!valid)
				throw new DBAppException("htbl not valid");
			Enumeration<String> columnNames = htblColNameValue.keys();
			
			
			
			
			BufferedReader br = new BufferedReader(new FileReader("metadata.csv"));
			String line = br.readLine();
			line = br.readLine();
			String pkName="";
			String pkType="";
			while (line != null) 
			{
				String[] content = line.split(",");
				if(content[0]==strTableName && content[3]=="TRUE")
					{
					pkName=content[1];
					pkType=content[2];
					
					}
				
				
				line=br.readLine();
			}
			
			Object insertedPk=strClusteringKeyValue;
			
			if(pkType== "java.lang.Double"|| pkType=="java.lang.Integer")
			{
				insertedPk= Integer.parseInt(strClusteringKeyValue);
			}
			
			
			
			boolean found = false;
			
			int lowPage = 0;
			int highPage = loadedPages.size() - 1;
			int midPage = (lowPage + highPage) / 2;
			int lowRow = 0;
			int highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
			int midRow = (lowRow + highRow) / 2;
			while(!found && lowPage <= highPage && lowRow<=highRow)
			{
				 Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(midPage).getRow(midRow).getValue(pkName);
			        int compare = pkValue.compareTo(insertedPk);
			        
			        
			        if(compare >0) 
					{
						 Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(lowRow).getValue(pkName);
					        int compare2 = pkValue2.compareTo(insertedPk);
						if(compare2> 0)
						{
							highPage = midPage - 1;
							midPage = (lowPage + highPage) / 2;
							highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
							midRow = (lowRow + highRow) / 2;
						}
						else
						{
							highRow = midRow-1;
							midRow = (lowRow + highRow) / 2;
						}
					}
			        else if(compare<0) 
					{
						 Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(highRow).getValue(pkName);
					        int compare2 = pkValue2.compareTo(insertedPk);
						if(compare2< 0)
						{
							lowPage = midPage + 1;
							midPage = (lowPage + highPage) / 2;
							highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
							midRow = (lowRow + highRow) / 2;
						}
						else
						{
							lowRow = midRow+1;
							midRow = (lowRow + highRow) / 2 ;
						}
					} 
			        else
			        {
			        	while (columnNames.hasMoreElements()) 
			        	{
			        		found=true;
			        		String columnName = columnNames.nextElement();
			        		Object columnValue = htblColNameValue.get(columnName);

			        		//check en el hash valid (helper)
			        		
			        		//update el 7aga  
			        		
			        			loadedPages.get(midPage).getRow(midRow).addValue(columnName, columnValue);
			        	


			        	}
			        }


			}



		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		boolean valid=validateHashtable(strTableName, htblColNameValue);
		if (!valid)
			throw new DBAppException("htbl not valid");
		
		boolean hasPk = checkForPrimaryKey(strTableName, htblColNameValue );
		boolean firstloop = true;
		Vector<Row> tmpRows = new Vector<Row>();

		
		Enumeration<String> columnNames = htblColNameValue.keys();
			String columnName = columnNames.nextElement();
			Object columnValue = htblColNameValue.get(columnName);


			if (hasPk) 
			{
				//binary search
				
				boolean found = false;
				
				int lowPage = 0;
				int highPage = loadedPages.size() - 1;
				int midPage = (lowPage + highPage) / 2;
				int lowRow = 0;
				int highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
				int midRow = (lowRow + highRow) / 2;
				//5ale el pk fel colName
				columnName= getPrimaryKey(strTableName, htblColNameValue );
				columnValue = htblColNameValue.get(columnName);
				
				
				
				
				
//				if(!loadedTable.getPages().contains(columnValue))
//					throw new DBAppException();
//				
//				
//				for(int i=0;i<loadedTable.getPages().size(); i++)
//				{
//					Page p= loadedTable.getPages().get(i);
//					
//				}
				
				
				//law el value ely badawar 3aleha aslan msh 3andy?? daniela
				while(!found && lowPage <= highPage && lowRow<=highRow)
				{
					
					
					 Comparable<Object> pkValue = (Comparable<Object>) loadedPages.get(midPage).getRow(midRow).getValue(columnName);
				        int compare = pkValue.compareTo(columnValue);
						if(compare >0) 
						{
							 Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(lowRow).getValue(columnName);
						        int compare2 = pkValue2.compareTo(columnValue);
							if(compare2> 0)
							{
								highPage = midPage - 1;
								midPage = (lowPage + highPage) / 2;
								highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
								midRow = (lowRow + highRow) / 2;
							}
							else
							{
								highRow = midRow-1;
								midRow = (lowRow + highRow) / 2;
							}
						} else if(compare<0) 
						{
							 Comparable<Object> pkValue2 = (Comparable<Object>) loadedPages.get(midPage).getRow(highRow).getValue(columnName);
						        int compare2 = pkValue2.compareTo(columnValue);
							if(compare2< 0)
							{
								lowPage = midPage + 1;
								midPage = (lowPage + highPage) / 2;
								highRow = loadedPages.get(midPage).getNumUsedRows() - 1;
								midRow = (lowRow + highRow) / 2;
							}
							else
							{
								lowRow = midRow+1;
								midRow = (lowRow + highRow) / 2 ;
							}
						} 
						else //b2o equal le b3d 5las 
						{
							//found immediately  check ba2et el cases 
							
							
							found=true;
							boolean hnmsa7=true ;
							 
							while(columnNames.hasMoreElements())
							{
								columnName = columnNames.nextElement();
								columnValue = htblColNameValue.get(columnName);
								if( loadedPages.get(midPage).getRow(midRow).getValue(columnName) !=  columnValue)
								{
									hnmsa7=false;
									break;
								}


							}
							if(hnmsa7)  // lw kol el conditions kanet 7lwa fa mfesh 7aga 3'yret el flag hmsa7 el row da 
							{
								loadedPages.get(midPage).deleteRow(loadedPages.get(midPage).getRow(midRow));
								
								//if the page is empty ba2a :)

								if(loadedPages.get(midPage).isEmpty())
								{

									//wa5deno mn ta7t m3rfsh sa7 wla eh el nezam
									File pageFile = new File((loadedPages.get(midPage)).getPageName()+ ".class");
									loadedTable.getPages().remove((loadedPages.get(midPage)).getPageName()+ ".class");
									loadedPages.remove((loadedPages.get(midPage)));
									pageFile.delete();

								}

							}

							
						}
					}
					
				savePages();
				saveTable();

			}

			else
			{
				columnNames = htblColNameValue.keys();
				while (columnNames.hasMoreElements()) 
				{
					columnName = columnNames.nextElement();
					columnValue = htblColNameValue.get(columnName);


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







/*
 * 	boolean found = false;
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
					    	*/
