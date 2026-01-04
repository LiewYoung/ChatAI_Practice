# 替换Protobuf为SQLite的实现计划

## 1. 移除Protobuf相关配置和依赖
- 从`app/build.gradle.kts`中移除protobuf插件和依赖
- 删除`src/main/proto/user_prefs.proto`文件
- 删除`UserPreferencesSerializer.kt`文件

## 2. 添加Room SQLite依赖
- 在`app/build.gradle.kts`中添加Room相关依赖
- 配置Room编译器插件

## 3. 创建Room数据库模型
- 创建`AppDatabase`类作为数据库入口
- 将现有的Kotlin数据模型（Contact, ChatMessage）转换为Room实体
- 创建新的Settings实体类

## 4. 创建数据访问对象（DAO）
- 创建`ContactDao`用于联系人数据操作
- 创建`ChatMessageDao`用于聊天消息数据操作
- 创建`SettingsDao`用于设置数据操作

## 5. 重写Repository层
- 重写`SettingsRepository`，使用Room替代DataStore
- 重写`ContactRepository`，使用Room替代DataStore
- 重写`ChatRepository`，使用Room替代DataStore

## 6. 更新依赖注入配置
- 修改`AppContainer`，移除DataStore相关配置
- 添加Room数据库实例的初始化
- 更新Repository的依赖注入

## 7. 测试和验证
- 运行应用，确保所有功能正常工作
- 验证数据持久化和读取功能

## 技术栈选择
- Room：Android官方推荐的SQLite ORM库，与Jetpack组件无缝集成
- Kotlin协程：用于异步数据库操作
- Flow：用于数据流处理，保持与现有代码风格一致

## 预期结果
- 移除了Protobuf相关的所有代码和依赖
- 成功迁移到SQLite数据库存储
- 保持了原有的功能和性能
- 提高了数据查询和操作的效率
- 便于后续扩展和维护

## 简化说明
- 由于是beta版本，不需要考虑旧数据迁移
- 直接实现新的SQLite存储方案，简化开发流程
- 保持原有API接口不变，确保上层代码无需修改

这个计划将确保我们平稳地从Protobuf迁移到SQLite，同时保持代码的可维护性和功能的完整性。