package top.liewyoung.aiwechat.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import top.liewyoung.aiwechat.data.AccountRepository
import top.liewyoung.aiwechat.data.ChatRepository
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.data.SettingsRepository
import top.liewyoung.aiwechat.data.database.AppDatabase
import top.liewyoung.aiwechat.data.llm.LLMClient
import top.liewyoung.aiwechat.viewmodel.AccountViewModel
import top.liewyoung.aiwechat.viewmodel.AddContactViewModel
import top.liewyoung.aiwechat.viewmodel.ChatListViewModel
import top.liewyoung.aiwechat.viewmodel.ChatViewModel
import top.liewyoung.aiwechat.viewmodel.ContactViewModel
import top.liewyoung.aiwechat.viewmodel.SettingsViewModel

class AppContainer(private val context: Context) {

    // 初始化Room数据库
    private val database by lazy {
        AppDatabase.getDatabase(context)
    }

    // 初始化DAO
    private val settingsDao by lazy {
        database.settingsDao()
    }

    private val contactDao by lazy {
        database.contactDao()
    }

    private val chatMessageDao by lazy {
        database.chatMessageDao()
    }

    private val accountDao by lazy {
        database.accountDao()
    }

    // 初始化Repository
    private val settingsRepository by lazy {
        SettingsRepository(settingsDao)
    }

    val contactRepository by lazy {
        ContactRepository(contactDao)
    }

    val chatRepository by lazy {
        ChatRepository(chatMessageDao)
    }

    val llmClient by lazy {
        LLMClient(settingsRepository)
    }

    val accountRepository by lazy {
        AccountRepository(accountDao)
    }

    fun provideAccountViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
                    return AccountViewModel(accountRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
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
                    return ChatViewModel(
                        contactId,
                        contactRepository,
                        chatRepository,
                        llmClient
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
