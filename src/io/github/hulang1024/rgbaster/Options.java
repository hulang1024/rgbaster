package io.github.hulang1024.rgbaster;

import java.awt.Color;

/**
 * 选项。
 * @author hulang
 */
public class Options {
    Color[] exclude;
    ExcludeClosure excludeClosure;
    boolean paletteEnabled = false;
    int paletteSize = -1;
    Color paletteFillColor;
    boolean ignoreAlpha = true;

    /**
     * 排除颜色规则。
     */
    public static interface ExcludeClosure {
        /**
         * @return 为true则排除
         */
        boolean exclude(int rgba);
    }
    
    /**
     * 设置是否忽略图像的Alpha通道，默认为<code>true</code>。
     * @param isIgnore
     * @return
     */
    public Options ignoreAlpha(boolean isIgnore) {
        this.ignoreAlpha = isIgnore;
        return this;
    }
    
    /**
     * 设置排除颜色。
     * @param exclude 想要排除的颜色数组
     */
    public Options exclude(Color[] exclude) {
        this.exclude = exclude;
        return this;
    }
    
    /**
     * 设置排除颜色规则。
     * @param closure 自定义排除的规则实现
     */
    public Options exclude(ExcludeClosure closure) {
        this.excludeClosure = closure;
        return this;
    }
    
    /**
     * 启用并设置调色板参数。
     * @param paletteSize 调色板大小，-1 表示动态大小
     * @param fillColor 填充颜色，当从图像中分析出的颜色数量小于调色板大小时
     */
    public Options palette(int paletteSize, Color fillColor) {
        enbalePalette();
        this.paletteSize = paletteSize;
        this.paletteFillColor = fillColor;
        return this;
    }
    
    /**
     * 启用调色板
     */
    public Options enbalePalette() {
        this.paletteEnabled = true;
        this.paletteSize = -1;  // 默认动态大小
        return this;
    }

    public boolean isPaletteAutoSize() {
        return this.paletteSize == -1;
    }
    
    public Color[] getExclude() {
        return exclude;
    }

    public ExcludeClosure getExcludeClosure() {
        return excludeClosure;
    }

    public boolean isPaletteEnabled() {
        return paletteEnabled;
    }

    public int getPaletteSize() {
        return paletteSize;
    }

    public Color getPaletteFillColor() {
        return paletteFillColor;
    }

    public boolean isIgnoreAlpha() {
        return ignoreAlpha;
    }
}