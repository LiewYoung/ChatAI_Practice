package top.liewyoung.aiwechat.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY last_message_time DESC")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE id = :id")
    fun getContactById(id: String): Flow<ContactEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Update
    suspend fun updateContact(contact: ContactEntity)

    @Delete
    suspend fun deleteContact(contact: ContactEntity)

    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: String)

    @Query("UPDATE contacts SET last_message = :message, last_message_time = :timestamp WHERE id = :contactId")
    suspend fun updateLastMessage(contactId: String, message: String, timestamp: Long)

    @Query("UPDATE contacts SET last_message = NULL, last_message_time = 0 WHERE id = :contactId")
    suspend fun clearLastMessage(contactId: String)

    @Query("UPDATE contacts SET unread_count = unread_count + 1 WHERE id = :contactId")
    suspend fun incrementUnreadCount(contactId: String)

    @Query("UPDATE contacts SET unread_count = 0 WHERE id = :contactId")
    suspend fun clearUnreadCount(contactId: String)
}
