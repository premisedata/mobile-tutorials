# Monitoring Changes In Firebase Remote Config Using Kotlin, Slack, and Google Cloud Functions
This tutorial describes how to monitor changes in Firebase Remote Config using a Google Cloud Function written in Kotlin to send change notifications to Slack.

## Deploying From Locally Development Machine

```
gcloud functions deploy remote-config-monitor-service \
--entry-point RemoteConfigMonitorService \
--runtime java11 \
--trigger-event google.firebase.remoteconfig.update \
--set-env-vars GOOGLE_CLOUD_PROJECT=<target gcp proejct>,SLACK_TOKEN=<Slack Bot Token>,SLACK_CHANNEL_ID=<Id for target Slack channel>
```