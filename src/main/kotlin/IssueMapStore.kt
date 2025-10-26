package org.github_youtrack_adapter

import kotlinx.serialization.*

@Serializable
data class IssueMap(val map: MutableMap<String, String> = mutableMapOf())

object IssueMapStore {

    private fun getGithubIdFromName(issue: YouTrackService.YouTrackIssue): String {
        return issue.summary.split('#')[1].trimEnd(')')
    }

    fun mapIssues(issues: List<YouTrackService.YouTrackIssue>): IssueMap {
        val map = IssueMap()
        for(issue in issues) {
            if(issue.id == null)
                continue
            map.map.put(getGithubIdFromName(issue), issue.id)
        }
        return map
    }
}
