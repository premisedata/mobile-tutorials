import com.google.cloud.functions.BackgroundFunction
import com.google.cloud.functions.Context
import java.util.logging.Logger


class RemoteConfigMonitorService
    : BackgroundFunction<RemoteConfigUpdateEvent> {

    private val logger: Logger = Logger.getLogger(RemoteConfigMonitorService::class.java.name)

    override fun accept(
        event: RemoteConfigUpdateEvent,
        context: Context
    ) {
        logger.info("Remote Config updated to version ${event.version} by ${event.user.email}")
    }
}