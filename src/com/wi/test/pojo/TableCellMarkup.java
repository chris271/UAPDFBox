package com.wi.test.pojo;

public class TableCellMarkup {

    private boolean header;
    private String id;
    private String scope;
    private String[] headers;
    private int rowSpan;
    private int colspan;

    public TableCellMarkup() {
        this.scope = "";
        this.id = "";
        this.header = false;
        this.colspan = 1;
        this.rowSpan = 1;
        this.headers = new String[0];
    }

    public TableCellMarkup(String scope, String id) {
        this.scope = scope;
        this.id = id;
        this.header = true;
        this.colspan = 1;
        this.rowSpan = 1;
        this.headers = new String[0];
    }

    public TableCellMarkup(int colSpan, String scope, String id) {
        this.scope = scope;
        this.id = id;
        this.header = true;
        this.colspan = colSpan;
        this.rowSpan = 1;
        this.headers = new String[0];
    }

    public TableCellMarkup(String[] headers, String id) {
        this.scope = "";
        this.id = id;
        this.header = true;
        this.colspan = 1;
        this.rowSpan = 1;
        this.headers = headers;
    }

    public TableCellMarkup(String[] headers) {
        this.scope = "";
        this.id = "";
        this.header = false;
        this.colspan = 1;
        this.rowSpan = 1;
        this.headers = headers;
    }

    public TableCellMarkup(String scope, String[] headers, String id) {
        this.scope = "";
        this.id = id;
        this.header = true;
        this.colspan = 1;
        this.headers = headers;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String[] getHeaders() {
        return headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }
}
