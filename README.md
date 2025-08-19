# Code Generator JPA

这是一个基于 Java 注解处理器和 JPA 的代码生成器。你只需要在你的项目中引入它，并为一个 JPA 实体类添加 `@AutoEntity` 注解，它就能在编译时自动为你生成一套完整的、生产级的 Spring Boot 后端代码，包括 Repository, Service, Controller 等。

## 它是如何工作的？

本工具遵循“约定优于配置”的原则。你只需要提供三个核心的输入类：
1.  **Entity**: 你的 JPA 实体类。
2.  **VM (VO)**: 用于API接口返回的视图模型。
3.  **Request**: 用于接收前端查询参数的模型。

当你编译项目时，本工具的注解处理器会自动找到被`@AutoEntity`标注的`Entity`，然后根据这三个输入类，为你生成所有其他必需的后端代码。

## 功能特点

- **自动化**: 基于一个注解，自动生成全套后端代码。
- **约定驱动**: 只需要提供核心数据类，无需繁琐配置。
- **功能完善**: 生成的代码自带 Swagger API 文档、MapStruct 对象映射和标准的分页查询接口。
- **高度集成**: 无缝集成 Spring Boot 和 Spring Data JPA。

## 技术栈

- Java 17
- Spring Boot 3
- Spring Data JPA
- JavaPoet (代码生成)
- MapStruct
- Swagger

## 如何使用

下面是一个完整的使用教程。

### 第1步: 在你的项目中引入依赖

将本代码生成器作为依赖添加到你项目的`pom.xml`中。最关键的是，需要将它添加到`maven-compiler-plugin`的`annotationProcessorPaths`路径下。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <encoding>UTF-8</encoding>
        <annotationProcessorPaths>
            <!-- 添加这里的依赖 -->
            <path>
                <groupId>cool.auv</groupId>
                <artifactId>code-generator-jpa</artifactId>
                <version>0.0.1-SNAPSHOT</version> <!-- 请替换为你的版本号 -->
            </path>
            <!-- 如果你的项目也使用 MapStruct 或 Lombok，它们的处理器也需要放在这里 -->
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>1.5.5.Final</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.24</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

### 第2步: 准备你的输入类

你需要手动创建以下三个类。假设我们要为一个`Product`（商品）创建API。

**1. Product.java (JPA 实体)**
```java
package com.yourcompany.project.domain;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
@AutoEntity // <-- 核心注解！
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
}
```

**2. ProductVM.java (接口返回的VO)**
```java
package com.yourcompany.project.service.dto;

import lombok.Data;

@Data
public class ProductVM {
    private Long id;
    private String name;
    private Double price;
}
```

**3. ProductRequest.java (查询条件)**
这个类必须实现`RequestInterface`接口。
```java
package com.yourcompany.project.web.rest.request;

import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import com.yourcompany.project.domain.Product;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest implements RequestInterface<Product> {
    private String name;

    @Override
    public Specification<Product> buildSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null) {
                predicates.add(cb.like(root.get("name"), "%%" + name + "%%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

### 第3步: （可选）配置生成包名

在你的项目的`src/main/resources`目录下创建一个`generator.yml`或`generator-default.yml`文件，来指定生成代码的包路径。

**注意：** 配置文件中的键必须使用**烤串式命名（kebab-case）**。

```yaml
auto-processor:
  entity:
    package-name: com.yourcompany.project.domain
  vm:
    package-name: com.yourcompany.project.service.dto
  request:
    package-name: com.yourcompany.project.web.rest.request
  repository:
    package-name: com.yourcompany.project.repository
  mapstruct:
    package-name: com.yourcompany.project.service.mapper
  service:
    package-name: com.yourcompany.project.service
    impl-package-name: com.yourcompany.project.service.impl
  controller:
    package-name: com.yourcompany.project.web.rest
```
*如果省略此文件，代码将默认生成在`cool.auv.gen.*`包下。*

### 第4步: 编译你的项目

现在，只需正常编译你的项目即可。
```bash
mvn clean compile
```
编译完成后，所有生成的代码（`BaseProductRepository`, `BaseProductService`等）将会自动出现在你的`target/generated-sources/annotations`目录下，并被IDE识别。你的项目现在已经拥有了一套完整的关于`Product`的CRUD API。

## 贡献

欢迎通过提交Issue或Pull Request来改进项目。