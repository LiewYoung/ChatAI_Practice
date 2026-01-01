package top.liewyoung.aiwechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.model.Contact

/** ViewModel for managing contacts list */
class ContactViewModel(private val contactRepository: ContactRepository) : ViewModel() {

    val contacts: StateFlow<List<Contact>> =
            contactRepository
                    .getAllContacts()
                    .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = emptyList()
                    )

    fun deleteContact(id: String) {
        viewModelScope.launch { contactRepository.deleteContact(id) }
    }

    suspend fun addContact(contact: Contact) {
        contactRepository.addContact(contact)
    }
}
