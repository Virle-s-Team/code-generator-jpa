# Code Generator JPA

这是一个基于JPA的代码生成器，可以帮助你快速生成基于JPA的实体类、Repository、Service和Controller层代码。

## 功能特点

- 支持从数据库表自动生成JPA实体类
- 自动生成Repository接口
- 自动生成Service层代码
- 自动生成Controller层代码
- 支持自定义模板
- 支持Swagger文档生成
- 支持MapStruct对象映射

## 技术栈

- Java 17
- Spring Boot 3.4.3
- Spring Data JPA
- MySQL
- Lombok
- MapStruct
- Freemarker/Velocity (模板引擎)
- Swagger

## 项目结构

```
src/main/java/cool/auv/codegeneratorjpa/
├── core/
│   ├── annotation/    # 自定义注解
│   ├── config/        # 配置类
│   ├── entity/        # 实体类
│   ├── enums/         # 枚举类
│   ├── exception/     # 异常处理
│   ├── mapstruct/     # MapStruct映射
│   ├── processors/    # 注解处理器
│   ├── service/       # 服务层
│   ├── utils/         # 工具类
│   └── vm/            # 模板引擎
```

## 快速开始

1. 克隆项目
```bash
git clone https://github.com/Virle-s-Team/code-generator-jpa.git
```

2. 配置数据库连接
在`application.yml`中配置你的数据库连接信息。

3. 运行项目
```bash
mvn clean install
```

## 使用方法

1. 在实体类上添加相关注解
2. 运行代码生成器
3. 生成的代码将自动添加到项目中

## 配置说明

项目支持以下配置项：

- 数据库连接配置
- 代码生成路径配置
- 模板配置
- Swagger配置

## 贡献指南

欢迎提交Issue和Pull Request来帮助改进这个项目。
