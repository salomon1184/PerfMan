PerfMan
=======

pc端的android性能监控工具，cpu 内存采用dumpsys数据，流量采用proc的采样数据


java 编写，跨平台 要求必须安装jdk/jre 1.7以上版本
只适用于android
不能有中文目录


使用介绍：
插入手机
选择监控的测试包（res/param.xml里配置）
点击start cpu 内存数据实时输出
点击stop一次采样结束
采样数据放在Log文件夹下

如要生成jpg的性能监控用于发邮件
左下方
选择log文件所在地，一定要进入最底层目录
选择图片输出路径
点击生成


Note: 感谢孙珊同学，找到一处bug，流量采样数据只采样了wlan，没有记录rmnet和usbnet，所以该工具只有在wifi情况下才会输出流量值
