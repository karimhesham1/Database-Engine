package dbmain;

import java.io.IOException;
import java.util.Hashtable;

public class MainMethod {

	public MainMethod() {
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] args) throws DBAppException, IOException, ClassNotFoundException {
//		String strTableName = "Student";
//		DBApp dbApp = new DBApp( );
//		
//		//table htbl names and type and max/min
//		Hashtable<String,String> htblColNameType = new Hashtable<String, String>( );
//		Hashtable<String,String> htblColNameMin = new Hashtable<String, String>( );
//		Hashtable<String,String> htblColNameMax = new Hashtable<String, String>( );
//		htblColNameType.put("id", "java.lang.Integer");
//		htblColNameType.put("name", "java.lang.String");
//		htblColNameType.put("gpa", "java.lang.Double");
//		htblColNameMin.put("id", "0");
//		htblColNameMax.put("id", "999999999");
//		htblColNameMin.put("name", "a");
//		htblColNameMax.put("name", "ZZZZZZZZ");
//		htblColNameMin.put("gpa", "0");
//		htblColNameMax.put("gpa", "4");
//		
//		//create the table, done, working
//		//dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
//		
//		//inserting some records in the database
//		Hashtable<String, Object> htblColNameValue = new Hashtable();
//		htblColNameValue.put("id", new Integer( 1 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) );
//		htblColNameValue.put("gpa", new Double( 0.95 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 5 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) );
//		htblColNameValue.put("gpa", new Double( 0.95 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 3 ));
//		htblColNameValue.put("name", new String("Dalia Noor" ) );
//		htblColNameValue.put("gpa", new Double( 1.25 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 6 ));
//		htblColNameValue.put("name", new String("John Noor" ) );
//		htblColNameValue.put("gpa", new Double( 1.5 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 4 ));
//		htblColNameValue.put("name", new String("Zaky Noor" ) );
//		htblColNameValue.put("gpa", new Double( 0.88 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 2 ));
//		htblColNameValue.put("name", new String("Nour" ) );
//		htblColNameValue.put("gpa", new Double( 1.25 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 7 ));
//		htblColNameValue.put("name", new String("Moh" ) );
//		htblColNameValue.put("gpa", new Double( 0.4 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//
//		
////		for(int i=0;i<25;i++) {
////			if(i<6)
////			{
////				htblColNameValue.clear( );
////				htblColNameValue.put("id", new Integer( i ));
////				htblColNameValue.put("name", new String( "kimo" ));
////				htblColNameValue.put("gpa", new Double( 0.4 ) );
////				dbApp.insertIntoTable( strTableName , htblColNameValue );
////			}
////			else
////			{
////			htblColNameValue.clear( );
////			htblColNameValue.put("id", new Integer( i ));
////			htblColNameValue.put("name", new String( "manga" ));
////			htblColNameValue.put("gpa", new Double( 0.4 ) );
////			dbApp.insertIntoTable( strTableName , htblColNameValue );
////			}
////		}
//	
//		
//		htblColNameValue.clear( );
////		htblColNameValue.put("id", new Integer( i ));
//		htblColNameValue.put("name", new String( "manga" ));
////		htblColNameValue.put("gpa", new Double( 0.4 ) );
//		
//		//dbApp.deleteFromTable(strTableName, htblColNameValue);
////		String id = "201";
////		dbApp.updateTable(strTableName, id, htblColNameValue);
//		
//		//print the table
//		dbApp.printTable(strTableName);
//		
//
//		//System.out.println(htblColNameType);
//		
//		String manga = "manga";
//		
		
		
		String strTableName = "Student";
		DBApp dbApp = new DBApp( );
		
		//table htbl names and type and max/min
//		Hashtable<String,String> htblColNameType = new Hashtable<String, String>( );
//		Hashtable<String,String> htblColNameMin = new Hashtable<String, String>( );
//		Hashtable<String,String> htblColNameMax = new Hashtable<String, String>( );
//		htblColNameType.put("id", "java.lang.Integer");
//		htblColNameType.put("gpa", "java.lang.Double");
//		htblColNameMin.put("id", "0");
//		htblColNameMax.put("id", "999999999");
//		htblColNameMin.put("gpa", "0");
//		htblColNameMax.put("gpa", "4");
		
		//create the table, done, working
		//dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
		
		Hashtable<String, Object> htblColNameValue = new Hashtable();
		htblColNameValue.put("id", new Integer( 10000000 ));
		htblColNameValue.put("name", new String("MANGAAAAAAAAAAAAA") );
		//htblColNameValue.put("gpa", new Double( 0.95 ) );
		dbApp.insertIntoTable( strTableName , htblColNameValue );
		
		
//		htblColNameValue.put("id", new Integer( 400 ));
//		htblColNameValue.put("name", new String("Ahmed Noor" ) );
//		htblColNameValue.put("gpa", new Double( 0.95 ) );
//		dbApp.insertIntoTable( strTableName , htblColNameValue );
		dbApp.printTable(strTableName);
	}
	

}
