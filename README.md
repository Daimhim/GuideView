* <h1>版本更新</h1>
+ <h5>1.0.7</h5>
1. 增加了DialogLayout
2. 优化了原有的DialogGuide
3. 解决了个别机型系统栏白边
+ <h5>1.0.2</h5>
1. 增加返回键的可控，新增Cancelable属性和监听
2. mOutsideTouchable属性可以控制所有蒙层是否可以被点击
3. focusClick 仅焦点可以被点击 最好和mOutsideTouchable结合使用
+ <h5>1.0.1</h5>
1. 修改了部分BUG
+ <h5>1.0.0</h5>
1. 加入kotlin
2. 扩展了蒙层子View功能
3. 重写了MaskView的绘制，测量，布局
4. 优化了布局绘制
5. 新增 支持仅高亮处可以处理触摸事件
6. 新增 可以屏蔽指定高亮处的触摸事件
7. 项目已经迁移到androidx
8. 调整了项目架构，增加了工厂模式来创建展示模式和绘制形状
9. 增加了以View插入Wind、Dialog、DialogFragmeng三种方式添加蒙层
10. 新增多高亮支持

 * <h1>GuideView</h1>
 * 本系统能够快速的为一个Activity里的任何一个View控件创建一个遮罩式的导航页，并且可以再高亮区域绘制任何你想要的布局或者lottie动画等炫酷效果</p>
 * <h3>工作原理</h3>
 * 首先它需要一个目标View或者它的id,我们通过findViewById来得到这个View，计算它在屏幕上的区域targetRect,通过这个区域，开始绘制一个覆盖整个Activity的遮罩，可以定义遮罩的颜色和透明度，然而目标View被绘制成透明从而实现高亮的效果。接下来是在相对于这个targetRect的区域绘制一些图片或者文字。我们把这样一张图片或者文字抽象成一个Component接口，设置文字或者图片，所有的图片文字都是相对于targetRect来定义的。可以设定额外的x，y偏移量,可以对遮罩系统设置可见状态的发生变化时的监听回调，可以对遮罩系统设置开始和结束时的动画效。</p>
 * <h3>注意：具体用法参见demo，内附详细注释</h3>
 * <img src = "https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/guide.gif"></img>

 * <h3>使用方法</h3>
 *  <h4>1.添加gradle依赖</h4>
 
        implementation 'org.daimhim.guideview:guideview:1.0'
        
 *  <h4>2.编写用于在高亮区域周围展示的component</h4>
 *  
        public class SimpleComponent implements Component {
            
              @Override public View getView(LayoutInflater inflater) {
            
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_frends, null);
                ll.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                    Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
                  }
                });
                return ll;
              }
            
              @Override public int getAnchor() {
                return Component.ANCHOR_BOTTOM;
              }
            
              @Override public int getFitPosition() {
                return Component.FIT_END;
              }
            
              @Override public int getXOffset() {
                return 0;
              }
            
              @Override public int getYOffset() {
                return 10;
          }
        }
        
*  <h4>3.展示引导蒙层，并监听蒙层展示、隐藏事件</h4>
*  
         public void showGuideView() {
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(header_imgbtn)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding(10);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
              @Override
              public void onShown() {
              }
        
              @Override
              public void onDismiss() {
                showGuideView2();
              }
            });
        
            builder.addComponent(new SimpleComponent());
            guide = builder.createGuide();
            guide.show(SimpleGuideViewActivity.this);


## License

        Copyright 2016 binIoter
    
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
