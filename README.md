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

- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- JavaPoet (代码生成)
- MapStruct 1.5.5.Final
- Swagger 2.2.28

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
        <source>21</source>
        <target>21</target>
        <encoding>UTF-8</encoding>
        <annotationProcessorPaths>
            <!-- 添加这里的依赖 -->
            <path>
                <groupId>cool.auv</groupId>
                <artifactId>code-generator-jpa</artifactId>
                <version>0.0.2-SNAPSHOT</version> <!-- 请替换为你的版本号 -->
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

## 生成的API接口

假设我们有一个`TestEntity`，并且在`@AutoEntity`注解中设置了`basePath = "/api/test"`，那么生成的接口如下：

#### **1. 创建实体 (Create)**

*   **功能**: 创建一个新的`TestEntity`。
*   **方法**: `POST`
*   **路径**: `/api/test`
*   **请求体 (Request Body)**: 一个`TestEntityVM`对象（JSON格式），其中`id`字段必须为空。
    ```json
    {
      "name": "Some Name",
      "email": "test@example.com"
    }
    ```
*   **成功响应**: `200 OK`，无返回内容。

#### **2. 更新实体 (Update)**

*   **功能**: 根据ID更新一个已存在的`TestEntity`。
*   **方法**: `PUT`
*   **路径**: `/api/test`
*   **请求体 (Request Body)**: 一个`TestEntityVM`对象（JSON格式），其中`id`字段必须有值。
    ```json
    {
      "id": 1,
      "name": "New Name",
      "email": "new@example.com"
    }
    ```
*   **成功响应**: `200 OK`，无返回内容。

#### **3. 根据ID查询 (Find by ID)**

*   **功能**: 获取单个`TestEntity`的详细信息。
*   **方法**: `GET`
*   **路径**: `/api/test/{id}`
*   **路径参数 (Path Variable)**:
    *   `id`: 要查询的实体ID。
*   **成功响应**: `200 OK`，响应体为一个`TestEntityVM`对象。
    ```json
    {
      "id": 1,
      "name": "New Name",
      "email": "new@example.com"
    }
    ```

#### **4. 分页和条件查询 (Find by Page)**

*   **功能**: 根据一组条件进行分页和排序查询。
*   **方法**: `GET`
*   **路径**: `/api/test/findByPage`
*   **查询参数 (Query Parameters)**:
    *   来自`TestEntityRequest`类的所有字段，例如 `name=some_value&email=test@example.com`。
    *   分页和排序参数，例如 `page=0&size=10&sort=id,desc`。
*   **成功响应**: `200 OK`，响应体为一个`TestEntityVM`对象的数组。
    ```json
    [
      { "id": 1, "name": "Name 1", "email": "email1@example.com" },
      { "id": 2, "name": "Name 2", "email": "email2@example.com" }
    ]
    ```
    同时，响应头（Response Headers）中会包含分页信息，如`X-Total-Count`和`Link`。

#### **5. 根据ID删除 (Delete)**

*   **功能**: 删除一个`TestEntity`。
*   **方法**: `DELETE`
*   **路径**: `/api/test/{id}`
*   **路径参数 (Path Variable)**:
    *   `id`: 要删除的实体ID。
*   **成功响应**: `200 OK`，无返回内容。

## 依赖说明

当你在项目中引入 `code-generator-jpa` 时，以下传递依赖会被自动引入到你的项目中：

| 依赖                           | 用途                  |
|------------------------------|---------------------|
| spring-boot-starter-data-jpa | 生成的 Repository 需要使用 |
| spring-web / spring-webmvc   | 生成的 Controller 需要使用 |
| mapstruct                    | 生成的 Mapper 需要       |
| swagger-annotations          | 生成的 API 文档需要        |
| jackson-annotations/databind | 生成的实体类需要            |

**注意**：编译时工具依赖（如 auto-service、lombok）已设置为 `optional`，不会传递到你的项目。

## 版本兼容性

| 版本             | Java | Spring Boot |
|----------------|------|-------------|
| 0.0.2-SNAPSHOT | 21   | 3.4.3       |

## 贡献

欢迎通过提交Issue或Pull Request来改进项目。