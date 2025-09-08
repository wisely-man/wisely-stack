# 🚀 WiselyStack — Lightweight Full-Stack Development Framework

Built on Java, Spring Boot 3.5.5, and JDK 21, FastStack is a lightweight full-stack framework designed to help developers set up, run, and deliver applications faster. With plug-and-play plugins and minimal dependencies, it’s production-ready with little or no configuration.

## 🌟 Key Features
- ✅ Rapid Startup: Leverage Spring Boot 3.5.5 with pre-integrated modules — get up and running in minutes
- ✅ High Concurrency: Ride the JDK 21 Virtual Threads wave — built for high-throughput, I/O-intensive workloads with dramatically reduced overhead
- ✅ Full-Stack Ready: Supports REST APIs, MyBatis for persistence, and static asset serving (HTML/CSS/JS) — ideal for server-side rendering or lightweight frontend scenarios
- ✅ Production-Grade: Comes with ready-to-use utilities (logging, error handling, CRUD templates) to reduce boilerplate code
- ✅ Modular Design: Clean, layered architecture for easy extension and maintenance
- ✅ Minimal Dependencies: Only essential components included — lightweight by design


## 🛠 Prerequisites
| Component                   | Version     | Description                                                          |
|-----------------------------|-------------|----------------------------------------------------------------------|
| Java                        | JDK 21+     | Runtime environment with Virtual Threads for high concurrency        |
| Spring Boot                 | 3.5.5       | Core framework with auto-configuration and production-ready features |
| Spring Cloud                | 2025.0.0    | Cloud-native development framework                                   |
| MyBatis                     | 3.5.13+     | Persistence framework with dynamic SQL and XML mapping support       |
| MyBatis-Spring-Boot-Starter | 3.0.3+      | Seamless Spring Boot integration                                     |
| Lombok                      | 1.18.36+    | Reduces boilerplate code (e.g. getters, setters, toString)           |
| HikariCP                    | Built-in    | Default high-performance database connection pool                    |
| Spring Boot DevTools        | 3.5.5       | Enables hot reload during development                                |
| Jakarta Validation          | 3.0+        | Support for request parameter validation                             |
| Frontend Assets             | HTML/CSS/JS | Static resource serving for lightweight UI or server-side rendering  |

## 🚀 快速开始

### 克隆项目
```bash
git clone https://github.com/wisely-man/wisely-stack.git
```

### 构建项目
```bash
mvn clean install
```

## 📁 项目结构（示例）
```
wiselt-stack/
├── wisely-bom/                       # BOM 文件
│── wisely-starter/
│       ├── wisely-starter-core/    # 核心代码
│       ├── wisely-starter-jdbc/    # 数据库连接
│       ├── wisely-starter-web/     # Web 层
│       ├── wisely-starter-redis/   # Redis 集成
├       ├── pom.xml                 # Maven 配置，定义通用的插件、依赖版本
└── README.md
```
