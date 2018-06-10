package io.github.hulang1024.rgbaster;

import java.awt.Color;
import java.util.List;

/**
 * 色调结果
 */
public class Colors {
    Color dominant;
    Color secondary;
    int colorCount;
    List<Color> palette;
    
    /**
     * 创建一个色调结果
     * @param dominant
     * @param secondary
     * @param colorCount
     * @param palette
     */
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
