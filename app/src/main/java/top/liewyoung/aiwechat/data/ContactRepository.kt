package top.liewyoung.aiwechat.data

import androidx.datastore.core.DataStore
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import top.liewyoung.aiwechat.UserPreferences
import top.liewyoung.aiwechat.model.Contact

/** Repository for managing contacts */
class ContactRepository(private val userPreferencesStore: DataStore<UserPreferences>) {

    fun getAllContacts(): Flow<List<Contact>> =
            userPreferencesStore.data.map { prefs ->
                prefs.contactsList
                        .map { contactProto ->
                            Contact(
                                    id = contactProto.id,
                                    name = contactProto.name,
                                    avatar = contactProto.avatar.ifEmpty { "😀" },
                                    prompt = contactProto.prompt,
                                    lastMessageTime = contactProto.lastMessageTime,
                                    lastMessage = contactProto.lastMessage.ifEmpty { null },
                                    unreadCount = contactProto.unreadCount,
                                    createdAt = contactProto.createdAt
                            )
                        }
                        .sortedByDescending { it.lastMessageTime }
            }

    fun getContactById(id: String): Flow<Contact?> =
            userPreferencesStore.data.map { prefs ->
                prefs.contactsList.find { it.id == id }?.let { contactProto ->
                    Contact(
                            id = contactProto.id,
                            name = contactProto.name,
                            avatar = contactProto.avatar.ifEmpty { "😀" },
                            prompt = contactProto.prompt,
                            lastMessageTime = contactProto.lastMessageTime,
                            lastMessage = contactProto.lastMessage.ifEmpty { null },
                            unreadCount = contactProto.unreadCount,
                            createdAt = contactProto.createdAt
                    )
                }
            }

    suspend fun addContact(contact: Contact) {
        userPreferencesStore.updateData { prefs ->
            val contactProto =
                    top.liewyoung.aiwechat.Contact.newBuilder()
                            .setId(contact.id.ifEmpty { UUID.randomUUID().toString() })
                            .setName(contact.name)
                            .setAvatar(contact.avatar)
                            .setPrompt(contact.prompt)
                            .setLastMessageTime(contact.lastMessageTime)
                            .setLastMessage(contact.lastMessage ?: "")
                            .setUnreadCount(contact.unreadCount)
                            .setCreatedAt(contact.createdAt)
                            .build()

            prefs.toBuilder().addContacts(contactProto).build()
        }
    }

    suspend fun updateContact(contact: Contact) {
        userPreferencesStore.updateData { prefs ->
            val updatedContacts =
                    prefs.contactsList.map { existingContact ->
                        if (existingContact.id == contact.id) {
                            existingContact
                                    .toBuilder()
                                    .setName(contact.name)
                                    .setAvatar(contact.avatar)
                                    .setPrompt(contact.prompt)
                                    .setLastMessageTime(contact.lastMessageTime)
                                    .setLastMessage(contact.lastMessage ?: "")
                                    .setUnreadCount(contact.unreadCount)
                                    .build()
                        } else {
                            existingContact
                        }
                    }

            prefs.toBuilder().clearContacts().addAllContacts(updatedContacts).build()
        }
    }

    suspend fun deleteContact(id: String) {
        userPreferencesStore.updateData { prefs ->
            val filteredContacts = prefs.contactsList.filter { it.id != id }
            prefs.toBuilder().clearContacts().addAllContacts(filteredContacts).build()
        }
    }

    suspend fun updateLastMessage(contactId: String, message: String, timestamp: Long) {
        userPreferencesStore.updateData { prefs ->
            val updatedContacts =
                    prefs.contactsList.map { contact ->
                        if (contact.id == contactId) {
                            contact.toBuilder()
                                    .setLastMessage(message)
                                    .setLastMessageTime(timestamp)
                                    .build()
                        } else {
                            contact
                        }
                    }

            prefs.toBuilder().clearContacts().addAllContacts(updatedContacts).build()
        }
    }

    suspend fun clearLastMessage(contactId: String) {
        userPreferencesStore.updateData { prefs ->
            val updatedContacts =
                    prefs.contactsList.map { contact ->
                        if (contact.id == contactId) {
                            contact.toBuilder().setLastMessage("").setLastMessageTime(0L).build()
                        } else {
                            contact
                        }
                    }

            prefs.toBuilder().clearContacts().addAllContacts(updatedContacts).build()
        }
    }

    suspend fun incrementUnreadCount(contactId: String) {
        userPreferencesStore.updateData { prefs ->
            val updatedContacts =
                    prefs.contactsList.map { contact ->
                        if (contact.id == contactId) {
                            contact.toBuilder().setUnreadCount(contact.unreadCount + 1).build()
                        } else {
                            contact
                        }
                    }

            prefs.toBuilder().clearContacts().addAllContacts(updatedContacts).build()
        }
    }

    suspend fun clearUnreadCount(contactId: String) {
        userPreferencesStore.updateData { prefs ->
            val updatedContacts =
                    prefs.contactsList.map { contact ->
                        if (contact.id == contactId) {
                            contact.toBuilder().setUnreadCount(0).build()
                        } else {
                            contact
                        }
                    }

            prefs.toBuilder().clearContacts().addAllContacts(updatedContacts).build()
        }
    }
}
