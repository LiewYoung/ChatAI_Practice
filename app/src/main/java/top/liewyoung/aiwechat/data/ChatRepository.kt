package top.liewyoung.aiwechat.data

import androidx.datastore.core.DataStore
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.UserPreferences
import top.liewyoung.aiwechat.model.ChatMessage

/** Repository for managing chat messages */
class ChatRepository(private val userPreferencesStore: DataStore<UserPreferences>) {

    fun getMessagesForContact(contactId: String): Flow<List<ChatMessage>> =
        userPreferencesStore.data.map { prefs ->
            prefs.messagesList
                .filter { it.contactId == contactId }
                .map { messageProto ->
                    ChatMessage(
                        id = messageProto.id,
                        contactId = messageProto.contactId,
                        content = messageProto.content,
                        isFromUser = messageProto.isFromUser,
                        timestamp = messageProto.timestamp
                    )
                }
                .sortedBy { it.timestamp }
        }

    suspend fun addMessage(message: ChatMessage) {
        userPreferencesStore.updateData { prefs ->
            val messageProto =
                top.liewyoung.aiwechat.ChatMessage.newBuilder()
                    .setId(message.id.ifEmpty { UUID.randomUUID().toString() })
                    .setContactId(message.contactId)
                    .setContent(message.content)
                    .setIsFromUser(message.isFromUser)
                    .setTimestamp(message.timestamp)
                    .build()

            prefs.toBuilder().addMessages(messageProto).build()
        }
    }

    suspend fun getChatContext(contactId: String, limit: Int = 1024): List<ChatMessage> {
        val prefs = userPreferencesStore.data.first()
        return prefs.messagesList
            .filter { it.contactId == contactId }
            .map { messageProto ->
                ChatMessage(
                    id = messageProto.id,
                    contactId = messageProto.contactId,
                    content = messageProto.content,
                    isFromUser = messageProto.isFromUser,
                    timestamp = messageProto.timestamp
                )
            }
            .sortedByDescending { it.timestamp }
            .take(limit)
            .reversed()
    }

    suspend fun deleteChat(contactId: String) {
        userPreferencesStore.updateData { prefs ->
            val filteredMessages = prefs.messagesList.filter { it.contactId != contactId }
            prefs.toBuilder().clearMessages().addAllMessages(filteredMessages).build()
        }
    }
}
