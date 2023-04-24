package dbmain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxRows;
    private int numUsedRows;
    private String tableName;
    private Vector<Row> rows;
    private String pageName;

    public Page(Table table) throws IOException {
    	
    	Properties props = new Properties();
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream("DBApp.config");
    	props.load(inputStream);

    	this.maxRows = Integer.parseInt(props.getProperty("MaximumRowsCountinTablePage"));

        this.tableName = table.getTableName();
        this.numUsedRows = 0;
        this.rows = new Vector<Row>(maxRows);
        int tmp = table.getNumOfPages() +1;
        this.pageName = this.tableName + tmp;
        //add page to table.pages
        table.addPage(pageName);
        
        //create pagefile and serialize it
        File pageFile = new File(pageName + ".class");
        pageFile.createNewFile();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pageFile));
        out.writeObject(this); //not sure momken yedy error
        out.close();
    }

    public boolean isFull() {
        return numUsedRows == maxRows;
    }

    public boolean isEmpty() {
        return numUsedRows == 0;
    }

    public void addRow(Row row, int index) throws DBAppException {
        if (isFull()) {
            throw new DBAppException("Page is full");
        }
        rows.add(index, row);
        numUsedRows++;
        
    }

    public void deleteRow(Object o) {
      
        rows.remove(o);
        numUsedRows--;
    }

    public Row getRow(int index) {
        if (index < 0 || index >= numUsedRows) {
            throw new IndexOutOfBoundsException();
        }
        return rows.get(index);
    }
    
    public Vector<Row> getRows() {
       
        return this.rows;
        
    }
    

    public int getMaxRows() {
        return maxRows;
    }

    public int getNumUsedRows() {
        return numUsedRows;
    }
    
    public String getPageName()
    {
    	return this.pageName;
    }
    
    public String getTableName() {
    	return tableName;
    	
    }
}