import com.github.difflib.text.DiffRowGenerator
import com.google.gson.Gson

private const val REMOVAL_SLACK_EMOJI = ":github-changes-requested:"
private const val ADDITION_SLACK_EMOJI = ":github-check-mark:"

/**
 * Generates a markdown-formatted string for a given [RemoteConfigDiffCollection]
 */
class RemoteConfigDiffFormatter(
    private val diff: RemoteConfigDiffCollection,
    private val firebaseProjectUrl: String,
) {
    private val generator = DiffRowGenerator.create()
        .showInlineDiffs(true)
        .inlineDiffByWord(true)
        .oldTag { f: Boolean? -> "~" }
        .newTag { f: Boolean? -> "*" }
        .build()

    fun toMarkdownString(): String {
        return diff.run {
            buildString {
                appendLine("Blog post demo:")
                appendLine("$numberOfChanges changes made to <$firebaseProjectUrl|Remote Config> in $projectId")
                appendLine("Updated by: ${update.user.email}")

                markdownFromContainer(conditions)
                markdownFromContainer(parameters)
            }
        }
    }

    private fun Any.toJson(): String {
        return Gson().toJson(this)
    }

    private fun <T : Any> StringBuilder.markdownFromContainer(container: RemoteConfigDiffContainer<T>) {
        removedSection(container)
        addedSection(container)
        updatedSection(container)
    }

    private fun <T : Any> StringBuilder.updatedSection(container: RemoteConfigDiffContainer<T>) {
        if (container.updated.isNotEmpty()) {
            appendLine("")
            appendLine("")
            appendLine("$ADDITION_SLACK_EMOJI *Updated ${container.label}:*")
            appendLine("")

            container.updated.forEach { diff ->
                val rows = generator.generateDiffRows(
                    listOf(diff.original.toJson()),
                    listOf(diff.updated.toJson())
                )
                appendLine("  • _${diff.key}_")
                appendLine("      • ${rows.first().oldLine}")
                appendLine("      • ${rows.first().newLine}")
            }
        }
    }

    private fun <T : Any> StringBuilder.addedSection(container: RemoteConfigDiffContainer<T>) {
        if (container.added.isNotEmpty()) {
            appendLine("")
            appendLine("")
            appendLine("$ADDITION_SLACK_EMOJI *Added ${container.label}:*")
            appendLine("")

            container.added.forEach { diff ->
                val rows = generator.generateDiffRows(
                    listOf(""),
                    listOf(diff.updated.toJson())
                )
                appendLine("  • _${diff.key}_: ${rows.first().newLine}")
            }
        }
    }

    private fun <T : Any> StringBuilder.removedSection(container: RemoteConfigDiffContainer<T>) {
        if (container.removed.isNotEmpty()) {
            appendLine("")
            appendLine("")
            appendLine("$REMOVAL_SLACK_EMOJI *Removed ${container.label}:*")
            appendLine("")

            container.removed.forEach { diff ->
                val rows = generator.generateDiffRows(
                    listOf(diff.original.toJson()),
                    listOf("")
                )
                appendLine("  • _${diff.key}_: ${rows.first().oldLine}")
            }
        }
    }
}