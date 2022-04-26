import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import com.google.cloud.functions.RawBackgroundFunction
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger


class RemoteConfigMonitorService
    : RawBackgroundFunction {

    private val projectId: String =
        System.getenv("GOOGLE_CLOUD_PROJECT")
    private val slackChannelId: String =
        System.getenv("SLACK_CHANNEL_ID")
    private val slackToken: String =
        System.getenv("SLACK_TOKEN")
    private val firebaseProjectUrl: String =
        "https://console.firebase.google.com/project/${projectId}/config"


    private val slack =
        SlackClient(token = slackToken, channelId = slackChannelId)
    private val logger: Logger =
        Logger.getLogger(RemoteConfigMonitorService::class.java.name)

    private val gson: Gson = Gson()

    init {
        FirebaseApp.initializeApp()
    }

    override fun accept(
        json: String,
        context: Context
    ): Unit = runBlocking {

        val event: RemoteConfigUpdateEvent =
            gson.fromJson(json, RemoteConfigUpdateEvent::class.java)
        logger.info(event.toString())

        val newConfig = async(Dispatchers.IO) {
            FirebaseRemoteConfig
                .getInstance()
                .getTemplateAtVersion(event.version)
        }
        val previousConfig = async(Dispatchers.IO) {
            FirebaseRemoteConfig
                .getInstance()
                .getTemplateAtVersion(event.version - 1)
        }

        val diffCollection = getRemoteConfigDiff(
            projectId,
            event,
            previousConfig.await(),
            newConfig.await()
        )

        val response = slack.notify(
            message = RemoteConfigDiffFormatter(
                diff = diffCollection,
                firebaseProjectUrl = firebaseProjectUrl
            ).toMarkdownString())

        when (response.isOk) {
            true -> logger.info("Slack channel $slackChannelId notified")
            false -> logger.info("Failed to notify Slack channel $slackChannelId: ${response.error}")
        }
    }
}