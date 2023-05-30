package dbmain;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
//import org.junit.jupiter.api.Assertions;

public class MainMethod {

	public MainMethod() {
		// TODO Auto-generated constructor stub
	}
	
	private static void  insertCoursesRecords(DBApp dbApp, int limit) throws Exception {
        BufferedReader coursesTable = new BufferedReader(new FileReader("C:\\Users\\karim\\git\\Database-II-Project\\Database\\src\\courses_table.csv"));
        String record;
        Hashtable<String, Object> row = new Hashtable<>();
        int c = limit;
        if (limit == -1) {
            c = 1;
        }
        while ((record = coursesTable.readLine()) != null && c > 0) {
            String[] fields = record.split(",");


            int year = Integer.parseInt(fields[0].trim().substring(0, 4));
            int month = Integer.parseInt(fields[0].trim().substring(5, 7));
            int day = Integer.parseInt(fields[0].trim().substring(8));

            Date dateAdded = new Date(year - 1900, month - 1, day);

            row.put("date_added", dateAdded);

            row.put("course_id", fields[1]);
            row.put("course_name", fields[2]);
            row.put("hours", Integer.parseInt(fields[3]));

            dbApp.insertIntoTable("courses", row);
            row.clear();

            if (limit != -1) {
                c--;
            }
        }

        coursesTable.close();
    }

 private static void  insertStudentRecords(DBApp dbApp, int limit) throws Exception {
        BufferedReader studentsTable = new BufferedReader(new FileReader("C:\\Users\\karim\\git\\Database-II-Project\\Database\\src\\students_table.csv"));
        String record;
        int c = limit;
        if (limit == -1) {
            c = 1;
        }

        Hashtable<String, Object> row = new Hashtable<>();
        while ((record = studentsTable.readLine()) != null && c > 0) {
            String[] fields = record.split(",");

            row.put("id", fields[0]);
            row.put("first_name", fields[1]);
            row.put("last_name", fields[2]);

            int year = Integer.parseInt(fields[3].trim().substring(0, 4));
            int month = Integer.parseInt(fields[3].trim().substring(5, 7));
            int day = Integer.parseInt(fields[3].trim().substring(8));

            Date dob = new Date(year - 1900, month - 1, day);
            row.put("dob", dob);

            double gpa = Double.parseDouble(fields[4].trim());

            row.put("gpa", gpa);

            dbApp.insertIntoTable("students", row);
            row.clear();
            if (limit != -1) {
                c--;
            }
        }
        studentsTable.close();
    }
 private static void insertTranscriptsRecords(DBApp dbApp, int limit) throws Exception {
        BufferedReader transcriptsTable = new BufferedReader(new FileReader("C:\\Users\\karim\\git\\Database-II-Project\\Database\\src\\transcripts_table.csv"));
        String record;
        Hashtable<String, Object> row = new Hashtable<>();
        int c = limit;
        if (limit == -1) {
            c = 1;
        }
        while ((record = transcriptsTable.readLine()) != null && c > 0) {
            String[] fields = record.split(",");

            row.put("gpa", Double.parseDouble(fields[0].trim()));
            row.put("student_id", fields[1].trim());
            row.put("course_name", fields[2].trim());

            String date = fields[3].trim();
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8));

            Date dateUsed = new Date(year - 1900, month - 1, day);
            row.put("date_passed", dateUsed);

            dbApp.insertIntoTable("transcripts", row);
            row.clear();

