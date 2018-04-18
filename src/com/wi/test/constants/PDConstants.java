package com.wi.test.constants;

import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

public final class PDConstants {
    public static final PDColor RED = new PDColor(new float[]{1,0,0}, PDDeviceRGB.INSTANCE);
    public static final PDColor GREEN = new PDColor(new float[]{0,1,0}, PDDeviceRGB.INSTANCE);
    public static final PDColor BLUE = new PDColor(new float[]{0,0,1}, PDDeviceRGB.INSTANCE);
    public static final PDColor MAGENTA = new PDColor(new float[]{1,0,1}, PDDeviceRGB.INSTANCE);
    public static final PDColor CYAN = new PDColor(new float[]{0,1,1}, PDDeviceRGB.INSTANCE);
    public static final PDColor YELLOW = new PDColor(new float[]{1,0,1}, PDDeviceRGB.INSTANCE);
    public static final PDColor BLACK = new PDColor(new float[]{0,0,0}, PDDeviceRGB.INSTANCE);
    public static final PDColor WHITE = new PDColor(new float[]{1,1,1}, PDDeviceRGB.INSTANCE);
    public static final String CENTER_ALIGN = "CENTER";
    public static final String LEFT_ALIGN = "LEFT";
    public static final String RIGHT_ALIGN = "RIGHT";
    public static final String TOP_ALIGN = "TOP";
    public static final String BOTTOM_ALIGN = "BOTTOM";

}
