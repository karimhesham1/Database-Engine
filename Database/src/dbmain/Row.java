package dbmain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Row implements Serializable {
    private static final long serialVersionUID = 1L;
    private Vector<Object> values;
    private int pkIndex;

    public Row() {
        this.values = new Vector<Object>();
    }

    public void addValue(Object value) {
        values.add(value);
    }

    public Object getValue(int index) {
        return values.get(index);
    }

    public int getNumValues() {
        return values.size();
    }
    public int getPkIndex() {
    	return pkIndex;
    }
}