            if (limit != -1) {
                c--;
            }
        }

        transcriptsTable.close();
    }
 private static void insertPCsRecords(DBApp dbApp, int limit) throws Exception {
        BufferedReader pcsTable = new BufferedReader(new FileReader("C:\\Users\\karim\\git\\Database-II-Project\\Database\\src\\pcs_table.csv"));
        String record;
        Hashtable<String, Object> row = new Hashtable<>();
        int c = limit;
        if (limit == -1) {
            c = 1;
        }
        while ((record = pcsTable.readLine()) != null && c > 0) {
            String[] fields = record.split(",");

            row.put("pc_id", Integer.parseInt(fields[0].trim()));
            row.put("student_id", fields[1].trim());

            dbApp.insertIntoTable("pcs", row);
            row.clear();

            if (limit != -1) {
                c--;
            }
        }

        pcsTable.close();
    }
 private static void createTranscriptsTable(DBApp dbApp) throws Exception {
        // Double CK
        String tableName = "transcripts";

        Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
        htblColNameType.put("gpa", "java.lang.Double");
        htblColNameType.put("student_id", "java.lang.String");
        htblColNameType.put("course_name", "java.lang.String");
        htblColNameType.put("date_passed", "java.util.Date");

        Hashtable<String, String> minValues = new Hashtable<>();
        minValues.put("gpa", "0.7");
        minValues.put("student_id", "43-0000");
        minValues.put("course_name", "AAAAAA");
        minValues.put("date_passed", "1990-01-01");

        Hashtable<String, String> maxValues = new Hashtable<>();
        maxValues.put("gpa", "5.0");
        maxValues.put("student_id", "99-9999");
        maxValues.put("course_name", "zzzzzz");
        maxValues.put("date_passed", "2020-12-31");

        dbApp.createTable(tableName, "gpa", htblColNameType, minValues, maxValues);
    }

    private static void createStudentTable(DBApp dbApp) throws Exception {
        // String CK
        String tableName = "students";

        Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
        htblColNameType.put("id", "java.lang.String");
        htblColNameType.put("first_name", "java.lang.String");
        htblColNameType.put("last_name", "java.lang.String");
        htblColNameType.put("dob", "java.util.Date");
        htblColNameType.put("gpa", "java.lang.Double");

        Hashtable<String, String> minValues = new Hashtable<>();
        minValues.put("id", "43-0000");
        minValues.put("first_name", "AAAAAA");
        minValues.put("last_name", "AAAAAA");
        minValues.put("dob", "1990-01-01");
        minValues.put("gpa", "0.7");

        Hashtable<String, String> maxValues = new Hashtable<>();
        maxValues.put("id", "99-9999");
        maxValues.put("first_name", "zzzzzz");
        maxValues.put("last_name", "zzzzzz");
        maxValues.put("dob", "2000-12-31");
        maxValues.put("gpa", "5.0");

        dbApp.createTable(tableName, "id", htblColNameType, minValues, maxValues);
    }
    private static void createPCsTable(DBApp dbApp) throws Exception {
        // Integer CK
        String tableName = "pcs";

        Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
        htblColNameType.put("pc_id", "java.lang.Integer");
        htblColNameType.put("student_id", "java.lang.String");


        Hashtable<String, String> minValues = new Hashtable<>();
        minValues.put("pc_id", "0");
        minValues.put("student_id", "43-0000");

        Hashtable<String, String> maxValues = new Hashtable<>();
        maxValues.put("pc_id", "20000");
        maxValues.put("student_id", "99-9999");

        dbApp.createTable(tableName, "pc_id", htblColNameType, minValues, maxValues);
    }
    private static void createCoursesTable(DBApp dbApp) throws Exception {
        // Date CK
        String tableName = "courses";

        Hashtable<String, String> htblColNameType = new Hashtable<String, String>();
        htblColNameType.put("date_added", "java.util.Date");
        htblColNameType.put("course_id", "java.lang.String");
        htblColNameType.put("course_name", "java.lang.String");
        htblColNameType.put("hours", "java.lang.Integer");


        Hashtable<String, String> minValues = new Hashtable<>();
        minValues.put("date_added", "1901-01-01");
        minValues.put("course_id", "0000");
        minValues.put("course_name", "AAAAAA");
        minValues.put("hours", "1");

        Hashtable<String, String> maxValues = new Hashtable<>();
        maxValues.put("date_added", "2020-12-31");
        maxValues.put("course_id", "9999");
        maxValues.put("course_name", "zzzzzz");
        maxValues.put("hours", "24");

        dbApp.createTable(tableName, "date_added", htblColNameType, minValues, maxValues);

    }
    public void testWrongStudentsKeyInsertion() {
        final DBApp dbApp = new DBApp();
        dbApp.init();

        String table = "students";
        Hashtable<String, Object> row = new Hashtable();
        row.put("id", 123);
        
        row.put("first_name", "foo");
        row.put("last_name", "bar");

        Date dob = new Date(1995 - 1900, 4 - 1, 1);
        row.put("dob", dob);
        row.put("gpa", 1.1);

//        Assertions.assertThrows(DBAppException.class, () -> {
//                    dbApp.insertIntoTable(table, row);
//                }
//        );

    }
    public void testExtraTranscriptsInsertion() {
        final DBApp dbApp = new DBApp();
        dbApp.init();

        String table = "transcripts";
        Hashtable<String, Object> row = new Hashtable();
        row.put("gpa", 1.5);
        row.put("student_id", "34-9874");
        row.put("course_name", "bar");
        row.put("elective", true);


        Date date_passed = new Date(2011 - 1900, 4 - 1, 1);
        row.put("date_passed", date_passed);


//        Assertions.assertThrows(DBAppException.class, () -> {
//                    dbApp.insertIntoTable(table, row);
//                }
//        );
    }


	public static void main(String[] args) throws Exception {

		DBApp db = new DBApp();
//	      db.init();
//		String []arr =  {"id", "first_name", "gpa"}; 
//	     db.createIndex("students", arr);
//	     

	        SQLTerm[] arrSQLTerms = new SQLTerm[2];
	        arrSQLTerms[0] = new SQLTerm();
	        arrSQLTerms[0]._strTableName = "courses";
	        arrSQLTerms[0]._strColumnName= "course_id";
	        arrSQLTerms[0]._strOperator = ">";
	        arrSQLTerms[0]._objValue = "0950";

	        arrSQLTerms[1] = new SQLTerm();
	        arrSQLTerms[1]._strTableName = "courses";
	        arrSQLTerms[1]._strColumnName= "hours";
	        arrSQLTerms[1]._strOperator = "!=";
	        arrSQLTerms[1]._objValue = 16;
	        
//	        arrSQLTerms[2] = new SQLTerm();
//	        arrSQLTerms[2]._strTableName = "courses";
//	        arrSQLTerms[2]._strColumnName= "date_added";
//	        arrSQLTerms[2]._strOperator = ">";
//	        arrSQLTerms[2]._objValue = new Date(2008 - 1900, 06-1, 31);

	        String[]strarrOperators = new String[1];
	        strarrOperators[0] = "AND";
//	        strarrOperators[1] = "OR";
//	        
	        Iterator it = db.selectFromTable(arrSQLTerms, strarrOperators);
	        System.out.println(it.hasNext());
//	        while (it.)
//	      String table = "students";
//
//	        row.put("first_name", "fooooo");
//	        row.put("last_name", "baaaar");
//
//	        Date dob = new Date(1992 - 1900, 9 - 1, 8);
//	        row.put("dob", dob);
//	        row.put("gpa", 1.1);
//
////	        dbApp.updateTable(table, clusteringKey, row);
//	      createCoursesTable(db);
//	      createPCsTable(db);
//	      createTranscriptsTable(db);
//	      createStudentTable(db);
//	      insertPCsRecords(db,200);
//	      insertTranscriptsRecords(db,200);
//	      insertStudentRecords(db,200);
//	      insertCoursesRecords(db,200);
//	      db.printTable("pcs");

		
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
//		htblColNameType.put("dob", "java.util.Date");
//		htblColNameMin.put("id", "0");
//		htblColNameMax.put("id", "999999999");
//		htblColNameMin.put("name", "a");
//		htblColNameMax.put("name", "ZZZZZZZZ");
//		htblColNameMin.put("gpa", "0");
//		htblColNameMax.put("gpa", "4");
//		htblColNameMin.put("dob", "1990-01-01");
//		htblColNameMax.put("dob", "2025-01-01");
//		
//		//create the table, done, working
//		//dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax );
//		
//		//inserting some records in the database
//		Hashtable<String, Object> htblColNameValue = new Hashtable();
////		htblColNameValue.put("hours", new Integer(16));
//		htblColNameValue.put("date_added", new Date(2018-1900, 5-1, 3 ) );
////		htblColNameValue.put("course_id", new String( "1224" ) );
//		db.deleteFromTable( "courses" ,  htblColNameValue );
//		db.printTable("courses");
////		
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
//		htblColNameValue.put("id", new Integer( 8 ));
//		htblColNameValue.put("name", new String("Moh" ) );
//		htblColNameValue.put("gpa", new Double( 0.4 ) );
//		htblColNameValue.put("dob", new Date(1990 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 9 ));
//		htblColNameValue.put("name", new String("alo" ) );
//		htblColNameValue.put("gpa", new Double( 0.4 ) );
//		htblColNameValue.put("dob", new Date(1990 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 10 ));
//		htblColNameValue.put("name", new String("MANGA" ) );
//		htblColNameValue.put("gpa", new Double( 2.7 ) );
//		htblColNameValue.put("dob", new Date(2007 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 13 ));
//		htblColNameValue.put("name", new String("susFash5" ) );
//		htblColNameValue.put("gpa", new Double( 3.2 ) );
//		htblColNameValue.put("dob", new Date(2002 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//	
//		htblColNameValue.clear( );
//		htblColNameValue.put("id", new Integer( 14 ));
//		htblColNameValue.put("name", new String("zozawy" ) );
//		htblColNameValue.put("gpa", new Double( 3.8 ) );
//		htblColNameValue.put("dob", new Date(2008 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		htblColNameValue.clear( );
//		//htblColNameValue.put("id", new Integer( 67 ));
//		//htblColNameValue.put("name", new String( "manga" ));
//		htblColNameValue.put("gpa", new Double( 0.4 ) );
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
////		for(int i=0;i<20;i++) {
////		if(i<10)
////		{
////			htblColNameValue.clear( );
////			htblColNameValue.put("id", new Integer( i ));
////			htblColNameValue.put("name", new String( "kimo" ));
////			htblColNameValue.put("gpa", new Double( 0.4 ) );
////			htblColNameValue.put("dob", new Date(2005 - 1900, 1 - 1, 1));
////			dbApp.insertIntoTable( strTableName , htblColNameValue );
////		}
////		else
////		{
////		htblColNameValue.clear( );
////		htblColNameValue.put("id", new Integer( i ));
////		htblColNameValue.put("name", new String( "manga" ));
////		htblColNameValue.put("gpa", new Double( 3.7 ) );
////		htblColNameValue.put("dob", new Date(2010 - 1900, 1 - 1, 1));
////		dbApp.insertIntoTable( strTableName , htblColNameValue );
////		}
////	}
//		
//		
//		htblColNameValue.clear( );
//		//htblColNameValue.put("id", new Integer( 21 ));
//		htblColNameValue.put("name", new String("susFash5" ) );
//		htblColNameValue.put("gpa", new Double( 3.2 ) );
//		htblColNameValue.put("dob", new Date(2002 - 1900, 1 - 1, 1));
//		//dbApp.insertIntoTable( strTableName , htblColNameValue );
//		
//		
//		dbApp.deleteFromTable(strTableName, htblColNameValue);
//		String id = "201";
//		//dbApp.updateTable(strTableName, id, htblColNameValue);
//		
//		//print the table
//		//dbApp.printTable(strTableName);
//		String[] cols = {"name", "gpa", "dob"};
//		//dbApp.createIndex(strTableName, cols);
//		dbApp.printTable(strTableName);
//		
//		
//		
//		//System.out.println(htblColNameType);
//		
//		String manga = "manga";
//		
//
//	
//	

	}
	

}
