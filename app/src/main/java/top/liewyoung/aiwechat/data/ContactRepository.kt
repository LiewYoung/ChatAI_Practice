package top.liewyoung.aiwechat.data

import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.data.database.ContactDao
import top.liewyoung.aiwechat.data.database.ContactEntity
import top.liewyoung.aiwechat.model.Contact

/** Repository for managing contacts */
class ContactRepository(private val contactDao: ContactDao) {

    fun getAllContacts(): Flow<List<Contact>> =
            contactDao.getAllContacts().map {
                it.map { contactEntity ->
                    Contact(
                            id = contactEntity.id,
                            name = contactEntity.name,
                            avatar = contactEntity.avatar.ifEmpty { "😀" },
                            prompt = contactEntity.prompt,
                            lastMessageTime = contactEntity.lastMessageTime,
                            lastMessage = contactEntity.lastMessage?.ifEmpty { null },
                            unreadCount = contactEntity.unreadCount,
                            createdAt = contactEntity.createdAt
                    )
                }
            }

    fun getContactById(id: String): Flow<Contact?> =
            contactDao.getContactById(id).map {
                it?.let { contactEntity ->
                    Contact(
                            id = contactEntity.id,
                            name = contactEntity.name,
                            avatar = contactEntity.avatar.ifEmpty { "😀" },
                            prompt = contactEntity.prompt,
                            lastMessageTime = contactEntity.lastMessageTime,
                            lastMessage = contactEntity.lastMessage?.ifEmpty { null },
                            unreadCount = contactEntity.unreadCount,
                            createdAt = contactEntity.createdAt
                    )
                }
            }

    suspend fun addContact(contact: Contact) {
        val contactEntity = ContactEntity(
                id = contact.id.ifEmpty { UUID.randomUUID().toString() },
                name = contact.name,
                avatar = contact.avatar,
                prompt = contact.prompt,
                lastMessageTime = contact.lastMessageTime,
                lastMessage = contact.lastMessage ?: "",
                unreadCount = contact.unreadCount,
                createdAt = contact.createdAt
        )
        contactDao.insertContact(contactEntity)
    }

    suspend fun updateContact(contact: Contact) {
        val contactEntity = ContactEntity(
                id = contact.id,
                name = contact.name,
                avatar = contact.avatar,
                prompt = contact.prompt,
                lastMessageTime = contact.lastMessageTime,
                lastMessage = contact.lastMessage ?: "",
                unreadCount = contact.unreadCount,
                createdAt = contact.createdAt
        )
        contactDao.updateContact(contactEntity)
    }

    suspend fun deleteContact(id: String) {
        contactDao.deleteContactById(id)
    }

    suspend fun updateLastMessage(contactId: String, message: String, timestamp: Long) {
        contactDao.updateLastMessage(contactId, message, timestamp)
    }

    suspend fun clearLastMessage(contactId: String) {
        contactDao.clearLastMessage(contactId)
    }

    suspend fun incrementUnreadCount(contactId: String) {
        contactDao.incrementUnreadCount(contactId)
    }

    suspend fun clearUnreadCount(contactId: String) {
        contactDao.clearUnreadCount(contactId)
    }
}
