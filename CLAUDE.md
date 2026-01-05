# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

**Code Generator JPA** 是一个基于 Java 注解处理器（Annotation Processing）的编译时代码生成器。它通过 `@AutoEntity` 注解自动为 JPA 实体类生成完整的 Spring Boot 后端代码，包括 Repository、Service、Controller 和 MapStruct Mapper。

### 核心设计理念

- **编译时代码生成**：利用 `javax.annotation.processing` API 在编译时生成代码
- **约定优于配置**：用户只需提供 Entity、VM（视图模型）、Request（查询条件）三个核心类
- **代码生成引擎**：使用 Google JavaPoet 库动态生成 Java 源码
- **配置驱动**：支持 YAML 配置文件自定义生成行为

---

## 常用命令

### 构建与编译

```bash
# 编译项目（会触发注解处理器）
mvn clean compile

# 打包项目
mvn package

# 安装到本地 Maven 仓库
mvn install

# 部署到远程仓库（需要配置认证）
mvn deploy
```

### 测试

```bash
# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=AutoGeneratorProcessorTest

# 运行单个测试方法
mvn test -Dtest=AutoGeneratorProcessorTest#testSpecificMethod
```

---

## 项目架构

### 目录结构

```
src/main/java/cool/auv/codegeneratorjpa/
├── core/
│   ├── annotation/           # 核心注解定义
│   ├── config/               # 配置类
│   ├── entity/               # 生成器上下文实体
│   ├── processors/           # 注解处理器入口
│   ├── service/              # 代码生成服务层
│   │   ├── javapoet/         # JavaPoet 生成器实现
│   │   └── RequestInterface.java
│   └── utils/                # 工具类
```

### 核心组件

#### 1. 注解处理器 (Annotation Processor)

**入口类**：`AutoGeneratorProcessor` (src/main/java/cool/auv/codegeneratorjpa/core/processors/AutoGeneratorProcessor.java)

- 使用 `@SupportedAnnotationTypes("cool.auv.codegeneratorjpa.core.annotation.AutoEntity")` 声明处理的注解
- 使用 `@SupportedSourceVersion(SourceVersion.RELEASE_17)` 声明支持的 Java 版本
- 实现 `process()` 方法，在编译时扫描被 `@AutoEntity` 标注的类

#### 2. 代码生成服务 (Service Layer)

**核心服务**位于 `src/main/java/cool/auv/codegeneratorjpa/core/service/javapoet/`：

- `GeneratorByJavaPoetService.java`：生成器协调器，负责协调各个生成器
- `RepositoryGenerateService.java`：生成 Spring Data JPA Repository 接口
- `ServiceGenerateService.java`：生成业务逻辑接口
- `ServiceImplGenerateService.java`：生成业务逻辑实现类
- `ControllerGenerateService.java`：生成 REST API 控制器
- `MapperStructGenerateService.java`：生成 MapStruct 对象映射器

#### 3. 配置系统

**配置加载**：`ConfigUtil.java` (src/main/java/cool/auv/codegeneratorjpa/core/utils/ConfigUtil.java)

支持 YAML 配置文件，配置文件优先级（从高到低）：
1. `generator.yml`
2. `generator-ui.yml`
3. `generator-ui-default.yml`
4. `generator-default.yml`
5. 默认配置

**重要**：配置文件中的键必须使用**烤串式命名（kebab-case）**，例如：
```yaml
auto-processor:
  entity:
    package-name: com.yourcompany.project.domain
  controller:
    package-name: com.yourcompany.project.web.rest
```

#### 4. 核心注解

- `@AutoEntity`：标记需要生成代码的 JPA 实体类
  - `basePath`：API 基础路径（如 `/api/test`）
  - `docTag`：Swagger 文档标签
  - `hasAuthority`：权限控制（可选）

- `@Exclude`：排除特定 Controller 方法的生成
  - 可选值：`POST`, `PUT`, `GET_ONE`, `GET_PAGE`, `DELETE`

- `@AutoEnum`：枚举类型定义

---

## 生成的代码结构

对于每个被 `@AutoEntity` 标注的实体类，生成以下组件：

| 组件类型 | 命名规则 | 说明 |
|---------|---------|------|
| Repository | `Base{Entity}Repository` | Spring Data JPA 仓库接口 |
| Service | `Base{Entity}Service` | 业务逻辑接口 |
| ServiceImpl | `Base{Entity}ServiceImpl` | 业务逻辑实现类 |
| Controller | `Base{Entity}Controller` | REST API 控制器 |
| Mapper | `{Entity}Mapper` | MapStruct 对象映射器 |

生成的代码位于：`target/generated-sources/annotations/`

---

## 生成的 REST API

对于 `@AutoEntity(basePath = "/api/test")` 标注的实体，自动生成以下接口：

| 方法 | 路径 | 功能 |
|-----|------|------|
| POST | `/api/test` | 创建实体 |
| PUT | `/api/test` | 更新实体 |
| GET | `/api/test/{id}` | 根据 ID 查询 |
| GET | `/api/test/findByPage` | 分页查询 |
| DELETE | `/api/test/{id}` | 根据 ID 删除 |

---

## 技术栈

- **Java 17**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **JavaPoet**：代码生成核心库
- **MapStruct 1.5.5.Final**：对象映射
- **Swagger 2.2.28**：API 文档
- **Maven 3.9.6**
- **Lombok**：代码简化
- **JUnit 5**：单元测试
- **Compile Testing**：注解处理器测试

---

## 开发注意事项

### 1. 修改注解处理器后

修改注解处理器代码后，需要：
1. 执行 `mvn clean install` 重新安装到本地仓库
2. 在使用该库的项目中执行 `mvn clean compile` 重新编译

### 2. 生成的代码不可手动编辑

生成代码位于 `target/generated-sources/annotations/`，每次编译都会重新生成，不要手动修改。

### 3. 包名约定

默认情况下，生成的代码使用以下包名约定（可通过 YAML 配置覆盖）：
- Entity：`cool.auv.gen.*`
- VM：`cool.auv.gen.service.dto.*`
- Request：`cool.auv.gen.web.rest.request.*`
- Repository：`cool.auv.gen.repository.*`
- Service：`cool.auv.gen.service.*`
- ServiceImpl：`cool.auv.gen.service.impl.*`
- Controller：`cool.auv.gen.web.rest.*`

### 4. Request 接口实现

Request 类必须实现 `RequestInterface<Entity>` 接口，并实现 `buildSpecification()` 方法，用于构建 JPA Specification 查询条件。

### 5. 依赖冲突

当作为注解处理器使用时，需要添加到使用项目的 `maven-compiler-plugin` 的 `annotationProcessorPaths` 中。同时使用 MapStruct 或 Lombok 时，它们的处理器也需要配置在该路径下。

---

## 测试框架

项目使用 **Compile Testing** 框架测试注解处理器：

```java
// 测试示例位于：src/test/java/cool/auv/codegeneratorjpa/AutoGeneratorProcessorTest.java
```

测试流程：
1. 编写测试用的 JPA 实体类（带 `@AutoEntity` 注解）
2. 使用 Compile Testing 编译该类
3. 验证生成的代码是否符合预期
