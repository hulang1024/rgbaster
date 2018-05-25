package io.github.hulang1024.rgbaster;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * 获取图片色调
 * @author hulang
 * @sice 2018-05-04
 */
public class Rgbaster {
    /**
     * 色调结果
     */
    public static class Colors {
        private Color dominant;
        private Color secondary;
        private int colorCount;
        private List<Color> palette;
        
        public Colors(Color dominant, Color secondary, int colorCount, List<Color> palette) {
            this.dominant = dominant;
            this.secondary = secondary;
            this.colorCount = colorCount;
            this.palette = palette;
        }

        /** 主色调 */
        public Color getDominant() {
            return dominant;
        }
        
        /** 次色调 */
        public Color getSecondary() {
            return secondary;
        }

        /**
         * 图片中颜色数量
         * @return
         */
        public int getColorCount() {
            return colorCount;
        }

        /**
         * 升序排序的色调
         */
        public List<Color> getPalette() {
            return palette;
        }

    }
    
    public static class Options {
        private Color[] exclude;
        private ExcludeClosure excludeClosure;
        private boolean palette;
        private int paletteSize;
        private Color paletteFill;
        
        /**
         * 排除颜色规则
         */
        public static interface ExcludeClosure {
            /**
             * @return 为true则排除
             */
            boolean exclude(int color);
        }
        
        /**
         * 设置排除颜色
         * @param exclude 想要排除的颜色数组
         * @return
         */
        public Options exclude(Color[] exclude) {
            this.exclude = exclude;
            return this;
        }
        
        /**
         * 设置排除颜色规则
         * @param closure 自定义排除的规则实现
         * @return
         */
        public Options exclude(ExcludeClosure closure) {
            this.excludeClosure = closure;
            return this;
        }
        
        /**
         * 调色板选项
         * @param paletteSize 要返回的调色板里包含的颜色数量, 如果为0, 表示返回所有颜色. 调色板是根据次数排序后的一组颜色.
         * @param fill 填充颜色, 如果图像中颜色数量不够<code>paletteSize</code>就填充颜色. 但是如果图片只有单色, 则忽略此参数并自动设置单色.
         * @return
         */
        public Options palette(int paletteSize, Color fill) {
            this.palette = true;
            this.paletteSize = paletteSize;
            this.paletteFill = fill;
            return this;
        }
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
    
    public static Colors colors(File imageFile) throws IOException {
        return colors(imageFile, new Rgbaster.Options());
    }
    
    /**
     * 获取色调
     * @param imgFile 图片文件
     * @param options 选项
     * @return Rgbaster.Colors
     * @throws IOException
     */
    public static Colors colors(File imageFile, Options options) throws IOException {
        BufferedImage image = ImageIO.read(imageFile);
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
