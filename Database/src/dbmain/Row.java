package dbmain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Row implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Object> values;

    public Row() {
        this.values = new ArrayList<Object>();
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
}