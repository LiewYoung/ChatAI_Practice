package top.liewyoung.aiwechat

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import top.liewyoung.aiwechat.di.AppContainer
import top.liewyoung.aiwechat.service.AgentMessageWorker

class AIWeChatApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        setupAgentMessaging()
    }

    private fun setupAgentMessaging() {
        val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val agentWorkRequest =
                PeriodicWorkRequestBuilder<AgentMessageWorker>(15, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .build()

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork(
                        "agent_messages",
                        ExistingPeriodicWorkPolicy.KEEP,
                        agentWorkRequest
                )
    }
}
