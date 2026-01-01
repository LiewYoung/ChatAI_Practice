package top.liewyoung.aiwechat.ui

import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel() {
    private var account = ""

    fun getAccount(): String {
        return account
    }

    fun setAccount(account: String) {
        this.account = account
    }

}