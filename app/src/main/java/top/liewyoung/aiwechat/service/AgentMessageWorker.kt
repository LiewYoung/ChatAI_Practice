package top.liewyoung.aiwechat.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.flow.first
import top.liewyoung.aiwechat.MainActivity
import top.liewyoung.aiwechat.R
import top.liewyoung.aiwechat.model.ChatMessage

/** WorkManager worker for generating proactive agent messages */
class AgentMessageWorker(context: Context, params: WorkerParameters) :
        CoroutineWorker(context, params) {

        companion object {
                const val CHANNEL_ID = "ai_messages"
                const val NOTIFICATION_ID = 1001
        }

        override suspend fun doWork(): Result {
                return try {
                        val appContainer =
                                (applicationContext as top.liewyoung.aiwechat.AIWeChatApplication)
                                        .container

                        // Get all contacts
                        val contacts = appContainer.contactRepository.getAllContacts().first()

                        if (contacts.isEmpty()) {
                                return Result.success()
                        }

                        // Randomly select a contact (50% chance to send a message)
                        if (Random.nextFloat() > 0.5f) {
                                return Result.success()
                        }

                        val contact = contacts.random()

                        // Get chat history for context
                        val chatHistory =
                                appContainer.chatRepository.getChatContext(contact.id, limit = 1024)

                        // Generate proactive message
                        val result =
                                appContainer.llmClient.generateProactiveMessage(
                                        systemPrompt = contact.prompt,
                                        chatHistory = chatHistory
                                )

                        result.onSuccess { message ->
                                // Save the message
                                val chatMessage =
                                        ChatMessage(
                                                id = UUID.randomUUID().toString(),
                                                contactId = contact.id,
                                                content = message,
                                                isFromUser = false
                                        )
                                appContainer.chatRepository.addMessage(chatMessage)

                                // Update contact's last message
                                appContainer.contactRepository.updateLastMessage(
                                        contactId = contact.id,
                                        message = message,
                                        timestamp = chatMessage.timestamp
                                )

                                // Increment unread count
                                appContainer.contactRepository.incrementUnreadCount(contact.id)

                                // Show notification
                                showNotification(contact.name, message)
                        }

                        Result.success()
                } catch (e: Exception) {
                        e.printStackTrace()
                        Result.retry()
                }
        }

        private fun showNotification(contactName: String, message: String) {
                val notificationManager =
                        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                                NotificationManager

                // Create notification channel (for Android 8.0+)
                val channel =
                        NotificationChannel(
                                        CHANNEL_ID,
                                        "AI Messages",
                                        NotificationManager.IMPORTANCE_DEFAULT
                                )
                                .apply { description = "Notifications for new AI messages" }
                notificationManager.createNotificationChannel(channel)

                // Create intent to open the app
                val intent =
                        Intent(applicationContext, MainActivity::class.java).apply {
                                flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                val pendingIntent =
                        PendingIntent.getActivity(
                                applicationContext,
                                0,
                                intent,
                                PendingIntent.FLAG_IMMUTABLE
                        )

                // Build notification
                val notification =
                        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                                .setSmallIcon(R.drawable.avastar)
                                .setContentTitle(contactName)
                                .setContentText(message)
                                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .build()

                notificationManager.notify(NOTIFICATION_ID, notification)
        }
}
