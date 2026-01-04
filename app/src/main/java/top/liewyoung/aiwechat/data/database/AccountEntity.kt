package top.liewyoung.aiwechat.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "user_avatar") val userAvatar: String? = null
)
