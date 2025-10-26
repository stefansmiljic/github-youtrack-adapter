package org.github_youtrack_adapter

import io.ktor.client.call.*
import kotlinx.coroutines.runBlocking

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

        println("Fetching projects from YouTrack...")
        val projects = youtrack.getProjects()
        projects.forEach { println("- ${it.id}") }

        print("\nEnter the project id to import issues into: ")
        val projId = readlnOrNull()?.trim().takeUnless { it.isNullOrEmpty() }
            ?: error("Empty project id")

        val githubIssues = github.getIssues(repo)
        val youtrackIssues = youtrack.getAllIssues()

        val issueMap = IssueMapStore.mapIssues(youtrackIssues)


        println("\nFound ${githubIssues.size} GitHub issues. Importing to YouTrack...\n")

        for (ghIssue in githubIssues) {
            val existingYouTrackId = issueMap.map[ghIssue.id.toString()]
            val mapped = IssueMapper.mapGithubIssueToYouTrack(ghIssue, projId)

            if (existingYouTrackId == null) {
                val response = youtrack.createIssue(mapped)
                if (response.status.value in 200..299) {
                    val body = response.body<YouTrackService.CreatedIssueResponse>()
                    issueMap.map[ghIssue.id.toString()] = body.id
                    println("Created YouTrack issue ${body.id} for GitHub #${ghIssue.id}")
                } else {
                    println("Failed to create YouTrack issue for GitHub #${ghIssue.id}: ${response.status}")
                }
            } else {
                val response = youtrack.updateIssue(existingYouTrackId, mapped)
                println("Updated YouTrack issue $existingYouTrackId for GitHub #${ghIssue.id} (status: ${response.status})")
            }
        }
        println("\nSync complete.")
    }
}

