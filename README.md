# Java-web-server

本项目 fork 自 [berb/java-web-server](https://github.com/berb/java-web-server)，原项目是基于BIO 实现的一个简单 `Web Server`。

本基于原项目的基础上模仿 SpringMVC 实现 MVC模式，实现的注解包括 `@Controller`、`@Service`、`Qualifier`，基于包扫面实现了简单的 IOC 容器，实现路由的返回值 `View` （获取页面）、与 `Model`（获取数据）。

## Example

下面的两个访问的例子，

[http://localhost:9001/example/index](http://localhost:9001/example/index)

[http://localhost:9001/example/get](http://localhost:9001/example/get)

## TODO

- URL 参数获取
- 实现返回 ModelAndView

## License


	Copyright (c) 2010 Benjamin Erb

	Permission is hereby granted, free of charge, to any person obtaining
	a copy of this software and associated documentation files (the
	"Software"), to deal in the Software without restriction, including
	without limitation the rights to use, copy, modify, merge, publish,
	distribute, sublicense, and/or sell copies of the Software, and to
	permit persons to whom the Software is furnished to do so, subject to
	the following conditions:

	The above copyright notice and this permission notice shall be
	included in all copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
	EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
	MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
	NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
	LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
	OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
	WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.