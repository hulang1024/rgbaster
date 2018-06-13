<a href="#rgbaster"><img width="190" src="doc/images/title.png" alt="RGBaster"></a>


一个简单的Java库，从图像中提取色调。  


## Usage
#### 示例
```java
import java.awt.Color;
import java.io.File;
import static java.lang.System.*;
import io.github.hulang1024.rgbaster.Colors;
import io.github.hulang1024.rgbaster.Options;
import io.github.hulang1024.rgbaster.Rgbaster;
import io.github.hulang1024.rgbaster.WebColorValueUtils;

Colors colors = Rgbaster.colors( new File(imageDir, "foo.png") );
out.println("  Dominant Color: " + WebColorValueUtils.rgbString(colors.getDominant()));
out.println("  Secondary Color: " + WebColorValueUtils.rgbaString(colors.getSecondary()));
out.println("  Color Count: " + colors.getColorCount());
out.println("  Palette: ");
for (Color c : colors.getPalette()) {
    out.println("\t" + WebColorValueUtils.hexString(c));
}
```

#### Options

##### 示例
```java
Colors colors = Rgbaster.colors(
    imageFile,
    new Options()
        .palette(5000, Color.black)
        .exclude(new Color[] { Color.black, Color.white }));

Colors colors = Rgbaster.colors(
    imageFile,
    new Options()
        .enbalePalette()
        .ignoreAlpha(false)
        .exclude(new Options.ExcludeClosure() {
            public boolean exclude(int color) {
                return new Color(color).getBlue() == 255;
            }
        }));
```


## [API文档](https://hulang1024.github.io/rgbaster/doc/index.html)


## About
JavaScript版本：[rgbaster.js](https://github.com/briangonzalez/rgbaster.js)，JavaScript版本有一些延迟，因此有了本Java版本，并增加了一些特性。  

##### 运用例子截图  
![kanxi](doc/images/kanxi.gif)
![kanxi_palette](doc/images/kanxi_palette.png)

![cat_palette](doc/images/cat_palette.png)

![login_color_adaptive](doc/images/login_color_adaptive.png)


## Thanks
[Febby315](https://github.com/Febby315)  
   GitHub markdown不支持文字颜色样式，只好将标题做成图片 :D。

## License
MIT
