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

    //Appearance strings for off and on radio button states
    public static final String OFF_N_STRING = "1 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  5 0 m\n" +
            "  5 2.7615 2.7615 5 0 5 c\n" +
            "  -2.7615 5 -5 2.7615 -5 0 c\n" +
            "  -5 -2.7615 -2.7615 -5 0 -5 c\n" +
            "  2.7615 -5 5 -2.7615 5 0 c\n" +
            "  f\n" +
            "Q\n" +
            "0 G\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  4.5 0 m\n" +
            "  4.5 2.4854 2.4854 4.5 0 4.5 c\n" +
            "  -2.4854 4.5 -4.5 2.4854 -4.5 0 c\n" +
            "  -4.5 -2.4854 -2.4854 -4.5 0 -4.5 c\n" +
            "  2.4854 -4.5 4.5 -2.4854 4.5 0 c\n" +
            "  s\n" +
            "Q\n";
    public static final String ON_N_STRING = "1 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  5 0 m\n" +
            "  5 2.7615 2.7615 5 0 5 c\n" +
            "  -2.7615 5 -5 2.7615 -5 0 c\n" +
            "  -5 -2.7615 -2.7615 -5 0 -5 c\n" +
            "  2.7615 -5 5 -2.7615 5 0 c\n" +
            "  f\n" +
            "Q\n" +
            "0 G\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  4.5 0 m\n" +
            "  4.5 2.4854 2.4854 4.5 0 4.5 c\n" +
            "  -2.4854 4.5 -4.5 2.4854 -4.5 0 c\n" +
            "  -4.5 -2.4854 -2.4854 -4.5 0 -4.5 c\n" +
            "  2.4854 -4.5 4.5 -2.4854 4.5 0 c\n" +
            "  s\n" +
            "Q\n" +
            "0 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  2 0 m\n" +
            "  2 1.1046 1.1046 2 0 2 c\n" +
            "  -1.1046 2 -2 1.1046 -2 0 c\n" +
            "  -2 -1.1046 -1.1046 -2 0 -2 c\n" +
            "  1.1046 -2 2 -1.1046 2 0 c\n" +
            "  f\n" +
            "Q\n";
    public static final String OFF_D_STRING = "0.749023 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  5 0 m\n" +
            "  5 2.7615 2.7615 5 0 5 c\n" +
            "  -2.7615 5 -5 2.7615 -5 0 c\n" +
            "  -5 -2.7615 -2.7615 -5 0 -5 c\n" +
            "  2.7615 -5 5 -2.7615 5 0 c\n" +
            "  f\n" +
            "Q\n" +
            "0 G\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  4.5 0 m\n" +
            "  4.5 2.4854 2.4854 4.5 0 4.5 c\n" +
            "  -2.4854 4.5 -4.5 2.4854 -4.5 0 c\n" +
            "  -4.5 -2.4854 -2.4854 -4.5 0 -4.5 c\n" +
            "  2.4854 -4.5 4.5 -2.4854 4.5 0 c\n" +
            "  s\n" +
            "Q\n";
    public static final String ON_D_STRING = "0.749023 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  5 0 m\n" +
            "  5 2.7615 2.7615 5 0 5 c\n" +
            "  -2.7615 5 -5 2.7615 -5 0 c\n" +
            "  -5 -2.7615 -2.7615 -5 0 -5 c\n" +
            "  2.7615 -5 5 -2.7615 5 0 c\n" +
            "  f\n" +
            "Q\n" +
            "0 G\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  4.5 0 m\n" +
            "  4.5 2.4854 2.4854 4.5 0 4.5 c\n" +
            "  -2.4854 4.5 -4.5 2.4854 -4.5 0 c\n" +
            "  -4.5 -2.4854 -2.4854 -4.5 0 -4.5 c\n" +
            "  2.4854 -4.5 4.5 -2.4854 4.5 0 c\n" +
            "  s\n" +
            "Q\n" +
            "0 g\n" +
            "q\n" +
            "  1 0 0 1 5 10 cm\n" +
            "  2 0 m\n" +
            "  2 1.1046 1.1046 2 0 2 c\n" +
            "  -1.1046 2 -2 1.1046 -2 0 c\n" +
            "  -2 -1.1046 -1.1046 -2 0 -2 c\n" +
            "  1.1046 -2 2 -1.1046 2 0 c\n" +
            "  f\n" +
            "Q\n";

}
