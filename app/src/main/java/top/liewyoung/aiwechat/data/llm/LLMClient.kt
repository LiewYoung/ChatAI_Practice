package top.liewyoung.aiwechat.data.llm

import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.liewyoung.aiwechat.BuildConfig
import top.liewyoung.aiwechat.data.SettingsRepository
import top.liewyoung.aiwechat.model.ChatMessage

/** LLM API client for interacting with AI models */
class LLMClient(private val settingsRepository: SettingsRepository) {

    private val okHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .apply {
                        if (BuildConfig.DEBUG) {
                            val loggingInterceptor =
                                    HttpLoggingInterceptor().apply {
                                        level = HttpLoggingInterceptor.Level.BODY
                                    }
                            addInterceptor(loggingInterceptor)
                        }
                    }
                    .build()

    private fun createService(baseUrl: String): LLMService {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LLMService::class.java)
    }

    /**
     * Send a chat message and get AI response
     * @param systemPrompt The contact's personality prompt
     * @param chatHistory Recent chat messages for context
     * @param userMessage The new user message
     * @return AI response content
     */
    suspend fun chat(
            systemPrompt: String,
            chatHistory: List<ChatMessage>,
            userMessage: String
    ): Result<String> {
        return try {
            val settings = settingsRepository.settingsFlow.first()

            if (settings.apiKey.isEmpty() || settings.baseUrl.isEmpty()) {
                return Result.failure(Exception("API key or base URL not configured"))
            }

            // Build messages list with system prompt, history, and new message
            val messages = buildList {
                // System prompt defines the AI's personality
                add(LLMMessage(role = "system", content = systemPrompt))

                // Add chat history for context
                chatHistory.forEach { msg ->
                    add(
                            LLMMessage(
                                    role = if (msg.isFromUser) "user" else "assistant",
                                    content = msg.content
                            )
                    )
                }

                // Add the new user message
                add(LLMMessage(role = "user", content = userMessage))
            }

            val request = LLMRequest(model = settings.model, messages = messages, temperature = 0.8)

            val service = createService(settings.baseUrl)
            val response =
                    service.chat(authorization = "Bearer ${settings.apiKey}", request = request)

            if (response.choices.isNotEmpty()) {
                Result.success(response.choices[0].message.content)
            } else {
                Result.failure(Exception("No response from AI"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Generate a proactive message from an AI agent
     * @param systemPrompt The contact's personality prompt
     * @param chatHistory Recent chat messages for context
     * @return AI generated message
     */
    suspend fun generateProactiveMessage(
            systemPrompt: String,
            chatHistory: List<ChatMessage>
    ): Result<String> {
        return try {
            val settings = settingsRepository.settingsFlow.first()

            if (settings.apiKey.isEmpty() || settings.baseUrl.isEmpty()) {
                return Result.failure(Exception("API key or base URL not configured"))
            }

            val messages = buildList {
                add(LLMMessage(role = "system", content = systemPrompt))

                chatHistory.forEach { msg ->
                    add(
                            LLMMessage(
                                    role = if (msg.isFromUser) "user" else "assistant",
                                    content = msg.content
                            )
                    )
                }

                // Prompt for proactive message
                add(
                        LLMMessage(
                                role = "user",
                                content =
                                        "Generate a natural, conversational message to initiate or continue our conversation. Be contextual and engaging."
                        )
                )
            }

            val request = LLMRequest(model = settings.model, messages = messages, temperature = 0.9)
            val service = createService(settings.baseUrl)
            val response =
                    service.chat(authorization = "Bearer ${settings.apiKey}", request = request)

            if (response.choices.isNotEmpty()) {
                Result.success(response.choices[0].message.content)
            } else {
                Result.failure(Exception("No response from AI"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
