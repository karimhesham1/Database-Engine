package dbmain;

import java.io.Serializable;
import java.util.Hashtable;

public class Row implements Serializable {
    private static final long serialVersionUID = 1L;
    private Hashtable<String, Object> colNameValue;
    

    public Row(Hashtable<String, Object> htbl) {
        this.colNameValue = htbl;
    }

    public void addValue(String colName, Object value) {
    	colNameValue.put(colName, value);
    }

    public Object getValue(String colName) {
        return colNameValue.get(colName);
    }
    
//    public void setValue(String colName, Object colValue)
//    {
//    	colNameValue.put(colName, colValue);
//    }
    
    public Hashtable<String, Object> getRowHashtable()
    {
    	return this.colNameValue;
    }

    public int getNumValues() {
        return colNameValue.size();
    }

    
    
}
