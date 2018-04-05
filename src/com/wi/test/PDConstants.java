package com.wi.test;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

final class PDConstants {
    static final PDColor RED = new PDColor(new float[]{1,0,0}, PDDeviceRGB.INSTANCE);
    static final PDColor GREEN = new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE);
    static final PDColor BLUE = new PDColor(new float[]{0,0,1}, PDDeviceRGB.INSTANCE);
    static final PDColor MAGENTA = new PDColor(new float[]{1,0,1}, PDDeviceRGB.INSTANCE);
    static final PDColor CYAN = new PDColor(new float[]{0,1,1}, PDDeviceRGB.INSTANCE);
    static final PDColor YELLOW = new PDColor(new float[]{1,0,1}, PDDeviceRGB.INSTANCE);
    static final PDColor BLACK = new PDColor(new float[]{0,0,0}, PDDeviceRGB.INSTANCE);
    static final PDColor WHITE = new PDColor(new float[]{1,1,1}, PDDeviceRGB.INSTANCE);
    static final String CENTER_ALIGN = "CENTER";
    static final String LEFT_ALIGN = "LEFT";
    static final String RIGHT_ALIGN = "RIGHT";
    static final String TOP_ALIGN = "TOP";
    static final String BOTTOM_ALIGN = "BOTTOM";

}
