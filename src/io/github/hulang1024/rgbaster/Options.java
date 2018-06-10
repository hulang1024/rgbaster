package io.github.hulang1024.rgbaster;

import java.awt.Color;

/**
 * 选项
 */
public class Options {
    Color[] exclude;
    ExcludeClosure excludeClosure;
    boolean palette;
    int paletteSize;
    Color paletteFill;

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
     * @param paletteSize 要返回的调色板里包含的颜色数量，如果为0，表示返回所有颜色。
     *  调色板是根据次数排序后的一组颜色。
     * @param fill 填充颜色，如果图像中颜色数量不够paletteSize就填充颜色。
     *  但是如果图片只有单色，则忽略此参数并自动设置单色。
     * @return
     */
    public Options palette(int paletteSize, Color fill) {
        this.palette = true;
        this.paletteSize = paletteSize;
        this.paletteFill = fill;
        return this;
    }
    
    public Color[] getExclude() {
        return exclude;
    }

    public ExcludeClosure getExcludeClosure() {
        return excludeClosure;
    }

    public boolean isPalette() {
        return palette;
    }

    public int getPaletteSize() {
        return paletteSize;
    }

    public Color getPaletteFill() {
        return paletteFill;
    }
}