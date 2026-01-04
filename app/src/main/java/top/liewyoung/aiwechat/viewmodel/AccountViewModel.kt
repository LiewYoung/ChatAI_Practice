package top.liewyoung.aiwechat.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import top.liewyoung.aiwechat.data.AccountRepository
import top.liewyoung.aiwechat.data.AccountUiState
import top.liewyoung.aiwechat.model.Account

class AccountViewModel(private val accountRepository: AccountRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountUiState(isLoading = true))

    //只读
    val uiState = _uiState.asStateFlow()




    init {
        observeAccountFromDisk()
    }

    private fun observeAccountFromDisk(){
        viewModelScope.launch {
            accountRepository.accountFlow.collectLatest {
                account ->
                if(account!=null){
                    _uiState.value = AccountUiState(isLoading = false, account = account)
                }else{
                    _uiState.value = AccountUiState(isLoading = false,account = null)
                }
            }
        }
    }


    fun updateInfo(name: String,id: String){
        viewModelScope.launch {
            val currentAccount = _uiState.value.account

            val toSaveAccount = currentAccount?.copy(
                userName = name,
                userId =  id
            ) ?: Account(name,id)

            accountRepository.saveAccount(toSaveAccount)
        }
    }

    fun updateAvatar(uri: Uri,context: Context){
        viewModelScope.launch {
            val  currentAccount = _uiState.value.account

            val permanentPath = try {
                accountRepository.saveAvatar(uri, context)
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch // 保存失败则不更新账户信息
            }

            val toSaveAccount = currentAccount?.copy(
                userAvatar = permanentPath
            ) ?: Account("NULL","NULL",permanentPath)

            accountRepository.saveAccount(toSaveAccount)
        }
    }

}