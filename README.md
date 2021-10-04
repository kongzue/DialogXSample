# DialogXSample

DialogX 计划提供模块扩展包
这个库目的在于实现一些常用的模块功能，例如BottomDialog+地址选择、日期选择、分享对话框等半实现功能，你可以选择性的引入部分模块，配合DialogX完成功能的构建

扩展包的目的在于一键实现目标功能，复杂需求还请自行开发。

此框架基于 [DialogX](https://github.com/kongzue/DialogX) 实现，需要先引入 DialogX 框架才可以使用，对话框样式、主题等继承自 DialogX 设置。

![DialogXSample](https://github.com/kongzue/DialogXSample/raw/master/img_dialogx_sample.png)

### 目前进度

施工中...

### 计划

```
√ 地址滚动选择对话框

√ 日期滚动选择对话框

  - 日历选择对话框
  
    - 日期区间选择功能

√ 分享选择对话框（纵向堆叠图标样式）

  - 横向列滑动模式

√ 自定义联动滚动选择对话框

√ 底部弹出的评论输入对话框
```

建议功能可以直接提交 [issues](https://github.com/kongzue/DialogXSample/issues)

### 试用

请注意目前本扩展包尚未完成开发，目前提供预览版试用，实际正式版依然可能出现 API 变更需，请谨慎使用！

在使用前，您需要先引入 [DialogX](https://github.com/kongzue/DialogX)

引入方法：

在您的 build.gradle(Project)（新版本 Android Studio 请在 settings.gradle）添加 jitpack 仓库：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

引入各功能模块：

#### 最新版本：

[![最新版本](https://jitpack.io/v/kongzue/DialogXSample.svg)](https://jitpack.io/#kongzue/DialogXSample)

```
//地址选择对话框
implementation 'com.github.kongzue.DialogXSample:CityPicker:0.0.1.alpha2'

//日期选择对话框
implementation 'com.github.kongzue.DialogXSample:DatePicker:0.0.1.alpha2'

//分享选择对话框
implementation 'com.github.kongzue.DialogXSample:ShareDialog:0.0.1.alpha2'
```

### 其他

目前使用到的第三方库：
```
城市选择器
https://github.com/crazyandcoder/citypicker

```
