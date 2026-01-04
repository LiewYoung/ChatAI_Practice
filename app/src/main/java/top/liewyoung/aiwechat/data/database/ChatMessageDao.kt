package top.liewyoung.aiwechat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow



@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages WHERE contact_id = :contactId ORDER BY timestamp ASC")
    fun getMessagesForContact(contactId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages WHERE contact_id = :contactId")
    suspend fun deleteMessagesForContact(contactId: String)

    @Query("SELECT * FROM chat_messages WHERE contact_id = :contactId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentMessagesForContact(contactId: String, limit: Int): List<ChatMessageEntity>
}
