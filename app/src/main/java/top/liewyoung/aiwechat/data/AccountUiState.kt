package top.liewyoung.aiwechat.data

import top.liewyoung.aiwechat.model.Account


data class AccountUiState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val error: String? = null
)