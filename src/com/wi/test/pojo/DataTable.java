package com.wi.test.pojo;

import java.util.ArrayList;
import java.util.List;

public class DataTable {

    private List<Row> rows = new ArrayList<>();
    private String summary = "";
    private String id = "";

    public DataTable(String summary, String id) {
        this.summary = summary;
        this.id = id;
    }

    public DataTable(List<Row> rows, String summary, String id) {
        this.rows = rows;
        this.summary = summary;
        this.id = id;
    }

    public void addRow(Row row) {
        row.setRadioName(row.getRadioName() + " " + this.getId() + " Row " + rows.size());
        this.rows.add(row);
    }

    public Cell getCell(int row, int col) {
        return rows.get(row).getCells().get(col);
    }

    public float getRowPosition(int rowIndex) {
        float currentPosition = 0;
        for (int i = 0; i < rowIndex; i++) {
            currentPosition += rows.get(i).getHeight();
        }
        return currentPosition;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "{ \n");
        sb.append("\t\"rows\" : [ ");
        for (Row r : rows) {
            sb.append(r.toString().replace("\n\t", "\n\t\t").replace("\n}", "\n\t}"));
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        sb.append( "]\n");
        sb.append("}");
        return sb.toString();
    }
}
