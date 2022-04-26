/**
 * Holds a diff between two Parameters or Conditions
 */
data class RemoteConfigDiff<T>(
    val key: String,
    val original: T,
    val updated: T
)