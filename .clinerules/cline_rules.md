# 项目描述
## 功能介绍
使用annotation process在编译阶段生成代码的组件,减少开发中对于简单业务中基础crud代码的开发
## 原理
- 使用AutoEntity注解.
项目中定义了AutoEntity注解,在编译过程中会通过扫描包的形式来判断哪些业务对象需要处理.在实际Jpa项目中Entity对象使用AutoEntity注解标注,配置相应的参数,如当前对象在web的访问路径,生成的文件类型,生成的方法
- 定义VM Request
在实际项目中定义VM和Request对象,VM用来对外显示,Request为查询参数
- 配置文件
配置文件中配置了Entity,VM,Request,Controlelr,Service.,Mapper,Mapstruct等对象的包路径,用于动态生成代码时保证包路径的正确性
# 路径
- 源代码 /src/main/java