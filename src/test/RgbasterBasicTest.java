package test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import io.github.hulang1024.rgbaster.Colors;
import io.github.hulang1024.rgbaster.Options;
import io.github.hulang1024.rgbaster.Rgbaster;
import io.github.hulang1024.rgbaster.WebColorValueUtils;

/**
 * @author hulang
 */
public class RgbasterBasicTest {
    static String imageDir = RgbasterBasicTest.class.getResource("/").getPath();
    static Colors colors = null;
    
    @Test
    public void testPureColor() {
        Color grape = new Color(128, 0, 255);

        colors = Rgbaster.colors( new File(imageDir, "grape.png") );
        assertEquals(grape, colors.getDominant());
        assertEquals(grape, colors.getSecondary());
        assertEquals(1, colors.getColorCount());
        assertEquals(null, colors.getPalette());
        
        colors = Rgbaster.colors(
            new File(imageDir, "grape.png"),
            new Options()
                .enbalePalette());
        assertEquals(grape, colors.getDominant());
        assertEquals(grape, colors.getSecondary());
        assertEquals(1, colors.getColorCount());
        assertTrue(colors.getPalette() != null && colors.getPalette().size() == colors.getColorCount());
    }

    @Test
    public void testPalette() {
        Color orderedColors[] = new Color[]{
            new Color(255,255,255),
            new Color(255,0,0),
            new Color(255,0,255),
            new Color(255,128,0),
            new Color(128,0,128),
            new Color(0,0,0),
            new Color(0,255,0) };

        final int fixPaletteSize = 8;
        colors = Rgbaster.colors( new File(imageDir, "7colors.png"),
                new Options().palette(fixPaletteSize, Color.white));
        
        assertEquals(orderedColors[0], colors.getDominant());
        assertEquals(orderedColors[1], colors.getSecondary());
        assertEquals(7, colors.getColorCount());
        assertEquals(fixPaletteSize, colors.getPalette().size());
        assertArrayEquals(orderedColors, colors.getPalette().subList(0, orderedColors.length).toArray());
    }

    @Test
    public void testExcludeOption() {
        Color grape = new Color(128, 0, 255);
        
        colors = Rgbaster.colors(
            new File(imageDir, "grape.png"),
            new Options()
                .exclude(new Color[]{ grape }));
        assertNull(colors.getDominant());
        assertNull(colors.getSecondary());
        assertEquals(0, colors.getColorCount());
        assertNull(colors.getPalette());
        
        colors = Rgbaster.colors(
            new File(imageDir, "7colors.png"),
            new Options()
                .exclude(new Options.ExcludeClosure() {
                    public boolean exclude(int rgb) {
                        Color color = new Color(rgb);
                        return color.getGreen() == 255 && color.getBlue() == 255;
                    }
                }));
        assertEquals(6, colors.getColorCount());
    }
    
    @Test
    public void testAlpha() {
        colors = Rgbaster.colors(
            new File(imageDir, "hasalpha.png"),
            new Options().exclude(new Color[]{ Color.white }));
        assertTrue(new Color(12, 160, 251).equals(colors.getDominant()));
        assertTrue(new Color(12, 160, 251).equals(new Color(colors.getDominant().getRGB(), false)));
        assertTrue(new Color(128, 128, 128).equals(new Color(colors.getSecondary().getRGB(), false)));
        assertEquals(2, colors.getColorCount());

        colors = Rgbaster.colors(
            new File(imageDir, "hasalpha.png"),
            new Options()
                .exclude(new Color[]{ new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 0) })
                .ignoreAlpha(false));

        assertEquals(
            new Color(colors.getDominant().getRGB()),
            new Color(colors.getSecondary().getRGB()));

        assertTrue(!new Color(12, 160, 251).equals(colors.getDominant()));
        assertTrue(new Color(12, 160, 251).equals(new Color(colors.getDominant().getRGB(), false)));
        assertEquals(84, colors.getColorCount());
    }
    
    @Test(expected=Exception.class)
    public void testException() {
        Rgbaster.colors( new File(imageDir, "non existent") );
    }

    @Test
    public void testPhoto() {
        File imageFile = new File(imageDir, "sky_and_greens_photo.png");
        
        colors = Rgbaster.colors(imageFile,
            new Options().enbalePalette());
        assertTrue(new Color(78, 184, 255).equals(colors.getDominant())
            || new Color(67, 172, 255).equals(colors.getDominant()));

        colors = Rgbaster.colors(imageFile,
            new Options()
                .palette(10, null));

        //输出为html
        File htmlFile = new File(imageDir, "sky_and_greens_photo_colors_output.html");
        FileWriter writer;
        try {
            writer = new FileWriter(htmlFile);
            writer.write("<style>"
                + " .cell { margin:0;padding:0;width:20px;height:20px; }"
                + " .palette>div { float: left; } </style>");

            writer.write("<img src=" + imageFile.getPath() + "><br>");
            
            writer.write("<span>Dominant Color: </span>");
            writer.write("<div class='cell' style='background-color:" + WebColorValueUtils.rgbString(colors.getDominant()) + "'></div>");
            
            writer.write("<span>Secondary Color: </span>");
            writer.write("<div class='cell' style='background-color:" + WebColorValueUtils.rgbString(colors.getSecondary()) + "'></div>");
            
            writer.write("<span>Blues Palette: </span>");
            writer.write("<div class='palette'>");
            for (Color c : colors.getPalette()) {
                writer.write("<div class='cell' style='background-color:" + WebColorValueUtils.rgbString(c) + "'></div>");
            }
            writer.write("</div>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputStreamMethod() throws FileNotFoundException {
        assertEquals(7, Rgbaster.colors( new FileInputStream(new File(imageDir, "7colors.png")) ).getColorCount());
    }

    @Test
    public void testByteMethod() throws IOException {
        File file = new File(imageDir, "7colors.png");
        byte[] imageByte = new byte[(int)file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(imageByte);
        fis.close();
        assertEquals(7, Rgbaster.colors(imageByte).getColorCount());
    }
    
    @Test
    public void testValueUtils() {
        Color color = new Color(255, 2, 170);
        assertEquals("rgb(255,2,170)", WebColorValueUtils.rgbString(color));
        assertEquals("rgba(255,2,170,255)", WebColorValueUtils.rgbaString(color));
        assertEquals("#ff02aa", WebColorValueUtils.hexString(color));
        Color colorWithAlpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        assertEquals("#ff02aa64", WebColorValueUtils.hexString(colorWithAlpha));
        assertEquals("#ff02aa64", WebColorValueUtils.hexString(colorWithAlpha, true));
        assertEquals("#ff02aa", WebColorValueUtils.hexString(colorWithAlpha, false));
        Color colorWith255Alpha = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        assertEquals("#ff02aa64", WebColorValueUtils.hexString(colorWith255Alpha));
        assertEquals("#ff02aa64", WebColorValueUtils.hexString(colorWith255Alpha, true));
        assertEquals("#ff02aa", WebColorValueUtils.hexString(colorWith255Alpha, false));
        
        assertEquals("ff02aa", WebColorValueUtils.hex(color, false));
        
        assertEquals("", WebColorValueUtils.rgbString(null));
        assertEquals("", WebColorValueUtils.rgbaString(null));
        assertEquals("", WebColorValueUtils.hexString(null));
        assertEquals("", WebColorValueUtils.hexString(null, false));
        assertEquals("", WebColorValueUtils.hex(null, false));
    }
}
