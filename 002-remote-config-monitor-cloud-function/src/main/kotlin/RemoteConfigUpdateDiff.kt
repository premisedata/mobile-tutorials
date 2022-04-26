import com.google.firebase.remoteconfig.Condition
import com.google.firebase.remoteconfig.Parameter
import com.google.firebase.remoteconfig.Template

/**
 * Constructs the diffs for Parameters and for Templates
 */
fun getRemoteConfigDiff(
    projectId: String,
    update: RemoteConfigUpdateEvent,
    original: Template,
    updated: Template
) = RemoteConfigDiffCollection(
    projectId = projectId,
    update = update,
    parameters = constructDiff(
        label = "Parameters",
        originalValues = original.getAllParameters(),
        updatedValues = updated.getAllParameters(),
        getDefault = { Parameter() }
    ),
    conditions = constructDiff(
        label = "Conditions",
        originalValues = original.conditions.associateBy { condition -> condition.name },
        updatedValues = updated.conditions.associateBy { condition -> condition.name },
        getDefault = { Condition(" ", " ") }
    )
)

private fun Template.getAllParameters() = parameters + buildMap {
    parameterGroups.entries.forEach { entry ->
        putAll(entry.value.parameters)
    }
}

/**
 * Uses Set math to identify which keys were updated, added, or removed
 * Builds List<RemoteConfigDiff> for each of these sets of keys
 */
private fun <T : Any> constructDiff(
    label: String,
    originalValues: Map<String, T>,
    updatedValues: Map<String, T>,
    getDefault: () -> T
): RemoteConfigDiffContainer<T> {

    val keysInBothConfigVersion = originalValues.keys intersect updatedValues.keys
    val removedKeys = originalValues.keys subtract keysInBothConfigVersion
    val addedKeys = updatedValues.keys subtract keysInBothConfigVersion

    val removedDiffs: List<RemoteConfigDiff<T>> = removedKeys.map { key ->
        RemoteConfigDiff(
            key = key,
            original = originalValues.getOrDefault(key, getDefault()),
            updated = getDefault()
        )
    }

    val addedDiffs: List<RemoteConfigDiff<T>> = addedKeys.map { key ->
        RemoteConfigDiff(
            key = key,
            original = getDefault(),
            updated = updatedValues.getOrDefault(key, getDefault())
        )
    }

    val updatedDiffs: List<RemoteConfigDiff<T>> = keysInBothConfigVersion.map { key ->
        val originalParameter = originalValues.getOrDefault(key, getDefault())
        val updatedParameter = updatedValues.getOrDefault(key, getDefault())
        Triple(key, originalParameter, updatedParameter)
    }.filter { it.second != it.third }
        .map {
            RemoteConfigDiff(
                key = it.first,
                original = it.second,
                updated = it.third
            )
        }

    return RemoteConfigDiffContainer(
        label = label,
        removed = removedDiffs,
        added = addedDiffs,
        updated = updatedDiffs
    )
}