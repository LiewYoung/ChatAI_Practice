package top.liewyoung.aiwechat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.ContactRepository
import top.liewyoung.aiwechat.model.Contact
import java.util.UUID

class AddContactViewModel(
    private val contactRepository: ContactRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _prompt = MutableStateFlow("")
    val prompt: StateFlow<String> = _prompt

    private val _avatar = MutableStateFlow("😀")
    val avatar: StateFlow<String> = _avatar

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess

    fun updateName(value: String) {
        _name.value = value
    }

    fun updatePrompt(value: String) {
        _prompt.value = value
    }

    fun updateAvatar(value: String) {
        _avatar.value = value
    }

    fun saveContact() {
        if (_name.value.isBlank() || _prompt.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val contact = Contact(
                    id = UUID.randomUUID().toString(),
                    name = _name.value.trim(),
                    prompt = _prompt.value.trim(),
                    avatar = _avatar.value
                )
                contactRepository.addContact(contact)
                _saveSuccess.value = true
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun resetSaveSuccess() {
        _saveSuccess.value = false
    }
}
