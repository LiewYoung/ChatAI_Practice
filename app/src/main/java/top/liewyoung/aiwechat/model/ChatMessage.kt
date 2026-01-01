package top.liewyoung.aiwechat.model

/**
 * Represents a single chat message
 */
data class ChatMessage(
    val id: String,
    val contactId: String,
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
