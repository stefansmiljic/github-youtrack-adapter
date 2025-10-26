package org.github_youtrack_adapter

import kotlinx.serialization.json.*

object IssueMapper {
    private val labelToTypeMap = mapOf(
        "bug" to "Bug",
        "documentation" to "Task",
        "duplicate" to "Task",
        "enhancement" to "Feature",
        "good first issue" to "Task",
        "help wanted" to "Task",
        "invalid" to "Task",
        "question" to "Task",
        "wontfix" to "Task"
    )

    fun mapGithubIssueToYouTrack(issue: GithubService.Issue, projectId: String): YouTrackService.YouTrackIssue {
        val mappedStateName = when (issue.state.lowercase()) {
            "closed" -> "Fixed"
            else -> "Open"
        }

        val fields = mutableListOf<YouTrackService.IssueCustomField>()

        val stateField = YouTrackService.IssueCustomField(
            name = "State",
            type = "StateIssueCustomField",
            value = JsonObject(mapOf("name" to JsonPrimitive(mappedStateName)))
        )
        fields.add(stateField)

        val typeName = issue.labels
            .map { it.name.lowercase() }
            .firstNotNullOfOrNull { labelToTypeMap[it] }
            ?: "Task"

        val typeField = YouTrackService.IssueCustomField(
            name = "Type",
            type = "SingleEnumIssueCustomField",
            value = JsonObject(mapOf("name" to JsonPrimitive(typeName)))
        )
        fields.add(typeField)

        return YouTrackService.YouTrackIssue(
            project = YouTrackService.Project(id = projectId),
            summary = issue.title + "(GitHub Issue: #${issue.id})",
            description = issue.body,
            customFields = fields
        )
    }
}
