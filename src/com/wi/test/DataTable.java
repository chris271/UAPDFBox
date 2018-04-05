package com.wi.test;

import java.util.ArrayList;
import java.util.List;

class DataTable {
    private List<Row> rows = new ArrayList<>();
    
    void addRow(Row row) {
        this.rows.add(row);
    }

    Cell getCell(int row, int col) {
        return rows.get(row).getCells().get(col);
    }

    float getRowPosition(int rowIndex) {
        float currentPosition = 0;
        for (int i = 0; i < rowIndex; i++) {
            currentPosition += rows.get(i).getHeight();
        }
        return currentPosition;
    }

    List<Row> getRows() {
        return rows;
    }

    void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
