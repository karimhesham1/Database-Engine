package dbmain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;



public class DBApp implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
















