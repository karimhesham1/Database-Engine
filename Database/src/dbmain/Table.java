package dbmain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class Table implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName;
	private Vector<String> pages;
 

    public Table(String tableName) {
        this.tableName = tableName;
        this.pages = new Vector<String>();
    }

    public String getTableName() {
        return tableName;
    }

    public Vector<String> getPages() {
        return pages;
    }

    public void addPage(String pageName) {
        pages.add(pageName);
    }
    
    public int getNumOfPages()
    {
    	return this.pages.size();
    }
}