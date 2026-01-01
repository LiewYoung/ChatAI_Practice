package top.liewyoung.aiwechat.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.liewyoung.aiwechat.UserPreferences
import top.liewyoung.aiwechat.data.ChatRepository
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.data.SettingsRepository
import top.liewyoung.aiwechat.data.UserPreferencesSerializer
import top.liewyoung.aiwechat.data.llm.LLMClient
import top.liewyoung.aiwechat.viewmodel.AddContactViewModel
import top.liewyoung.aiwechat.viewmodel.ChatListViewModel
import top.liewyoung.aiwechat.viewmodel.ChatViewModel
import top.liewyoung.aiwechat.viewmodel.ContactViewModel
import top.liewyoung.aiwechat.viewmodel.SettingsViewModel

private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = USER_PREFERENCES_NAME,
    serializer = UserPreferencesSerializer
)

class AppContainer(private val context: Context) {

    private val settingsRepository by lazy {
        SettingsRepository(context.userPreferencesStore)
    }

    val contactRepository by lazy {
        ContactRepository(context.userPreferencesStore)
    }

    val chatRepository by lazy {
        ChatRepository(context.userPreferencesStore)
    }

    val llmClient by lazy {
        LLMClient(settingsRepository)
    }

    fun provideSettingsViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(settingsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun provideContactViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ContactViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ContactViewModel(contactRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun provideChatListViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ChatListViewModel(contactRepository, chatRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun provideAddContactViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AddContactViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AddContactViewModel(contactRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun provideChatViewModelFactory(contactId: String): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ChatViewModel(contactId, contactRepository, chatRepository, llmClient) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
