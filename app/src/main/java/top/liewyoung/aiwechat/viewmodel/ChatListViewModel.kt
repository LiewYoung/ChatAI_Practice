package top.liewyoung.aiwechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.ChatRepository
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.model.Contact

class ChatListViewModel(
    private val contactRepository: ContactRepository, 
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _allContacts = contactRepository.getAllContacts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val filteredContacts: StateFlow<List<Contact>> =
        searchQuery.combine(_allContacts) { query, contacts ->
            val chats = contacts.filter { it.lastMessageTime > 0L } // Only show contacts with messages
                .sortedByDescending { it.lastMessageTime } // Sort by most recent
            if (query.isBlank()) {
                chats
            } else {
                chats.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearch(query: String) {
        _searchQuery.value = query
    }

    fun deleteChat(contactId: String) {
        viewModelScope.launch {
            chatRepository.deleteChat(contactId)
            contactRepository.clearLastMessage(contactId) 
        }
    }
}
