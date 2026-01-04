package top.liewyoung.aiwechat.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "api_key") val apiKey: String = "",
    @ColumnInfo(name = "base_url") val baseUrl: String = "",
    @ColumnInfo(name = "model") val model: String = ""
)
