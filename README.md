# 🤖 AI WeChat

<div align="center">

<p>
    <b>一个完全基于 Kotlin 和 Jetpack Compose 构建的现代 Android 聊天应用。</b>
</p>

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple?style=for-the-badge&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Ready-4285F4?style=for-the-badge&logo=android)
![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-orange?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)

</div>

---

## 📖 项目简介

**AI WeChat** 是一个完全采用 **Kotlin** 编写的示例聊天应用程序，旨在演示 **现代 Android 开发的最佳实践**。

本项目全面展示了如何使用 Jetpack Compose 构建声明式 UI。无论您是正在学习 Compose，还是寻找清晰、可扩展的 MVVM 架构参考，本项目都能为您提供全面的指导。

## 🚀 功能特性

* 💬 **实时聊天:** 与您的联系人进行流畅的对话体验。
* 👥 **联系人管理:** 支持手动添加联系人或通过扫描二维码添加。
* 📲 **便捷分享:** 快速分享名片或联系人信息。
* ⚙️ **个性化设置:** 支持应用配置和选项调整。
* ℹ️ **关于开发者:** 查看开发者的详细信息与介绍。

## 🛠️ 技术栈与架构

本项目紧跟 Android 开发潮流，采用最新的 Jetpack 库构建。

| 类别 | 库 / 技术 |
| :--- | :--- |
| **编程语言** | [Kotlin](https://kotlinlang.org/) (100% 纯 Kotlin) |
| **UI 框架** | [Jetpack Compose](https://developer.android.com/jetpack/compose) (声明式 UI) |
| **架构模式** | [MVVM (Model-View-ViewModel)](https://developer.android.com/topic/architecture) |
| **导航路由** | [Jetpack Navigation Compose](https://developer.android.com/jetpack/compose/navigation) |
| **依赖注入** | Manual DI (手动依赖注入容器) |
| **构建系统** | Gradle (Kotlin DSL) |

## 📁 项目结构

代码库严格遵循分层架构原则，结构清晰：

```text
com.liewyoung.aiwechat
├── 📂 data          # 数据层：包含数据源、仓库 (Repository) 和本地存储
├── 📂 di            # 依赖注入：手动 DI 容器配置
├── 📂 model         # 模型层：定义数据类 (Data Classes) 和 UI 状态
├── 📂 service       # 服务层：包含业务逻辑和后台服务
├── 📂 ui            # 视图层：Compose 屏幕、主题 (Theme) 和 UI 组件
├── 📂 util          # 工具层：扩展函数和通用工具类
└── 📂 viewmodel     # 视图模型：用于状态管理的 Jetpack ViewModel