package com.github.artemych.analytics_annotation

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class AdditionalEventsMapping(
        val key: String,
        val event: AnalyticEvent
)