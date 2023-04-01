package dbmain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Table implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableName;
	private ArrayList<Page> pages;
 

    public Table(String tableName) {
        this.tableName = tableName;
        this.pages = new ArrayList<>();
    }

    public String getTableName() {
        return tableName;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public void addPage(Page page) {
        pages.add(page);
    }
}