package org.github_youtrack_adapter

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable

class GithubService(private val token: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }
            )
        }
    }

    @Serializable
    data class Label(val name: String)

    @Serializable
    data class Issue(
        val id: Long,
        val title: String,
        val body: String? = null,
        val state: String,
        val labels: List<Label> = emptyList()
    )

    suspend fun getIssues(repo: String): List<Issue> {
        return client.get("https://api.github.com/repos/$repo/issues?state=all") {
            header("Authorization", "Bearer $token")
        }.body()
    }
}
