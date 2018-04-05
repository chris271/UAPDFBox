package com.wi.test;

import java.util.ArrayList;
import java.util.List;

class Row {
    private List<Cell> cells = new ArrayList<>();
    private int cols = 0;
    private float height = 0;

    Row(List<Cell> cells, float height) {
        this.cells = cells;
        cols = cells.size();
        this.height = height;
    }

    void addCell(Cell cell) {
        cells.add(cell);
        cols++;
    }

    int getCols() {
        return cols;
    }

    List<Cell> getCells() {
        return cells;
    }

    void setCells(List<Cell> cells) {
        this.cells = cells;
        cols = cells.size();
    }

    float getCellPosition(int cellIndex) {
        float currentPosition = 0;
        for (int i = 0; i < cellIndex; i++) {
            currentPosition += cells.get(i).getWidth();
        }
        return currentPosition;
    }

    float getHeight() {
        return height;
    }

    void setHeight(float height) {
        this.height = height;
    }
}
