package dbmain;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

public class MainMethod {

	public MainMethod() {
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] args) throws DBAppException, IOException, ClassNotFoundException {
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		
		//table htbl names and type and max/min
		Hashtable<String,String> htblColNameType = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMin = new Hashtable<String, String>( );
		Hashtable<String,String> htblColNameMax = new Hashtable<String, String>( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.Double");
		htblColNameType.put("dob", "java.util.Date");
		htblColNameMin.put("id", "0");
		htblColNameMax.put("id", "999999999");
		htblColNameMin.put("name", "a");
		htblColNameMax.put("name", "ZZZZZZZZ");
		htblColNameMin.put("gpa", "0");
		htblColNameMax.put("gpa", "4");
		htblColNameMin.put("dob", "1990-01-01");
		htblColNameMax.put("dob", "2025-01-01");
		
		//create the table, done, working
		//dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
		
		//inserting some records in the database
		Hashtable<String, Object> htblColNameValue = new Hashtable();
		
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 8 ));
		htblColNameValue.put("name", new String("Moh" ) );
		htblColNameValue.put("gpa", new Double( 0.4 ) );
		htblColNameValue.put("dob", new Date(1990 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );

		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 9 ));
		htblColNameValue.put("name", new String("alo" ) );
		htblColNameValue.put("gpa", new Double( 0.4 ) );
		htblColNameValue.put("dob", new Date(1990 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 10 ));
		htblColNameValue.put("name", new String("MANGA" ) );
		htblColNameValue.put("gpa", new Double( 2.7 ) );
		htblColNameValue.put("dob", new Date(2007 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 13 ));
		htblColNameValue.put("name", new String("susFash5" ) );
		htblColNameValue.put("gpa", new Double( 3.2 ) );
		htblColNameValue.put("dob", new Date(2002 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
	
		htblColNameValue.clear( );
		htblColNameValue.put("id", new Integer( 14 ));
		htblColNameValue.put("name", new String("zozawy" ) );
		htblColNameValue.put("gpa", new Double( 3.8 ) );
		htblColNameValue.put("dob", new Date(2008 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		htblColNameValue.clear( );
		//htblColNameValue.put("id", new Integer( 67 ));
		//htblColNameValue.put("name", new String( "manga" ));
		htblColNameValue.put("gpa", new Double( 0.4 ) );
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
		
//		for(int i=1;i<20;i++) {
//		if(i<10)
//		{
//			htblColNameValue.clear( );
//			htblColNameValue.put("id", new Integer( i ));
//			htblColNameValue.put("name", new String( "kimo" ));
//			htblColNameValue.put("gpa", new Double( 0.4 ) );
//			htblColNameValue.put("dob", new Date(2005 - 1900, 1 - 1, 1));
//			dbApp.insertIntoTable( strTableName , htblColNameValue );
//		}
//		else
//		{
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( i ));
//		htblColNameValue.put("name", new String( "manga" ));
//		htblColNameValue.put("gpa", new Double( 3.7 ) );
//		htblColNameValue.put("dob", new Date(2010 - 1900, 1 - 1, 1));
//		dbApp.insertIntoTable( strTableName , htblColNameValue );
//		}
//	}
		
		
		htblColNameValue.clear( );
		//htblColNameValue.put("id", new Integer( 29 ));
		htblColNameValue.put("name", new String("susFash5" ) );
		htblColNameValue.put("gpa", new Double( 3.2 ) );
		htblColNameValue.put("dob", new Date(2002 - 1900, 1 - 1, 1));
		//dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		
		dbApp.deleteFromTable(strTableName, htblColNameValue);
		String id = "201";
		//dbApp.updateTable(strTableName, id, htblColNameValue);
		
		//print the table
		//dbApp.printTable(strTableName);
		String[] cols = {"name", "gpa", "dob"};
		//dbApp.createIndex(strTableName, cols);
		dbApp.printTable(strTableName);
		
		
		
		//System.out.println(htblColNameType);
		
		String manga = "manga";
		

	
	

	}
	

}
