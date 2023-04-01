package dbmain;

import java.io.Serializable;
import java.util.Vector;

public class Page implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxRows;
    private int numUsedRows;
    private Vector<Row> rows;

    public Page(int maxRows) {
        this.maxRows = maxRows;
        this.numUsedRows = 0;
        this.rows = new Vector<Row>(maxRows);
    }

    public boolean isFull() {
        return numUsedRows == maxRows;
    }

    public boolean isEmpty() {
        return numUsedRows == 0;
    }

    public void addRow(Row row) {
        if (isFull()) {
            throw new RuntimeException("Page is full");
        }
        rows.add(row);
        numUsedRows++;
    }

    public void deleteRow(int index) {
        if (isEmpty()) {
            throw new RuntimeException("Page is empty");
        }
        rows.remove(index);
        numUsedRows--;
    }

    public Row getRow(int index) {
        if (index < 0 || index >= numUsedRows) {
            throw new IndexOutOfBoundsException();
        }
        return rows.get(index);
    }

    public int getMaxRows() {
        return maxRows;
    }

    public int getNumUsedRows() {
        return numUsedRows;
    }
}