![License](res/img/apache-2.0.svg)

`QuickJava` `快速` `MVC` `轻量` `Java`

# QuickJava
QuickJava是一个开源免费的、快速、简单的轻量级Java MVC开发框架，是为了敏捷应用开发和简化企业应用开发而诞生的。

* 更多信息：http://www.quickjava.org

* 开发文档：http://doc.quickjava.org

## 基础

### 开发规范
- 遵循[谷歌Java开发规范](https://google.github.io/styleguide/javaguide.html) 
- 严格的MVC分离

### 目录结构
```
org.demo.www 
├─application           应用目录
│  ├─index              index模块
│  │  ├─controller      控制器
│  │  ├─lang            语言包
│  │  ├─model           模型
│  │  ├─service         业务类
│  │  ├─validator       验证器
│  │  ├─view            视图
│  ├─admin              admin模块
│  │  ├─controller      控制器
│  │  ├─model           模型
│  │  ├─view            视图
│  │  ├─....            
│  ├─ApplicationBoot.java  应用程序入口
```

### 命令概念
* 控制器`controller`：接收前台发送的请求，复制任务调度
* 模型`model`：对数据库的抽象操作层，基本的CRUD一行代码即可操作
* 视图`view`：侵入式的模板标签，实际开发中会更好的区分`html`的标签

### 环境要求
- JDK >= 1.8
- Maven >= 4.0.0
- Mysql >= 5.5
- 内存 >= 1GB

### 程序打包
`
Maven->QuickJava-web->Lifecycle->install
Maven->QuickJava-www->Lifecycle->packing
运行：java -jar jar包
`

## 反馈

* 反馈邮箱：[`issue@quickjava.org`](mailto:issue@quickjava.org)

* 社区论坛：[`ask.quickjava.org`](http://ask.quickjava.org)

* Github：(https://github.com/quickgroup/quickjava)

## 用爱发电
<p align="center">
  <img src="data/res/img/001.jpg" width="49%" alt="001.jpg" />
  <img src="data/res/img/002.jpg" width="49%" alt="002.jpg" />
</p>

License
--------
Copyright (c) 2013, Aldo Cortesi. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

