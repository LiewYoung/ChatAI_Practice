package top.liewyoung.aiwechat.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettingsFlow(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    @Query("UPDATE settings SET api_key = :apiKey WHERE id = 1")
    suspend fun updateApiKey(apiKey: String)

    @Query("UPDATE settings SET base_url = :baseUrl WHERE id = 1")
    suspend fun updateBaseUrl(baseUrl: String)

    @Query("UPDATE settings SET model = :model WHERE id = 1")
    suspend fun updateModel(model: String)
}
