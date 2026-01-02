package top.liewyoung.aiwechat.util


import android.icu.text.SimpleDateFormat
import java.util.Locale


interface TimeProvider {
    fun getCurrentTimeMillis(): Long
}

class RealTimeProvider : TimeProvider{
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    fun formatTime(timestamp: Long): String {
        return dateFormat.format(timestamp)
    }

    fun getCurrentTime(): String {
        return formatTime(getCurrentTimeMillis())
    }

}



