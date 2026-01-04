package top.liewyoung.aiwechat.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import top.liewyoung.aiwechat.data.database.AccountDao
import top.liewyoung.aiwechat.data.database.AccountEntity
import top.liewyoung.aiwechat.model.Account
import java.io.File

class AccountRepository(private val accountDao: AccountDao) {
    
    val accountFlow: Flow<Account?> = accountDao.getAccountFlow()
        .map {
            it?.let {accountEntity ->
                Account(
                    userName = accountEntity.userName,
                    userId = accountEntity.userId,
                    userAvatar = accountEntity.userAvatar ?: ""
                )
            }
        }

    suspend fun saveAccount(account: Account) {
        val accountEntity = AccountEntity(
            userName = account.userName,
            userId = account.userId,
            userAvatar = account.userAvatar as? String
        )
        accountDao.insertAccount(accountEntity)
    }

    suspend fun saveAvatar(uri: Uri, context: Context): String {
        return withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.filesDir, "user_avatar.jpg")

            inputStream.use {
                input ->
                file.outputStream().use {
                    output ->
                    input?.copyTo(output) ?: throw IOException("无法打开: $uri")
                }
            }
            file.absolutePath
        }
    }

    suspend fun clear() {
        accountDao.clearAccount()
    }

}