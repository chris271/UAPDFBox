package com.wi.test;

import java.awt.Color;

class Cell {
    private String rbVal = "";
    private String textVal = "";
    private int fontSize = 10;
    private float width = 0;
    private String text = "";
    private String align = "CENTER";
    private Color cellColor = null;
    private Color textColor = null;
    private Color borderColor = null;

    Cell(String text, int fontSize, float width, String align, String rbVal, String textVal) {
        this.text = text;
        this.fontSize = fontSize;
        this.cellColor = Color.white;
        this.textColor = Color.black;
        this.borderColor = Color.black;
        this.width = width;
        this.align = align;
        this.rbVal = rbVal;
        this.textVal = textVal;
    }

    Cell(String text, int fontSize, float width, String align) {
        this.text = text;
        this.fontSize = fontSize;
        this.cellColor = Color.white;
        this.textColor = Color.black;
        this.borderColor = Color.black;
        this.width = width;
        this.align = align;
    }

    Cell(String text, Color cellColor, int fontSize, float width, String align) {
        this.text = text;
        this.fontSize = fontSize;
        this.cellColor = cellColor;
        this.textColor = Color.black;
        this.borderColor = Color.black;
        this.width = width;
        this.align = align;
    }

    String getAlign() {
        return align;
    }

    void setAlign(String align) {
        this.align = align;
    }

    String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    Color getCellColor() {
        return cellColor;
    }

    void setCellColor(Color cellColor) {
        this.cellColor = cellColor;
    }

    Color getTextColor() {
        return textColor;
    }

    void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    Color getBorderColor() {
        return borderColor;
    }

    void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    int getFontSize() {
        return fontSize;
    }

    void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getRbVal() {
        return rbVal;
    }

    public void setRbVal(String rbVal) {
        this.rbVal = rbVal;
    }

    public String getTextVal() {
        return textVal;
    }

    public void setTextVal(String textVal) {
        this.textVal = textVal;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \n");
        sb.append("\t\"Text\": \"");
        sb.append(getText());
        sb.append("\", \n");
        sb.append("\t\"Width\": \"");
        sb.append(getWidth());
        sb.append("\", \n");
        sb.append("\t\"FontSize\": \"");
        sb.append(getFontSize());
        sb.append("\", \n");
        sb.append("\t\"Align\": \"");
        sb.append(getAlign());
        sb.append("\", \n");
        sb.append("\t\"TextColor\": \"");
        sb.append(getTextColor());
        sb.append("\", \n");
        sb.append("\t\"BorderColor\": \"");
        sb.append(getBorderColor());
        sb.append("\", \n");
        sb.append("\t\"CellColor\": \"");
        sb.append(getCellColor());
        sb.append("\", \n");
        sb.append("}");
        return sb.toString();
    }
}
