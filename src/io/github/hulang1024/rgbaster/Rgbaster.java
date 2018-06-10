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
 * 获取图片色调
 * @author hulang
 * @sice 2018-05-04
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
     */
    public static Colors colors(File imageFile, Options... options) throws IOException {
        return colors(ImageIO.read(imageFile), options);
    }

    /**
     * 获取色调
     */
    public static Colors colors(InputStream imageInputStream, Options... options) throws IOException {
        return colors(ImageIO.read(imageInputStream), options);
    }

    /**
     * 获取色调
     */
    public static Colors colors(byte[] imageByte, Options... options) throws IOException {
        return colors(new ByteArrayInputStream(imageByte), options);
    }

    /**
     * 获取色调
     * @param image BufferedImage对象
     * @param optionsArgs 可选的选项
     * @return {@link Colors}
     * @throws IOException
     */
    public static Colors colors(BufferedImage image, Options... optionsArgs) throws IOException {
        Options options = optionsArgs.length == 0 ? new Options() : optionsArgs[0];

        int width = image.getWidth();
        int height = image.getHeight();
        TreeMap<Integer, Integer> colorCountsMap = new TreeMap<Integer, Integer>();
        int x, y, color;
        Integer count;
        for (x = 0; x < width; x++) {  
            for (y = 0; y < height; y++) {
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
                    colorCountsMap.put(color, 0);
            }
        }
        
        int colorCount = colorCountsMap.size();
        ColorCount[] colorCounts = new ColorCount[colorCount];
        int index = 0;
        for (Entry<Integer, Integer> entry : colorCountsMap.entrySet()) {
            colorCounts[index++] = new ColorCount(entry.getKey(), entry.getValue());
        }
        
        Colors result = new Colors(null, null, colorCount, null);
        if (colorCounts.length > 0) {
            Arrays.sort(colorCounts);
            result.dominant = new Color(colorCounts[0].color);
            result.secondary = colorCounts.length > 1 ? new Color(colorCounts[1].color) : result.dominant;

            if (options.palette) {
                int paletteSize = options.paletteSize == 0 ?
                    colorCounts.length : Math.min(options.paletteSize, colorCounts.length);
                result.palette = new ArrayList<Color>(paletteSize);
                for (index = 0; index < paletteSize; index++) {
                    result.palette.add( new Color(colorCounts[index].color) );
                }
                Color fill = colorCount > 1 ? options.paletteFill : result.dominant;
                for (int fillCnt = options.paletteSize - paletteSize; fillCnt > 0; fillCnt--) {
                    result.palette.add(fill);
                }
            } else {
                result.palette = new ArrayList<Color>();
            }
        }

        return result;
    }
}
