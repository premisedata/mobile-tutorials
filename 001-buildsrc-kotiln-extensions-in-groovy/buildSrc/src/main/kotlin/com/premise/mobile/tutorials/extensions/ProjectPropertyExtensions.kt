package com.premise.mobile.tutorials.extensions

import org.gradle.api.Project

fun Project.getVersionName(): String? {
    val versionName = findProperty("VERSION_NAME")
    return if (versionName is String) versionName else null
}

val Project.shouldLogAnalytics: Boolean
    get() {
        val shouldLogAnalytics = findProperty("LOG_ANALYTICS")
        return if (shouldLogAnalytics is String) shouldLogAnalytics.toBoolean() else false
    }