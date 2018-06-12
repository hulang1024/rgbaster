package io.github.hulang1024.rgbaster;

import java.awt.Color;

/**
 * @author hulang
 */
public class WebColorValueUtils {
    
    /**
     * rgb string.
     * @return rgb(r, g, b)
     */
    public static String rgbString(Color color) {
        return color == null ? "" : new StringBuilder("rgb(")
            .append(color.getRed()).append(",")
            .append(color.getGreen()).append(",")
            .append(color.getBlue()).append(")").toString();
    }
    
    /**
     * rgba string.
     * @return rgba(r, g, b, a)
     */
    public static String rgbaString(Color color) {
        return color == null ? "" : new StringBuilder("rgba(")
            .append(color.getRed()).append(",")
            .append(color.getGreen()).append(",")
            .append(color.getBlue()).append(",")
            .append(color.getAlpha()).append(")").toString();
    }
    
    /**
     * rgb hex string.
     * @return <code>#rrggbb</code>, eg. <code>#ffffff</code>
     */
    public static String hexString(Color color) {
        return color == null ? "" : new StringBuilder("#").append(hex(color, color.getAlpha() != 255)).toString();
    }

    /**
     * rgba hex string.
     * @return eg. #ffffffff
     */
    public static String hexString(Color color, boolean hasAlpha) {
        return color == null ? "" : new StringBuilder("#").append(hex(color, hasAlpha)).toString();
    }
    
    /**
     * rgb hex string.
     * @return <code>rrggbb</code> or <code>rrggbbaa</code>, eg. <code>ff00ff</code> or <code>ff00ff11</code>
     */
    public static String hex(Color color, boolean hasAlpha) {
        if (color == null)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(Integer.toHexString(color.getRGB() & 0xFFFFFF));
        if (hasAlpha) {
            sb.append(Integer.toHexString(color.getAlpha()));
        }
        return sb.toString();
    }
}
