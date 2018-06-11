package io.github.hulang1024.rgbaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * 图片RGB颜色分析器。
 * @author hulang
 */
public class Rgbaster {
    /**
     * 获取色调。
     * @return {@link Colors}
     */
    public static Colors colors(File imageFile, Options... options) {
        try {
            return colors(ImageIO.read(imageFile), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取色调。
     * @return {@link Colors}
     */
    public static Colors colors(InputStream imageInputStream, Options... options) {
        try {
            return colors(ImageIO.read(imageInputStream), options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取色调。
     * @return {@link Colors}
     */
    public static Colors colors(byte[] imageByte, Options... options) {
        return colors(new ByteArrayInputStream(imageByte), options);
    }

    /**
     * 获取色调。
     * @param image
     * @param optionsArgs 选项
     * @return {@link Colors}
     * @throws IOException
     */
    public static Colors colors(BufferedImage image, Options... optionsArgs) {
        Options options = optionsArgs.length == 0 ? new Options() : optionsArgs[0];

        int width = image.getWidth();
        int height = image.getHeight();
        TreeMap<Integer, ColorCount> colorCountsMap = new TreeMap<Integer, ColorCount>();
        int rgba, key;
        ColorCount counter;
        for (int x = 0, y; x < width; x++) {  
            for (y = 0; y < height; y++) {
                rgba = image.getRGB(x, y);
                key = options.ignoreAlpha ? (rgba & 0xFFFFFF) : rgba;
                if (options.excludeClosure == null || !options.excludeClosure.exclude(rgba)) {
                    counter = colorCountsMap.get(key);
                    if (counter == null) {
                        colorCountsMap.put(key, new ColorCount(rgba, 1));
                    } else {
                        counter.count++;
                    }
                }
            }
        }
        
        Color[] exclude = options.exclude;
        if (exclude != null && exclude.length > 0) {
            for (int i = 0; i < exclude.length; i++) {
                rgba = exclude[i].getRGB();
                key = options.ignoreAlpha ? (rgba & 0xFFFFFF) : rgba;
                colorCountsMap.remove(key);
            }
        }
        
        int colorCount = colorCountsMap.size();
        ColorCount[] colorCounts = new ColorCount[colorCount];
        colorCountsMap.values().toArray(colorCounts);
        Arrays.sort(colorCounts);

        Colors result = new Colors(null, null, 0, null);
        result.colorCount = colorCount;
        
        if (options.isPaletteEnabled()) {
            result.palette = new ArrayList<Color>(
                options.isPaletteAutoSize() ? colorCounts.length : options.paletteSize);
            if (options.isPaletteAutoSize()) {
                for (int index = 0; index < colorCounts.length; index++) {
                    result.palette.add( new Color(colorCounts[index].color, true) );
                }
            } else {
                int cnt = Math.min(options.paletteSize, colorCounts.length);
                for (int index = 0; index < cnt; index++) {
                    result.palette.add( new Color(colorCounts[index].color, true) );
                }
                for (cnt = options.paletteSize - colorCounts.length; cnt > 0; cnt--) {
                    result.palette.add(options.paletteFillColor);
                }
            }
        }
        
        if (colorCount > 0) {
            result.dominant = new Color(colorCounts[0].color, true);
            result.secondary = colorCounts.length > 1 ? new Color(colorCounts[1].color, true) : result.dominant;
        }

        return result;
    }

    private static class ColorCount implements Comparable<ColorCount> {
        int color;
        int count;
        public ColorCount(int color, int count) {
            this.color = color;
            this.count = count;
        }
        
        public int compareTo(ColorCount cc) {
            return cc.count - this.count;
        }
    }
}
