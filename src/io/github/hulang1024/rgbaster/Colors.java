package io.github.hulang1024.rgbaster;

import java.awt.Color;
import java.util.List;

/**
 * 图片颜色分析结果
 * @author hulang
 */
public class Colors {
    Color dominant;
    Color secondary;
    int colorCount;
    List<Color> palette;
    
    /**
     * 创建一个颜色结果
     */
    public Colors(Color dominant, Color secondary, int colorCount, List<Color> palette) {
        this.dominant = dominant;
        this.secondary = secondary;
        this.colorCount = colorCount;
        this.palette = palette;
    }

    /**
     * 主色
     */
    public Color getDominant() {
        return dominant;
    }
    
    /**
     * 次色 。少数情况下，当图片只有一种颜色（主色）时，等于<code>getDominant()</code>
     */
    public Color getSecondary() {
        return secondary;
    }

    /**
     * 图片中不同颜色的数量
     * （ps：<code>getColorCount() != getPalette().size()</code>）
     */
    public int getColorCount() {
        return colorCount;
    }

    /**
     * 返回调色板
     * @return 按颜色在图片中的出现次数降序排序后的一组颜色
     */
    public List<Color> getPalette() {
        return palette;
    }
}
