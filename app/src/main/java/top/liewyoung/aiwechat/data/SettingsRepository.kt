package top.liewyoung.aiwechat.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.UserPreferences

class SettingsRepository(private val userPreferencesStore: DataStore<UserPreferences>) {

    val settingsFlow: Flow<Settings> = userPreferencesStore.data
        .map { userPreferences ->
            Settings(
                apiKey = userPreferences.apiKey,
                baseUrl = userPreferences.baseUrl,
                model = userPreferences.model
            )
        }

    suspend fun updateApiKey(apiKey: String) {
        userPreferencesStore.updateData {
            it.toBuilder().setApiKey(apiKey).build()
        }
    }

    suspend fun updateBaseUrl(baseUrl: String) {
        userPreferencesStore.updateData {
            it.toBuilder().setBaseUrl(baseUrl).build()
        }
    }

    suspend fun updateModel(model: String) {
        userPreferencesStore.updateData {
            it.toBuilder().setModel(model).build()
        }
    }
}

data class Settings(val apiKey: String, val baseUrl: String, val model: String)
