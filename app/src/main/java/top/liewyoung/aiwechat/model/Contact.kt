package top.liewyoung.aiwechat.model

/**
 * Represents an AI contact with LLM personality
 */
data class Contact(
    val id: String,
    val name: String,
    val avatar: String = "😀",
    val prompt: String, // LLM system prompt defining personality
    val lastMessageTime: Long = 0L,
    val lastMessage: String? = null,
    val unreadCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
