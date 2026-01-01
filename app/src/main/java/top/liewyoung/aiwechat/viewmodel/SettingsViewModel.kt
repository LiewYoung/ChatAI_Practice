package top.liewyoung.aiwechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.SettingsRepository

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = settingsRepository.settingsFlow.map {
        SettingsUiState(apiKey = it.apiKey, baseUrl = it.baseUrl, model = it.model)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun saveSettings(apiKey: String, baseUrl: String, model: String) {
        viewModelScope.launch {
            settingsRepository.updateApiKey(apiKey)
            settingsRepository.updateBaseUrl(baseUrl)
            settingsRepository.updateModel(model)
        }
    }
}

data class SettingsUiState(
    val apiKey: String = "",
    val baseUrl: String = "",
    val model: String = ""
)
