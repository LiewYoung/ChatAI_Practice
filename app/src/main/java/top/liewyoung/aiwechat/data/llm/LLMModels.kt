package top.liewyoung.aiwechat.data.llm

import com.google.gson.annotations.SerializedName

/**
 * Data models for LLM API communication
 */

data class LLMRequest(
    @SerializedName("model")
    val model: String = "gpt-3.5-turbo",
    @SerializedName("messages")
    val messages: List<LLMMessage>,
    @SerializedName("temperature")
    val temperature: Double = 0.7,
    @SerializedName("max_tokens")
    val maxTokens: Int? = null
)

data class LLMMessage(
    @SerializedName("role")
    val role: String, // "system", "user", or "assistant"
    @SerializedName("content")
    val content: String
)

data class LLMResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("object")
    val objectType: String,
    @SerializedName("created")
    val created: Long,
    @SerializedName("model")
    val model: String,
    @SerializedName("choices")
    val choices: List<LLMChoice>,
    @SerializedName("usage")
    val usage: LLMUsage? = null
)

data class LLMChoice(
    @SerializedName("index")
    val index: Int,
    @SerializedName("message")
    val message: LLMMessage,
    @SerializedName("finish_reason")
    val finishReason: String
)

data class LLMUsage(
    @SerializedName("prompt_tokens")
    val promptTokens: Int,
    @SerializedName("completion_tokens")
    val completionTokens: Int,
    @SerializedName("total_tokens")
    val totalTokens: Int
)
