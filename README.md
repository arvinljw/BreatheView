## 简介

这是一个自定义的转场动画实现方式，目前支持两种方式:

* 圆形展开
* 矩形展开

效果如下：

![](gif/bv_sample.gif)

## 使用

### 引用：

在根目录级的build.gradle中添加

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

再在要使用的项目级的build.gradle中添加：

```
dependencies {
	compile 'com.github.arvinljw:BreatheView:v1.0.0'
}
```

### api介绍

#### 1、打开界面动画

```
breatheView.startScaleBigger();
```

#### 2、关闭界面动画

```
breatheView.startScaleSmaller();
```

#### 3、设置背景颜色

```
breatheView.setBgColor(int color)
```

简单介绍一下，这个动画是通过绘制，使用Xfermode实现的，先绘制背景部分，再绘制展开的部分，展开部分和背景重叠部分就变透明，这里设置的颜色就是这个背景颜色。

#### 4、绘制的起始点

这里要分情况，目前提供了两种方式去展开：

* CircleBreatheView 
	
	
	```
	circleBreatheView.setDrawPoint(Point point)
	circleBreatheView.setDrawPoint(int x,int y)
	```
	
* RectBreatheView

	```
	rectBreatheView.setDrawRect(Rect rect)
	rectBreatheView.setDrawRect(int left,int top,int right,int bottom)
	```
	
	*建议使用setDrawRect时，尽量保证水平或者垂直方向一边是占满屏幕的，因为目前没有分水平和垂直去分别计算*
	
#### 5、使用技巧

可参照app中例子的代码。

**重点**：

启动新的界面后，在startActivity后边加上一句

```
overridePendingTransition(0, 0);
```

去掉系统转场动画，同理在关闭的时候，在finish后边也加上这一句。

#### 6、扩展

可以自己继承BreatheView，参照RectBreatheView或者CircleBreatheView实现自己的自定义转场动画。

## 原理


```
mMode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);

@Override
protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int saveCount = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
    drawBg(canvas);
    mPaintSrc.setXfermode(mMode);
    drawBreathe(canvas);
    mPaintSrc.setXfermode(null);
    canvas.restoreToCount(saveCount);
}
```

如果知道Xfermode，那么一看上边的代码应该就明白一大半了，当然这只是其中重要的一步，还有一步就是使用属性动画，改变绘制BreatheView部分的大小，例如

```
public void setScale(int scale) {
    this.mScale = scale;
    postInvalidate();
}

public int getScale() {
    return mScale;
}

protected void startScaleAnim(int start, int end) {
    final boolean isOpen = start < end;
    ObjectAnimator scale = ObjectAnimator.ofInt(this, "scale", start, end).setDuration(mDuration);
    scale.setInterpolator(new LinearInterpolator());
    scale.addListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            setVisibility(View.VISIBLE);
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            canClose = isOpen;
            if (mAnimEndListener != null) {
                mAnimEndListener.onAnimEnd(isOpen);
            }
        }
    });
    scale.start();
}

protected void drawBg(Canvas canvas) {
    canvas.drawRect(mBgRect, mBgPaint);
}

@Override
protected void drawBreathe(Canvas canvas) {
    canvas.drawCircle(mDrawPoint.x, mDrawPoint.y, mScale, mPaintBreathe);
}
```

核心代码就是这些，最重要就是通过属性动画，改变绘制的breathe部分的大小，并在onDraw时使用Xfermode实现。

## License

```
Copyright 2017 arvinljw

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

