# SDKTools
神策数据 Android SDK 调试小工具,App 下载体验地址：[蒲公英](https://www.pgyer.com/zbCk)

# 用户手册
### 整体功能
本应用为方便用户快速调试神策 SDK 而开发，具备以下功能：原生 SDK 相关事件调试、H5 打通调试、可视化全埋点/App 点击分析调试、应用设置等功能
### 原生 SDK 相关事件
进入原生 SDK 相关事件页面后，可对原生 SDK 初始化信息进行设置，包括扫码录入数据接收地址、是否开启全埋点（包括应用启动、页面浏览、页面点击、应用退出及 Fragment 页面浏览）、是否开启加密（密钥类型由服务端控制）。参数设置完毕后，即可进入原生 SDK 调试页面。支持调试原生 SDK 核心采集接口，包括：事件操作、用户属性操作、item 操作、用户标识、IDM3 及常用控件点击（参数设置中开启全埋点）等。调用 SDK 相关接口后，数据会正常上报到参数中设置的数据接收地址，滑动到日志页面，即可查看相关操作日志。
### H5 打通调试
进入到 H5 打通调试页面后，可对打通参数进行设置，包括：是否打通、是否使用旧版本打通、是否支持 Android 4.1 及以下版本、是否开启数据接收地址校验（仅旧版打通生效）、扫码录入数据接收地址 H5 页面地址、选择加载 H5 页面的浏览器内核（原生或腾讯 X5）。参数设置完毕后，即可进入 H5 打通 调试页面。对 H5 页面执行操作，滑动到日志页面，可查看操作日志（jsdk 需要设置 show_log: true）
### 可视化全埋点/App 点击分析
进入可视化全埋点/App 点击分析页面后，可对相关参数进行设置，包括：扫码录入数据接收地址、是否开启可视化全埋点自定义属性、页面类型（H5 页面或原生页面）、是否开启打通、H5 页面地址（跳转到原生页面无需录入）。跳转后，弹出可视化全埋点/App 点击分析配对码（需要数据接收地址对应的 SA 环境支持配对码），输入正确的配对码后即可进入可视化全埋点/App 点击分析页面
### 应用设置
点击应用右上角设置图标，即可进入应用设置页面。应用设置包括：是否保存 SDK 历史信息、是否保存参数配置信息、用户手册、关于、开源框架。
- 默认情况下，保存 SDK 历史信息，即：上次 SDK 产生的历史数据，下次 SDK 调试后会继续使用，例如上次 SDK 调试进行登录操作产生的登录 ID、设置静态公共属性产生的公共属性等。如果选择不保存，下次启动应用后，会主动清除 SDK 产生的历史数据
- 参数配置信息同理，默认保存，参数配置信息指的是扫码例如的数据接收地址、H5 页面地址、各开关状态等。选择不保存后，下次启动应用会清除以上信息，进入相关页面后，历史产生的配置信息均需要重新录入
- 用户手册：介绍应用的相关功能，即本页面：）
- 关于页面：介绍应用基本信息，包括应用版本、SDK 版本、开发者、应用简介
- 开源框架：本应用使用多款开源框架，为感谢开源作者，特别展示开源框架相关信息，欢迎大家多多支持开源事业
### 其他
- SDK 初始化：配置初始化参数并完成初始化后，再次初始化无效（SDK 设计就是如此），依然会使用第一次初始化的配置。如果需要修改初始化参数，请重启 App
- 二维码生成技巧：使用谷歌浏览器访问数据接收地址/H5 页面地址，然后点击右上角分享按钮，即可生成地址二维码，如图所示：

![二维码生成](https://liuweiqiang2016.github.io/page.io/qrcode_make.png)