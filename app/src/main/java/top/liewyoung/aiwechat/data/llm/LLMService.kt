package top.liewyoung.aiwechat.data.llm

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * Retrofit service interface for LLM API
 */
interface LLMService {
    @POST("chat/completions")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Body request: LLMRequest
    ): LLMResponse
}
