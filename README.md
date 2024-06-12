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

## 版权申明

QuickJava遵循Apache2开源协议发布，并提供免费使用。

本项目包含的第三方源码和二进制文件之版权信息另行标注。

版权所有Copyright © 2020-2021 by QuickJava (www.quickjava.org)

All rights reserved。

License
--------

    Copyright 2019-2021 QuickJava.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
