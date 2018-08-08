#《摇篮孕育》Android 
>这里简单做一个简介吧

[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=15)

+ 使用Android Studio作为IDE进行开发。
+ 主要开发语言为kotlin。
+ 设计思路，采用模块化和组件化思想。
	+ 以moudle的形式展现模块化思想，即每一个moudle视为一个模块。
		+ 模块分Application模块和Library模块，Library禁止依赖Application，防止错乱。
		+ Library模块之间不建议但可以相互依赖，必须编写README.md文档说明，方便查阅。
		+ Application禁止直接依赖Library，需将Library打包至maven，可加快编译速度[^1] 。
[^1]: 省去javac将java文件编译为class文件的过程
		+ 依赖关系使用implementation替代api，最终由Application完成汇总，防止重复依赖冲突[^2]。
[^2]：implementation不会将依赖加入打包
		+ Library Version写入项目build.gradle里，禁止在模块中直接写死，版本冲突。
		+ 已发布至maven的aar如何解决Library Version冲突问题？？？
	+ 对于高频或复杂UI，使用组定义View、ViewGroup或者Fragment来完成组件化开发。
	+ 开发中对代码进行混淆，切勿拖延，杜绝项目肥大后混淆规则过于混乱问题。
	+ 抽离网络层，图片层，common层为单独模块。
		+ 网络使用RESTful风格，retrofit+okhttp组合实现
		+ 图片使用fresco框架，封装一下
		+ common定义公用interface、class和资源文件

