# DialogXSample

![DialogXSample](https://github.com/kongzue/DialogXSample/raw/master/img_sample.png)

DialogX 计划提供模块扩展包
这个库目的在于实现一些常用的模块功能，例如BottomDialog+地址选择、日期选择、分享对话框等半实现功能，你可以选择性的引入部分模块，配合DialogX完成功能的构建

扩展包的目的在于一键实现目标功能，复杂需求还请自行开发。

此框架基于 [DialogX](https://github.com/kongzue/DialogX) 实现，需要先引入 DialogX 框架才可以使用，对话框样式、主题等继承自 DialogX 设置。

![Demo](https://github.com/kongzue/DialogXSample/raw/master/img_dialogx_sample.jpg)

### 目前进度

施工中...

### 计划

```
√ 地址滚动选择对话框

√ 日期滚动选择对话框

  √ 日历选择对话框
  
    √ 日期区间选择功能(待办：最大可选日期数限制、多选的预设已选择)

√ 分享选择对话框（纵向堆叠图标样式）

  - 横向列滑动模式

√ 自定义联动滚动选择对话框

√ 底部弹出的评论输入对话框

√ 选择文件对话框

  √ 多选文件功能
  
  √ 文件类型筛选功能
  
  - 文件选择支持暗色模式
  
√ 抽屉对话框

√ 照片选择器
```

建议功能可以直接提交 [issues](https://github.com/kongzue/DialogXSample/issues)

### Demo

您可以先下载 Demo 进行尝试：http://beta.kongzue.com/dialogxsample

<div align=center>    
    <img src="https://github.com/kongzue/DialogXSample/raw/master/README.assets/qrcode.png" width="350">    
</div>

### 使用

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

如果默认的就能满足你的业务需求，直接引入对应功能的包即可，如果不能，请自行拉取代码集成到自己的项目里修改使用。

[![最新版本](https://jitpack.io/v/kongzue/DialogXSample.svg)](https://jitpack.io/#kongzue/DialogXSample)

```gradle
def dialogx_sample_version = "0.0.14"

//地址选择对话框
implementation 'com.github.kongzue.DialogXSample:CityPicker:${dialogx_sample_version}'

//日期选择对话框
implementation 'com.github.kongzue.DialogXSample:DatePicker:${dialogx_sample_version}'

//分享选择对话框
implementation 'com.github.kongzue.DialogXSample:ShareDialog:${dialogx_sample_version}'

//自定义联动选择对话框
implementation 'com.github.kongzue.DialogXSample:CustomWheelPicker:${dialogx_sample_version}'

//回复消息对话框
implementation 'com.github.kongzue.DialogXSample:ReplyDialog:${dialogx_sample_version}'

//文件选择对话框
implementation 'com.github.kongzue.DialogXSample:FileDialog:${dialogx_sample_version}'

//抽屉对话框
implementation 'com.github.kongzue.DialogXSample:DrawerBox:${dialogx_sample_version}'

//照片选择器
implementation 'com.github.kongzue.DialogXSample:AlbumDialog:${dialogx_sample_version}'
```

### 如何使用

<a href="https://github.com/kongzue/DialogXSample/wiki/"><img src="https://github.com/kongzue/DialogX/raw/master/readme/img_how_to_use_tip.png" alt="如何使用" width="450" height="280" /></a>

具体的使用说明，请参阅 [DialogXSample Wiki](https://github.com/kongzue/DialogXSample/wiki/)

### 定制化开发

⚠️ 请注意 DialogXSample 提供了一些功能的具体实现范例，但不一定能完美满足你的实际使用需求，如需做细微功能调整，建议拉取代码到本地进行修改，方法如下：

![如何添加库代码到自己的项目](https://user-images.githubusercontent.com/10115359/186886902-9d1237fe-383c-4aaf-b2c7-24e357d0acef.png)

如有更复杂的定制开发请联系 myzcxhh@live.cn

### 其他

DialogXSample 遵循 Apache License 2.0 开源协议。

```
Copyright Kongzue DialogXSample

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

目前使用到的第三方库：
```
城市选择器
https://github.com/crazyandcoder/citypicker

```
