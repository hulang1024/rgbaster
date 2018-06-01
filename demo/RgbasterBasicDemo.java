import static java.lang.System.out;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.github.hulang1024.rgbaster.Rgbaster;

/**
 * Rgbaster基本用法示例
 * @author hulang
 * @sice 2018-05-25
 */
public class RgbasterBasicDemo {
    public static void main(String[] args) {
        String imageDir = RgbasterBasicDemo.class.getResource("/").getPath();

        Rgbaster.Colors colors = null;
        // 单色
        try {
            colors = Rgbaster.colors( new File(imageDir, "grape.png") );
        } catch (IOException e) { return; }
        out.println("\ngrape.png: ");
        out.println("  Dominant Color: " + colors.getDominant());
        out.println("  Secondary Color: " + colors.getSecondary());
        
        // 多纯色
        try {
            colors = Rgbaster.colors( new File(imageDir, "colors.png"),
                new Rgbaster.Options().palette(8, Color.white));
        } catch (IOException e) { return; }
        out.println("\ncolors.png: ");
        out.println("  Dominant Color: " + colors.getDominant());
        out.println("  Secondary Color: " + colors.getSecondary());
        out.println("  palette: ");
        for (Color c : colors.getPalette()) {
            out.println("\t" + c);
        }

        // 杂色
        File imageFile = new File(imageDir, "image3.png");
        try {
            colors = Rgbaster.colors(imageFile,
                new Rgbaster.Options()
                    .palette(5000, Color.black)
                    .exclude(new Rgbaster.Options.ExcludeClosure() {
                        public boolean exclude(int color) {
                            return false;
                            //return new Color(color).getBlue() == 255;
                        }
                    }));
        } catch (IOException e) { return; }
        //输出为output.html
        File htmlFile = new File(imageDir + "../", "output.html");
        FileWriter writer;
        try {
            writer = new FileWriter(htmlFile);
            writer.write("<style> div { float:right;margin:0;padding:0;width:20px;height:20px; } </style>");
            writer.write("<img src=" + imageFile.getPath() + "><br>");
            for (Color c : colors.getPalette()) {
                writer.write("<div style='background-color:" + rgb(c) + "'></div>");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String rgb(Color color) {
        return "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")";
    }
}
