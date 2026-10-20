# Spring Hibernate3 Classic

以 **Spring 3.2 XML 設定** 整合 **Hibernate 3（`.hbm.xml` mapping）** 連線 **MySQL**，實作 `Product` 商品資料 CRUD 的經典範本專案。

## 專案類型

- **學習／參考用範本**：示範在 Spring Boot 與 JPA annotation 普及之前，傳統 Java 專案如何以 Spring + Hibernate 建立資料庫存取層。
- **獨立 Console 應用程式**：沒有 Web 層與 Servlet 容器，透過 `main` 方法啟動 Spring container，依序執行查詢、新增、修改、刪除，並將結果輸出到 log。
- **純 XML 設定**：所有 Bean 皆以 XML 宣告並使用 setter injection 組裝，不使用 `@Component`、`@Autowired`、`@Transactional` 等 annotation，也不使用 component scan。
- **Hibernate 原生 mapping**：Entity 為單純 POJO（搭配 Lombok），欄位對應寫在 `product.hbm.xml`，資料存取使用 Spring 提供的 `HibernateDaoSupport` / `HibernateTemplate` 與 HQL。

適合想了解舊系統（legacy）架構、維護既有 Spring 3 / Hibernate 3 專案，或比較新舊寫法差異的開發者參考。

## 技術棧

| 項目 | 版本 |
| --- | --- |
| JDK | 1.8 |
| Spring Framework（`spring-context`、`spring-orm`） | 3.2.18.RELEASE |
| Hibernate | 3.1.3 |
| MySQL Connector/J | 9.5.0 |
| MySQL（Docker） | 8.0.31 |
| Lombok | 1.18.42 |
| Build Tool | Maven |

## 專案結構

```
spring-hibernate3-classic
├── docker
│   ├── docker-compose.yml          # MySQL 8 環境
│   └── init/product_schema.sql     # 容器初始化時自動執行的建表與種子資料
├── src/main/java/idv/module
│   ├── SpringHibernateClassicApplication.java   # 進入點，示範 CRUD 流程
│   ├── entity/Product.java                      # 商品 POJO（Lombok @Data）
│   ├── repository/ProductDao.java               # 繼承 HibernateDaoSupport，使用 HQL 存取資料
│   └── service/ProductService.java              # 委派給 ProductDao 的 Service 層
└── src/main/resources
    ├── integration-config.xml      # 整合所有 Spring 設定檔的入口
    ├── data-source.xml             # 讀取 properties 並建立 DataSource
    ├── database-config.properties  # JDBC 連線資訊
    ├── hibernate-config.xml        # Hibernate SessionFactory 設定
    ├── product-bean.xml            # productDao、productService Bean 宣告
    ├── product.hbm.xml             # Product 與 product table 的欄位對應
    └── product_schema.sql          # 手動建表用 SQL
```

## 設定載入流程

```
SpringHibernateClassicApplication
  └─ ClassPathXmlApplicationContext("integration-config.xml")
       ├─ data-source.xml      → dataSource（讀取 database-config.properties）
       ├─ hibernate-config.xml → sessionFactory（引用 dataSource、載入 product.hbm.xml）
       └─ product-bean.xml     → productDao（注入 sessionFactory）
                                 productService（注入 productDao）
```

## 準備作業

需要 `JDK 8`、`Maven` 及 `MySQL` 資料庫。IDE 需安裝 `Lombok plugin`。

### 建立資料庫（default schema: `master`）

可選擇以下兩種方式，擇一使用：

1. **使用 Docker**：在專案根目錄執行下列指令，容器啟動時會自動執行 `init/product_schema.sql`，建立 `master` schema、`product` table 以及 45 筆範例商品資料。

   ```bash
   docker compose -f docker/docker-compose.yml up -d
   ```

2. **自行安裝 MySQL**：執行 `product_schema.sql` 建立 schema、table 與範例資料。

### 連線設定

連線資訊位於 `src/main/resources/database-config.properties`，預設值與 Docker 環境一致：

```properties
jdbc.url = jdbc:mysql://localhost:3306/master?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
jdbc.username = root
jdbc.password = root
```

若使用自行安裝的 MySQL，請依實際環境修改。

## 啟動方式

- **IDE**：直接執行 `SpringHibernateClassicApplication` 的 `main` 方法。
- **Maven**：

  ```bash
  mvn compile exec:java -Dexec.mainClass=idv.module.SpringHibernateClassicApplication
  ```

## 執行內容

程式啟動後會依序執行以下操作，並將結果與 Hibernate 產生的 SQL（`hibernate.show_sql=true`）輸出到 console：

1. **查詢全部**：列出所有商品。
2. **查詢單筆**：隨機選一個 id 查詢商品。
3. **新增**：新增一筆名為 `Test Product` 的測試商品。
4. **修改**：更新測試商品的中文名稱、售價與最後更新日期。
5. **刪除**：刪除所有英文名稱包含 `Test Product` 的商品，執行結束後資料庫會回到初始狀態。

## 注意事項

- 專案未設定 transaction manager，每次 `HibernateTemplate` 操作會各自提交，沒有跨方法的交易邊界。
- `hibernate.hbm2ddl.auto` 未啟用，Hibernate 不會自動建立或更新 table，需先執行 schema SQL。
- schema SQL 有兩份（`docker/init/` 與 `src/main/resources/`），修改時請同步更新。
