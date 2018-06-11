# RGBaster
一个非常简单的Java库，从图像中提取色调。  
JavaScript版本：[rgbaster.js](https://github.com/briangonzalez/rgbaster.js)  


### 用法
##### 示例
```java
import java.awt.Color;
import java.io.File;
import io.github.hulang1024.rgbaster.Colors;
import io.github.hulang1024.rgbaster.Options;
import io.github.hulang1024.rgbaster.Rgbaster;

Colors colors = Rgbaster.colors( new File(imageDir, "image3.png") );
System.out.println("  Dominant Color: " + colors.getDominant());
System.out.println("  Secondary Color: " + colors.getSecondary());
System.out.println("  Color Count: " + colors.getColorCount());
System.out.println("  Palette: ");
for (Color c : colors.getPalette()) {
    System.out.println("\t" + c);
}
```

#### 配置选项

##### 示例
```java
Colors colors = Rgbaster.colors(
    imageFile,
    new Options()
        .palette(5000, Color.gray)
        .exclude(new Options.ExcludeClosure() {
            public boolean exclude(int color) {
                return new Color(color).getBlue() == 255;
            }
        }));
Colors colors = Rgbaster.colors(
    imageFile,
    new Options()
        .palette(5000, Color.black)
        .exclude(new Color[] { Color.black, Color.white }));
```


### [API文档](https://hulang1024.github.io/rgbaster/doc/index.html)


### 相关
本项目源于做一个自适应背景图片色调的登录按钮的Web系统登录界面，一开始使用了JavaScript版本，但它有一个延迟。  
本Java版本可以在B/S的服务端使用，也可以用于桌面端。


License
-------
MIT
