package io.github.hulang1024.rgbaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * 图片RGB色调分析器
 * @author hulang
 */
public class Rgbaster {
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

    /**
     * 获取色调
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
     * 获取色调
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
     * 获取色调
     * @return {@link Colors}
     */
    public static Colors colors(byte[] imageByte, Options... options) {
        return colors(new ByteArrayInputStream(imageByte), options);
    }

    /**
     * 获取色调
     * @param image
     * @param optionsArgs 选项
     * @return {@link Colors}
     * @throws IOException
     */
    public static Colors colors(BufferedImage image, Options... optionsArgs) {
        Options options = optionsArgs.length == 0 ? new Options() : optionsArgs[0];

        int width = image.getWidth();
        int height = image.getHeight();
        TreeMap<Integer, Integer> colorCountsMap = new TreeMap<Integer, Integer>();
        int color;
        Integer count;
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {
                color = image.getRGB(x, y);
                if (options.excludeClosure == null || !options.excludeClosure.exclude(color)) {
                    count = colorCountsMap.get(color);
                    colorCountsMap.put(color, count != null ? count + 1 : 1);
                }
            }
        }
        
        Color[] exclude = options.exclude;
        if (exclude != null && exclude.length > 0) {
            for (int i = 0; i < exclude.length; i++) {
                color = exclude[i].getRGB();
                if (colorCountsMap.get(color) != null)
                    colorCountsMap.remove(color);
            }
        }
        
        int colorCount = colorCountsMap.size();
        ColorCount[] colorCounts = new ColorCount[colorCount];
        int index = 0;
        for (Entry<Integer, Integer> entry : colorCountsMap.entrySet()) {
            colorCounts[index++] = new ColorCount(entry.getKey(), entry.getValue());
        }
        Arrays.sort(colorCounts);

        Colors result = new Colors(null, null, 0, null);
        result.colorCount = colorCount;
        
        if (options.isPaletteEnabled()) {
            result.palette = new ArrayList<Color>(
                options.isPaletteAutoSize() ? colorCounts.length : options.paletteSize);
            if (options.isPaletteAutoSize()) {
                for (index = 0; index < colorCounts.length; index++) {
                    result.palette.add( new Color(colorCounts[index].color) );
                }
            } else {
                int cnt = Math.min(options.paletteSize, colorCounts.length);
                for (index = 0; index < cnt; index++) {
                    result.palette.add( new Color(colorCounts[index].color) );
                }
                for (cnt = options.paletteSize - colorCounts.length; cnt > 0; cnt--) {
                    result.palette.add(options.paletteFillColor);
                }
            }
        }
        
        if (colorCount > 0) {
            result.dominant = new Color(colorCounts[0].color);
            result.secondary = colorCounts.length > 1 ? new Color(colorCounts[1].color) : result.dominant;
        }

        return result;
    }
}
