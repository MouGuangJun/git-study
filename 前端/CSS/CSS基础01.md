# CSS基础

## Display

### flex布局

#### 定义

display:flex 是一种<font color='red'>布局方式</font>。它即可以应用于容器中，也可以应用于行内元素。是W3C提出的一种新的方案，可以简便、完整、响应式地实现<font color='red'>各种页面布局</font>。目前，它已经得到了所有浏览器的支持。

Flex是Flexible Box的缩写，意为"弹性布局"，用来为盒状模型提供最大的灵活性。设为Flex布局以后，子元素的float、clear和vertical-align属性会失效。

#### flex的六大属性

##### flex-direction

容器内元素的排列方向(默认横向排列)

- flex-direction:row; 沿水平主轴让元素从左向右排列
- flex-direction:column; 让元素沿垂直主轴从上到下垂直排列
- flex-direction:row-reverse;沿水平主轴让元素从右向左排列

##### flex-wrap

容器内元素的换行(默认不换行)

- flex-wrap: nowrap; (默认)元素不换行,比如：一个div宽度100%，设置此属性，2个div宽度就自动变成各50%；
- flex-wrap: wrap; 元素换行,比如：一个div宽度100%，设置此属性，第二个div就在第二行了；



##### justify-content

元素在主轴（页面）上的排列

- justify-content : center;元素在主轴（页面）上居中排列
- justify-content : flex-start;元素在主轴（页面）上由左或者上开始排列
- justify-content : flex-end;元素在主轴（页面）上由右或者下开始排列
- justify-content : space-between;元素在主轴（页面）上左右两端或者上下两端开始排列、
- justify-content : space-around;每个元素两侧的间隔相等。所以，元素之间的间隔比元素与边框的间隔大一倍。

##### align-items

元素在主轴（页面）当前行的横轴（纵轴）方向上的对齐方式

- align-items : flex-start; 弹性盒子元素的侧轴（纵轴）起始位置的边界紧靠住该行的侧轴起始边界（靠上对齐）。
- align-items : flex-end; 弹性盒子元素的侧轴（纵轴）起始位置的边界紧靠住该行的侧轴结束边界。（靠下对齐）
- align-items : center; 弹性盒子元素在该行的侧轴（纵轴）上居中放置。（居中对齐）
- align-items : baseline; 如弹性盒子元素的行内轴与侧轴为同一条，则该值与’flex-start’等效。其它情况下，该值将参与基线对齐。（靠上对齐）



##### align-content

在弹性容器内的元素没有占用交叉轴上所有可用的空间时对齐容器内的各项（垂直）

- align-content: flex-start; 元素位于容器的开头。各行向弹性盒容器的起始位置堆叠。
- align-content: flex-end; 元素位于容器的结尾。各行向弹性盒容器的结尾位置堆叠。
- align-content: stretch; 元素位于容器的中心。各行向弹性盒容器的中间位置堆叠。
- align-content: center; 默认值。元素被拉伸以适应容器。各行将会伸展以占用剩余的空间。如果剩余的空间是负数，该值等效于’flex-start’。
- align-content: space-between;元素位于各行之间留有空白的容器内。各行在弹性盒容器中平均分布。
- align-content: space-around;元素位于各行之前、之间、之后都留有空白的容器内。各行在弹性盒容器中平均分布，两端保留子元素与子元素之间间距大小的一半。如果剩余的空间是负数或弹性盒容器中只有一行，该值等效于’center’。



## box-sizing

```css
.login_form {
  padding: 0 20px;
  box-sizing: border-box;
}
```

### content-box

padding和border不被包含在定义的width和height之内。对象的实际宽度等于设置的width值和border、padding之和，即 ( Element width = width + border + padding)

此属性表现为标准模式下的盒模型。

![image-20221019204132238](../../../md-photo/image-20221019204132238.png)

### border-box

padding和border被包含在定义的width和height之内。对象的实际宽度就等于设置的width值，即使定义有border和padding也不会改变对象的实际宽度，即 ( Element width = width )

![image-20221019204114129](../../../md-photo/image-20221019204114129.png)