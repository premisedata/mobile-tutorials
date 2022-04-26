import com.google.firebase.remoteconfig.Condition
import com.google.firebase.remoteconfig.Parameter

/**
 * A container for the overall diff between two Remote Config template versions
 */
data class RemoteConfigDiffCollection(
    val projectId: String,
    val update: RemoteConfigUpdateEvent,
    val parameters: RemoteConfigDiffContainer<Parameter>,
    val conditions: RemoteConfigDiffContainer<Condition>
) {
    val numberOfChanges = parameters.numberOfChanges + conditions.numberOfChanges
}