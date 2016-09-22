# LockScreenSample
Android LockScreenSample Using Service 


Download
--------

This is a  Music Lockscreen Sample 

Like QQ Music 、Cloud Music

fork from [Android-LockScreenSample-DisableHomeButtonKey](https://github.com/mugku/Android-LockScreenSample-DisableHomeButtonKey)

锁屏代码还是比较简单的

自定义锁屏页的基本原理参照的是这篇文章：http://dev.qq.com/topic/57875330c9da73584b025873

[Android-LockScreenSample-DisableHomeButtonKey](https://github.com/mugku/Android-LockScreenSample-DisableHomeButtonKey)里面有一些基础组件就在基础上写了点东西，音乐锁屏一般也不需要Disable Home键。增加了沉浸模式和透明栏。滑动退出使用[SwipeBackLayout](https://github.com/ikew0ng/SwipeBackLayout)修改了下源码`ViewDragHelper`

```java
 private int getEdgeTouched(int x, int y) {
       return mTrackingEdges;
    }
```

，使得不局限于边缘。增加了时间显示。

**i am lazy  Passing lazy**


License
=======
Copyright 2015 DUBULEE

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
