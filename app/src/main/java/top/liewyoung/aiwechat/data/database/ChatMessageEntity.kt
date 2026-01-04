package top.liewyoung.aiwechat.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "contact_id") val contactId: String,
    val content: String,
    @ColumnInfo(name = "is_from_user") val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
