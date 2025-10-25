package org.github_youtrack_adapter

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class YouTrackService(private val token: String, private val baseUrl: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true; prettyPrint = true })
        }
    }

    @Serializable
    data class Project(val id: String)

    @Serializable
    data class CustomFieldValue<T>(val name: String, val value: T)

    @Serializable
    data class IssueCustomField(
        val name: String,
        @SerialName("\$type") val type: String,
        val value: JsonElement
    )

    @Serializable data class
    YouTrackIssue(
        val project: Project,
        val summary: String,
        val description: String? = null,
        val customFields: List<IssueCustomField>? = null
    )

    suspend fun getProjects() {
        val response: String = client.get("$baseUrl/api/admin/projects?fields=id,name") {
            header("Authorization", "Bearer $token")
        }.bodyAsText()
    }

    suspend fun getIssue(): HttpResponse {
        return client.get("$baseUrl/api/issues"){
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun createIssue(issue: YouTrackIssue): HttpResponse {
        println("OVDE STAMPAMO ISSUE")
        println(issue)
        return client.post("$baseUrl/api/issues?fields=id,summary,description,state,type") {
            header("Accept", "application/json")
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(issue)
        }
    }
}
