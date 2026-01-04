package top.liewyoung.aiwechat.data

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.data.database.ChatMessageDao
import top.liewyoung.aiwechat.data.database.ChatMessageEntity
import top.liewyoung.aiwechat.model.ChatMessage

/** Repository for managing chat messages */
class ChatRepository(private val chatMessageDao: ChatMessageDao) {

    fun getMessagesForContact(contactId: String): Flow<List<ChatMessage>> =
        chatMessageDao.getMessagesForContact(contactId).map {
            it.map { messageEntity ->
                ChatMessage(
                    id = messageEntity.id,
                    contactId = messageEntity.contactId,
                    content = messageEntity.content,
                    isFromUser = messageEntity.isFromUser,
                    timestamp = messageEntity.timestamp
                )
            }
        }

    suspend fun addMessage(message: ChatMessage) {
        val messageEntity = ChatMessageEntity(
            id = message.id.ifEmpty { UUID.randomUUID().toString() },
            contactId = message.contactId,
            content = message.content,
            isFromUser = message.isFromUser,
            timestamp = message.timestamp
        )
        chatMessageDao.insertMessage(messageEntity)
    }

    suspend fun getChatContext(contactId: String, limit: Int = 1024): List<ChatMessage> {
        return chatMessageDao.getRecentMessagesForContact(contactId, limit)
            .map { messageEntity ->
                ChatMessage(
                    id = messageEntity.id,
                    contactId = messageEntity.contactId,
                    content = messageEntity.content,
                    isFromUser = messageEntity.isFromUser,
                    timestamp = messageEntity.timestamp
                )
            }
            .reversed()
    }

    suspend fun deleteChat(contactId: String) {
        chatMessageDao.deleteMessagesForContact(contactId)
    }
}
