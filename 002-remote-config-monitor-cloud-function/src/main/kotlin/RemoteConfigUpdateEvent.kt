/**
 * The json payload provided to the Cloud Function in response to the
 * remoteconfig.update event
 *
 * This payload doesn't give the actual Remote Config changes - those must be fetched separately
 */
data class RemoteConfigUpdateEvent(
    val type: String,
    val origin: String,
    val version: Long,
    val user: User
) {
    data class User(val email: String)
}