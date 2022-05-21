# 项目名称（CloudMusic）

仿网易云音乐Android客户端V6.1版本(MVVM架构)


![logo](https://inews.gtimg.com/newsapp_bt/0/14252532161/1000)

## 项目简介
安卓初学者，为完成安卓大作业，精心“策划”两个月（借鉴大佬代码）写出的一坨垃圾，不过真的学到了很多东西
#### 软件架构
- Jetpack中的许多架构组件是专门为了MVVM架构而量身打造的。
- MVVM（Model-View-ViewModel）是一种高级项目架构模式，目前已被广泛应用在Android程序设计领域，类似的架构模式还有MVP、MVC等。简单来讲，MVVM架构可以将程序结构主要分成3部分：Model是数据模型部分；View是界面展示部分；而ViewModel比较特殊，可以将它理解成一个连接数据模型和界面展示的桥梁，从而实现让业务逻辑和界面展示分离的程序结构设计。
- 这里是列表文本当然，一个优秀的项目架构除了会包含以上3部分内容之外，还应该包含仓库、数据源等，这里我画了一幅非常简单易懂的MVVM项目架构示意图，　MVVM项目架构示意图![输入图片说明](https://res.weread.qq.com/wrepub/epub_37683759_402 "在这里输入图片标题")
- MVVM项目架构示意图可以看到，我们通过这张架构示意图将程序分为了若干层。其中，UI控制层包含了我们平时写的Activity、Fragment、布局文件等与界面相关的东西。ViewModel层用于持有和UI元素相关的数据，以保证这些数据在屏幕旋转时不会丢失，并且还要提供接口给UI控制层调用以及和仓库层进行通信。仓库层要做的主要工作是判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获取到的数据返回给调用方。本地数据源可以使用数据库、SharedPreferences等持久化技术来实现，而网络数据源则通常使用Retrofit访问服务器提供的Webservice接口来实现。
- 另外，对于这张架构示意图，我还有必要再解释一下。图中所有的箭头都是单向的，比方说UI控制层指向了ViewModel层，表示UI控制层会持有ViewModel层的引用，但是反过来ViewModel层却不能持有UI控制层的引用，其他几层也是一样的道理。除此之外，引用也不能跨层持有，比如UI控制层不能持有仓库层的引用，谨记每一层的组件都只能与它相邻层的组件进行交互。
- 那么接下来，我们会严格按照刚才的架构示意图对CloudMusic这个项目进行实现。为了让项目能够有更好的结构，这里需要在com.cloudmusic.android包下再新建几个包， 如图所示。![输入图片说明](http://r.photo.store.qq.com/psc?/V54RkypD3keoz827SRe44Oy15N3vfu8d/ruAMsa53pVQWN7FLK88i5rYX.*ep1RpERnZRWtB2nECajwfiOHcBAiTrn7S9m.XArwM6mt1ILarcUP0VtCyg2QtXLR2Lbc*5KLq9MIn9wk8!/r"在这里输入图片标题")
- 项目的新结构很明显，api包用于存放网络服务相关的代码，dao存放数据存储相关代码，ui包用于存放界面展示相关的代码。其中，data包中又包含了model、repository这2个子包，分别用于存放对象模型以及仓库的代码。
- 另外，在整个项目的开发过程中，我们还会用到许多依赖库，为了方便后面的代码编写，这里就提前把所有会用到的依赖库都声明一下吧。编辑app/build.gradle文件，在dependencies闭包中添加如下内容：
-     `
        dependencies {
        implementation 'androidx.recyclerview:recyclerview:1.0.0'
        implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
        implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
        implementation 'com.google.android.material:material:1.1.0'
        implementation?"androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"
        implementation 'com.squareup.retrofit2:retrofit:2.6.1'
        implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0"
        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1"
  }
  `
### 安装要求

* 安卓8.0以上

### 安装步骤

下载CloudMusic.apk文件并安装


## 使用到的框架
- **网易云音乐API**：[**NeteaseCloudMusicApi**](https://github.com/Binaryify/NeteaseCloudMusicApi)
- **视频播放**： [JiaoZiVideoPlayer](https://github.com/Jzvd/JiaoZiVideoPlayer)
- **图片加载**： [glide](https://github.com/bumptech/glide)
- **XUI**： [XUI](https://github.com/xuexiangjys/XUI)
- **音乐播放**： [StarrySky](https://github.com/EspoirX/StarrySky)
- **歌词控件**： [LyricViewX](https://github.com/Moriafly/LyricViewX)
- **搜索框**： [SearchDialog](https://github.com/xiaoyou-xblog/SearchDialog)

## 作者

作者：jhp

您也可以在 [contributors](https://github.com/liuxing2001/CloudMusic/contributors) 参看所有参与该项目的开发者。

## 鸣谢

* 该项目参考了 我走路带着风啊 的 [CloudMusic](https://gitee.com/gsphelow/cloud-music)
* UI参考了zion223 的 [NeteaseCloudMusic](https://github.com/zion223/NeteaseCloudMusic)
* 灵感来源于网易云音乐Android客户端V6.1版本

