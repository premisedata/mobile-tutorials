/**
 * A container for a set of changes to Parameters or Conditions
 */
data class RemoteConfigDiffContainer<T : Any>(
    val label: String,
    val removed: List<RemoteConfigDiff<T>>,
    val added: List<RemoteConfigDiff<T>>,
    val updated: List<RemoteConfigDiff<T>>,
) {
    val numberOfChanges: Int = removed.size + added.size + updated.size
}