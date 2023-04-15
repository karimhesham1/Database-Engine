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
	private Vector<Page> pages;
 

    public Table(String tableName) {
        this.tableName = tableName;
        this.pages = new Vector<>();
    }

    public String getTableName() {
        return tableName;
    }

    public Vector<Page> getPages() {
        return pages;
    }

    public void addPage(Page page) {
        pages.add(page);
    }
}