package com.wi.test.pojo;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Cell> cells = new ArrayList<>();
    private int cols = 0;
    private float height = 0;

    public Row(List<Cell> cells, float height) {
        this.cells = cells;
        cols = cells.size();
        this.height = height;
    }

    public Row(float height) {
        this.height = height;
    }

    public Row() { }

    public void addCell(Cell cell) {
        cells.add(cell);
        cols++;
    }

    public int getCols() {
        return cols;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
        cols = cells.size();
    }

    public float getCellPosition(int cellIndex) {
        float currentPosition = 0;
        for (int i = 0; i < cellIndex; i++) {
            currentPosition += cells.get(i).getWidth();
        }
        return currentPosition;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "{ \n");
        sb.append("\t\"cells\" : [ ");
        for (Cell c : cells) {
            sb.append(c.toString().replace("\n\t", "\n\t\t").replace("\n}", "\n\t}"));
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(", "));
        sb.append( "]\n");
        sb.append("}");
        return sb.toString();
    }
}
