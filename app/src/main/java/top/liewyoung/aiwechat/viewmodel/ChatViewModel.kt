package top.liewyoung.aiwechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.ChatRepository
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.data.llm.LLMClient
import top.liewyoung.aiwechat.model.ChatMessage
import top.liewyoung.aiwechat.model.Contact
import java.util.UUID

/**
 * ViewModel for individual chat conversations
 */
class ChatViewModel(
    private val contactId: String,
    private val contactRepository: ContactRepository,
    private val chatRepository: ChatRepository,
    private val llmClient: LLMClient
) : ViewModel() {

    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?> = _contact

    val messages: StateFlow<List<ChatMessage>> = chatRepository.getMessagesForContact(contactId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadContact()
        clearUnreadCount()
    }

    private fun loadContact() {
        viewModelScope.launch {
            contactRepository.getContactById(contactId).collect { contact ->
                _contact.value = contact
            }
        }
    }

    private fun clearUnreadCount() {
        viewModelScope.launch {
            contactRepository.clearUnreadCount(contactId)
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val contact = _contact.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Save user message
                val userMessage = ChatMessage(
                    id = UUID.randomUUID().toString(),
                    contactId = contactId,
                    content = text.trim(),
                    isFromUser = true
                )
                chatRepository.addMessage(userMessage)

                // Update contact's last message
                contactRepository.updateLastMessage(
                    contactId = contactId,
                    message = text.trim(),
                    timestamp = userMessage.timestamp
                )

                // Get chat context for LLM
                val context = chatRepository.getChatContext(contactId, limit = 10)
                
                // Add word count constraint to the system prompt
                val systemPrompt = "${contact.prompt}\n\nYour response should be between 20 and 100 words."

                // Get AI response
                val result = llmClient.chat(
                    systemPrompt = systemPrompt,
                    chatHistory = context,
                    userMessage = text.trim()
                )

                result.onSuccess { aiResponse ->
                    // Save AI response
                    val aiMessage = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        contactId = contactId,
                        content = aiResponse,
                        isFromUser = false
                    )
                    chatRepository.addMessage(aiMessage)

                    // Update contact's last message
                    contactRepository.updateLastMessage(
                        contactId = contactId,
                        message = aiResponse,
                        timestamp = aiMessage.timestamp
                    )
                }.onFailure { error ->
                    _errorMessage.value = error.message ?: "Failed to get AI response"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
