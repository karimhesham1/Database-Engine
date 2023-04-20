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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;



public class DBApp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vector<Page> loadedPages;
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
				while (line != null) 
				{
					boolean flag = false;
					String[] content = line.split(",");
					
					if(columnNames.hasMoreElements()== false)
						break;
					
					if(content[0] == strTableName)
					{
						
						
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
										if (content [3] == "true")
										{
											//new
											String pk = content[1];
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
										if (content [3] == "true")
										{
											//new
											String pk = content[1];
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
//					Vector<String> pages = table.getPages();
//					int pageSize = pages.size();
//
//					while(true) {
//						int select = pageSize/2;
//						Page P1 = pages.get(select);
//						int used = P1.getNumUsedRows();
//						int rowMid = used/2;
//						Row R1 = P1.getRow(rowMid);
//						int PKIndex = R1.getPkIndex();
//						if(insertedPkValue.equals(R1.getValue(PKIndex))){
//							throw new DBAppException();
//						}
//						else {
//							//if(inserted)
//							//Still need to work out how moving to left or right side of tree depending on < or >
//						}
//
//
//					}
				}
				
		        
		        
		        // Check that all columns in the table are present in the hashtable
		       

		        // Check that the primary key column is present in the hashtable
		       

		        // Get primary key value from hashtable
		       

		        // Check that the primary key value is of the correct type
		     

		        // Check if the row already exists
		        
		        
		        // Insert row into table
		      
		        
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

	
	
	//alo
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
	throws DBAppException
	{
		
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
	
	
}


//Dina gmela fsh5 

//alo alo


//ana gmela 








