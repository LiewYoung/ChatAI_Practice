package top.liewyoung.aiwechat.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.data.database.SettingsDao
import top.liewyoung.aiwechat.data.database.SettingsEntity

class SettingsRepository(private val settingsDao: SettingsDao) {

    val settingsFlow: Flow<Settings> = settingsDao.getSettingsFlow()
        .map {
            it?.let {settingsEntity ->
                Settings(
                    apiKey = settingsEntity.apiKey,
                    baseUrl = settingsEntity.baseUrl,
                    model = settingsEntity.model
                )
            } ?: Settings(
                apiKey = "",
                baseUrl = "",
                model = ""
            )
        }

    suspend fun updateApiKey(apiKey: String) {
        // 确保数据库中有记录
        ensureSettingsExist()
        settingsDao.updateApiKey(apiKey)
    }

    suspend fun updateBaseUrl(baseUrl: String) {
        // 确保数据库中有记录
        ensureSettingsExist()
        settingsDao.updateBaseUrl(baseUrl)
    }

    suspend fun updateModel(model: String) {
        // 确保数据库中有记录
        ensureSettingsExist()
        settingsDao.updateModel(model)
    }

    private suspend fun ensureSettingsExist() {
        // 检查数据库中是否有记录，如果没有则插入一条默认记录
        if (settingsDao.getSettings() == null) {
            settingsDao.insertSettings(SettingsEntity())
        }
    }
}

data class Settings(val apiKey: String, val baseUrl: String, val model: String)
