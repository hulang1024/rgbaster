# RGBaster
一个非常简单的Java库，从图像中提取色调。  
JavaScript版本：[rgbaster.js](https://github.com/briangonzalez/rgbaster.js)  

### 用法
用法非常简单，创建一个图像File，就可以获取色调和调色板。

```java
import java.awt.Color;
import java.io.File;
import io.github.hulang1024.rgbaster.Colors;
import io.github.hulang1024.rgbaster.Options;
import io.github.hulang1024.rgbaster.Rgbaster;

Colors colors = Rgbaster.colors( new File(imageDir, "image3.png") );
System.out.println("  Dominant Color: " + colors.getDominant());
System.out.println("  Secondary Color: " + colors.getSecondary());
System.out.println("  Palette: ");
for (Color c : colors.getPalette()) {
    System.out.println("\t" + c);
}
```


### 配置选项
`colors`函数可以接收一个表示选项的对象，它是可选的第二个参数。有以下选项:
#### `palette` `()`
* `int` `paletteSize`  
要返回的调色板里包含的颜色数量，如果为0，表示返回所有颜色。调色板是根据次数排序后的一组颜色。
* `java.awt.Color` `fill`  
填充颜色，如果图像中颜色数量不够`paletteSize`就填充颜色。但是如果图片只有单色，则忽略此参数并自动设置单色。

#### `exclude` `()`
* `Color[]`  
在计数过程中要排除的颜色们的数组，一个排除白色和黑色的例子: `new Color[] { Color.black, Color.white }`
* `ExcludeClosure`  
同上，但是接口，可以更为动态地排除。


### 完整示例

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


### 相关
本项目源于做一个自适应背景图片色调的登录按钮的Web系统登录界面，一开始使用了JavaScript版本，但它有一个延迟。  
本Java版本可以在B/S的服务端使用，也可以用于桌面端。


License
-------
MIT
