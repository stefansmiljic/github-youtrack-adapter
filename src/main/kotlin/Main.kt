package org.github_youtrack_adapter

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

        val issues = github.getIssues(repo)
        println("\nFound ${issues.size} GitHub issues. Importing to YouTrack...\n")

        for(issue in issues) {
            val youtrackIssue = IssueMapper.mapGithubIssueToYouTrack(issue, projId)
            val response = youtrack.createIssue(youtrackIssue)
            println("Created YouTrack issue: ${response.status}")
        }
    }
}

