import com.slack.api.Slack
import com.slack.api.methods.MethodsClient
import com.slack.api.methods.response.chat.ChatPostMessageResponse

class SlackClient(token: String, private val channelId: String) {

    private val methods: MethodsClient = Slack.getInstance().methods(token)

    fun notify(message: String): ChatPostMessageResponse = methods.chatPostMessage { req ->
        req.channel(channelId).text(message).mrkdwn(true)
    }
}