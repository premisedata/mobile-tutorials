import com.google.gson.annotations.SerializedName

/**
 * The json payload provided to the Cloud Function in response to the
 * remoteconfig.update event
 *
 * This payload doesn't give the actual Remote Config changes - those must be fetched separately
 */
data class RemoteConfigUpdateEvent(
    @SerializedName("updateType")
    val type: String,
    @SerializedName("updateOrigin")
    val origin: String,
    @SerializedName("versionNumber")
    val version: Long,
    @SerializedName("updateUser")
    val user: User
) {
    data class User(val email: String)
}