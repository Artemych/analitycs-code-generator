package com.github.artemych.analytics_annotation

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class AnalyticsInteractor(
    val additionalEventsMappings: Array<AdditionalEventsMapping> = []
)
