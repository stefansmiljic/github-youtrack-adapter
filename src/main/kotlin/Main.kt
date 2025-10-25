package org.github_youtrack_adapter
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking

suspend fun getProjects(){
    val token = System.getenv("YOUTRACK_TOKEN")
    val baseUrl = System.getenv("YOUTRACK_URL")
    val client = HttpClient()
    val response: String = client.get("$baseUrl/api/admin/projects?fields=id,name"){
        header("Authorization", "Bearer $token") }.bodyAsText()
    println("Projects: $response")
}

fun main() {
    runBlocking {
        val githubToken = System.getenv("GITHUB_TOKEN")
            ?:error("Missing GITHUB_TOKEN env var")
        val youtrackToken = System.getenv("YOUTRACK_TOKEN")
            ?:error("Missing YOUTRACK_TOKEN env var")
        val youtrackUrl = System.getenv("YOUTRACK_URL")
            ?:error("Missing YOUTRACK_URL env var")
        val repo = System.getenv("GITHUB_REPO")
            ?:error("Missing GITHUB_REPO env var")

        val github = GithubService(githubToken)
        val youtrack = YouTrackService(youtrackToken, youtrackUrl)

        val issues = github.getIssues(repo)
        println("Importing ${issues.size} Github issues into YouTrack...")

        getProjects()

        println("Please enter the id of your project")

        val projId = readlnOrNull() ?: throw Exception("Empty project id")
        for(issue in issues) {
            val youtrackIssue = IssueMapper.mapGithubIssueToYouTrack(issue, projId)
            val response = youtrack.createIssue(youtrackIssue)
            println("Created YouTrack issue: ${response.status}")
        }
    }
}

